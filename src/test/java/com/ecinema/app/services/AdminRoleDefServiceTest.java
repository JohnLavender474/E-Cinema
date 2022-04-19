package com.ecinema.app.services;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.User;
import com.ecinema.app.repositories.AdminRoleDefRepository;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.implementations.AdminRoleDefServiceImpl;
import com.ecinema.app.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminRoleDefServiceTest {

    @InjectMocks
    AdminRoleDefServiceImpl adminAuthorityService;

    @Mock
    AdminRoleDefRepository adminRoleDefRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Test
    void testFindByUser() {
        // given
        User user = new User();
        userService.save(user);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setUser(user);
        adminAuthorityService.save(adminRoleDef);
        given(adminRoleDefRepository.findByUser(user))
                .willReturn(Optional.of(adminRoleDef));
        // when
        Optional<AdminRoleDef> adminAuthorityOptional = adminAuthorityService.findByUser(user);
        // then
        assertTrue(adminAuthorityOptional.isPresent() &&
                adminRoleDef.equals(adminAuthorityOptional.get()) &&
                adminAuthorityOptional.get().getUser().equals(user));
        verify(adminRoleDefRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).save(user);
    }

}