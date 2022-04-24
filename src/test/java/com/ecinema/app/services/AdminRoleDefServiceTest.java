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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * The type Admin role def service test.
 */
@ExtendWith(MockitoExtension.class)
class AdminRoleDefServiceTest {

    /**
     * The Admin trainee role def service.
     */
    AdminTraineeRoleDefService adminTraineeRoleDefService;
    /**
     * The Moderator role def service.
     */
    ModeratorRoleDefService moderatorRoleDefService;
    /**
     * The Customer role def service.
     */
    CustomerRoleDefService customerRoleDefService;
    /**
     * The Admin role def service.
     */
    AdminRoleDefService adminRoleDefService;
    /**
     * The Theater service.
     */
    TheaterService theaterService;
    /**
     * The User service.
     */
    UserService userService;
    /**
     * The Admin trainee role def repository.
     */
    @Mock
    AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;
    /**
     * The Admin role def repository.
     */
    @Mock
    AdminRoleDefRepository adminRoleDefRepository;
    /**
     * The Moderator role def repository.
     */
    @Mock
    ModeratorRoleDefRepository moderatorRoleDefRepository;
    /**
     * The Customer role def repository.
     */
    @Mock
    CustomerRoleDefRepository customerRoleDefRepository;
    /**
     * The User repository.
     */
    @Mock
    UserRepository userRepository;
    /**
     * The Theater repository.
     */
    @Mock
    TheaterRepository theaterRepository;

    /**
     * Sets up.
     */
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

    /**
     * Find by user.
     */
    @Test
    void findByUser() {
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

    /**
     * Add theater to admin role def.
     */
    @Test
    void addTheaterToAdminRoleDef() {
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

    /**
     * Remove theater from admin role def.
     */
    @Test
    void removeTheaterFromAdminRoleDef() {
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

    /**
     * Delete admin role def.
     */
    @Test
    void deleteAdminRoleDef() {
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
        // given
        given(adminTraineeRoleDefRepository.findAllByMentor(adminRoleDef))
                .willReturn(List.of(adminTraineeRoleDef));
        // when
        List<AdminTraineeRoleDef> shouldContain = adminTraineeRoleDefService.findAllByMentor(adminRoleDef);
        // then
        assertTrue(shouldContain.contains(adminTraineeRoleDef));
        // when
        adminRoleDefService.removeTraineeFromAdminRoleDef(
                adminTraineeRoleDef.getId(), adminRoleDef.getId());
        given(adminTraineeRoleDefRepository.findAllByMentor(adminRoleDef))
                .willReturn(List.of());
        List<AdminTraineeRoleDef> shouldNotContain = adminTraineeRoleDefService.findAllByMentor(adminRoleDef);
        // then
        assertFalse(shouldNotContain.contains(adminTraineeRoleDef));
        // when
        adminRoleDefService.deleteById(adminRoleDef.getId());
        // then
        assertFalse(theater.getAdmins().contains(adminRoleDef));
        assertFalse(adminRoleDef.getTheatersBeingManaged().contains(theater));
        assertNotEquals(adminTraineeRoleDef.getMentor(), adminRoleDef);
        assertFalse(adminRoleDef.getTrainees().contains(adminTraineeRoleDef));
        // when
    }

}