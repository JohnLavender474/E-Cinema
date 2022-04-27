package com.ecinema.app.services;

import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.exceptions.ClashException;
import com.ecinema.app.utils.exceptions.InvalidArgException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The interface User service.
 */
public interface UserService extends AbstractService<User>, UserDetailsService {

    /**
     * Find all by {@link User#getIsAccountLocked()} equal to isAccountLocked and return as list.
     *
     * @param isAccountLocked the value of getIsAccountLocked() for all User instances in returned list.
     * @return the list of User instances with locked value equal to isAccountLocked.
     */
    List<User> findAllByIsAccountLocked(boolean isAccountLocked);

    /**
     * Find all by {@link User#getIsAccountEnabled()} equal to isAccountEnabled and return as list.
     *
     * @param isAccountEnabled the value of getIsAccountEnabled() for all User instances in returned list.
     * @return the list of User instances with account enabled value equal to isAccountEnabled.
     */
    List<User> findAllByIsAccountEnabled(boolean isAccountEnabled);

    /**
     * Find all {@link User#getIsAccountExpired()} equal to isAccountExpired and return as list.
     *
     * @param isAccountExpired the value of getIsAccountExpired() for all User instances in returned list.
     * @return the list of User instances with expired value equal to isAccountExpired.
     */
    List<User> findAllByIsAccountExpired(boolean isAccountExpired);

    /**
     * Find all {@link User#getIsCredentialsExpired()} equal to isCredentialsExpired and return as list.
     *
     * @param isCredentialsExpired the value of getIsCredentialsExpired() for all User instances in returned list.
     * @return the list of User instances with credentials expired value equal to isCredentialsExpired.
     */
    List<User> findAllByIsCredentialsExpired(boolean isCredentialsExpired);

    /**
     * Find all by {@link User#getCreationDateTime()} before localDateTime.
     *
     * @param localDateTime the max value of localDateTime in returned list.
     * @return the list of User instances with creationDateTime less than localDateTime.
     */
    List<User> findAllByCreationDateTimeBefore(LocalDateTime localDateTime);

    /**
     * Find all by {@link User#getCreationDateTime()} after localDateTime.
     *
     * @param localDateTime the min value of LocalDateTime in returned list.
     * @return the list of User instances with creationDateTime greater than localDateTime.
     */
    List<User> findAllByCreationDateTimeAfter(LocalDateTime localDateTime);

    /**
     * Find all by {@link User#getLastActivityDateTime()} before localDateTime.
     *
     * @param localDateTime the max value of LocalDateTime in returned list.
     * @return the list of User instances with lastActivityDateTime less than localDateTime.
     */
    List<User> findAllByLastActivityDateTimeBefore(LocalDateTime localDateTime);

    /**
     * Find all by {@link User#getLastActivityDateTime()} after localDateTime.
     *
     * @param localDateTime the min value of LocalDateTime in returned list.
     * @return the list of User instances with lastActivityDateTime greater than localDateTime.
     */
    List<User> findAllByLastActivityDateTimeAfter(LocalDateTime localDateTime);

    /**
     * Exists by username boolean.
     *
     * @param username the username
     * @return the boolean
     */
    boolean existsByUsername(String username);

    /**
     * Find by {@link User#getEmail()} optional.
     *
     * @param email the email of the User
     * @return the optional User
     */
    Optional<User> findByEmail(String email);

