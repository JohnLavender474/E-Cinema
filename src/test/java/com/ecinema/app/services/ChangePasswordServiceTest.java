package com.ecinema.app.services;

import com.ecinema.app.domain.entities.ChangePassword;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.forms.ChangePasswordForm;
import com.ecinema.app.repositories.ChangePasswordRepository;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.implementations.ChangePasswordServiceImpl;
import com.ecinema.app.services.implementations.UserServiceImpl;
import com.ecinema.app.domain.validators.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

/**
 * The type Change password service test.
 */
@ExtendWith(MockitoExtension.class)
class ChangePasswordServiceTest {

    private UserService userService;
    private ChangePasswordService changePasswordService;
    private PasswordValidator passwordValidator;
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChangePasswordRepository changePasswordRepository;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository, null, null,
                null, null);
        passwordValidator = new PasswordValidator();
        passwordEncoder = new BCryptPasswordEncoder();
        changePasswordService = new ChangePasswordServiceImpl(
                changePasswordRepository, userService,
                emailService, passwordValidator, passwordEncoder);
    }

    /**
     * Submit change password form.
     */
    @Test
    void submitChangePasswordForm() {
        // when
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        user.setUsername("Test_Username");
        user.setPassword(passwordEncoder.encode("password123!"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findIdByEmail("test@gmail.com")).willReturn(Optional.of(1L));
        userService.save(user);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        changePasswordForm.setEmail("test@gmail.com");
        changePasswordForm.setPassword("new_password123!?");
        changePasswordForm.setConfirmPassword("new_password123!?");
        ChangePassword changePassword = new ChangePassword();
        changePassword.setUserId(1L);
        changePassword.setToken("123");
        changePassword.setPassword(passwordEncoder.encode("new_password123!?"));
        changePassword.setExpirationDateTime(LocalDateTime.now());
        changePassword.setExpirationDateTime(LocalDateTime.now().plusMinutes(30));
        given(changePasswordRepository.findByToken("123"))
                .willReturn(Optional.of(changePassword));
        given(changePasswordRepository.existsByToken(anyString())).willReturn(false);
        assertTrue(passwordEncoder.matches("password123!", user.getPassword()));
        // when
        changePasswordService.submitChangePasswordForm(changePasswordForm);
        changePasswordService.confirmChangePassword("123");
        // then
        assertTrue(passwordEncoder.matches("new_password123!?", user.getPassword()));
    }

}