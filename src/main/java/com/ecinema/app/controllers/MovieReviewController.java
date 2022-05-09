package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserRole;
import com.ecinema.app.domain.forms.ReviewForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.ecinema.app.utils.UtilMethods.addPageNumbersAttribute;

@Controller
@RequiredArgsConstructor
public class MovieReviewController {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(MovieReviewController.class);

    @GetMapping("/movie-reviews/{id}")
    public String movieReviewsPage(
            final Model model, final RedirectAttributes redirectAttributes,
            @PathVariable("id") final Long movieId,
            @RequestParam(value = "page", required = false, defaultValue = "1") final Integer page) {
        try {
            PageRequest pageRequest = PageRequest.of(page - 1, 6);
            Page<ReviewDto> pageOfDtos = reviewService.findPageByMovieId(movieId, pageRequest);
            model.addAttribute("reviews", pageOfDtos.getContent().toArray());
            addPageNumbersAttribute(model, pageOfDtos);
            logger.debug(UtilMethods.getDelimiterLine());
            logger.debug("Page of DTOs: " + pageOfDtos);
            return "movie-reviews";
        } catch (NoEntityFoundException e) {
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            return "redirect:/error";
        }
    }
    
    @GetMapping("/write-review/{id}")
    public String showWriteReviewPage(final Model model, final RedirectAttributes redirectAttributes,
                                      @PathVariable("id") final Long movieId) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Write review get mapping");
        UserDto userDto = securityService.findLoggedInUserDTO();
        if (userDto == null) {
            redirectAttributes.addFlashAttribute("error", "Fatal error: Forced to logout");
            return "redirect:/logout";
        }
        if (!userDto.getUserRoles().contains(UserRole.CUSTOMER)) {
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
        MovieDto movieDto = movieService.findDtoById(movieId).orElseThrow(
                () -> new NoEntityFoundException("movie", "id", movieId));
        model.addAttribute("movie", movieDto);
        logger.debug("Added movie dto to model: " + movieDto);
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setMovieId(movieId);
        reviewForm.setUserId(userDto.getId());
        model.addAttribute("reviewForm", reviewForm);
        logger.debug("Added review form to model: " + reviewForm);
        return "write-review";
    }

    @PostMapping("/write-review")
    public String writeReview(final Model model, final RedirectAttributes redirectAttributes,
                              @ModelAttribute("reviewForm") final ReviewForm reviewForm) {
        try {
            logger.debug(UtilMethods.getDelimiterLine());
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
