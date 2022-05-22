package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ShowroomDto;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.domain.forms.ScreeningForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.services.ShowroomService;
import com.ecinema.app.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final MovieService movieService;
    private final ShowroomService showroomService;
    private final ScreeningService screeningService;
    private final Logger logger = LoggerFactory.getLogger(AdminController.class);

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
        List<MovieDto> movies = movieService.findAll();
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
        List<MovieDto> movies = movieService.findAll();
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
            MovieDto movieDto = movieService.findById(id);
            model.addAttribute("movie", movieDto);
            model.addAttribute("movieId", id);
            List<String> info = new ArrayList<>();
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
        movieService.delete(movieId);
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
        model.addAttribute("movies", movieService.findAll());
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
        MovieDto movieDto = movieService.findById(movieId);
        logger.debug("Movie DTO: " + movieDto);
        model.addAttribute("movie", movieDto);
        List<ShowroomDto> showrooms = showroomService.findAll();
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
