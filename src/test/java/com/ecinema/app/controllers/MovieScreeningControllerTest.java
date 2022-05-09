package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.entities.ScreeningSeat;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.utils.UtilMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MovieScreeningControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private ScreeningService screeningService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
    }

    @Test
    void showMovieScreeningsPage()
            throws Exception {
        List<ScreeningDto> screenings = new ArrayList<>(Collections.nCopies(10, new ScreeningDto()));
        given(screeningService.findPageByMovieId(1L, PageRequest.of(0, 6)))
                .willReturn(UtilMethods.convertListToPage(screenings, PageRequest.of(0, 6)));
        mockMvc
                .perform(get("/movie-screenings/" + 1L))
                .andExpect(status().isOk())
                .andExpect(result -> model().attributeExists("screnings"));
    }

    @Test
    void seeScreeningPage() {
    }

}