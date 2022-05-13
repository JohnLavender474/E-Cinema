package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.AdminAuthorityDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
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

/**
 * The type Admin role def service test.
 */
@ExtendWith(MockitoExtension.class)
class AdminAuthorityServiceTest {

    private ModeratorAuthorityService moderatorAuthorityService;
    private CustomerAuthorityService customerAuthorityService;
    private AdminAuthorityService adminAuthorityService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    private UserService userService;
    @Mock
    private AdminAuthorityRepository adminAuthorityRepository;
    @Mock
    private ModeratorAuthorityRepository moderatorAuthorityRepository;
    @Mock
    private CustomerAuthorityRepository customerAuthorityRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewRepository reviewRepository;
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
        reviewService = new ReviewServiceImpl(
                reviewRepository, null,
                customerAuthorityRepository, null);
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        customerAuthorityService = new CustomerAuthorityServiceImpl(
                customerAuthorityRepository, reviewService,
                ticketService, paymentCardService, couponService);
        moderatorAuthorityService = new ModeratorAuthorityServiceImpl(moderatorAuthorityRepository,
                                                                      customerAuthorityService);
        adminAuthorityService = new AdminAuthorityServiceImpl(adminAuthorityRepository);
        userService = new UserServiceImpl(
                userRepository, customerAuthorityService,
                moderatorAuthorityService, adminAuthorityService, null);
    }

    /**
     * Find by user.
     */
    @Test
    void findByUser() {
        // given
        User user = new User();
        userService.save(user);
        AdminAuthority adminAuthority = new AdminAuthority();
        adminAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.ADMIN, adminAuthority);
        adminAuthorityService.save(adminAuthority);
        given(adminAuthorityRepository.findByUser(user))
                .willReturn(Optional.of(adminAuthority));
        // when
        Optional<AdminAuthority> adminAuthorityOptional = adminAuthorityService.findByUser(user);
        // then
        assertTrue(adminAuthorityOptional.isPresent() &&
                           adminAuthority.equals(adminAuthorityOptional.get()) &&
                           adminAuthorityOptional.get().getUser().equals(user));
        verify(adminAuthorityRepository, times(1)).findByUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deleteAdminAndCascade() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        AdminAuthority adminAuthority = new AdminAuthority();
        adminAuthority.setId(2L);
        adminAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.ADMIN, adminAuthority);
        given(adminAuthorityRepository.findById(2L))
                .willReturn(Optional.of(adminAuthority));
        adminAuthorityService.save(adminAuthority);
        assertNotNull(adminAuthority.getUser());
        assertNotNull(user.getUserAuthorities().get(UserAuthority.ADMIN));
        // when
        adminAuthorityService.delete(adminAuthority);
        // then
        assertNull(adminAuthority.getUser());
        assertNull(user.getUserAuthorities().get(UserAuthority.ADMIN));
    }

    @Test
    void adminRoleDefDto() {
        // given
        AdminAuthority adminAuthority = new AdminAuthority();
        adminAuthority.setId(1L);
        adminAuthorityService.save(adminAuthority);
        given(adminAuthorityRepository.findById(1L))
                .willReturn(Optional.of(adminAuthority));
        // when
        AdminAuthorityDto adminAuthorityDto = adminAuthorityService.convertIdToDto(1L);
        // then
        assertEquals(adminAuthority.getId(), adminAuthorityDto.getId());
    }

}