package com.ecinema.app.controllers;

import com.ecinema.app.configs.InitializationConfig;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.domain.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private UserService userService;

    @MockBean
    private InitializationConfig initializationConfig;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void successfullyAccessAdminPage()
            throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("user");
        userDto.getUserRoles().add(UserRole.ADMIN);
        given(securityService.findLoggedInUserDTO()).willReturn(userDto);
        given(userService.userRolesAsListOfStrings(1L)).willReturn(List.of("ADMIN"));
        mockMvc.perform(get("/admin"))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void failToAccessAdminPage()
            throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("user");
        userDto.getUserRoles().add(UserRole.CUSTOMER);
        given(securityService.findLoggedInUserDTO()).willReturn(userDto);
        given(userService.userRolesAsListOfStrings(1L)).willReturn(List.of("CUSTOMER"));
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

}