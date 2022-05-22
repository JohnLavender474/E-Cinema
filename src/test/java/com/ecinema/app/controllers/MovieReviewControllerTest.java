package com.ecinema.app.controllers;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.configs.InitializationConfig;
import com.ecinema.app.domain.contracts.IReview;
import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.Customer;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.domain.validators.ReviewValidator;
import com.ecinema.app.repositories.CustomerRepository;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.repositories.ReviewRepository;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.util.UtilMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MovieReviewControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private MovieService movieService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewValidator reviewValidator;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CustomerRepository customerRepository;

    @MockBean
    InitializationConfig initializationConfig;

    @MockBean
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .alwaysDo(print())
                .build();
        given(securityContext.findIdOfLoggedInUser()).willReturn(null);
    }

    @Test
    void movieReviewsPage()
            throws Exception {
        List<ReviewDto> reviews = new ArrayList<>(Collections.nCopies(10, new ReviewDto()));
        given(reviewService.findPageByMovieId(1L, PageRequest.of(0, 6)))
                .willReturn(UtilMethods.convertListToPage(reviews, PageRequest.of(0, 6)));
        mockMvc.perform(get("/movie-reviews/" + 1L))
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeExists("reviews"));
    }

    @Test
    @WithMockUser(username = "username", authorities = {"CUSTOMER"})
    void successfullyShowWriteReviewPage1()
            throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.getUserAuthorities().add(UserAuthority.CUSTOMER);
        given(securityContext.findIdOfLoggedInUser()).willReturn(1L);
        given(userService.findById(1L)).willReturn(userDto);
        given(movieService.findById(anyLong())).willReturn(new MovieDto());
        given(reviewService.existsByUserIdAndMovieId(1L, 2L)).willReturn(false);
        mockMvc.perform(get("/write-review/" + 2L))
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeExists("movie"))
               .andExpect(result -> model().attributeExists("reviewForm"));
    }

    @Test
    @WithMockUser(username = "username", authorities = {"CUSTOMER", "ADMIN", "MODERATOR"})
    void successfullyShowWriteReviewPage2()
            throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.getUserAuthorities().add(UserAuthority.CUSTOMER);
        given(securityContext.findIdOfLoggedInUser()).willReturn(1L);
        given(userService.findById(1L)).willReturn(userDto);
        given(movieService.findById(anyLong())).willReturn(new MovieDto());
        given(reviewService.existsByUserIdAndMovieId(1L, 2L)).willReturn(false);
        mockMvc.perform(get("/write-review/" + 2L))
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeExists("movie"))
               .andExpect(result -> model().attributeExists("reviewForm"));
    }

    @Test
    @WithMockUser(username = "username", authorities = {"ADMIN", "MODERATOR"})
    void accessDeniedToShowWriteReviewPage1()
            throws Exception {
        mockMvc.perform(get("/write-review/" + 0L))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username")
    void accessDeniedToShowWriteReviewPage2()
            throws Exception {
        mockMvc.perform(get("/write-review/" + 0L))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username", authorities = {"CUSTOMER"})
    void successfullyWriteReview1()
            throws Exception {
        Customer customer = new Customer();
        given(customerRepository.findByUserWithId(1L))
                .willReturn(Optional.of(customer));
        Movie movie = new Movie();
        movie.setId(2L);
        given(movieRepository.findById(2L)).willReturn(Optional.of(movie));
        given(reviewRepository.existsByWriterAndMovie(customer, movie))
                .willReturn(false);
        doNothing().when(reviewValidator).validate(any(IReview.class), anyCollection());
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setUserId(1L);
        reviewForm.setMovieId(2L);
        reviewForm.setRating(10);
        reviewForm.setReview("This movie is absolutely garbage, jeez why did I ever pay to see this?!");
        UserDto userDto = new UserDto();
        userDto.getUserAuthorities().add(UserAuthority.CUSTOMER);
        given(securityContext.findIdOfLoggedInUser()).willReturn(1L);
        given(userService.findById(1L)).willReturn(userDto);
        mockMvc.perform(post(("/write-review/" + 2L))
                                 .flashAttr("reviewForm", reviewForm))
                .andExpect(result -> model().attributeExists("success"))
                .andExpect(redirectedUrlPattern("/movie-reviews/" + reviewForm.getMovieId() + "**"));
    }

    @Test
    @WithMockUser(username = "username", authorities = {"CUSTOMER", "MODERATOR"})
    void successfullyWriteReview2()
            throws Exception {
        Customer customer = new Customer();
        given(customerRepository.findByUserWithId(1L))
                .willReturn(Optional.of(customer));
        Movie movie = new Movie();
        movie.setId(2L);
        given(movieRepository.findById(2L)).willReturn(Optional.of(movie));
        given(reviewRepository.existsByWriterAndMovie(customer, movie))
                .willReturn(false);
        doNothing().when(reviewValidator).validate(any(IReview.class), anyCollection());
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setUserId(1L);
        reviewForm.setMovieId(2L);
        reviewForm.setRating(10);
        reviewForm.setReview("This movie is absolutely garbage, jeez why did I ever pay to see this?!");
        UserDto userDto = new UserDto();
        userDto.getUserAuthorities().add(UserAuthority.CUSTOMER);
        given(securityContext.findIdOfLoggedInUser()).willReturn(1L);
        given(userService.findById(1L)).willReturn(userDto);
        mockMvc.perform(post("/write-review/" + 2L)
                                 .flashAttr("reviewForm", reviewForm))
                .andExpect(result -> model().attributeExists("success"))
                .andExpect(redirectedUrlPattern("/movie-reviews/" + reviewForm.getMovieId() + "**"));
    }

    @Test
    @WithMockUser(username = "username", authorities = {"ADMIN", "MODERATOR"})
    void accessDeniedToWriteReview1()
            throws Exception {
        mockMvc.perform(post("/write-review"))
               .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "username")
    void accessDeniedToWriteReview2()
            throws Exception {
        mockMvc.perform(post("/write-review"))
               .andExpect(status().isForbidden());
    }

}