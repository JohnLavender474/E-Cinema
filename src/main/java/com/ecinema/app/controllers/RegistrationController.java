package com.ecinema.app.controllers;

import com.ecinema.app.domain.enums.SecurityQuestions;
import com.ecinema.app.domain.forms.RegistrationForm;
import com.ecinema.app.exceptions.*;
import com.ecinema.app.services.RegistrationService;
import com.ecinema.app.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Registration controller.
 */
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    /**
     * Show registration page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/submit-registration")
    public String showRegistrationPage(final Model model) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Show submit registration page");
        model.addAttribute("registrationForm", new RegistrationForm());
        logger.debug("Added blank registration form to model");
        List<String> securityQuestions = SecurityQuestions.getList();
        model.addAttribute("securityQuestions", securityQuestions);
        logger.debug("Added list of security questions to model");
        logger.debug("Security questions: " + securityQuestions);
        model.addAttribute("maxDate", LocalDate.now());
        model.addAttribute("minDate", LocalDate.now().minusYears(120));
        return "submit-registration";
    }

    /**
     * Register string.
     *
     * @param model              the model
     * @param redirectAttributes the redirect attributes
     * @param registrationForm   the registration form
     * @return the string
     */
    @PostMapping("/submit-registration")
    public String register(final Model model, final RedirectAttributes redirectAttributes,
                           @ModelAttribute("registrationForm") final RegistrationForm registrationForm) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Post mapping: submit registration");
        try {
            registrationService.submitRegistrationForm(registrationForm);
            redirectAttributes.addFlashAttribute(
                    "message", "Registration request has been processed. \n Please see " +
                            "the email sent to your email address with a link to confirm your registration.");
            logger.debug("Successfully submitted registration form");
            return "redirect:/message-page";
        } catch (ClashException | InvalidArgsException | EmailException e) {
            model.addAttribute("errors", e.getErrors());
            logger.debug("Errors: " + e.getErrors());
            return "submit-registration";
        }
    }

    /**
     * Confirm registration string.
     *
     * @param model the model
     * @param token the token
     * @return the string
     */
    @GetMapping("/confirm-registration/{token}")
    public String confirmRegistration(final Model model, @PathVariable("token") final String token) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Showing confirm registration page");
        logger.debug("Token: " + token);
        try {
            if (token == null || token.isBlank()) {
                throw new BadRuntimeVarException("Token cannot be empty");
            }
            registrationService.confirmRegistrationRequest(token);
            logger.debug("Successfully confirmed registration token");
            model.addAttribute(
                    "message", "Successfully confirmed registration! " +
                            "You may now login.");
            return "message-page";
        } catch (NoEntityFoundException | BadRuntimeVarException e) {
            model.addAttribute("errors", e.getErrors());
            logger.debug("Errors: " + e.getErrors());
            return "error";
        }
    }

}
