package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.entities.AbstractUserAuthority;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.UserProfileForm;
import com.ecinema.app.domain.validators.RegistrationValidator;
import com.ecinema.app.domain.validators.UserProfileValidator;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.*;
import com.ecinema.app.domain.contracts.IPassword;
import com.ecinema.app.domain.contracts.IRegistration;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.domain.validators.PasswordValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 * The implementation User service.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<User, UserRepository> implements UserService {

    private final Map<UserAuthority, AbstractUserAuthorityService<? extends AbstractUserAuthority>>
            userAuthorityServices = new EnumMap<>(UserAuthority.class);
    private final EncoderService encoderService;
    private final PasswordValidator passwordValidator;
    private final UserProfileValidator userProfileValidator;
    private final RegistrationValidator registrationValidator;

    /**
     * Instantiates a new User service.
     *
     * @param repository                 the repository
     * @param customerAuthorityService     the customer role def service
     * @param moderatorAuthorityService    the moderator role def service

     * @param adminAuthorityService        the admin role def service
     */
    public UserServiceImpl(UserRepository repository,
                           CustomerAuthorityService customerAuthorityService,
                           ModeratorAuthorityService moderatorAuthorityService,
                           AdminAuthorityService adminAuthorityService,
                           EncoderService encoderService,
                           PasswordValidator passwordValidator,
                           UserProfileValidator userProfileValidator,
                           RegistrationValidator registrationValidator) {
        super(repository);
        this.encoderService = encoderService;
        this.passwordValidator = passwordValidator;
        this.userProfileValidator = userProfileValidator;
        this.registrationValidator = registrationValidator;
        userAuthorityServices.put(UserAuthority.CUSTOMER, customerAuthorityService);
        userAuthorityServices.put(UserAuthority.MODERATOR, moderatorAuthorityService);
        userAuthorityServices.put(UserAuthority.ADMIN, adminAuthorityService);
    }

    @Override
    protected void onDelete(User user) {
        logger.debug("User Service on delete");
        // cascade delete AbstractUserAuthoritys, iterate over copy of keySet to avoid concurrent modification exception
        Set<UserAuthority> userAuthorities = new HashSet<>(user.getUserAuthorities().keySet());
        logger.debug("User roles: " + userAuthorities);
        for (UserAuthority userAuthority : userAuthorities) {
            logger.debug("Deleting user role: " + userAuthority);
            AbstractUserAuthorityService<? extends AbstractUserAuthority> userAuthorityService =
                    userAuthorityServices.get(userAuthority);
            userAuthorityService.delete(userAuthority.cast(user.getUserAuthorities().get(userAuthority)));
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        User user = findById(id).orElseThrow(
                () -> new NoEntityFoundException("user", "id", id));
        onDeleteInfo(user, info);
    }

    @Override
    public void onDeleteInfo(User user, Collection<String> info) {
        user.getUserAuthorities().forEach((userRole, AbstractUserAuthority) -> {
            AbstractUserAuthorityService<? extends AbstractUserAuthority> userAuthorityService =
                    userAuthorityServices.get(userRole);
            userAuthorityService.onDeleteInfo(userRole.cast(AbstractUserAuthority), info);
        });
    }

    @Override
    public void editUserProfile(UserProfileForm userProfileForm)
            throws NoEntityFoundException, InvalidArgsException {
        User user = findById(userProfileForm.getUserId()).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userProfileForm.getUserId()));
        List<String> errors = new ArrayList<>();
        userProfileValidator.validate(userProfileForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        user.setFirstName(userProfileForm.getFirstName());
        user.setLastName(userProfileForm.getLastName());
        user.setBirthDate(userProfileForm.getBirthDate());
        save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s)
            throws UsernameNotFoundException {
        return repository.findByUsernameOrEmail(s).orElseThrow(
                () -> new UsernameNotFoundException("No user found with username or email " + s));
    }

    @Override
    public void updateLastActivityDateTimeOfUserWithId(Long userId)
            throws NoEntityFoundException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        user.setLastActivityDateTime(LocalDateTime.now());
        save(user);
    }

    @Override
    public UserDto register(IRegistration registration, boolean passwordEncoded,
                            boolean securityAnswer1Encoded, boolean securityAnswer2Encoded)
            throws InvalidArgsException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("User registration");
        List<String> errors = new ArrayList<>();
        registrationValidator.validate(registration, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        if (existsByEmail(registration.getEmail())) {
            throw new ClashException("User with email " + registration.getEmail() + " already exists");
        }
        if (existsByUsername(registration.getUsername())) {
            throw new ClashException("User with username " + registration.getUsername() + " already exists");
        }
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoded ? registration.getPassword() :
                                 encoderService.encode(registration.getPassword()));
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setBirthDate(registration.getBirthDate());
        user.setSecurityQuestion1(registration.getSecurityQuestion1());
        user.setSecurityAnswer1(securityAnswer1Encoded ? registration.getSecurityAnswer1() :
                                        encoderService.encode(
                                                registration.getSecurityAnswer1()));
        user.setSecurityQuestion2(registration.getSecurityQuestion2());
        user.setSecurityAnswer2(securityAnswer2Encoded ? registration.getSecurityAnswer2() :
                                        encoderService.encode(
                                                registration.getSecurityAnswer2()));
        user.setCreationDateTime(LocalDateTime.now());
        user.setLastActivityDateTime(LocalDateTime.now());
        user.setIsAccountEnabled(true);
        user.setIsAccountLocked(false);
        user.setIsAccountExpired(false);
        user.setIsCredentialsExpired(false);
        save(user);
        if (!registration.getUserAuthorities().isEmpty()) {
            addUserAuthorityToUser(user, registration.getUserAuthorities());
        }
        logger.debug("Instantiated and saved new user: " + user);
        UserDto userDto = convertToDto(user);
        logger.debug("Returning new user DTO: " + userDto);
        return userDto;
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow(
                () -> new NoEntityFoundException("user", "username", username));
        return convertToDto(user);
    }

    @Override
    public UserDto findByUsernameOrEmail(String s) {
        User user = repository.findByUsernameOrEmail(s).orElseThrow(
                () -> new NoEntityFoundException("user", "email or username", s));
        return convertToDto(user);
    }

    @Override
    public Optional<Long> findIdByUsername(String username) {
        return repository.findIdByUsername(username);
    }

    @Override
    public Optional<Long> findIdByEmail(String email) {
        return repository.findIdByEmail(email);
    }

    @Override
    public UserDto findByEmail(String email)
            throws NoEntityFoundException {
        User user = repository.findByEmail(email).orElseThrow(
                () -> new NoEntityFoundException("user", "email", email));
        return convertToDto(user);
    }

    @Override
    public <T extends AbstractUserAuthority> Optional<T> getUserAuthorityOf(Long userId, Class<T> userAuthorityClass)
            throws InvalidArgsException, NoEntityFoundException {
        UserAuthority userAuthority = UserAuthority.defClassToUserRole(userAuthorityClass);
        if (userAuthority == null) {
            throw new InvalidArgsException("The provided class " + userAuthorityClass.getName() +
                                                   " is not mapped to a user role value");
        }
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("User", "id", userId));
        AbstractUserAuthority userAuthorityDef = user.getUserAuthorities().get(userAuthority);
        return Optional.ofNullable(userAuthorityClass.cast(userAuthorityDef));
    }

    @Override
    public Long findIdByUsernameOrEmail(String s) {
        return repository.findIdByUsernameOrEmail(s);
    }

    @Override
    public List<UserDto> findAllByIsAccountLocked(boolean isAccountLocked) {
        List<User> users = repository.findAllByIsAccountLocked(isAccountLocked);
        return convertToDto(users);
    }

    @Override
    public List<UserDto> findAllByIsAccountEnabled(boolean isAccountEnabled) {
        List<User> users = repository.findAllByIsAccountEnabled(isAccountEnabled);
        return convertToDto(users);
    }

    @Override
    public List<UserDto> findAllByIsAccountExpired(boolean isAccountExpired) {
        List<User> users = repository.findAllByIsAccountExpired(isAccountExpired);
        return convertToDto(users);
    }

    @Override
    public List<UserDto> findAllByIsCredentialsExpired(boolean isCredentialsExpired) {
        List<User> users = repository.findAllByIsCredentialsExpired(isCredentialsExpired);
        return convertToDto(users);
    }

    @Override
    public List<UserDto> findAllByCreationDateTimeBefore(LocalDateTime localDateTime) {
        List<User> users = repository.findAllByCreationDateTimeBefore(localDateTime);
        return convertToDto(users);
    }

    @Override
    public List<UserDto> findAllByCreationDateTimeAfter(LocalDateTime localDateTime) {
        List<User> users = repository.findAllByCreationDateTimeAfter(localDateTime);
        return convertToDto(users);
    }

    @Override
    public List<UserDto> findAllByLastActivityDateTimeBefore(LocalDateTime localDateTime) {
        List<User> users = repository.findAllByLastActivityDateTimeBefore(localDateTime);
        return convertToDto(users);
    }

    @Override
    public List<UserDto> findAllByLastActivityDateTimeAfter(LocalDateTime localDateTime) {
        List<User> users = repository.findAllByLastActivityDateTimeAfter(localDateTime);
        return convertToDto(users);
    }

    @Override
    public Set<UserAuthority> userAuthorities(Long userId)
            throws NoEntityFoundException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        return new HashSet<>(user.getUserAuthorities().keySet());
    }

    @Override
    public List<String> userAuthoritiesAsListOfStrings(Long userId)
            throws NoEntityFoundException {
        return userAuthorities(userId)
                .stream().map(UserAuthority::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public void addUserAuthorityToUser(User user, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        addUserAuthorityToUser(user.getId(), userAuthorities);
    }

    @Override
    public void addUserAuthorityToUser(User user, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        addUserAuthorityToUser(user.getId(), userAuthorities);
    }

    @Override
    public void addUserAuthorityToUser(Long userId, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        addUserAuthorityToUser(userId, Set.of(userAuthorities));
    }

    @Override
    public void addUserAuthorityToUser(Long userId, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        List<UserAuthority> userRolesAlreadyInstantiated = UtilMethods.findAllKeysThatMapContainsIfAny(
                user.getUserAuthorities(), userAuthorities);
        if (!userRolesAlreadyInstantiated.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (UserAuthority userAuthority : userRolesAlreadyInstantiated) {
                errors.add("FAILURE: User already has " + userAuthority + " role definition");
            }
            throw new ClashException(errors);
        }
        for (UserAuthority userAuthority : userAuthorities) {
            AbstractUserAuthority userAuthorityDef = userAuthority.instantiate();
            userAuthorityDef.setUser(user);
            user.getUserAuthorities().put(userAuthority, userAuthorityDef);
            userAuthorityDef.setIsAuthorityValid(true);
            userAuthorityServices.get(userAuthority).save(userAuthority.cast(userAuthorityDef));
        }
    }

    @Override
    public void removeUserAuthorityFromUser(User user, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException {
        removeUserAuthorityFromUser(user.getId(), userAuthorities);
    }

    @Override
    public void removeUserAuthorityFromUser(User user, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException {
        removeUserAuthorityFromUser(user.getId(), userAuthorities);
    }

    @Override
    public void removeUserAuthorityFromUser(Long userId, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException {
        removeUserAuthorityFromUser(userId, Set.of(userAuthorities));
    }

    @Override
    public void removeUserAuthorityFromUser(Long userId, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("User", "id", userId));
        for (UserAuthority userAuthority : userAuthorities) {
            AbstractUserAuthority userAuthorityDef = user.getUserAuthorities().get(userAuthority);
            AbstractUserAuthorityService<? extends AbstractUserAuthority> userAuthorityService =
                    userAuthorityServices.get(userAuthority);
            userAuthorityService.deleteById(userAuthorityDef.getId());
        }
    }

    @Override
    public void requestPasswordChange(IPassword iPassword)
            throws InvalidArgsException {
        List<String> errors = new ArrayList<>();
        passwordValidator.validate(iPassword, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }

    }

    @Override
    public UserDto convertIdToDto(Long id)
            throws NoEntityFoundException {
        User user = findById(id).orElseThrow(
                () -> new NoEntityFoundException("user", "id", id));
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setBirthDate(user.getBirthDate());
        userDto.setCreationDateTime(user.getCreationDateTime());
        userDto.setLastActivityDateTime(user.getLastActivityDateTime());
        userDto.getUserAuthorities().addAll(user.getUserAuthorities().keySet());
        logger.debug("Converting user to DTO: " + userDto);
        logger.debug("User: " + user);
        return userDto;
    }

}
