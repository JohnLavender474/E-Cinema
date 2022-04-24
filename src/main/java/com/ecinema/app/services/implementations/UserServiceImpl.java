package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.*;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.exceptions.ClashException;
import com.ecinema.app.utils.exceptions.InvalidArgException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/** The implementated User service. */
@Service
@Transactional
public class UserServiceImpl extends AbstractServiceImpl<User, UserRepository> implements UserService {

    private final DaoAuthenticationProvider daoAuthenticationProvider;
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
                           DaoAuthenticationProvider daoAuthenticationProvider) {
        super(repository);
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        userRoleDefServices.put(UserRole.CUSTOMER, customerRoleDefService);
        userRoleDefServices.put(UserRole.MODERATOR, moderatorRoleDefService);
        userRoleDefServices.put(UserRole.ADMIN_TRAINEE, adminTraineeRoleDefService);
        userRoleDefServices.put(UserRole.ADMIN, adminRoleDefService);
    }

    @Override
    protected void onDelete(User user) {

    }

    @Override
    public void login(String s, String password)
            throws NoEntityFoundException {
        User user = findByUsernameOrEmail(s).orElseThrow(
                () -> new NoEntityFoundException("user", "credential", s));
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            logger.debug(String.format("Auto login %s success!", user.getUsername()));
        }
    }

    @Override
    public String findLoggedInUserEmail() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userDetails instanceof UserDetails ? ((UserDetails) userDetails).getUsername() : null;
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
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("No user found with email " + email));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public <T extends UserRoleDef> Optional<T> getUserRoleDefOf(Long userId, Class<T> userRoleDefClass)
            throws InvalidArgException, NoEntityFoundException {
        UserRole userRole = UserRole.defClassToUserRole(userRoleDefClass);
        if (userRole == null) {
            throw new InvalidArgException("The provided class " + userRoleDefClass.getName() +
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
            throws NoEntityFoundException, InvalidArgException, ClashException {
        addUserRoleDefToUser(user.getId(), userRoles);
    }

    @Override
    public void addUserRoleDefToUser(User user, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException, ClashException {
        addUserRoleDefToUser(user.getId(), userRoles);
    }

    @Override
    public void addUserRoleDefToUser(Long userId, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgException, ClashException {
        addUserRoleDefToUser(userId, Set.of(userRoles));
    }

    @Override
    public void addUserRoleDefToUser(Long userId, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException, ClashException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("user", "id", userId));
        List<UserRole> userRolesAlreadyInstantiated = UtilMethods.findFirstKeyThatMapContainsIfAny(
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
            throws NoEntityFoundException, InvalidArgException {
        removeUserRoleDefFromUser(user.getId(), userRoles);
    }

    @Override
    public void removeUserRoleDefFromUser(User user, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException {
        removeUserRoleDefFromUser(user.getId(), userRoles);
    }

    @Override
    public void removeUserRoleDefFromUser(Long userId, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgException {
        removeUserRoleDefFromUser(userId, Set.of(userRoles));
    }

    @Override
    public void removeUserRoleDefFromUser(Long userId, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException {
        User user = findById(userId).orElseThrow(
                () -> new NoEntityFoundException("User", "id", userId));
        for (UserRole userRole : userRoles) {
            UserRoleDef userRoleDef = user.getUserRoleDefs().get(userRole);
            UserRoleDefService<? extends UserRoleDef> userRoleDefService = userRoleDefServices.get(userRole);
            userRoleDefService.deleteById(userRoleDef.getId());
        }
    }

}
