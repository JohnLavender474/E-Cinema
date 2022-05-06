package com.ecinema.app.controllers;

import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.forms.ChangePasswordForm;
import com.ecinema.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PasswordControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

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
    void showChangePasswordPage()
            throws Exception {
        mockMvc.perform(get("/change-password"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeExists("changePasswordForm"));
    }

    @Test
    void changePassword()
            throws Exception {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword(passwordEncoder.encode("old_password123?!"));
        userService.save(user);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        changePasswordForm.setEmail("test@gmail.com");
        changePasswordForm.setPassword("new_password123?!");
        changePasswordForm.setConfirmPassword("new_password123?!");
        mockMvc.perform(post("/change-password")
                                .flashAttr("changePasswordForm", changePasswordForm)
                                .secure(true))
               .andDo(print())
               .andExpect(redirectedUrl("/message-page"));
    }

}