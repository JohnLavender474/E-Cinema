package com.ecinema.app.services;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.Theater;
import com.ecinema.app.entities.User;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.constants.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AdminRoleDefServiceTest {

    AdminTraineeRoleDefService adminTraineeRoleDefService;
    ModeratorRoleDefService moderatorRoleDefService;
    CustomerRoleDefService customerRoleDefService;
    AdminRoleDefService adminRoleDefService;
    TheaterService theaterService;
    UserService userService;
    @Mock
    AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;
    @Mock
    AdminRoleDefRepository adminRoleDefRepository;
    @Mock
    ModeratorRoleDefRepository moderatorRoleDefRepository;
    @Mock
    CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TheaterRepository theaterRepository;

    @BeforeEach
    void setUp() {
        adminTraineeRoleDefService = new AdminTraineeRoleDefServiceImpl(adminTraineeRoleDefRepository);
        moderatorRoleDefService = new ModeratorRoleDefServiceImpl(moderatorRoleDefRepository);
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository);
        theaterService = new TheaterServiceImpl(theaterRepository);
        adminRoleDefService = new AdminRoleDefServiceImpl(
                adminRoleDefRepository, theaterService, adminTraineeRoleDefService);
        userService = new UserServiceImpl(
                userRepository, customerRoleDefService,
                moderatorRoleDefService, adminTraineeRoleDefService,
                adminRoleDefService);
    }

    @Test
    void testFindByUser() {
        // given
        User user = new User();
        userService.save(user);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setUser(user);
        user.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        adminRoleDefService.save(adminRoleDef);
        given(adminRoleDefRepository.findByUser(user))
                .willReturn(Optional.of(adminRoleDef));
        // when
        Optional<AdminRoleDef> adminAuthorityOptional = adminRoleDefService.findByUser(user);
        // then
        assertTrue(adminAuthorityOptional.isPresent() &&
                adminRoleDef.equals(adminAuthorityOptional.get()) &&
                adminAuthorityOptional.get().getUser().equals(user));
        verify(adminRoleDefRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddTheaterToAdminRoleDef() {
        // given
        Theater theater = new Theater();
        theater.setId(1L);
        theaterService.save(theater);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setId(2L);
        adminRoleDefService.save(adminRoleDef);
        given(theaterRepository.findById(1L))
                .willReturn(Optional.of(theater));
        given(adminRoleDefRepository.findById(2L))
                .willReturn(Optional.of(adminRoleDef));
        // when
        adminRoleDefService.addTheaterToAdminRoleDef(
                theater.getId(), adminRoleDef.getId());
        // then
        assertTrue(theater.getAdmins().contains(adminRoleDef));
        assertTrue(adminRoleDef.getTheatersBeingManaged().contains(theater));
    }

    @Test
    void testRemoveTheaterFromAdminRoleDef() {
        // given
        Theater theater = new Theater();
        theater.setId(1L);
        theaterService.save(theater);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setId(2L);
        adminRoleDefService.save(adminRoleDef);
        given(theaterRepository.findById(1L))
                .willReturn(Optional.of(theater));
        given(adminRoleDefRepository.findById(2L))
                .willReturn(Optional.of(adminRoleDef));
        adminRoleDefService.addTheaterToAdminRoleDef(
                theater.getId(), adminRoleDef.getId());
        // when
        adminRoleDefService.removeTheaterFromAdminRoleDef(
                theater.getId(), adminRoleDef.getId());
        // then
        assertFalse(theater.getAdmins().contains(adminRoleDef));
        assertFalse(adminRoleDef.getTheatersBeingManaged().contains(theater));
    }

    @Test
    void testDeleteAdminRoleDef() {
        // given
        Theater theater = new Theater();
        theater.setId(1L);
        theaterService.save(theater);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setId(2L);
        adminRoleDefService.save(adminRoleDef);
        AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
        adminTraineeRoleDef.setId(3L);
        adminTraineeRoleDefService.save(adminTraineeRoleDef);
        given(theaterRepository.findById(1L))
                .willReturn(Optional.of(theater));
        given(adminRoleDefRepository.findById(2L))
                .willReturn(Optional.of(adminRoleDef));
        given(adminTraineeRoleDefRepository.findById(3L))
                .willReturn(Optional.of(adminTraineeRoleDef));
        // when
        adminRoleDefService.addTheaterToAdminRoleDef(
                theater.getId(), adminRoleDef.getId());
        adminRoleDefService.addTraineeToAdminRoleDef(
                adminTraineeRoleDef.getId(), adminRoleDef.getId());
        // then
        assertTrue(theater.getAdmins().contains(adminRoleDef));
        assertTrue(adminRoleDef.getTheatersBeingManaged().contains(theater));
        assertEquals(adminTraineeRoleDef.getMentor(), adminRoleDef);
        assertTrue(adminRoleDef.getTrainees().contains(adminTraineeRoleDef));
        // when
        adminRoleDefService.deleteById(adminRoleDef.getId());
        // then
        assertFalse(theater.getAdmins().contains(adminRoleDef));
        assertFalse(adminRoleDef.getTheatersBeingManaged().contains(theater));
        assertNotEquals(adminTraineeRoleDef.getMentor(), adminRoleDef);
        assertFalse(adminRoleDef.getTrainees().contains(adminTraineeRoleDef));
    }

}