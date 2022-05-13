package com.ecinema.app.controllers;

import com.ecinema.app.configs.InitializationConfig;
import com.ecinema.app.domain.contracts.IMovie;
import com.ecinema.app.domain.contracts.IScreening;
import com.ecinema.app.domain.contracts.IShowroom;
import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.domain.forms.ScreeningForm;
import com.ecinema.app.domain.validators.MovieValidator;
import com.ecinema.app.domain.validators.ScreeningValidator;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.services.*;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AdminControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private SecurityService securityService;

    @MockBean
    private UserService userService;

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @MockBean
    private ShowroomService showroomService;

    @MockBean
    private ShowroomRepository showroomRepository;

    @MockBean
    private ScreeningService screeningService;

    @MockBean
    private MovieValidator movieValidator;

    @MockBean
    private ScreeningValidator screeningValidator;

    @MockBean
    private InitializationConfig initializationConfig;

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
        setUpAdminUserDto();
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToAccessAdminPage1()
            throws Exception {
        expectGetForbidden("/admin");
    }

    @Test
    @WithAnonymousUser
    void failToAccessAdminPage2()
            throws Exception {
        expectGetRedirectToLogin("/admin");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void accessEditMovieSearchPage()
            throws Exception {
        testAdminMovieChoose("/edit-movie-search", "/edit-movie");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToAccessEditMovieSearch1()
            throws Exception {
        expectGetForbidden("/edit-movie-search");
    }

    @Test
    @WithAnonymousUser
    void failToAccessEditMovieSearch2()
            throws Exception {
        expectGetRedirectToLogin("/edit-movie-search");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void accessEditMoviePage()
            throws Exception {
        MovieForm movieForm = new MovieForm();
        given(movieService.fetchAsForm(1L)).willReturn(movieForm);
        mockMvc.perform(get("/edit-movie/" + 1L))
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("action", "/edit-movie"))
               .andExpect(result -> model().attribute("movieForm", movieForm));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToAccessEditMoviePage1()
            throws Exception {
        expectGetForbidden("/edit-movie/" + 1L);
    }

    @Test
    @WithAnonymousUser
    void failToAccessEditMoviePage2()
            throws Exception {
        expectGetRedirectToLogin("/edit-movie/" + 1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void postEditMovie()
            throws Exception {
        MovieForm movieForm = new MovieForm();
        movieForm.setId(1L);
        doNothing().when(movieValidator).validate(any(IMovie.class), anyCollection());
        given(movieService.findById(1L)).willReturn(Optional.of(new Movie()));
        mockMvc.perform(post("/edit-movie/" + 1L)
                                .flashAttr("movieForm", movieForm))
               .andExpect(result -> model().attributeExists("success"))
               .andExpect(redirectedUrl("/edit-movie-search"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void invalidArgsExceptionPostEditMovie()
            throws Exception {
        MovieForm movieForm = new MovieForm();
        doThrow(InvalidArgsException.class).when(movieService)
                                           .submitMovieForm(movieForm);
        mockMvc.perform(post("/edit-movie/" + 1L)
                                .flashAttr("movieForm", movieForm))
               .andExpect(redirectedUrl("/edit-movie/" + 1L))
               .andExpect(result -> model().attributeExists("errors"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void accessDeniedPostEditMovie1()
            throws Exception {
        expectPostForbidden("/edit-movie/" + 1L);
    }

    @Test
    @WithAnonymousUser
    void accessDeniedPostEditMovie2()
            throws Exception {
        expectedPostRedirectToLogin("/edit-movie/" + 1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void postDeleteMovie()
        throws Exception {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(1L);
        doAnswer(invocationOnMock -> {
            movieRepository.deleteById(1L);
            return null;
        }).when(movieService).deleteById(1L);
        mockMvc.perform(post("/delete-movie/" + 1L))
                .andExpect(result -> model().attributeExists("success"))
                .andExpect(redirectedUrl("/delete-movie-search"));
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void accessDeniedPostDeleteMovie1()
            throws Exception {
        expectPostForbidden("/delete-movie/" + 1L);
    }

    @Test
    @WithAnonymousUser
    void accessDeniedPostMovieDelete2()
            throws Exception {
        expectedPostRedirectToLogin("/delete-movie/" + 1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void accessDeleteMovieSearchPage()
            throws Exception {
        testAdminMovieChoose("/delete-movie-search", "/delete-movie");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToAccessDeleteMovieSearchPage1()
            throws Exception {
        expectGetForbidden("/delete-movie-search");
    }

    @Test
    @WithAnonymousUser
    void failToAccessDeleteMovieSearchPage2()
            throws Exception {
        expectGetRedirectToLogin("/delete-movie-search");
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void accessDeleteMoviePage()
            throws Exception {
        MovieDto movie = new MovieDto();
        given(movieService.findDtoById(1L)).willReturn(Optional.of(movie));
        mockMvc.perform(get("/delete-movie/" + 1L))
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("movie", movie));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void noEntityFoundExceptionOnDeleteMoviePage()
            throws Exception {
        mockMvc.perform(get("/delete-movie/" + 1L))
               .andExpect(redirectedUrl("/error"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToAccessDeleteMoviePage1()
            throws Exception {
        expectGetForbidden("/delete-movie/" + 1L);
    }

    @Test
    @WithAnonymousUser
    void failToAccessDeleteMoviePage2()
            throws Exception {
        expectGetRedirectToLogin("/delete-movie/" + 1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void accessAddScreeningSearchPage()
            throws Exception {
        testAdminMovieChoose("/add-screening-search", "/add-screening/{id}");
    }
    
    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void accessAddScreeningPage() 
            throws Exception {
        MovieDto movie = new MovieDto();
        given(movieService.findDtoById(1L)).willReturn(Optional.of(movie));
        mockMvc.perform(get("/add-screening/" + 1L))
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("movie", movie))
               .andExpect(result -> model().attribute("action", "/add-screening/{id}"))
               .andExpect(result -> model().attribute("screeningForm", new ScreeningForm()));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER", "MODERATOR"})
    void failToAccessAddScreeningPage1()
            throws Exception {
        expectGetForbidden("/add-screening/" + 1L);
    }

    @Test
    @WithAnonymousUser
    void failToAccessAddScreeningPage2()
            throws Exception {
        expectedPostRedirectToLogin("/add-screening/" + 1L);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void postAddScreening()
            throws Exception {
        ScreeningForm screeningForm = new ScreeningForm();
        screeningForm.setMovieId(1L);
        screeningForm.setShowroomId(2L);
        screeningForm.setShowDateTime(LocalDateTime.now().plusHours(8));
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        given(movieRepository.findById(1L))
                .willReturn(Optional.of(movie));
        Showroom showroom = new Showroom();
        showroom.setShowroomLetter(Letter.A);
        given(showroomRepository.findById(2L))
                .willReturn(Optional.of(showroom));
        doNothing().when(screeningValidator).validate(
                any(IScreening.class), anyCollection());
        given(screeningService.findScreeningByShowroomAndInBetweenStartTimeAndEndTime(
                any(Showroom.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .willReturn(Optional.empty());
        mockMvc.perform(post("/add-screening/" + 1L))
                .andExpect(redirectedUrl("/add-screening-search"))
                .andExpect(flash().attribute("success", "Successfully added screening"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void failToPostAddScreening() {

    }

    void setUpAdminUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("user");
        userDto.getUserAuthorities().add(UserAuthority.ADMIN);
        given(securityService.findLoggedInUserDTO()).willReturn(userDto);
        given(userService.userAuthoritiesAsListOfStrings(1L)).willReturn(List.of("ADMIN"));
    }

    void testAdminMovieChoose(String viewName, String href)
            throws Exception {
        List<MovieDto> movies = new ArrayList<>(Collections.nCopies(10, new MovieDto()));
        given(movieService.findAllDtos()).willReturn(movies);
        mockMvc.perform(get(viewName))
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("href", href))
               .andExpect(result -> model().attribute("movies", movies));
    }

    void expectGetForbidden(String viewName)
            throws Exception {
        mockMvc.perform(get(viewName))
               .andExpect(status().isForbidden());
    }

    void expectPostForbidden(String action)
            throws Exception {
        mockMvc.perform(post(action))
               .andExpect(status().isForbidden());
    }

    void expectGetRedirectToLogin(String viewName)
            throws Exception {
        mockMvc.perform(get(viewName))
               .andExpect(status().is3xxRedirection());
    }

    void expectedPostRedirectToLogin(String action)
            throws Exception {
        mockMvc.perform(post(action))
               .andExpect(status().is3xxRedirection());
    }

}