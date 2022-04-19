package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.implementations.CustomerRoleDefServiceImpl;
import com.ecinema.app.services.implementations.UserServiceImpl;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.constants.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomerRoleDefServiceImpl customerRoleDefService;

    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;

    List<User> generateUsersList(Consumer<User> consumer) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            User user = new User();
            consumer.accept(user);
            users.add(user);
        }
        return users;
    }

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

}