    /**
     * Gets user role def of {@link User}. Use {@link UserRole#defClassToUserRole(Class)} for the class param, i.e.:
     * <p>
     * {@code
     * Class<? extends UserRoleDef> clazz = UserRole.defClassToUserRole(UserRole.CUSTOMERS_PERMITTED);
     * CustomerRoleDef customerRoleDef = userService.getUserRoleDefOf(user, clazz);
     * }********
     * <p>
     * or
     * <p>
     * {@code
     * CustomerRoleDef customerRoleDef = userService.getUserRoleDefOf(user, UserRole.defClassToUserRole(UserRole
     * .CUSTOMERS_PERMITTED));
     * }********
     *
     * @param <T>    the type parameter, class must extend {@link UserRoleDef}.
     * @param userId the id of the User to fetch the UserRoleDef instance from.
     * @param clazz  the clazz of the UserRoleDef
     * @return the UserRoleDef to be fetched
     * @throws InvalidArgException    thrown if the UserRoleDef class is invalid.
     * @throws NoEntityFoundException thrown if no User exists associated with userId.
     */
    <T extends UserRoleDef> Optional<T> getUserRoleDefOf(Long userId, Class<T> clazz)
            throws InvalidArgException, NoEntityFoundException;

    /**
     * Returns true if there is a User that already has the provided email.
     *
     * @param email the email
     * @return true if there is a User that already has the provided email.
     */
    boolean existsByEmail(String email);

    /**
     * Find by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Find by username or email optional.
     *
     * @param s the s
     * @return the optional
     */
    Optional<User> findByUsernameOrEmail(String s);

    /**
     * See {@link #addUserRoleDefToUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userRoles the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgException    the invalid arg exception
     * @throws ClashException         the clash exception
     */
    void addUserRoleDefToUser(User user, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgException, ClashException;

    /**
     * See {@link #addUserRoleDefToUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userRoles the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgException    the invalid arg exception
     * @throws ClashException         the clash exception
     */
    void addUserRoleDefToUser(User user, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException, ClashException;

    /**
     * See {@link #addUserRoleDefToUser(Long, Set)} @param userId the user id
     *
     * @param userId    the user id
     * @param userRoles the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgException    the invalid arg exception
     * @throws ClashException         the clash exception
     */
    void addUserRoleDefToUser(Long userId, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgException, ClashException;

    /**
     * Instantiates a new {@link UserRoleDef} instance and maps it to the {@link User} with id equal to userId.
     * The User instance internally contains an enum map for UserRoleDef instances with {@link UserRole} as the key.
     * Each class extending UserRoleDef has a one-to-one mapping with a UserRole value. See {@link
     * UserRoleDef#getUserRole()}***.
     * User instances can only be mapped to one instance of each child class of UserRoleDef.
     *
     * @param userId    the user id of the User
     * @param userRoles the user roles corresponding to the UserRoleDef entities to map to the User
     * @throws NoEntityFoundException thrown if no User instance is found with id equal to userId.
     * @throws InvalidArgException    thrown if the provided value for userRole is invalid.
     * @throws ClashException         thrown if the User instance is already mapped to a UserRoleDef
     *                                instance corresponding to userRole.
     */
    void addUserRoleDefToUser(Long userId, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException, ClashException;

    /**
     * See {@link #removeUserRoleDefFromUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userRoles the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgException    the invalid arg exception
     */
    void removeUserRoleDefFromUser(User user, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgException;

    /**
     * See {@link #removeUserRoleDefFromUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userRoles the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgException    the invalid arg exception
     */
    void removeUserRoleDefFromUser(User user, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException;

    /**
     * See {@link #removeUserRoleDefFromUser(Long, Set)} @param userId the user id
     *
     * @param userId    the user id
     * @param userRoles the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgException    the invalid arg exception
     */
    void removeUserRoleDefFromUser(Long userId, UserRole... userRoles)
            throws NoEntityFoundException, InvalidArgException;

    /**
     * Removes the {@link UserRoleDef} instance that is mapped with the provided {@link UserRole} (see
     * {@link UserRoleDef#getUserRole()}) value from the {@link User} with id equal to userId.
     *
     * @param userId    the id of the User
     * @param userRoles the UserRole values that the UserRoleDef instance is mapped to.
     * @throws NoEntityFoundException thrown if no User instance is found with id equal to userId.
     * @throws InvalidArgException    thrown if the provided value of userRole is invalid.
     */
    void removeUserRoleDefFromUser(Long userId, Set<UserRole> userRoles)
            throws NoEntityFoundException, InvalidArgException;

}
