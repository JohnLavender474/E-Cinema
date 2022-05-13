package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.entities.AbstractUserAuthority;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.domain.contracts.IPassword;
import com.ecinema.app.domain.contracts.IRegistration;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * {@inheritDoc}
 * The interface User service. Acts as {@link UserDetailsService} for Spring Security.
 * Implements convertToDto method to convertToDto {@link User} using Long id to {@link UserDto}.
 */
public interface UserService extends UserDetailsService,
                                     AbstractService<User>,
                                     EntityDtoConverter<User, UserDto> {

    /**
     * Registers a new {@link User}. The {@link IRegistration} being passed into this method must
     * already have {@link IRegistration#getPassword()}, {@link IRegistration#getConfirmPassword()},
     * {@link IRegistration#getSecurityAnswer1()}, and {@link IRegistration#getSecurityAnswer2()}
     * encoded using {@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder}.
     *
     * @param registration the registration details used to instantiate a new User.
     * @return the user dto
     */
    UserDto register(IRegistration registration);

    /**
     * Find all by {@link User#getIsAccountLocked()} equal to isAccountLocked and return as list.
     *
     * @param isAccountLocked the value of getIsAccountLocked() for all User instances in returned list.
     * @return the list of User instances with locked value equal to isAccountLocked.
     */
    List<UserDto> findAllByIsAccountLocked(boolean isAccountLocked);

    /**
     * Find all by {@link User#getIsAccountEnabled()} equal to isAccountEnabled and return as list.
     *
     * @param isAccountEnabled the value of getIsAccountEnabled() for all User instances in returned list.
     * @return the list of User instances with account enabled value equal to isAccountEnabled.
     */
    List<UserDto> findAllByIsAccountEnabled(boolean isAccountEnabled);

    /**
     * Find all {@link User#getIsAccountExpired()} equal to isAccountExpired and return as list.
     *
     * @param isAccountExpired the value of getIsAccountExpired() for all User instances in returned list.
     * @return the list of User instances with expired value equal to isAccountExpired.
     */
    List<UserDto> findAllByIsAccountExpired(boolean isAccountExpired);

    /**
     * Find all {@link User#getIsCredentialsExpired()} equal to isCredentialsExpired and return as list.
     *
     * @param isCredentialsExpired the value of getIsCredentialsExpired() for all User instances in returned list.
     * @return the list of User instances with credentials expired value equal to isCredentialsExpired.
     */
    List<UserDto> findAllByIsCredentialsExpired(boolean isCredentialsExpired);

    /**
     * Find all by {@link User#getCreationDateTime()} before localDateTime.
     *
     * @param localDateTime the max value of localDateTime in returned list.
     * @return the list of User instances with creationDateTime less than localDateTime.
     */
    List<UserDto> findAllByCreationDateTimeBefore(LocalDateTime localDateTime);

    /**
     * Find all by {@link User#getCreationDateTime()} after localDateTime.
     *
     * @param localDateTime the min value of LocalDateTime in returned list.
     * @return the list of User instances with creationDateTime greater than localDateTime.
     */
    List<UserDto> findAllByCreationDateTimeAfter(LocalDateTime localDateTime);

    /**
     * Find all by {@link User#getLastActivityDateTime()} before localDateTime.
     *
     * @param localDateTime the max value of LocalDateTime in returned list.
     * @return the list of User instances with lastActivityDateTime less than localDateTime.
     */
    List<UserDto> findAllByLastActivityDateTimeBefore(LocalDateTime localDateTime);

    /**
     * Find all by {@link User#getLastActivityDateTime()} after localDateTime.
     *
     * @param localDateTime the min value of LocalDateTime in returned list.
     * @return the list of User instances with lastActivityDateTime greater than localDateTime.
     */
    List<UserDto> findAllByLastActivityDateTimeAfter(LocalDateTime localDateTime);

    /**
     * User roles as list of strings list.
     *
     * @param userId the user id
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<String> userAuthoritiesAsListOfStrings(Long userId)
            throws NoEntityFoundException;

    /**
     * User roles set.
     *
     * @param userId the user id
     * @return the set
     * @throws NoEntityFoundException the no entity found exception
     */
    Set<UserAuthority> userAuthorities(Long userId)
            throws NoEntityFoundException;

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
    UserDto findByEmail(String email);

    /**
     * Gets user role def of {@link User}. Use {@link UserAuthority#defClassToUserRole(Class)} for the class param,
     * i.e.:
     * <p>
     * {@code
     * Class<? extends AbstractUserAuthority> clazz =
     * UserAuthority.defClassToUserRole(UserAuthority.CUSTOMERS_PERMITTED);
     * CustomerAuthority customerRoleDef = userService.getUserAuthorityOf(user, clazz);
     * }****************
     * <p>
     * or
     * <p>
     * {@code
     * CustomerAuthority customerAuthority = userService.getUserAuthorityOf(user,
     * UserAuthority.classToEnum(UserAuthority.CUSTOMER));
     * }****************
     *
     * @param <T>    the type parameter, class must extend {@link AbstractUserAuthority}.
     * @param userId the id of the User to fetch the AbstractUserAuthority instance from.
     * @param clazz  the clazz of the AbstractUserAuthority
     * @return the AbstractUserAuthority to be fetched
     * @throws InvalidArgsException   thrown if the AbstractUserAuthority class is invalid.
     * @throws NoEntityFoundException thrown if no User exists associated with userId.
     */
    <T extends AbstractUserAuthority> Optional<T> getUserAuthorityOf(Long userId, Class<T> clazz)
            throws InvalidArgsException, NoEntityFoundException;


    /**
     * Find id by username or email long.
     *
     * @param s the s
     * @return the long
     */
    Long findIdByUsernameOrEmail(@Param("s") String s);


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
    UserDto findByUsername(String username);

    /**
     * Find by username or email optional.
     *
     * @param s the s
     * @return the optional
     */
    UserDto findByUsernameOrEmail(String s);

    /**
     * Find id by username optional.
     *
     * @param username the username
     * @return the optional
     */
    Optional<Long> findIdByUsername(String username);

    /**
     * Findid by email optional.
     *
     * @param email the email
     * @return the optional
     */
    Optional<Long> findIdByEmail(String email);

    /**
     * See {@link #addUserAuthorityToUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userAuthorities the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid arg exception
     * @throws ClashException         the clash exception
     */
    void addUserAuthorityToUser(User user, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException;

    /**
     * See {@link #addUserAuthorityToUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userAuthorities the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid arg exception
     * @throws ClashException         the clash exception
     */
    void addUserAuthorityToUser(User user, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException;

    /**
     * See {@link #addUserAuthorityToUser(Long, Set)} @param userId the user id
     *
     * @param userId    the user id
     * @param userAuthorities the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid arg exception
     * @throws ClashException         the clash exception
     */
    void addUserAuthorityToUser(Long userId, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException;

    /**
     * Instantiates a new {@link AbstractUserAuthority} instance and maps it to the {@link User} with id equal to userId.
     * The User instance internally contains an enum map for AbstractUserAuthority instances with {@link UserAuthority} as the key.
     * Each class extending AbstractUserAuthority has a one-to-one mapping with a UserAuthority value. See {@link
     * AbstractUserAuthority#getUserAuthority()}***********.
     * User instances can only be mapped to one instance of each child class of AbstractUserAuthority.
     *
     * @param userId    the user id of the User
     * @param userAuthorities the user roles corresponding to the AbstractUserAuthority entities to map to the User
     * @throws NoEntityFoundException thrown if no User instance is found with id equal to userId.
     * @throws InvalidArgsException   thrown if the provided value for userRole is invalid.
     * @throws ClashException         thrown if the User instance is already mapped to a AbstractUserAuthority
     *                                instance corresponding to userRole.
     */
    void addUserAuthorityToUser(Long userId, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException, ClashException;

    /**
     * See {@link #removeUserAuthorityFromUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userAuthorities the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid arg exception
     */
    void removeUserAuthorityFromUser(User user, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException;

    /**
     * See {@link #removeUserAuthorityFromUser(Long, Set)} @param user the user
     *
     * @param user      the user
     * @param userAuthorities the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid arg exception
     */
    void removeUserAuthorityFromUser(User user, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException;

    /**
     * See {@link #removeUserAuthorityFromUser(Long, Set)} @param userId the user id
     *
     * @param userId    the user id
     * @param userAuthorities the user roles
     * @throws NoEntityFoundException the no entity found exception
     * @throws InvalidArgsException   the invalid arg exception
     */
    void removeUserAuthorityFromUser(Long userId, UserAuthority... userAuthorities)
            throws NoEntityFoundException, InvalidArgsException;

    /**
     * Removes the {@link AbstractUserAuthority} instance that is mapped with the provided {@link UserAuthority} (see
     * {@link AbstractUserAuthority#getUserAuthority()}) value from the {@link User} with id equal to userId.
     *
     * @param userId    the id of the User
     * @param userAuthorities the UserAuthority values that the AbstractUserAuthority instance is mapped to.
     * @throws NoEntityFoundException thrown if no User instance is found with id equal to userId.
     * @throws InvalidArgsException   thrown if the provided value of userRole is invalid.
     */
    void removeUserAuthorityFromUser(Long userId, Set<UserAuthority> userAuthorities)
            throws NoEntityFoundException, InvalidArgsException;

    /**
     * Request password change.
     *
     * @param iPassword the password
     * @throws InvalidArgsException the invalid args exception
     */
    void requestPasswordChange(IPassword iPassword)
            throws InvalidArgsException;

}
