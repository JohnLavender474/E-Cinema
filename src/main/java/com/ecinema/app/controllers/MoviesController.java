package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.domain.dtos.ReviewDto;
import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.ecinema.app.controllers.ControllerUtils.addListOfListsAttribute;
import static com.ecinema.app.controllers.ControllerUtils.addPageNumbersAttribute;

@Controller
@RequiredArgsConstructor
public class MoviesController {

    private final Logger logger = LoggerFactory.getLogger(MoviesController.class);
    private final MovieService movieService;
    private final ReviewService reviewService;
    private final ScreeningService screeningService;

    @GetMapping("/movies")
    public String moviesPage(final Model model,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Page: " + page);
        logger.debug("Search: " + search);
        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        Page<MovieDto> pageOfDtos = (search == null || search.isBlank()) ?
                movieService.pageOfDtos(pageRequest) :
                movieService.pageOfDtosLikeTitle(search, pageRequest);
        logger.debug("Page of movies: " + pageOfDtos);
        addListOfListsAttribute(model, "mListOfLists", 3, pageOfDtos);
        addPageNumbersAttribute(model, pageOfDtos);
        model.addAttribute("search", search);
        return "movies";
    }

    @GetMapping("/movie-info/{id}")
    public String moviedebugPage(final Model model, @PathVariable("id") final Long id) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Movie debug get mapping");
        MovieDto movieDto = movieService.convertToDto(id);
        logger.debug("Movie DTO: " + movieDto);
        model.addAttribute("movie", movieDto);
        Double avgRating = reviewService.findAverageRatingOfMovieWithId(id);
        logger.debug("Avg rating: " + avgRating);
        Integer roundedAvgRating = (int) Math.round(avgRating);
        logger.debug("Rounded avg rating: " + roundedAvgRating);
        model.addAttribute("avgRating", roundedAvgRating);
        return "movie-info";
    }

    @GetMapping("/movie-reviews/{id}")
    public String movieReviewsPage(
            final Model model, @PathVariable("id") final Long id,
            @RequestParam(value = "page", required = false, defaultValue = "1") final Integer page) {
        logger.debug(UtilMethods.getDelimiterLine());
        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        Page<ReviewDto> pageOfDtos = reviewService.findPageOfDtos(id, pageRequest);
        logger.debug("Page of DTOs: " + pageOfDtos);
        model.addAttribute("reviews", pageOfDtos);
        addPageNumbersAttribute(model, pageOfDtos);
        return "movie-reviews";
    }

    @GetMapping("/movie-screenings/{id}")
    public String movieScreeningsPage(
            final Model model, @PathVariable("id") final Long id,
            @RequestParam(value = "page", required = false, defaultValue = "1") final Integer page) {
        logger.debug(UtilMethods.getDelimiterLine());
        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        Page<ScreeningDto> pageOfDtos = screeningService.findPageByMovieId(id, pageRequest);
        logger.debug("Screening DTOs: " + pageOfDtos);
        model.addAttribute("screenings", pageOfDtos);
        addPageNumbersAttribute(model, pageOfDtos);
        return "movie-screenings";
    }

    @ExceptionHandler(NoEntityFoundException.class)
    public String handleNoEntityFoundException(final Model model, final NoEntityFoundException e) {
        model.addAttribute("errors", e.getErrors());
        return "error";
    }

}