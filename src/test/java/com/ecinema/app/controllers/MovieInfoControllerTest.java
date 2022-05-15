package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MovieInfoControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ScreeningService screeningService;

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
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeExists("movies"));
    }

    @Test
    void searchMovie()
            throws Exception {
        MovieDto movieDto = movieService.findByTitle("dune");
        mockMvc.perform(get("/movies")
                                .param("search", "dune"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute(
                       "movies", is(Map.of(0, List.of(movieDto)))));
    }

    @Test
    void movieInfoPage()
            throws Exception {
        MovieDto movieDto = movieService.findByTitle("dune");
        mockMvc.perform(get("/movie-info/" + movieDto.getId()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("movie", movieDto));
    }

    @Test
    void movieReviewsPage()
            throws Exception {
        MovieDto movieDto = movieService.findByTitle("dune");
        PageRequest pageRequest = PageRequest.of(0, 6);
        Page<ReviewDto> pageOfDtos = reviewService.findPageByMovieId(movieDto.getId(), pageRequest);
        mockMvc.perform(get("/movie-reviews/" + movieDto.getId()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("reviews", pageOfDtos));
    }

    @Test
    void movieScreeningsPage()
            throws Exception {
        MovieDto movieDto = movieService.findByTitle("dune");
        PageRequest pageRequest = PageRequest.of(0, 6);
        Page<ScreeningDto> pageOfDtos = screeningService.findPageByMovieId(
                movieDto.getId(), pageRequest);
        mockMvc.perform(get("/movie-screenings/" + movieDto.getId()))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("screenings", pageOfDtos));
    }

}