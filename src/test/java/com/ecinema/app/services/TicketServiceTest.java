package com.ecinema.app.services;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.TicketStatus;
import com.ecinema.app.domain.enums.TicketType;
import com.ecinema.app.domain.enums.UserRole;
import com.ecinema.app.domain.forms.TicketForm;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    private final Logger logger = LoggerFactory.getLogger(TicketServiceTest.class);

    private UserService userService;
    private TicketService ticketService;
    private ScreeningService screeningService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningSeatService screeningSeatService;
    private CustomerRoleDefService customerRoleDefService;
    private ReviewService reviewService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    @Mock
    private ShowroomRepository showroomRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ScreeningRepository screeningRepository;
    @Mock
    private ShowroomSeatRepository showroomSeatRepository;
    @Mock
    private ScreeningSeatRepository screeningSeatRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        ticketService = new TicketServiceImpl(
                ticketRepository, userRepository,
                couponRepository, screeningSeatRepository);
        screeningSeatService = new ScreeningSeatServiceImpl(
                screeningSeatRepository, ticketService);
        showroomSeatService = new ShowroomSeatServiceImpl(
                showroomSeatRepository, screeningSeatService);
        screeningService = new ScreeningServiceImpl(
                screeningRepository, movieRepository,
                showroomRepository,  screeningSeatService, null);
        reviewService = new ReviewServiceImpl(
                reviewRepository, movieRepository,
                null, null);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        customerRoleDefService = new CustomerRoleDefServiceImpl(
                customerRoleDefRepository, reviewService,
                ticketService, paymentCardService, couponService);
        userService = new UserServiceImpl(
                userRepository, customerRoleDefService,
                null, null, null);
    }

    @Test
    void deleteTicketCascade() {
        // given
        Screening screening = new Screening();
        screening.setId(0L);
        screeningService.save(screening);
        List<ScreeningSeat> screeningSeats = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ScreeningSeat screeningSeat = new ScreeningSeat();
            screeningSeat.setId((long) i + 1);
            screeningSeat.setScreening(screening);
            screening.getScreeningSeats().add(screeningSeat);
            screeningSeatService.save(screeningSeat);
            screeningSeats.add(screeningSeat);
        }
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDef.setId(31L);
        customerRoleDefService.save(customerRoleDef);
        Ticket ticket = new Ticket();
        ticket.setId(32L);
        ticket.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getTickets().add(ticket);
        ticket.setScreeningSeat(screeningSeats.get(0));
        screeningSeats.get(0).setTicket(ticket);
        given(ticketRepository.findById(32L))
                .willReturn(Optional.of(ticket));
        ticketService.save(ticket);
        Coupon coupon = new Coupon();
        coupon.setId(33L);
        coupon.setTicket(ticket);
        ticket.getCoupons().add(coupon);
        couponService.save(coupon);
        assertFalse(customerRoleDef.getTickets().isEmpty());
        assertNotNull(ticket.getCustomerRoleDef());
        assertEquals(screeningSeats.get(0), ticket.getScreeningSeat());
        assertEquals(ticket, screeningSeats.get(0).getTicket());
        assertEquals(ticket, coupon.getTicket());
        assertEquals(1, ticket.getCoupons().size());
        for (ScreeningSeat screeningSeat : screeningSeats) {
            assertEquals(screening, screeningSeat.getScreening());
        }
        // when
        ticketService.delete(ticket);
        // then
        assertTrue(customerRoleDef.getTickets().isEmpty());
        assertNull(ticket.getCustomerRoleDef());
        assertNotEquals(screeningSeats.get(0), ticket.getScreeningSeat());
        for (ScreeningSeat screeningSeat : screeningSeats) {
            assertEquals(screening, screeningSeat.getScreening());
        }
        assertNull(coupon.getTicket());
        assertEquals(0, ticket.getCoupons().size());
    }

    @Test
    void submitTicketForm() {
        // given
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(1L);
        given(screeningSeatRepository.findById(1L))
                .willReturn(Optional.of(screeningSeat));
        screeningSeatService.save(screeningSeat);
        User user = new User();
        user.setId(2L);
        given(userRepository.findById(2L))
                .willReturn(Optional.of(user));
        userService.save(user);
        userService.addUserRoleDefToUser(user, UserRole.CUSTOMER);
        Coupon coupon = new Coupon();
        coupon.setId(3L);
        given(couponRepository.findAllById(List.of(3L)))
                .willReturn(List.of(coupon));
        couponService.save(coupon);
        // when
        TicketForm ticketForm = new TicketForm();
        ticketForm.setTicketType(TicketType.ADULT);
        ticketForm.setScreeningSeatId(1L);
        ticketForm.getCouponIds().add(3L);
        ticketForm.setUserId(2L);
        ticketService.submitTicketForm(ticketForm);
        // then
        assertNotNull(user.getUserRoleDefs().get(UserRole.CUSTOMER));
        CustomerRoleDef customerRoleDef = (CustomerRoleDef) user.getUserRoleDefs().get(UserRole.CUSTOMER);
        assertFalse(customerRoleDef.getTickets().isEmpty());
        Ticket ticket = customerRoleDef.getTickets()
                                       .stream().findFirst()
                                       .orElseThrow(IllegalStateException::new);
        assertEquals(customerRoleDef, ticket.getCustomerRoleDef());
        assertEquals(screeningSeat, ticket.getScreeningSeat());
        assertEquals(ticket, screeningSeat.getTicket());
        assertEquals(TicketStatus.VALID, ticket.getTicketStatus());
        assertEquals(TicketType.ADULT, ticket.getTicketType());
        assertEquals(1, ticket.getCoupons().size());
        Coupon testCoupon = ticket.getCoupons()
                                  .stream().findFirst().orElseThrow(
                        IllegalStateException::new);
        assertEquals(ticket, testCoupon.getTicket());
    }

    @Test
    void onDeleteInfo() {
        // given
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setRowLetter(Letter.A);
        showroomSeat.setSeatNumber(1);
        showroomSeatService.save(showroomSeat);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        screeningSeatService.save(screeningSeat);
        CustomerRoleDef customerRoleDef = new CustomerRoleDef();
        customerRoleDefService.save(customerRoleDef);
        Ticket ticket = new Ticket();
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        ticket.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getTickets().add(ticket);
        ticketService.save(ticket);
        Coupon coupon = new Coupon();
        coupon.setTicket(ticket);
        ticket.getCoupons().add(coupon);
        coupon.setCustomerRoleDef(customerRoleDef);
        customerRoleDef.getCoupons().add(coupon);
        couponService.save(coupon);
        // when
        List<String> info = new ArrayList<>();
        ticketService.onDeleteInfo(ticket, info);
        // then look at logger messages
        info.forEach(logger::debug);
    }

}