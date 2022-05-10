package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.entities.UserRoleDef;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.*;
import com.ecinema.app.domain.contracts.IPassword;
import com.ecinema.app.domain.contracts.IRegistration;
import com.ecinema.app.domain.enums.UserRole;
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

    private final Map<UserRole, UserRoleDefService<? extends UserRoleDef>> userRoleDefServices =
            new EnumMap<>(UserRole.class);
    private final PasswordValidator passwordValidator;

    /**
     * Instantiates a new User service.
     *
     * @param repository                 the repository
     * @param customerRoleDefService     the customer role def service
     * @param moderatorRoleDefService    the moderator role def service

     * @param adminRoleDefService        the admin role def service
     */
    public UserServiceImpl(UserRepository repository,
                           CustomerRoleDefService customerRoleDefService,
                           ModeratorRoleDefService moderatorRoleDefService,
                           AdminRoleDefService adminRoleDefService,
                           PasswordValidator passwordValidator) {
        super(repository);
        this.passwordValidator = passwordValidator;
        userRoleDefServices.put(UserRole.CUSTOMER, customerRoleDefService);
        userRoleDefServices.put(UserRole.MODERATOR, moderatorRoleDefService);
        userRoleDefServices.put(UserRole.ADMIN, adminRoleDefService);
    }

    @Override
    protected void onDelete(User user) {
        logger.debug("User Service on delete");
        // cascade delete UserRoleDefs, iterate over copy of keySet to avoid concurrent modification exception
        Set<UserRole> userRoles = new HashSet<>(user.getUserRoleDefs().keySet());
        logger.debug("User roles: " + userRoles);
        for (UserRole userRole : userRoles) {
            logger.debug("Deleting user role: " + userRole);
            UserRoleDefService<? extends UserRoleDef> userRoleDefService = userRoleDefServices.get(userRole);
            userRoleDefService.delete(userRole.castToDefType(user.getUserRoleDefs().get(userRole)));
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
        user.getUserRoleDefs().forEach((userRole, userRoleDef) -> {
            UserRoleDefService<? extends UserRoleDef> userRoleDefService = userRoleDefServices.get(userRole);
            userRoleDefService.onDeleteInfo(userRole.castToDefType(userRoleDef), info);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String s)
            throws UsernameNotFoundException {
        return repository.findByUsernameOrEmail(s).orElseThrow(
                () -> new UsernameNotFoundException("No user found with username or email " + s));
    }

    @Override
    public UserDto register(IRegistration registration) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("User registration");
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setPassword(registration.getPassword());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setBirthDate(registration.getBirthDate());
        user.setSecurityQuestion1(registration.getSecurityQuestion1());
        user.setSecurityAnswer1(registration.getSecurityAnswer1());
        user.setSecurityQuestion2(registration.getSecurityQuestion2());
        user.setSecurityAnswer2(registration.getSecurityAnswer2());
        user.setCreationDateTime(LocalDateTime.now());
        user.setLastActivityDateTime(LocalDateTime.now());
        user.setIsAccountEnabled(true);
        user.setIsAccountLocked(false);
        user.setIsAccountExpired(false);
        user.setIsCredentialsExpired(false);
        save(user);
        addUserRoleDefToUser(user, registration.getUserRoles());
        logger.debug("Instantiated and saved new user: " + user);
        UserDto userDto = convertToDto(user.getId());
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
    public <T extends UserRoleDef> Optional<T> getUserRoleDefOf(Long userId, Class<T> userRoleDefClass)
            throws InvalidArgsException, NoEntityFoundException {
        UserRole userRole = UserRole.defClassToUserRole(userRoleDefClass);
        if (userRole == null) {
            throw new InvalidArgsException("The provided class " + userRoleDefClass.getName() +
                                                   " is not mapped to a user role value");
        }
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("User", "id", userId));
        UserRoleDef userRoleDef = user.getUserRoleDefs().get(userRole);
        return Optional.ofNullable(userRoleDefClass.cast(userRoleDef));
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
    public Set<UserRole> userRoles(Long userId)
            throws NoEntityFoundException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        return new HashSet<>(user.getUserRoleDefs().keySet());
    }

    @Override
    public List<String> userRolesAsListOfStrings(Long userId)
            throws NoEntityFoundException {
        return userRoles(userId)
                .stream().map(UserRole::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public void addUserRoleDefToUser(User user, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        addUserRoleDefToUser(user.getId(), userRoles);
    }

    @Override
    public void addUserRoleDefToUser(User user, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        addUserRoleDefToUser(user.getId(), userRoles);
    }

    @Override
    public void addUserRoleDefToUser(Long userId, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        addUserRoleDefToUser(userId, Set.of(userRoles));
    }

    @Override
    public void addUserRoleDefToUser(Long userId, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgsException, ClashException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        List<UserRole> userRolesAlreadyInstantiated = UtilMethods.findAllKeysThatMapContainsIfAny(
                user.getUserRoleDefs(), userRoles);
        if (!userRolesAlreadyInstantiated.isEmpty()) {
            List<String> errors = new ArrayList<>();
            for (UserRole userRole : userRolesAlreadyInstantiated) {
                errors.add("FAILURE: User already has " + userRole + " role definition");
            }
            throw new ClashException(errors);
        }
        for (UserRole userRole : userRoles) {
            UserRoleDef userRoleDef = userRole.instantiateNew();
            userRoleDef.setUser(user);
            user.getUserRoleDefs().put(userRole, userRoleDef);
            userRoleDefServices.get(userRole).save(userRole.castToDefType(userRoleDef));
        }
    }

    @Override
    public void removeUserRoleDefFromUser(User user, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgsException {
        removeUserRoleDefFromUser(user.getId(), userRoles);
    }

    @Override
    public void removeUserRoleDefFromUser(User user, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgsException {
        removeUserRoleDefFromUser(user.getId(), userRoles);
    }

    @Override
    public void removeUserRoleDefFromUser(Long userId, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgsException {
        removeUserRoleDefFromUser(userId, Set.of(userRoles));
    }

    @Override
    public void removeUserRoleDefFromUser(Long userId, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgsException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("User", "id", userId));
        for (UserRole userRole : userRoles) {
            UserRoleDef userRoleDef = user.getUserRoleDefs().get(userRole);
            UserRoleDefService<? extends UserRoleDef> userRoleDefService = userRoleDefServices.get(userRole);
            userRoleDefService.deleteById(userRoleDef.getId());
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
    public UserDto convertToDto(Long id)
            throws NoEntityFoundException {
        User user = findById(id).orElseThrow(
                () -> new NoEntityFoundException("user", "id", id));
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.getUserRoles().addAll(user.getUserRoleDefs().keySet());
        logger.debug("Converting user to DTO: " + userDto);
        logger.debug("User: " + user);
        return userDto;
    }

}
