package com.ecinema.app.services;

import com.ecinema.app.domain.entities.*;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.repositories.*;
import com.ecinema.app.services.implementations.*;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.exceptions.ClashException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * The type User service test.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private ReviewService reviewService;
    private TicketService ticketService;
    private PaymentCardService paymentCardService;
    private CouponService couponService;
    private UserService userService;
    private CustomerAuthorityService customerAuthorityService;
    private ModeratorAuthorityService moderatorAuthorityService;
    private AdminAuthorityService adminAuthorityService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PaymentCardRepository paymentCardRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomerAuthorityRepository customerAuthorityRepository;
    @Mock
    private ModeratorAuthorityRepository moderatorAuthorityRepository;
    @Mock
    private AdminAuthorityRepository adminAuthorityRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        reviewService = new ReviewServiceImpl(
                reviewRepository, null, null, null);
        ticketService = new TicketServiceImpl(
                ticketRepository, null, null, null);
        couponService = new CouponServiceImpl(
                couponRepository, null, null);
        paymentCardService = new PaymentCardServiceImpl(
                paymentCardRepository, null, null);
        adminAuthorityService = new AdminAuthorityServiceImpl(adminAuthorityRepository);
        customerAuthorityService = new CustomerAuthorityServiceImpl(
                customerAuthorityRepository, reviewService, ticketService,
                paymentCardService, couponService);
        moderatorAuthorityService = new ModeratorAuthorityServiceImpl(
                moderatorAuthorityRepository, customerAuthorityService);
        userService = new UserServiceImpl(
                userRepository, customerAuthorityService,
                moderatorAuthorityService,
                adminAuthorityService, null);
    }

    /**
     * Test find by email.
     */
    @Test
    void findByEmail() {
        // given
        String email = "test@gmail.com";
        User user = new User();
        user.setId(1L);
        user.setEmail(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        // when
        UserDto userDto = userService.findByEmail(email);
        // then
        assertEquals(email, userDto.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    /**
     * Test add user role def to user.
     */
    @Test
    void addUserRoleDefToUser() {
        // given
        User user = new User();
        userService.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityService.save(customerAuthority);
        given(customerAuthorityRepository.findByUser(user))
                .willReturn(Optional.of(customerAuthority));
        // when
        Optional<CustomerAuthority> testCustomerRoleDefOptional
                = customerAuthorityService.findByUser(user);
        // then
        assertTrue(testCustomerRoleDefOptional.isPresent() &&
                           customerAuthority.equals(testCustomerRoleDefOptional.get()) &&
                           user.getUserAuthorities().get(UserAuthority.CUSTOMER).equals(testCustomerRoleDefOptional.get()));
    }

    /**
     * Test remove user role def from user.
     */
    @Test
    void removeUserRoleDefFromUser() {
        // given
        User user = new User();
        userService.save(user);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        customerAuthorityService.save(customerAuthority);
        user.getUserAuthorities().remove(UserAuthority.CUSTOMER);
        customerAuthority.setUser(null);
        customerAuthorityRepository.delete(customerAuthority);
        userService.save(user);
        // when
        when(customerAuthorityRepository.findByUser(user))
                .thenReturn(Optional.ofNullable((CustomerAuthority) user.getUserAuthorities().get(UserAuthority.CUSTOMER)));
        // then
        assertTrue(customerAuthorityService.findByUser(user).isEmpty());
    }

    /**
     * Add user role defs to user.
     */
    @Test
    void addUserRoleDefsToUser() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        // when
        userService.addUserAuthorityToUser(
                user, UserAuthority.CUSTOMER, UserAuthority.ADMIN, UserAuthority.MODERATOR);
        // then
        assertTrue(user.getUserAuthorities().containsKey(UserAuthority.CUSTOMER));
        assertTrue(user.getUserAuthorities().get(UserAuthority.CUSTOMER) instanceof CustomerAuthority);
        assertTrue(user.getUserAuthorities().containsKey(UserAuthority.ADMIN));
        assertTrue(user.getUserAuthorities().get(UserAuthority.ADMIN) instanceof AdminAuthority);
        assertTrue(user.getUserAuthorities().containsKey(UserAuthority.MODERATOR));
        assertTrue(user.getUserAuthorities().get(UserAuthority.MODERATOR) instanceof ModeratorAuthority);
    }

    /**
     * Fail to add user role def to user.
     */
    @Test
    void failToAddUserRoleDefToUser() {
        // given
        User user = new User();
        user.setId(1L);
        userService.save(user);
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        userService.addUserAuthorityToUser(user, UserAuthority.CUSTOMER);
        // then
        assertThrows(ClashException.class,
                     () -> userService.addUserAuthorityToUser(user, UserAuthority.CUSTOMER));
    }

    /**
     * Delete user and cascade.
     */
    @Test
    void deleteUserAndCascade() {
        // given
        User user = new User();
        user.setId(1L);
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));
        userService.save(user);
        AdminAuthority adminAuthority = new AdminAuthority();
        adminAuthority.setId(2L);
        adminAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.ADMIN, adminAuthority);
        given(adminAuthorityRepository.findById(2L))
                .willReturn(Optional.of(adminAuthority));
        adminAuthorityService.save(adminAuthority);
        CustomerAuthority customerAuthority = new CustomerAuthority();
        customerAuthority.setId(3L);
        customerAuthority.setUser(user);
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, customerAuthority);
        given(customerAuthorityRepository.findById(3L))
                .willReturn(Optional.of(customerAuthority));
        customerAuthorityService.save(customerAuthority);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(4L);
        paymentCard.setCardOwner(customerAuthority);
        customerAuthority.getPaymentCards().add(paymentCard);
        given(paymentCardRepository.findById(4L))
                .willReturn(Optional.of(paymentCard));
        paymentCardService.save(paymentCard);
        // then
        assertNotNull(user.getUserAuthorities().get(UserAuthority.ADMIN));
        assertNotNull(user.getUserAuthorities().get(UserAuthority.CUSTOMER));
        assertNotNull(adminAuthority.getUser());
        assertNotNull(customerAuthority.getUser());
        assertNotNull(paymentCard.getCardOwner());
        // when
        userService.delete(user);
        // then
        assertNull(user.getUserAuthorities().get(UserAuthority.ADMIN));
        assertNull(user.getUserAuthorities().get(UserAuthority.CUSTOMER));
        assertNull(adminAuthority.getUser());
        assertNull(customerAuthority.getUser());
        assertNull(paymentCard.getCardOwner());
    }

    /**
     * User dto.
     */
    @Test
    void userDto() {
        // given
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setUsername("test");
        user.setFirstName("John");
        user.setLastName("Lavender");
        user.getUserAuthorities().put(UserAuthority.CUSTOMER, null);
        userService.save(user);
        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));
        // when
        UserDto userDto = userService.convertIdToDto(1L);
        // then
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getEmail(), userDto.getEmail());
        assertEquals(user.getUsername(), userDto.getUsername());
        assertEquals(user.getFirstName(), userDto.getFirstName());
        assertEquals(user.getLastName(), userDto.getLastName());
        assertTrue(userDto.getUserAuthorities().contains(UserAuthority.CUSTOMER));
    }

    @Test
    void onDeleteInfo() {

    }

}