package com.ecinema.app.controllers;

import com.ecinema.app.VideoService;
import com.ecinema.app.domain.dtos.MovieDto;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.MovieService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class MoviesController {

    private final MovieService movieService;

    @GetMapping("/movies")
    public String moviesPage(final Model model,
                             @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        PageRequest pageRequest = PageRequest.of(page - 1, 6);

        Page<MovieDto> movies = (search == null || search.isBlank() ?
                movieService.pageOfDtos(pageRequest) :
                movieService.pageOfDtosLikeTitle(search, pageRequest));
        List<MovieDto> movies1 = new ArrayList<>();
        List<MovieDto> movies2 = new ArrayList<>();
        int i = 0;
        for (MovieDto movie : movies) {
            if (i < 3) {
                movies1.add(movie);
            } else {
                movies2.add(movie);
            }
            i++;
        }
        model.addAttribute("movies1", movies1);
        model.addAttribute("movies2", movies2);
        int totalPages = movies.getTotalPages();
        model.addAttribute("totalPages", totalPages);
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                                                 .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("search", search);
        return "movies";
    }

    @GetMapping("/movie-info/{id}")
    public String movieInfoPage(final Model model, @PathVariable("id") Long id) {
        MovieDto movieDto = movieService.convertToDto(id);
        model.addAttribute("movie", movieDto);
        return "movie-info";
    }

    @ExceptionHandler(NoEntityFoundException.class)
    public String handleNoEntityFoundException(final Model model, final NoEntityFoundException e) {
        model.addAttribute("errors", e.getErrors());
        return "error";
    }

}
