package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
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
public class MovieInfoController {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final Logger logger = LoggerFactory.getLogger(MovieInfoController.class);

    @GetMapping("/movies")
    public String moviesPage(final Model model,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        logger.debug(UtilMethods.getDelimiterLine());
        Page<MovieDto> pageOfDtos = (search == null || search.isBlank()) ?
                movieService.pageOfDtos(pageRequest) :
                movieService.findAllByLikeTitle(search, pageRequest);
        addPageNumbersAttribute(model, pageOfDtos);
        model.addAttribute("movies", pageOfDtos.getContent().toArray());
        model.addAttribute("search", search);
        logger.debug("Page: " + page);
        logger.debug("Search: " + search);
        logger.debug("Page of movies: " + pageOfDtos);
        return "movies";
    }

    @GetMapping("/movie-info/{id}")
    public String moviedebugPage(final Model model, final RedirectAttributes redirectAttributes,
                                 @PathVariable("id") final Long movieId) {
        try {
            MovieDto movieDto = movieService.convertToDto(movieId);
            model.addAttribute("movie", movieDto);
            Integer avgRating = reviewService.findAverageRatingOfMovieWithId(movieId);
            model.addAttribute("avgRating", avgRating);
            logger.debug(UtilMethods.getDelimiterLine());
            logger.debug("Movie debug get mapping");
            logger.debug("Movie DTO: " + movieDto);
            logger.debug("Avg rating: " + avgRating);
            return "movie-info";
        } catch (NoEntityFoundException e) {
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            return "redirect:/error";
        }
    }

}
