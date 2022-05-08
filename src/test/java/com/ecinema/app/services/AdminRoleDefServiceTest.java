package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.AdminRoleDefDto;
import com.ecinema.app.domain.entities.*;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.domain.enums.UserRole;
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
class AdminRoleDefServiceTest {

    private ModeratorRoleDefService moderatorRoleDefService;
    private CustomerRoleDefService customerRoleDefService;
    private AdminRoleDefService adminRoleDefService;
    private ReviewService reviewService;
    private TicketService ticketService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    private UserService userService;
    @Mock
    private AdminRoleDefRepository adminRoleDefRepository;
    @Mock
    private ModeratorRoleDefRepository moderatorRoleDefRepository;
    @Mock
    private CustomerRoleDefRepository customerRoleDefRepository;
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
                customerRoleDefRepository, null);
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        customerRoleDefService = new CustomerRoleDefServiceImpl(
                customerRoleDefRepository, reviewService,
                ticketService, paymentCardService, couponService);
        moderatorRoleDefService = new ModeratorRoleDefServiceImpl(moderatorRoleDefRepository,
                                                                  customerRoleDefService);
        adminRoleDefService = new AdminRoleDefServiceImpl(adminRoleDefRepository);
        userService = new UserServiceImpl(
                userRepository, customerRoleDefService,
                moderatorRoleDefService, adminRoleDefService, null);
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
        assertNotNull(adminRoleDef.getUser());
        assertNotNull(user.getUserRoleDefs().get(UserRole.ADMIN));
        // when
        adminRoleDefService.delete(adminRoleDef);
        // then
        assertNull(adminRoleDef.getUser());
        assertNull(user.getUserRoleDefs().get(UserRole.ADMIN));
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