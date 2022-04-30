package com.ecinema.app.services;

import com.ecinema.app.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.utils.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminTraineeRoleDefServiceTest {

    private ModelMapper modelMapper;
    private AdminTraineeRoleDefService adminTraineeRoleDefService;
    private AdminRoleDefService adminRoleDefService;
    private CustomerRoleDefService customerRoleDefService;
    private ModeratorRoleDefService moderatorRoleDefService;
    private TheaterService theaterService;
    private AddressService addressService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningService screeningService;
    private ScreeningSeatService screeningSeatService;
    private TicketService ticketService;
    private ReviewService reviewService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    private UserService userService;
    @Mock
    private AdminTraineeRoleDefRepository adminTraineeRoleDefRepository;
    @Mock
    private AdminRoleDefRepository adminRoleDefRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    private ModeratorRoleDefRepository moderatorRoleDefRepository;
    @Mock
    private TheaterRepository theaterRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        adminTraineeRoleDefService = new AdminTraineeRoleDefServiceImpl(adminTraineeRoleDefRepository);
        addressService = new AddressServiceImpl(addressRepository);
        ticketService = new TicketServiceImpl(ticketRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(screeningSeatRepository, ticketService);
        screeningService = new ScreeningServiceImpl(screeningRepository, screeningSeatService);
        showroomSeatService = new ShowroomSeatServiceImpl(showroomSeatRepository, screeningSeatService);
        showroomService = new ShowroomServiceImpl(showroomRepository, showroomSeatService, screeningService);
        reviewService = new ReviewServiceImpl(reviewRepository);
        theaterService = new TheaterServiceImpl(
                theaterRepository, addressService, showroomService, screeningService);
        paymentCardService = new PaymentCardServiceImpl(paymentCardRepository, addressService);
        couponService = new CouponServiceImpl(couponRepository);
        adminRoleDefService = new AdminRoleDefServiceImpl(
                adminRoleDefRepository, theaterService, adminTraineeRoleDefService);
        customerRoleDefService = new CustomerRoleDefServiceImpl(
                customerRoleDefRepository, reviewService, ticketService,
                paymentCardService, couponService);
        moderatorRoleDefService = new ModeratorRoleDefServiceImpl(moderatorRoleDefRepository);
        userService = new UserServiceImpl(userRepository, customerRoleDefService, moderatorRoleDefService,
                                          adminTraineeRoleDefService, adminRoleDefService);
    }

    @Test
    void findAllByMentor() {
        // given
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDefService.save(adminRoleDef);
        AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
        adminTraineeRoleDef.setMentor(adminRoleDef);
        adminRoleDef.getTrainees().add(adminTraineeRoleDef);
        adminTraineeRoleDefService.save(adminTraineeRoleDef);
        List<AdminTraineeRoleDef> trainees = new ArrayList<>();
        trainees.add(adminTraineeRoleDef);
        given(adminTraineeRoleDefRepository.findAllByMentor(adminRoleDef))
                .willReturn(trainees);
        // when
        List<AdminTraineeRoleDef> testTrainees = adminTraineeRoleDefService
                .findAllByMentor(adminRoleDef);
        // then
        assertEquals(trainees, testTrainees);
        verify(adminRoleDefRepository, times(1))
                .save(adminRoleDef);
        verify(adminTraineeRoleDefRepository, times(1))
                .save(adminTraineeRoleDef);
    }

    @Test
    void findAllByPercentageTrainingModulesCompletedLessThanEqual() {
        // given
        List<AdminTraineeRoleDef> trainees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
            adminTraineeRoleDef.setPercentageTrainingModulesCompleted(i * 10);
            adminTraineeRoleDefService.save(adminTraineeRoleDef);
            trainees.add(adminTraineeRoleDef);
        }
        List<AdminTraineeRoleDef> control = trainees
                .stream().filter(trainee -> trainee.getPercentageTrainingModulesCompleted() >= 50)
                .collect(Collectors.toList());
        given(adminTraineeRoleDefRepository.findAllByPercentageTrainingModulesCompletedGreaterThanEqual(50))
                .willReturn(control);
        // when
        List<AdminTraineeRoleDef> test = adminTraineeRoleDefService
                .findAllByPercentageTrainingModulesCompletedGreaterThanEqual(50);
        // then
        assertEquals(control, test);
    }

    @Test
    void findAllByPercentageTrainingModulesCompletedGreaterThanEqual() {
        // given
        List<AdminTraineeRoleDef> trainees = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
            adminTraineeRoleDef.setPercentageTrainingModulesCompleted(i * 10);
            adminTraineeRoleDefService.save(adminTraineeRoleDef);
            trainees.add(adminTraineeRoleDef);
        }
        List<AdminTraineeRoleDef> control = trainees
                .stream().filter(trainee -> trainee.getPercentageTrainingModulesCompleted() < 70)
                .collect(Collectors.toList());
        given(adminTraineeRoleDefRepository.findAllByPercentageTrainingModulesCompletedLessThanEqual(60))
                .willReturn(control);
        // when
        List<AdminTraineeRoleDef> test = adminTraineeRoleDefService
                .findAllByPercentageTrainingModulesCompletedLessThanEqual(60);
        // then
        assertEquals(control, test);
    }

    @Test
    void deleteAdminRoleCascade() {
        // given
        User user1 = new User();
        user1.setId(1L);
        userService.save(user1);
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setId(2L);
        user1.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        adminRoleDef.setUser(user1);
        adminRoleDefService.save(adminRoleDef);
        User user2 = new User();
        user2.setId(3L);
        userService.save(user2);
        AdminTraineeRoleDef adminTraineeRoleDef = new AdminTraineeRoleDef();
        adminTraineeRoleDef.setId(4L);
        user2.getUserRoleDefs().put(UserRole.ADMIN_TRAINEE, adminTraineeRoleDef);
        adminTraineeRoleDef.setUser(user2);
        adminRoleDef.getTrainees().add(adminTraineeRoleDef);
        adminTraineeRoleDef.setMentor(adminRoleDef);
        given(adminTraineeRoleDefRepository.findById(4L))
                .willReturn(Optional.of(adminTraineeRoleDef));
        adminTraineeRoleDefService.save(adminTraineeRoleDef);
        assertNotNull(user1.getUserRoleDefs().get(UserRole.ADMIN));
        assertNotNull(user2.getUserRoleDefs().get(UserRole.ADMIN_TRAINEE));
        assertFalse(adminRoleDef.getTrainees().isEmpty());
        assertTrue(adminRoleDef.getTrainees().contains(adminTraineeRoleDef));
        assertEquals(adminRoleDef, adminTraineeRoleDef.getMentor());
        // when
        adminTraineeRoleDefService.delete(adminTraineeRoleDef);
        // then
        assertNull(user2.getUserRoleDefs().get(UserRole.ADMIN_TRAINEE));
        assertTrue(adminRoleDef.getTrainees().isEmpty());
        assertFalse(adminRoleDef.getTrainees().contains(adminTraineeRoleDef));
        assertNotEquals(adminRoleDef, adminTraineeRoleDef.getMentor());
    }

}