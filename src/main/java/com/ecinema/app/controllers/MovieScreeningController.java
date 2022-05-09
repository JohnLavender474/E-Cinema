package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.ScreeningSeatService;
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

import java.util.Map;
import java.util.Set;

import static com.ecinema.app.utils.UtilMethods.addPageNumbersAttribute;

@Controller
@RequiredArgsConstructor
public class MovieScreeningController {

    private final ScreeningService screeningService;
    private final ScreeningSeatService screeningSeatService;
    private final Logger logger = LoggerFactory.getLogger(MovieScreeningController.class);

    @GetMapping("/movie-screenings/{id}")
    public String movieScreeningsPage(
            final Model model, @PathVariable("id") final Long id,
            @RequestParam(value = "page", required = false, defaultValue = "1") final Integer page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 6);
        Page<ScreeningDto> pageOfDtos = screeningService.findPageByMovieId(id, pageRequest);
        model.addAttribute("screenings", pageOfDtos.getContent().toArray());
        addPageNumbersAttribute(model, pageOfDtos);
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Screening DTOs: " + pageOfDtos);
        return "movie-screenings";
    }

    @GetMapping("/screening/{id}")
    public String seeScreeningPage(final Model model, @PathVariable("id") final Long screeningId) {
        ScreeningDto screeningDto = screeningService.convertToDto(screeningId);
        Map<Letter, Set<ScreeningSeatDto>> mapOfScreeningSeats = screeningSeatService
                .findScreeningSeatMapByScreeningWithId(screeningId);
        model.addAttribute("screening", screeningDto);
        model.addAttribute("mapOfScreeningSeats", mapOfScreeningSeats);
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Screening page get mapping");
        logger.debug("Screening DTO: " + screeningDto);
        logger.debug("Map of screening seats: has " + mapOfScreeningSeats.keySet().size() + " rows");
        for (Map.Entry<Letter, Set<ScreeningSeatDto>> entry : mapOfScreeningSeats.entrySet()) {
            logger.debug("Row " + entry.getKey() + " has " + entry.getValue() + " seats");
        }
        return "screening";
    }

    @ExceptionHandler(NoEntityFoundException.class)
    public String handleNoEntityFoundException(final Model model, final NoEntityFoundException e) {
        model.addAttribute("errors", e.getErrors());
        return "error";
    }

}
