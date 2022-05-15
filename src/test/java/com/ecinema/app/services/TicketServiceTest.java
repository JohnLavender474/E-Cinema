package com.ecinema.app.services;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.*;
import com.ecinema.app.domain.forms.TicketForm;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    private final Logger logger = LoggerFactory.getLogger(TicketServiceTest.class);

    private UserService userService;
    private TicketService ticketService;
    private ScreeningService screeningService;
    private ShowroomService showroomService;
    private ShowroomSeatService showroomSeatService;
    private ScreeningSeatService screeningSeatService;
    private CustomerAuthorityService customerAuthorityService;
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
    private CustomerAuthorityRepository customerAuthorityRepository;
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
        customerAuthorityService = new CustomerAuthorityServiceImpl(
                customerAuthorityRepository, reviewService,
                ticketService, paymentCardService, couponService);
        userService = new UserServiceImpl(
                userRepository, customerAuthorityService,
                null, null, null,
                null, null, null);
        showroomService = new ShowroomServiceImpl(
                showroomRepository, showroomSeatService,
                screeningService, null);
    }

    @Test
    void deleteTicketCascade() {
        // given
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        showroomService.save(showroom);
        for (int i = 0; i < 30; i++) {
            ShowroomSeat showroomSeat = new ShowroomSeat();
            showroomSeat.setId((long) i);
            showroomSeat.setRowLetter(Letter.A);
            showroomSeat.setSeatNumber(i);
            given(showroomSeatRepository.findById((long) i))
                    .willReturn(Optional.of(showroomSeat));
            showroomSeatService.save(showroomSeat);
        }
        Screening screening = new Screening();
        screening.setId(0L);
        screeningService.save(screening);
        List<ScreeningSeat> screeningSeats = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            final Integer j = i;
            ShowroomSeat showroomSeat = showroomSeatService.findById((long) i).orElseThrow(
                    () -> new NoEntityFoundException("showroom seat", "id", j));
            ScreeningSeat screeningSeat = new ScreeningSeat();
            screeningSeat.setId((long) i);
            screeningSeat.setShowroomSeat(showroomSeat);
            showroomSeat.getScreeningSeats().add(screeningSeat);
            screeningSeat.setScreening(screening);
            screening.getScreeningSeats().add(screeningSeat);
            screeningSeatService.save(screeningSeat);
            screeningSeats.add(screeningSeat);
        }
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setId(31L);
        customerAuthorityService.save(customerAuthority);
        Ticket ticket = new Ticket();
        ticket.setId(32L);
        ticket.setTicketOwner(customerAuthority);
        customerAuthority.getTickets().add(ticket);
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
        assertFalse(customerAuthority.getTickets().isEmpty());
        assertNotNull(ticket.getTicketOwner());
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
        assertTrue(customerAuthority.getTickets().isEmpty());
        assertNull(ticket.getTicketOwner());
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
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        showroomService.save(showroom);
        ShowroomSeat showroomSeat = new ShowroomSeat();
        showroomSeat.setRowLetter(Letter.A);
        showroomSeat.setSeatNumber(1);
        showroomSeat.setShowroom(showroom);
        showroom.getShowroomSeats().add(showroomSeat);
        showroomSeatService.save(showroomSeat);
        ScreeningSeat screeningSeat = new ScreeningSeat();
        screeningSeat.setId(1L);
        screeningSeat.setShowroomSeat(showroomSeat);
        showroomSeat.getScreeningSeats().add(screeningSeat);
        given(screeningSeatRepository.findById(1L))
                .willReturn(Optional.of(screeningSeat));
        screeningSeatService.save(screeningSeat);
        User user = new User();
        user.setId(2L);
        given(userRepository.findById(2L))
                .willReturn(Optional.of(user));
        userService.save(user);
        userService.addUserAuthorityToUser(user, UserAuthority.CUSTOMER);
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
        assertNotNull(user.getUserAuthorities().get(UserAuthority.CUSTOMER));
        CustomerAuthority customerAuthority = (CustomerAuthority) user.getUserAuthorities().get(UserAuthority.CUSTOMER);
        assertFalse(customerAuthority.getTickets().isEmpty());
        Ticket ticket = customerAuthority.getTickets()
                                         .stream().findFirst()
                                         .orElseThrow(IllegalStateException::new);
        assertEquals(customerAuthority, ticket.getTicketOwner());
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
        User user = new User();
        user.setUsername("TestUser123");
        userService.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityService.save(customerAuthority);
        Ticket ticket = new Ticket();
        ticket.setScreeningSeat(screeningSeat);
        screeningSeat.setTicket(ticket);
        ticket.setTicketOwner(customerAuthority);
        customerAuthority.getTickets().add(ticket);
        ticketService.save(ticket);
        Coupon coupon = new Coupon();
        coupon.setTicket(ticket);
        coupon.setCouponType(CouponType.FOOD_DRINK_COUPON);
        coupon.setDiscountType(DiscountType.PERCENTAGE_DISCOUNT);
        ticket.getCoupons().add(coupon);
        coupon.setCouponOwner(customerAuthority);
        customerAuthority.getCoupons().add(coupon);
        couponService.save(coupon);
        // when
        List<String> info = new ArrayList<>();
        ticketService.onDeleteInfo(ticket, info);
        // then look at logger messages
        info.forEach(logger::debug);
    }

}