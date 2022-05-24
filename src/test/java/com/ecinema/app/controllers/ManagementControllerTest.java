package com.ecinema.app.controllers;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.configs.InitializationConfig;
import com.ecinema.app.domain.contracts.IMovie;
import com.ecinema.app.domain.contracts.IScreening;
import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.Movie;
import com.ecinema.app.domain.entities.Screening;
import com.ecinema.app.domain.entities.Showroom;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.*;
import com.ecinema.app.domain.validators.MovieValidator;
import com.ecinema.app.domain.validators.ScreeningValidator;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.MovieRepository;
import com.ecinema.app.repositories.ShowroomRepository;
import com.ecinema.app.services.*;
import com.ecinema.app.util.UtilMethods;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
class ManagementControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

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
    private SecurityContext securityContext;

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
        given(securityContext.findIdOfLoggedInUser()).willReturn(null);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN", "MODERATOR"})
    void successfullyAccessAdminPage1()
            throws Exception {
        setUpAdminUserDto();
        mockMvc.perform(get("/management"))
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeExists("admin"))
               .andExpect(result -> model().attributeExists("moderator"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void successfullyAccessAdminPage2()
            throws Exception {
        setUpAdminUserDto();
        mockMvc.perform(get("/management"))
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeExists("admin"))
               .andExpect(result -> model().attributeDoesNotExist("moderator"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR"})
    void successfullyAccessAdminPage3()
            throws Exception {
        setUpAdminUserDto();
        mockMvc.perform(get("/management"))
               .andExpect(status().isOk())
               .andExpect(result -> model().attributeDoesNotExist("admin"))
               .andExpect(result -> model().attributeExists("moderator"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER"})
    void failToAccessAdminPage1()
            throws Exception {
        expectGetForbidden("/management");
    }

    @Test
    @WithAnonymousUser
    void failToAccessAdminPage2()
            throws Exception {
        expectGetRedirectToLogin("/management");
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
        given(movieService.findById(1L)).willReturn(new MovieDto());
        mockMvc.perform(post("/edit-movie/" + 1L)
                                .flashAttr("movieForm", movieForm))
               .andExpect(result -> model().attributeExists("success"))
               .andExpect(redirectedUrlPattern("/edit-movie-search**"));
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
               .andExpect(redirectedUrlPattern("/edit-movie/" + 1L + "**"))
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
        }).when(movieService).delete(1L);
        mockMvc.perform(post("/delete-movie/" + 1L))
                .andExpect(result -> model().attributeExists("success"))
                .andExpect(redirectedUrlPattern("/delete-movie-search**"));
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
        given(movieService.findById(1L)).willReturn(movie);
        mockMvc.perform(get("/delete-movie/" + 1L))
               .andExpect(status().isOk())
               .andExpect(result -> model().attribute("movie", movie));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void noEntityFoundExceptionOnDeleteMoviePage()
            throws Exception {
        given(movieService.findById(1L)).willThrow(
                new NoEntityFoundException("movie", "id", 1L));
        mockMvc.perform(get("/delete-movie/" + 1L))
               .andExpect(redirectedUrlPattern("/error**"));
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
        given(movieService.findById(1L)).willReturn(movie);
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
        mockMvc.perform(post("/add-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(redirectedUrlPattern("/add-screening-search**"))
                .andExpect(flash().attribute("success", "Successfully added screening"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void failToPostAddScreening()
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
        doThrow(InvalidArgsException.class)
                .when(screeningService)
                .submitScreeningForm(any(ScreeningForm.class));
        mockMvc.perform(post("/add-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(redirectedUrlPattern("/add-screening/" + 1L + "**"))
                .andExpect(result -> model().attributeExists("errors"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void showChooseScreeningToDeletePage1()
        throws Exception {
        List<Letter> showroomLettersInUse = new ArrayList<>() {{
            add(Letter.A);
            add(Letter.B);
            add(Letter.C);
        }};
        given(showroomRepository.findAllShowroomLetters()).willReturn(showroomLettersInUse);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Screening> screenings = new PageImpl<>(new ArrayList<>() {{
            add(new Screening());
            add(new Screening());
        }});
        Page<ScreeningDto> screeningDtos = screenings.map(screeningService::convertToDto);
        given(screeningService.findAll(eq(pageRequest))).willReturn(screeningDtos);
        mockMvc.perform(get("/choose-screening-to-delete"))
                .andExpect(status().isOk())
                .andExpect(result -> model().attribute("screenings", screenings.getContent()))
                .andExpect(result -> model().attribute("page", 1))
                .andExpect(result -> model().attribute("search", ""))
                .andExpect(result -> model().attribute("letterChecked", List.of()));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void showChooseScreeningToDelete2()
            throws Exception {
        List<Letter> showroomLettersInUse = new ArrayList<>() {{
            add(Letter.A);
            add(Letter.B);
            add(Letter.C);
        }};
        given(showroomRepository.findAllShowroomLetters()).willReturn(showroomLettersInUse);
        PageRequest pageRequest = PageRequest.of(3, 10);
        Page<Screening> screenings = new PageImpl<>(new ArrayList<>() {{
            add(new Screening());
            add(new Screening());
        }});
        Page<ScreeningDto> screeningDtos = screenings.map(screeningService::convertToDto);
        given(screeningService.findAllByShowroomLettersAndMovieWithTitleLike(
                eq(showroomLettersInUse), eq("TEST"), eq(pageRequest)))
                .willReturn(screeningDtos);
        mockMvc.perform(get("/choose-screening-to-delete")
                                .param("search", "TEST")
                                .param("page", String.valueOf(4))
                                .param("letterChecked", "A")
                                .param("letterChecked", "B")
                                .param("letterChecked", "C"))
                .andExpect(status().isOk())
                .andExpect(result -> model().attribute(
                        "screenings", screenings.getContent()))
                .andExpect(result -> model().attribute("page", 1))
                .andExpect(result -> model().attribute("search", "TEST"))
                .andExpect(result -> model().attribute("letterChecked", showroomLettersInUse));
    }

    @Test
    @WithAnonymousUser
    void failToShowChooseScreeningToDeletePage1()
            throws Exception {
        mockMvc.perform(get("/choose-screening-to-delete"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"CUSTOMER", "MODERATOR"})
    void failToShowChooseScreeningToDeletePage2()
            throws Exception {
        mockMvc.perform(get("/choose-screening-to-delete"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void showDeleteScreeningPage()
        throws Exception {
        ScreeningDto screeningDto = new ScreeningDto();
        given(screeningService.findById(1L)).willReturn(screeningDto);
        given(screeningService.onDeleteInfo(1L)).willReturn(List.of("TEST"));
        mockMvc.perform(get("/delete-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(result -> model().attribute("screening", screeningDto))
                .andExpect(result -> model().attribute("onDeleteInfo", List.of("TEST")));
    }

    @Test
    @WithAnonymousUser
    void failToShowDeleteScreeningPage1()
        throws Exception {
        mockMvc.perform(get("/delete-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToShowDeleteScreeningPage2()
            throws Exception {
        mockMvc.perform(get("/delete-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void deleteScreening()
            throws Exception {
        doNothing().when(screeningService).delete(anyLong());
        mockMvc.perform(post("/delete-screening")
                                .param("id",String.valueOf(1L)))
                .andExpect(redirectedUrlPattern("/choose-screening-to-delete**"))
                .andExpect(result -> model().attributeExists("success"));
    }

    @Test
    @WithAnonymousUser
    void failToDeleteScreening1()
        throws Exception {
        mockMvc.perform(post("/delete-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToDeleteScreening2()
            throws Exception {
        mockMvc.perform(post("/delete-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void throwExceptionOnDeleteScreening()
        throws Exception {
        NoEntityFoundException e = new NoEntityFoundException("screening", "id", 1L);
        doThrow(e).when(screeningService).delete(anyLong());
        mockMvc.perform(post("/delete-screening")
                                .param("id", String.valueOf(1L)))
                .andExpect(redirectedUrlPattern("/management**"))
                .andExpect(result -> model().attribute("errors", e.getErrors()));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void addNewShowroom()
            throws Exception {
        ShowroomForm showroomForm = new ShowroomForm();
        showroomForm.setShowroomLetter(Letter.A);
        showroomForm.setNumberOfRows(1);
        showroomForm.setNumberOfSeatsPerRow(3);
        given(showroomRepository.existsByShowroomLetter(Letter.A)).willReturn(false);
        mockMvc.perform(post("/add-showroom")
                                .flashAttr("showroomForm", showroomForm))
                .andExpect(redirectedUrlPattern("/management**"))
                .andExpect(result -> model().attributeExists("success"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void showChooseShowroomToDeletePage()
            throws Exception {
        given(showroomService.findAllShowroomLetters()).willReturn(
                List.of(Letter.A, Letter.B, Letter.C));
        mockMvc.perform(get("/choose-showroom-to-delete"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void failToShowChooseShowroomToDeletePage1()
            throws Exception {
        mockMvc.perform(get("/choose-showroom-to-delete"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToShowChooseShowroomToDeletePage2()
        throws Exception {
        mockMvc.perform(get("/choose-showroom-to-delete"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void showDeleteShowroomPage()
        throws Exception {
        StringForm showroomLetterForm = new StringForm();
        showroomLetterForm.setS(Letter.A.name());
        List<String> onDeleteInfo = new ArrayList<>() {{
            add("TEST1");
            add("TEST2");
        }};
        given(showroomService.existsByShowroomLetter(Letter.A)).willReturn(true);
        given(showroomService.onDeleteInfo(Letter.A)).willReturn(onDeleteInfo);
        mockMvc.perform(get("/delete-showroom")
                                .flashAttr("showroomLetterForm", showroomLetterForm))
                .andExpect(status().isOk())
                .andExpect(result -> model().attribute("showroomLetterForm", showroomLetterForm))
                .andExpect(result -> model().attribute("onDeleteInfo", onDeleteInfo));
    }

    @Test
    @WithAnonymousUser
    void failToShowDeleteShowroomPage1()
        throws Exception {
        mockMvc.perform(get("/delete-showroom"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToShowDeleteShowroomPage2()
            throws Exception {
        mockMvc.perform(get("/delete-showroom"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void throwExceptionOnAttemptToShowDeleteShowroomPage1()
        throws Exception {
        InvalidArgsException e = new InvalidArgsException("Showroom letter cannot be blank");
        mockMvc.perform(get("/delete-showroom")
                                .flashAttr("showroomLetterForm", new StringForm()))
                .andExpect(redirectedUrlPattern("/choose-showroom-to-delete**"))
                .andExpect(result -> model().attribute("errors", e.getErrors()));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void throwExceptionOnAttemptToShowDeleteShowroomPage2()
        throws Exception {
        given(showroomService.existsByShowroomLetter(Letter.A)).willReturn(false);
        NoEntityFoundException e = new NoEntityFoundException("showroom", "showroom letter", Letter.A);
        StringForm showroomLetterForm = new StringForm(Letter.A.name());
        mockMvc.perform(get("/delete-showroom")
                                .flashAttr("showroomLetterForm", showroomLetterForm))
                .andExpect(redirectedUrlPattern("/choose-showroom-to-delete**"))
                .andExpect(result -> model().attribute("errors", e.getErrors()));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void deleteShowroom()
            throws Exception {
        doNothing().when(showroomService).delete(Letter.A);
        mockMvc.perform(post("/delete-showroom")
                                .param("showroomLetter", Letter.A.name()))
                .andExpect(redirectedUrlPattern("/choose-showroom-to-delete**"))
                .andExpect(result -> model().attributeExists("success"));
    }

    @Test
    @WithAnonymousUser
    void failToDeleteShowroom1()
        throws Exception {
        mockMvc.perform(post("/delete-showroom"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"MODERATOR", "CUSTOMER"})
    void failToDeleteShowroom2()
            throws Exception {
        mockMvc.perform(post("/delete-showroom"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ADMIN"})
    void throwsExceptionOnAttemptToDeleteShowroom()
        throws Exception {
        NoEntityFoundException e = new NoEntityFoundException("showroom", "showroom letter", Letter.A);
        doThrow(e).when(showroomService).delete(Letter.A);
        mockMvc.perform(post("/delete-showroom")
                                .param("showroomLetter", Letter.A.name()))
                .andExpect(redirectedUrlPattern("/management**"))
                .andExpect(result -> model().attribute("errors", e.getErrors()))
                .andExpect(result -> model().attribute(
                        "showroomLetterForm", new StringForm(Letter.A.name())));
    }

    void setUpAdminUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("user");
        userDto.getUserAuthorities().add(UserAuthority.ADMIN);
        given(securityContext.findIdOfLoggedInUser()).willReturn(1L);
        given(userService.findById(1L)).willReturn(userDto);
        given(userService.userAuthoritiesAsListOfStrings(1L)).willReturn(List.of("ADMIN"));
    }

    void testAdminMovieChoose(String viewName, String href)
            throws Exception {
        List<MovieDto> movies = new ArrayList<>(Collections.nCopies(10, new MovieDto()));
        given(movieService.findAll()).willReturn(movies);
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