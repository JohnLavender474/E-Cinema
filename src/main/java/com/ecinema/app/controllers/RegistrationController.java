package com.ecinema.app.controllers;

import com.ecinema.app.domain.enums.SecurityQuestions;
import com.ecinema.app.domain.forms.RegistrationForm;
import com.ecinema.app.exceptions.*;
import com.ecinema.app.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * The type Registration controller.
 */
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Show registration page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/submit-registration")
    public String showRegistrationPage(final Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        model.addAttribute("securityQuestions", SecurityQuestions.getList());
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
        try {
            registrationService.submitRegistrationForm(registrationForm);
            redirectAttributes.addFlashAttribute(
                    "message", "Registration request has been processed. Please see " +
                            "the email sent to your email address with a link to confirm your registration.");
            return "redirect:/message-page";
        } catch (ClashException | InvalidArgsException | EmailException e) {
            model.addAttribute("errors", e.getErrors());
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
        try {
            registrationService.confirmRegistrationRequest(token);
            model.addAttribute("success",
                               "Successfully confirmed registration! You may now login.");
        } catch (NoEntityFoundException e) {
            model.addAttribute("errors", e.getErrors());
        }
        return "confirm-registration";
    }

}
