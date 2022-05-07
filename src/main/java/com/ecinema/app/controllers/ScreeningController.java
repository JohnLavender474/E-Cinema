package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.ScreeningService;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ScreeningController {

    private final Logger logger = LoggerFactory.getLogger(ScreeningController.class);
    private final ScreeningService screeningService;
    private final ScreeningSeatService screeningSeatService;

    @GetMapping("/screening/{id}")
    public String seeScreeningPage(final Model model, @PathVariable("id") Long id) {
        ScreeningDto screeningDto = screeningService.convertToDto(id);
        Map<Letter, Set<ScreeningSeatDto>> mapOfScreeningSeats = screeningSeatService
                .findScreeningSeatMapByScreeningWithId(id);
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
