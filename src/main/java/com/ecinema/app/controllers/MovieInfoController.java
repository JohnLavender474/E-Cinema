package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import com.ecinema.app.services.ReviewService;
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

import java.util.List;
import java.util.Map;

import static com.ecinema.app.util.UtilMethods.addPageNumbersAttribute;

@Controller
@RequiredArgsConstructor
public class MovieInfoController {

    private final MovieService movieService;
    private final ReviewService reviewService;
    private final Logger logger = LoggerFactory.getLogger(MovieInfoController.class);

    @GetMapping("/movies")
    public String moviesPage(final Model model,
                             @RequestParam(value = "page", required = false, defaultValue = "1") final Integer page,
                             @RequestParam(value = "search", required = false, defaultValue = "") final String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        Page<MovieDto> pageOfDtos = (search == null || search.isBlank()) ?
                movieService.findAll(pageRequest) :
                movieService.findAllByLikeTitle(search, pageRequest);
        addPageNumbersAttribute(model, pageOfDtos);
        Map<Integer, List<MovieDto>> movies = UtilMethods.get2dMapOf(pageOfDtos, 3);
        model.addAttribute("movies", movies);
        model.addAttribute("search", search);
        model.addAttribute("page", page);
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Page: " + page);
        logger.debug("Search: " + search);
        logger.debug("Page of movies: " + pageOfDtos);
        return "movies";
    }

    @GetMapping("/movie-info/{id}")
    public String movieInfoPage(final Model model, final RedirectAttributes redirectAttributes,
                                 @PathVariable("id") final Long movieId) {
        try {
            MovieDto movieDto = movieService.findById(movieId);
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
