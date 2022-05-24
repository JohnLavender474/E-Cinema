package com.ecinema.app.controllers;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.ecinema.app.util.UtilMethods.addPageNumbersAttribute;

/**
 * The type Movie review controller.
 */
@Controller
@RequiredArgsConstructor
public class MovieReviewController {

    private final UserService userService;
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final SecurityContext securityContext;
    private final Logger logger = LoggerFactory.getLogger(MovieReviewController.class);

    /**
     * Movie reviews page string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @param page               the page
     * @param movieId            the movie id
     * @return the string
     */
    @GetMapping("/movie-reviews/{id}")
    public String movieReviewsPage(
            final Model model, final RedirectAttributes redirectAttributes,
            @RequestParam(value = "page", required = false, defaultValue = "1") final Integer page,
            @PathVariable("id") final Long movieId) {
        try {
            model.addAttribute("movieId", movieId);
            Long userId = securityContext.findIdOfLoggedInUser();
            UserDto userDto = userId != null ? userService.convertToDto(userId) : null;
            boolean isCustomer = userDto != null && userDto.getUserAuthorities().contains(UserAuthority.CUSTOMER);
            boolean canWriteReview = isCustomer && !reviewService.existsByUserIdAndMovieId(
                    userDto.getId(), movieId);
            model.addAttribute("canWriteReview", canWriteReview);

            PageRequest pageRequest = PageRequest.of(page - 1, 6);
            Page<ReviewDto> pageOfDtos = reviewService.findPageByMovieId(movieId, pageRequest);
            model.addAttribute("reviews", pageOfDtos.getContent().toArray());
            addPageNumbersAttribute(model, pageOfDtos);
            logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
            logger.debug("Page of DTOs: " + pageOfDtos);
            return "movie-reviews";
        } catch (NoEntityFoundException e) {
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            return "redirect:/error";
        }
    }

    /**
     * Show report review page string.
     *
     * @param reviewId the review id
     * @return the string
     */
    @PostMapping("/report-review/{id}")
    public String showReportReviewPage(final RedirectAttributes redirectAttributes,
                                       @PathVariable("id") final Long reviewId) {
        return null;
    }

    /**
     * Show write review page string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @param movieId            the movie id
     * @return the string
     */
    @GetMapping("/write-review/{id}")
    public String showWriteReviewPage(final Model model, final RedirectAttributes redirectAttributes,
                                      @PathVariable("id") final Long movieId) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Write review get mapping");
        Long userId = securityContext.findIdOfLoggedInUser();
        if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "Fatal error: Forced to logout");
            return "redirect:/logout";
        }
        UserDto userDto = userService.findById(userId);
        if (!userDto.getUserAuthorities().contains(UserAuthority.CUSTOMER)) {
            redirectAttributes.addFlashAttribute(
                    "error", "Cannot access write-review page without customer credentials");
            return "redirect:/error";
        }
        if (reviewService.existsByUserIdAndMovieId(userDto.getId(), movieId)) {
            redirectAttributes.addFlashAttribute(
                    "error", "Customer has already written a review for this movie");
            return "redirect:/movie-reviews/" + movieId;
        }
        model.addAttribute("error", "Cannot access write-review page");
        logger.debug("Added user dto to model: " + userDto);
        MovieDto movieDto = movieService.findById(movieId);
        model.addAttribute("movie", movieDto);
        logger.debug("Added movie dto to model: " + movieDto);
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setMovieId(movieId);
        reviewForm.setUserId(userDto.getId());
        model.addAttribute("reviewForm", reviewForm);
        logger.debug("Added review form to model: " + reviewForm);
        return "write-review";
    }

    /**
     * Write review string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @param reviewForm         the review form
     * @param movieId            the movie id
     * @return the string
     */
    @PostMapping("/write-review/{id}")
    public String writeReview(final Model model, final RedirectAttributes redirectAttributes,
                              @ModelAttribute("reviewForm") final ReviewForm reviewForm,
                              @PathVariable("id") final Long movieId) {
        try {
            reviewForm.setMovieId(movieId);
            logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
            logger.debug("Write review post mapping");
            reviewService.submitReviewForm(reviewForm);
            redirectAttributes.addFlashAttribute(
                    "success", "Successfully created review for movie");
            return "redirect:/movie-reviews/" + reviewForm.getMovieId();
        } catch (NoEntityFoundException | ClashException e) {
            model.addAttribute("errors", e.getErrors());
            return "write-review";
        }
    }

}
