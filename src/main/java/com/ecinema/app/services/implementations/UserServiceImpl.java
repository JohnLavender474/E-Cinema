package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.*;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.contracts.IRegistration;
import com.ecinema.app.utils.dtos.UserDTO;
import com.ecinema.app.utils.exceptions.ClashException;
import com.ecinema.app.utils.exceptions.InvalidArgsException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * {@inheritDoc}
 * The implementation User service.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<User, UserRepository> implements UserService {

    private final ModelMapper modelMapper;
    private final Map<UserRole, UserRoleDefService<? extends UserRoleDef>> userRoleDefServices =
            new EnumMap<>(UserRole.class);

    /**
     * Instantiates a new User service.
     *
     * @param repository                 the repository
     * @param customerRoleDefService     the customer role def service
     * @param moderatorRoleDefService    the moderator role def service
     * @param adminTraineeRoleDefService the admin trainee role def service
     * @param adminRoleDefService        the admin role def service
     */
    public UserServiceImpl(UserRepository repository,
                           CustomerRoleDefService customerRoleDefService,
                           ModeratorRoleDefService moderatorRoleDefService,
                           AdminTraineeRoleDefService adminTraineeRoleDefService,
                           AdminRoleDefService adminRoleDefService,
                           ModelMapper modelMapper) {
        super(repository);
        this.modelMapper = modelMapper;
        userRoleDefServices.put(UserRole.CUSTOMER, customerRoleDefService);
        userRoleDefServices.put(UserRole.MODERATOR, moderatorRoleDefService);
        userRoleDefServices.put(UserRole.ADMIN_TRAINEE, adminTraineeRoleDefService);
        userRoleDefServices.put(UserRole.ADMIN, adminRoleDefService);
    }

    @Override
    protected void onDelete(User user) {
        logger.info("User Service on delete");
        // cascade delete UserRoleDefs, iterate over copy of UserRoleDefs keySet to avoid concurrent modification exception
        Set<UserRole> userRoles = new HashSet<>(user.getUserRoleDefs().keySet());
        logger.info("User Roles size: " + userRoles.size());
        for (UserRole userRole : userRoles) {
            logger.info("Has user role: " + userRole);
            UserRoleDefService<? extends UserRoleDef> userRoleDefService = userRoleDefServices.get(userRole);
            userRoleDefService.delete(userRole.castToDefType(user.getUserRoleDefs().get(userRole)));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String s)
            throws UsernameNotFoundException {
        return findByUsernameOrEmail(s).orElseThrow(
                () -> new UsernameNotFoundException("No user found with username or email " + s));
    }

    @Override
    public UserDTO register(IRegistration registration) {
        User user = new User();
        user.setUsername(registration.getUsername());
        user.setEmail(registration.getEmail());
        user.setPassword(registration.getPassword());
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
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
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String s) {
        return repository.findByUsernameOrEmail(s);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
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
    public List<User> findAllByIsAccountLocked(boolean isAccountLocked) {
        return repository.findAllByIsAccountLocked(isAccountLocked);
    }

    @Override
    public List<User> findAllByIsAccountEnabled(boolean isAccountEnabled) {
        return repository.findAllByIsAccountEnabled(isAccountEnabled);
    }

    @Override
    public List<User> findAllByIsAccountExpired(boolean isAccountExpired) {
        return repository.findAllByIsAccountExpired(isAccountExpired);
    }

    @Override
    public List<User> findAllByIsCredentialsExpired(boolean isCredentialsExpired) {
        return repository.findAllByIsCredentialsExpired(isCredentialsExpired);
    }

    @Override
    public List<User> findAllByCreationDateTimeBefore(LocalDateTime localDateTime) {
        return repository.findAllByCreationDateTimeBefore(localDateTime);
    }

    @Override
    public List<User> findAllByCreationDateTimeAfter(LocalDateTime localDateTime) {
        return repository.findAllByCreationDateTimeAfter(localDateTime);
    }

    @Override
    public List<User> findAllByLastActivityDateTimeBefore(LocalDateTime localDateTime) {
        return repository.findAllByLastActivityDateTimeBefore(localDateTime);
    }

    @Override
    public List<User> findAllByLastActivityDateTimeAfter(LocalDateTime localDateTime) {
        return repository.findAllByLastActivityDateTimeAfter(localDateTime);
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

}
