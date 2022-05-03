package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MoviesControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MovieService movieService;

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
    void moviesPage()
            throws Exception {
        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(result -> model().attributeExists("mListOfLists"));
    }

    @Test
    void movieInfoPage()
            throws Exception {
        MovieDto movieDto = movieService.findByTitle("dune");
        mockMvc.perform(get("/movie-info/" + movieDto.getId()))
                .andExpect(status().isOk())
                .andExpect(result -> model().attribute("movie", movieDto));
    }

}