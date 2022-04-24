package com.ecinema.app.services;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.entities.User;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.exceptions.ClashException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * The type User service test.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    private CustomerRoleDefService customerRoleDefService;
    private ModeratorRoleDefService moderatorRoleDefService;
    private AdminTraineeRoleDefService adminTraineeRoleDefService;
    private AdminRoleDefService adminRoleDefService;
    private TheaterService theaterService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    private ModeratorRoleDefRepository moderatorRoleDefRepository;
    @Mock
    private AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;
    @Mock
    private AdminRoleDefRepository adminRoleDefRepository;
    @Mock
    private TheaterRepository theaterRepository;


    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        theaterService = new TheaterServiceImpl(theaterRepository);
        adminTraineeRoleDefService = new AdminTraineeRoleDefServiceImpl(adminTraineeRoleDefRepository);
        adminRoleDefService = new AdminRoleDefServiceImpl(
                adminRoleDefRepository, theaterService, adminTraineeRoleDefService);
        moderatorRoleDefService = new ModeratorRoleDefServiceImpl(moderatorRoleDefRepository);
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository);
        userService = new UserServiceImpl(userRepository, customerRoleDefService, moderatorRoleDefService,
                                          adminTraineeRoleDefService, adminRoleDefService);
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        userService.deleteAll();
        customerRoleDefService.deleteAll();
        moderatorRoleDefService.deleteAll();
        adminTraineeRoleDefService.deleteAll();
        adminRoleDefService.deleteAll();
        theaterService.deleteAll();
    }

    /**
     * Generate users list list.
     *
     * @param consumer the consumer
     * @return the list
     */
    List<User> generateUsersList(Consumer<User> consumer) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            consumer.accept(user);
            users.add(user);
        }
        return users;
    }

    /**
     * Test find by email.
     */
    @Test
    void testFindByEmail() {
        // given
        String email = "test@gmail.com";
        User user = new User();
        user.setEmail(email);
        given(userRepository.findByEmail(email))
                .willReturn(Optional.of(user));
        // when
        Optional<User> userOptional = userService.findByEmail(email);
        // then
        assertTrue(userOptional.isPresent() && userOptional.get().equals(user));
        verify(userRepository, times(1)).findByEmail(email);
    }

    /**
     * Test find all by is account locked.
     */
    @Test
    void testFindAllByIsAccountLocked() {
        // given
        List<User> users = generateUsersList(user -> user.setIsAccountLocked(UtilMethods.getRandom().nextBoolean()));
        List<User> lockedUsers = users.stream()
                                      .filter(User::getIsAccountLocked)
                                      .collect(Collectors.toList());
        given(userRepository.findAllByIsAccountLocked(true))
                .willReturn(lockedUsers);
        // when
        List<User> testLockedUsers = userService.findAllByIsAccountLocked(true);
        // then
        assertEquals(lockedUsers, testLockedUsers);
        verify(userRepository, times(1)).findAllByIsAccountLocked(true);
    }

    /**
     * Test find all by is account enabled.
     */
    @Test
    void testFindAllByIsAccountEnabled() {
        // given
        List<User> users = generateUsersList(user -> user.setIsAccountEnabled(UtilMethods.getRandom().nextBoolean()));
        List<User> enabledUsers = users.stream()
                                       .filter(User::getIsAccountEnabled)
                                       .collect(Collectors.toList());
        given(userRepository.findAllByIsAccountEnabled(true))
                .willReturn(enabledUsers);
        // when
        List<User> testEnabledUsers = userService.findAllByIsAccountEnabled(true);
        // then
        assertEquals(enabledUsers, testEnabledUsers);
        verify(userRepository, times(1)).findAllByIsAccountEnabled(true);
    }

    /**
     * Test add user role def to user.
     */
    @Test
    void testAddUserRoleDefToUser() {
        // given
        User user = new User();
        userService.save(user);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setUser(user);
        user.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
        customerRoleDefService.save(customerRoleDef);
        given(customerRoleDefRepository.findByUser(user))
                .willReturn(Optional.of(customerRoleDef));
        // when
        Optional<CustomerRoleDef> testCustomerRoleDefOptional
                = customerRoleDefService.findByUser(user);
        // then
        assertTrue(testCustomerRoleDefOptional.isPresent() &&
                           customerRoleDef.equals(testCustomerRoleDefOptional.get()) &&
                           user.getUserRoleDefs().get(UserRole.CUSTOMER).equals(testCustomerRoleDefOptional.get()));
    }

    /**
     * Test remove user role def from user.
     */
    @Test
    void testRemoveUserRoleDefFromUser() {
        // given
        User user = new User();
        userService.save(user);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setUser(user);
        user.getUserRoleDefs().put(UserRole.CUSTOMER, customerRoleDef);
        customerRoleDefService.save(customerRoleDef);
        user.getUserRoleDefs().remove(UserRole.CUSTOMER);
        customerRoleDef.setUser(null);
        customerRoleDefRepository.delete(customerRoleDef);
        userService.save(user);
        // when
        when(customerRoleDefRepository.findByUser(user))
                .thenReturn(Optional.ofNullable((CustomerRoleDef) user.getUserRoleDefs().get(UserRole.CUSTOMER)));
        // then
        assertTrue(customerRoleDefService.findByUser(user).isEmpty());
    }

    @Test
    void testAddUserRoleDefsToUser() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        // when
        userService.addUserRoleDefToUser(
                user, UserRole.CUSTOMER, UserRole.ADMIN, UserRole.MODERATOR);
        // then
        assertTrue(user.getUserRoleDefs().containsKey(UserRole.CUSTOMER));
        assertTrue(user.getUserRoleDefs().get(UserRole.CUSTOMER) instanceof CustomerRoleDef);
        assertTrue(user.getUserRoleDefs().containsKey(UserRole.ADMIN));
        assertTrue(user.getUserRoleDefs().get(UserRole.ADMIN) instanceof AdminRoleDef);
        assertTrue(user.getUserRoleDefs().containsKey(UserRole.MODERATOR));
        assertTrue(user.getUserRoleDefs().get(UserRole.MODERATOR) instanceof ModeratorRoleDef);
    }

    @Test
    void testFailToAddUserRoleDefToUser() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        userService.addUserRoleDefToUser(user, UserRole.CUSTOMER);
        // then
        assertThrows(ClashException.class,
                     () -> userService.addUserRoleDefToUser(user, UserRole.CUSTOMER));
    }

}