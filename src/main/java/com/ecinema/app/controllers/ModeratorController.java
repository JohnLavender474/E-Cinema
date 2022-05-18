package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.CustomerAuthorityDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.CustomerAuthorityService;
import com.ecinema.app.services.ModeratorAuthorityService;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static com.ecinema.app.utils.UtilMethods.addPageNumbersAttribute;

@Controller
@RequiredArgsConstructor
public class ModeratorController {

    private final SecurityService securityService;
    private final CustomerAuthorityService customerAuthorityService;
    private final ModeratorAuthorityService moderatorAuthorityService;
    private final Logger logger = LoggerFactory.getLogger(ModeratorController.class);

    @GetMapping("/moderator-censorship")
    public String showModeratorCensorshipPage(final Model model, final RedirectAttributes redirectAttributes,
                                              @RequestParam(value = "page", required = false, defaultValue = "1")
                                              final Integer page) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: moderator censorship");
        UserDto userDto = securityService.findLoggedInUserDTO();
        logger.debug("User DTO: " + userDto);
        if (userDto == null || !userDto.getUserAuthorities().contains(UserAuthority.MODERATOR)) {
            logger.debug("ERROR: user does not have moderator authority");
            redirectAttributes.addFlashAttribute(
                    "errors", List.of("User does not have moderator authority"));
            return "redirect:/error";
        }
        Long moderatorId = moderatorAuthorityService.findIdByUserWithId(userDto.getId());
        logger.debug("Moderator id: " + moderatorId);
        model.addAttribute("moderatorId", moderatorId);
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        Page<CustomerAuthorityDto> pageOfDtos = customerAuthorityService.findAllDtos(pageRequest);
        model.addAttribute("censoredCustomers", pageOfDtos);
        addPageNumbersAttribute(model, pageOfDtos);
        logger.debug("Page number: " + page);
        model.addAttribute("page", page);
        return "moderator-censorship";
    }

    @PostMapping("/set-customer-censor-status")
    public String setCustomerCensorStatus(final Model model, final RedirectAttributes redirectAttributes,
                                          @RequestParam("page") final Integer page,
                                          @RequestParam("moderatorId") final Long moderatorId,
                                          @RequestParam("customerId") final Long customerId,
                                          @RequestParam("currentStatus") final Boolean currentStatus) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Post mapping: set customer censor status");
        logger.debug("Page number: " + page);
        logger.debug("Moderator id: " + moderatorId);
        logger.debug("Customer id: " + customerId);
        logger.debug("Current status: " + currentStatus);
        try {
            moderatorAuthorityService.setCustomerCensoredStatus(
                    moderatorId, customerId, !currentStatus);
            logger.debug("Successfully changed customer censor status from " + currentStatus + " to " + !currentStatus);
            redirectAttributes.addFlashAttribute("success", "Successfully changed " +
                    "customer censored status from " + currentStatus + " to " + !currentStatus);
            return "redirect:/moderator-censorship?page=" + page;
        } catch (NoEntityFoundException e) {
            logger.debug("Errors: " + e);
            model.addAttribute("errors", List.of("FATAL ERROR: " +
                                                         "Could not find customer by id " + customerId));
            return "error";
        }
    }

    @GetMapping("/moderator-reports")
    public String showModeratorReportsPage() {
        return null;
    }

}
