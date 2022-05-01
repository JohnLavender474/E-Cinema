package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.AdminRoleDefDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.UserRole;
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

    private AddressService addressService;
    private AdminTraineeRoleDefService adminTraineeRoleDefService;
    private ModeratorRoleDefService moderatorRoleDefService;
    private CustomerRoleDefService customerRoleDefService;
    private AdminRoleDefService adminRoleDefService;
    private TheaterService theaterService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningService screeningService;
    private ScreeningSeatService screeningSeatService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    private UserService userService;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;
    @Mock
    private AdminRoleDefRepository adminRoleDefRepository;
    @Mock
    private ModeratorRoleDefRepository moderatorRoleDefRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TheaterRepository theaterRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private CouponRepository couponRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        addressService = new AddressServiceImpl(addressRepository);
        reviewService = new ReviewServiceImpl(reviewRepository);
        ticketService = new TicketServiceImpl(ticketRepository);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, addressService);
        couponService = new CouponServiceImpl(couponRepository);
        adminTraineeRoleDefService = new AdminTraineeRoleDefServiceImpl(adminTraineeRoleDefRepository);
        moderatorRoleDefService = new ModeratorRoleDefServiceImpl(moderatorRoleDefRepository);
        customerRoleDefService = new CustomerRoleDefServiceImpl(customerRoleDefRepository, reviewService,
                                                                ticketService, paymentCardService, couponService);
        screeningSeatService = new ScreeningSeatServiceImpl(screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(screeningRepository, screeningSeatService);
        showroomSeatService = new ShowroomSeatServiceImpl(showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(showroomRepository, showroomSeatService, screeningService);
        theaterService = new TheaterServiceImpl(theaterRepository, addressService,
                                                showroomService, screeningService);
        adminRoleDefService = new AdminRoleDefServiceImpl(adminRoleDefRepository, theaterService,
                                                          adminTraineeRoleDefService);
        userService = new UserServiceImpl(userRepository, customerRoleDefService,
                                          moderatorRoleDefService, adminTraineeRoleDefService, adminRoleDefService);
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
    void deleteAdminRoleDef1() {
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

    @Test
    void deleteAdminAndCascade() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setId(2L);
        adminRoleDef.setUser(user);
        user.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        given(adminRoleDefRepository.findById(2L))
                .willReturn(Optional.of(adminRoleDef));
        adminRoleDefService.save(adminRoleDef);
        AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
        adminTraineeRoleDef.setId(3L);
        adminTraineeRoleDef.setMentor(adminRoleDef);
        adminRoleDef.getTrainees().add(adminTraineeRoleDef);
        adminTraineeRoleDefService.save(adminTraineeRoleDef);
        assertNotNull(adminRoleDef.getUser());
        assertNotNull(user.getUserRoleDefs().get(UserRole.ADMIN));
        assertNotNull(adminTraineeRoleDef.getMentor());
        assertFalse(adminRoleDef.getTrainees().isEmpty());
        // when
        adminRoleDefService.delete(adminRoleDef);
        // then
        assertNull(adminRoleDef.getUser());
        assertNull(user.getUserRoleDefs().get(UserRole.ADMIN));
        assertNull(adminTraineeRoleDef.getMentor());
        assertTrue(adminRoleDef.getTrainees().isEmpty());
    }

    @Test
    void adminRoleDefDto() {
        // given
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setId(1L);
        adminRoleDefService.save(adminRoleDef);
        given(adminRoleDefRepository.findById(1L))
                .willReturn(Optional.of(adminRoleDef));
        // when
        AdminRoleDefDto adminRoleDefDto = adminRoleDefService.convertToDto(1L);
        // then
        assertEquals(adminRoleDef.getId(), adminRoleDefDto.getId());
    }

}