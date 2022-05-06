package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.forms.MovieForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.utils.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The type Admin controller.
 */
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SecurityService securityService;
    private final UserService userService;
    private final MovieService movieService;

    /**
     * Show admin page string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @return the string
     */
    @GetMapping("/admin")
    public String showAdminPage(final Model model, final RedirectAttributes redirectAttributes) {
        UserDto userDto = securityService.findLoggedInUserDTO();
        if (userDto == null || !userDto.getUserRoles().contains(UserRole.ADMIN)) {
            redirectAttributes.addAttribute("failed", "Not authorized to view that page!");
            return "redirect:/error";
        }
        model.addAttribute("userRoles",
                           userService.userRolesAsListOfStrings(userDto.getId()));
        return "admin";
    }

    /**
     * Show add movie page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/add-movie")
    public String showAddMoviePage(final Model model) {
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
        model.addAttribute("action", "edit-movie");
        model.addAttribute("movies", movieService.findAllDtos());
        return "admin-movie-choose";
    }

    /**
     * Show edit movie page string.
     *
     * @param model the model
     * @param id    the id
     * @return the string
     */
    @GetMapping("/edit-movie/{id}")
    public String showEditMoviePage(final Model model, @PathVariable("id") final Long id) {
        model.addAttribute("action", "/edit-movie");
        MovieForm movieForm = movieService.fetchAsForm(id);
        model.addAttribute("movieForm", movieForm);
        return "edit-movie";
    }

    /**
     * Edit movie string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @param movieForm          the movie form
     * @return the string
     */
    @PostMapping("/edit-movie")
    public String editMovie(final Model model,final RedirectAttributes redirectAttributes,
                            @ModelAttribute("movieForm") MovieForm movieForm) {
        try {
            movieService.submitMovieForm(movieForm);
            redirectAttributes.addFlashAttribute("success", "Successfully edited movie!");
            return "redirect:/edit-movie-search";
        } catch (InvalidArgsException e) {
            model.addAttribute("errors", e.getErrors());
            return "edit-movie";
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
        model.addAttribute("action", "delete-movie");
        model.addAttribute("movies", movieService.findAllDtos());
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
        try {
            MovieDto movieDto = movieService.findDtoById(id).orElseThrow(
                    () -> new NoEntityFoundException("movie", "id", id));
            model.addAttribute("movie", movieDto);

            // TODO: Add all relevant info including screenings, tickets, etc.
            // associated with the movie for admin to adequately evaluate
            // if deleting the movie is actually a good idea

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
     * @param movieDto           the movie dto
     * @return the string
     */
    @PostMapping("/delete-movie")
    public String deleteMovie(final RedirectAttributes redirectAttributes,
                              @ModelAttribute("movie") MovieDto movieDto) {
        movieService.deleteById(movieDto.getId());
        redirectAttributes.addFlashAttribute("success",
                                             "Successfully deleted " + movieDto.getTitle());
        return "redirect:/admin";
    }

}
