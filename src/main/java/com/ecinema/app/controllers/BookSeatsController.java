package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.ScreeningDto;
import com.ecinema.app.domain.forms.GenericListForm;
import com.ecinema.app.domain.forms.SeatBookingsForm;
import com.ecinema.app.exceptions.*;
import com.ecinema.app.services.CustomerService;
import com.ecinema.app.services.ScreeningSeatService;
import com.ecinema.app.services.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.util.*;

import com.ecinema.app.domain.dtos.ScreeningSeatDto;
import com.ecinema.app.domain.enums.Letter;
import com.ecinema.app.util.UtilMethods;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@SessionAttributes("bookSeatsForm")
public class BookSeatsController {

    private final CustomerService customerService;
    private final ScreeningService screeningService;
    private final ScreeningSeatService screeningSeatService;
    private final Logger logger = LoggerFactory.getLogger(BookSeatsController.class);

    @GetMapping("/choose-seats-to-book")
    public String seeScreeningPage(final Model model, @RequestParam("id") final Long screeningId) {
        ScreeningDto screeningDto = screeningService.findById(screeningId);
        Map<Letter, Set<ScreeningSeatDto>> mapOfScreeningSeats = screeningSeatService
                .findScreeningSeatMapByScreeningWithId(screeningId);
        model.addAttribute("screening", screeningDto);
        model.addAttribute("mapOfScreeningSeats", mapOfScreeningSeats);
        model.addAttribute("seatIdsForm", new GenericListForm<Long>());
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: Choose seats to book");
        logger.debug("Screening DTO: " + screeningDto);
        logger.debug("Map of screening seats has " + mapOfScreeningSeats.keySet().size() + " rows");
        for (Map.Entry<Letter, Set<ScreeningSeatDto>> entry : mapOfScreeningSeats.entrySet()) {
            logger.debug("Row " + entry.getKey() + " has " + entry.getValue() + " seats");
        }
        return "choose-seats-to-book";
    }

    @GetMapping("/book-seats")
    public String showBookSeatsPage(final Model model, final RedirectAttributes redirectAttributes,
                                    @ModelAttribute("seatIdsForm") final GenericListForm<Long> seatIdsForm,
                                    @RequestParam("id") final Long screeningId) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: book seats");
        logger.debug("Screening id: " + screeningId);
        logger.debug("Seat ids form: " + seatIdsForm);
        if (seatIdsForm == null || seatIdsForm.getList().isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errors", List.of("Error: Must select at least one seat to book"));
            return "redirect:/choose-seats-to-book/" + screeningId;
        }
        ScreeningDto screeningDto = screeningService.findById(screeningId);
        logger.debug("Screening DTO: " + screeningDto);
        model.addAttribute("screening", screeningDto);
        SeatBookingsForm seatBookingsForm = new SeatBookingsForm();
        seatBookingsForm.setSeatIds(seatIdsForm.getList());
        logger.debug("Seat bookings form: " + seatBookingsForm);
        model.addAttribute("seatBookingsForm", seatBookingsForm);
        return "book-seats";
    }

    @PostMapping("/book-seats")
    public String bookSeats(final RedirectAttributes redirectAttributes,
                            @ModelAttribute("seatBookingsForm") final SeatBookingsForm seatBookingsForm,
                            @RequestParam("id") final Long screeningId) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Post mapping: book seats");
        try {
            customerService.bookTickets(seatBookingsForm);
            redirectAttributes.addFlashAttribute(
                    "message", "Successfully purchased tickets \n" +
                    "Check your email for the confirmation message with details of your purchase");
            logger.debug("Successfully booked tickets");
            return "redirect:/message-page";
        } catch (NoEntityFoundException | ClashException | PermissionDeniedException | ExpirationException e) {
            logger.debug("Encountered exception, redirection to book seats page");
            logger.debug("Errors: " + e);
            logger.debug("Seat bookings form: " + seatBookingsForm);
            logger.debug("Screening id: " + screeningId);
            String redirectUrl = "redirect:/book-seats?id=" + screeningId;
            logger.debug("Redirect url: " + redirectUrl);
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            redirectAttributes.addFlashAttribute("seatBookingsForm", seatBookingsForm);
            return redirectUrl;
        }
    }

}
