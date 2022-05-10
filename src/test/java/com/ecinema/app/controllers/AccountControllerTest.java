package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserRole;
import com.ecinema.app.services.SecurityService;
import org.atteo.evo.inflector.English.MODE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AccountControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithAnonymousUser
    void accessDeniedToAccountPage2()
            throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user")
    void accessAccountPage1()
            throws Exception {
        given(securityService.findLoggedInUserDTO()).willReturn(null);
        mockMvc.perform(get("/account"))
                .andExpect(redirectedUrl("/logout"));
    }

    @Test
    @WithMockUser(username = "user")
    void accessAccountPage2()
            throws Exception {
        UserDto userDto = new UserDto();
        given(securityService.findLoggedInUserDTO()).willReturn(userDto);
        mockMvc.perform(get("/account"))
                .andExpect(redirectedUrlPattern("/error*"))
                .andExpect(result -> model().attributeExists("error"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void accessAccountPage3()
            throws Exception {
        UserDto userDto = new UserDto();
        userDto.getUserRoles().add(UserRole.CUSTOMER);
        given(securityService.findLoggedInUserDTO()).willReturn(userDto);
        mockMvc.perform(get("/account"))
                .andExpect(redirectedUrl("/customer"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN", "MODERATOR"})
    void accessAccountPage4()
            throws Exception {
        UserDto userDto = new UserDto();
        userDto.getUserRoles().addAll(Arrays.asList(UserRole.ADMIN, UserRole.MODERATOR));
        given(securityService.findLoggedInUserDTO()).willReturn(userDto);
        mockMvc.perform(get("/account"))
                .andExpect(redirectedUrl("/admin"));
    }

}