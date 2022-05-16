package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ShowroomDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.domain.forms.ScreeningForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.*;
import com.ecinema.app.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Admin controller.
 */
@Controller
@RequiredArgsConstructor
public class ManagementController {

    private final Logger logger = LoggerFactory.getLogger(ManagementController.class);
    private final ScreeningService screeningService;
    private final ShowroomService showroomService;
    private final SecurityService securityService;
    private final MovieService movieService;
    private final UserService userService;

    /**
     * Show admin page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/management")
    public String showAdminPage(final Model model) {
        UserDto userDto = securityService.findLoggedInUserDTO();
        List<String> userAuthorities = userService.userAuthoritiesAsListOfStrings(userDto.getId());
        if (userDto.getUserAuthorities().contains(UserAuthority.MODERATOR)) {
            model.addAttribute("moderator", true);
        }
        if (userDto.getUserAuthorities().contains(UserAuthority.ADMIN)) {
            model.addAttribute("admin", true);
        }
        model.addAttribute("userAuthorities", userAuthorities);
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: Admin page");
        logger.debug("User authorities: " + userAuthorities);
        return "management";
    }

    /**
     * Show add movie page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/add-movie")
    public String showAddMoviePage(final Model model) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: add movie page");
        model.addAttribute("action", "/add-movie");
        model.addAttribute("movieForm", new MovieForm());
        return "add-movie";
    }

    /**
     * Add movie string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @param movieForm          the movie form
     * @return the string
     */
    @PostMapping("/add-movie")
    public String addMovie(final Model model,final RedirectAttributes redirectAttributes,
                           @ModelAttribute("movieForm") final MovieForm movieForm) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Post mapping: add movie");
        logger.debug("Movie form: " + movieForm);
        try {
            movieService.submitMovieForm(movieForm);
            redirectAttributes.addFlashAttribute("message", "Successfully added movie");
            return "redirect:/message-page";
        } catch (ClashException | InvalidArgsException e) {
            model.addAttribute("errors", e.getErrors());
            return "add-movie";
        }
    }

    /**
     * Show edit movie search page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/edit-movie-search")
    public String showEditMovieSearchPage(final Model model) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: edit movie search");
        model.addAttribute("href", "/edit-movie/{id}");
        List<MovieDto> movies = movieService.findAllDtos();
        logger.debug("Movies: " + movies);
        model.addAttribute("movies", movies);
        return "admin-movie-choose";
    }

    /**
     * Show edit movie page string.
     *
     * @param model   the model
     * @param movieId the id of the movie to be edited
     * @return the string
     */
    @GetMapping("/edit-movie/{id}")
    public String showEditMoviePage(final Model model, @PathVariable("id") final Long movieId) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: edit movie with id " + movieId);
        model.addAttribute("action", "/edit-movie/{id}");
        MovieForm movieForm = movieService.fetchAsForm(movieId);
        logger.debug("Movie form: " + movieForm);
        model.addAttribute("movieForm", movieForm);
        return "edit-movie";
    }

    /**
     * Edit movie string.
     *
     * @param redirectAttributes the redirect attributes
     * @param movieForm          the movie form
     * @param movieId            the movie id
     * @return the string
     */
    @PostMapping("/edit-movie/{id}")
    public String editMovie(final RedirectAttributes redirectAttributes,
                            @ModelAttribute("movieForm") final MovieForm movieForm,
                            @PathVariable("id") final Long movieId) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Post mapping: edit movie with id " + movieId);
        try {
            movieForm.setId(movieId);
            logger.debug("Movie form: " + movieForm);
            movieService.submitMovieForm(movieForm);
            redirectAttributes.addFlashAttribute("success", "Successfully edited movie!");
            return "redirect:/edit-movie-search";
        } catch (InvalidArgsException e) {
            redirectAttributes.addAttribute("errors", e.getErrors());
            return "redirect:/edit-movie/" + movieId;
        }
    }

    /**
     * Show delete movie search page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/delete-movie-search")
    public String showDeleteMovieSearchPage(final Model model) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: delete movie search");
        model.addAttribute("href", "/delete-movie/{id}");
        List<MovieDto> movies = movieService.findAllDtos();
        logger.debug("Movies: " + movies);
        model.addAttribute("movies", movies);
        return "admin-movie-choose";
    }

    /**
     * Show delete movie page string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @param id                 the id
     * @return the string
     */
    @GetMapping("/delete-movie/{id}")
    public String showDeleteMoviePage(final Model model, final RedirectAttributes redirectAttributes,
                              @PathVariable("id") final Long id) {
        logger.debug(UtilMethods.getDelimiterLine());
        try {
            MovieDto movieDto = movieService.findDtoById(id).orElseThrow(
                    () -> new NoEntityFoundException("movie", "id", id));
            model.addAttribute("movie", movieDto);
            model.addAttribute("movieId", id);
            List<String> info = new ArrayList<>();
            movieService.onDeleteInfo(id, info);
            model.addAttribute("onDeleteInfo", info);
            return "delete-movie";
        } catch (NoEntityFoundException e) {
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            return "redirect:/error";
        }
    }

    /**
     * Delete movie string.
     *
     * @param redirectAttributes the redirect attributes
     * @param movieId            the movie id
     * @return the string
     */
    @PostMapping("/delete-movie/{id}")
    public String deleteMovie(final RedirectAttributes redirectAttributes,
                              @PathVariable("id") final Long movieId) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Post mapping: delete movie");
        movieService.deleteById(movieId);
        redirectAttributes.addFlashAttribute("success", "Successfully deleted movie");
        return "redirect:/delete-movie-search";
    }

    /**
     * Show add screening search page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/add-screening-search")
    public String showAddScreeningSearchPage(final Model model) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: add screening search");
        model.addAttribute("href", "/add-screening/{id}");
        model.addAttribute("movies", movieService.findAllDtos());
        return "admin-movie-choose";
    }

    /**
     * Show add screening page string.
     *
     * @param model   the model
     * @param movieId the movie id
     * @return the string
     */
    @GetMapping("/add-screening/{id}")
    public String showAddScreeningPage(final Model model, @PathVariable("id") final Long movieId) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: add screening");
        MovieDto movieDto = movieService.findDtoById(movieId).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieId));
        logger.debug("Movie DTO: " + movieDto);
        model.addAttribute("movie", movieDto);
        List<ShowroomDto> showrooms = showroomService.findAllDtos();
        logger.debug("Showrooms: " + showrooms);
        model.addAttribute("showrooms", showrooms);
        model.addAttribute("action", "/add-screening/{id}");
        model.addAttribute("screeningForm", new ScreeningForm());
        String minDate = LocalDateTime.now().toLocalDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String maxDate = LocalDateTime.now().plusYears(2).format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        logger.debug("Min date: " + minDate);
        logger.debug("Max date: " + maxDate);
        model.addAttribute("minDate", minDate);
        model.addAttribute("maxDate", maxDate);
        return "add-screening";
    }

    /**
     * Add screening string.
     *
     * @param redirectAttributes the redirect attributes
     * @param movieId            the movie id
     * @param screeningForm      the screening form
     * @return the string
     */
    @PostMapping("/add-screening/{id}")
    public String addScreening(final RedirectAttributes redirectAttributes,
                               @PathVariable("id") final Long movieId,
                               @ModelAttribute("screeningForm") final ScreeningForm screeningForm) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Post mapping: add screening");
        try {
            screeningForm.setMovieId(movieId);
            logger.debug("Screening form: " + screeningForm);
            screeningService.submitScreeningForm(screeningForm);
            redirectAttributes.addFlashAttribute("success", "Successfully added screening");
            logger.debug("Success!");
            return "redirect:/add-screening-search";
        } catch (NoEntityFoundException | InvalidArgsException | ClashException e) {
            logger.debug("ERROR!");
            redirectAttributes.addAttribute("errors", e.getErrors());
            return "redirect:/add-screening/" + movieId;
        }
    }



}