package com.ecinema.app.services;

import com.ecinema.app.configs.InitializationConfig;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.enums.SecurityQuestions;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.RegistrationForm;
import com.ecinema.app.repositories.UserRepository;
import com.ecinema.app.services.implementations.RegistrationServiceImpl;
import com.ecinema.app.services.implementations.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Autowired
    @InjectMocks
    private UserServiceImpl userService;
    @Autowired
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private CustomerAuthorityService customerAuthorityService;
    @MockBean
    private EmailService emailService;
    @MockBean
    private InitializationConfig config;

    @Test
    void registerNewUser() {
        // given
        doNothing().when(emailService).sendFromBusinessEmail(anyString(), anyString(), anyString());
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setEmail("test@gmail.com");
        registrationForm.setUsername("TestUser123");
        registrationForm.setFirstName("First");
        registrationForm.setLastName("Last");
        registrationForm.setPassword("password123?!");
        registrationForm.setConfirmPassword("password123?!");
        registrationForm.setSecurityQuestion1(SecurityQuestions.SQ1);
        registrationForm.setSecurityAnswer1("Answer 1");
        registrationForm.setSecurityQuestion2(SecurityQuestions.SQ2);
        registrationForm.setSecurityAnswer2("Answer 2");
        registrationForm.setBirthDate(LocalDate.of(2000, Month.JANUARY, 1));
        registrationForm.getUserAuthorities().add(UserAuthority.CUSTOMER);
        String token = registrationService.submitRegistrationRequestAndGetToken(registrationForm);
        registrationService.confirmRegistrationRequest(token);
        // when
        UserDto userDto = userService.findByUsername("TestUser123");
        User user = userRepository.findById(userDto.getId()).orElseThrow(IllegalStateException::new);
        // then
        assertEquals(registrationForm.getEmail(), userDto.getEmail());
        assertEquals(registrationForm.getUsername(), userDto.getUsername());
        assertEquals(registrationForm.getFirstName(), userDto.getFirstName());
        assertEquals(registrationForm.getLastName(), userDto.getLastName());
        assertTrue(userDto.getUserAuthorities().contains(UserAuthority.CUSTOMER));
        assertTrue(passwordEncoder.matches("password123?!", user.getPassword()));
        assertTrue(passwordEncoder.matches("Answer 1", user.getSecurityAnswer1()));
        assertTrue(passwordEncoder.matches("Answer 2", user.getSecurityAnswer2()));
    }

}