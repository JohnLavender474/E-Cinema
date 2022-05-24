package com.ecinema.app.controllers;

import com.ecinema.app.domain.entities.Admin;
import com.ecinema.app.domain.entities.Customer;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.enums.SecurityQuestions;
import com.ecinema.app.domain.enums.UserAuthority;
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
 * Controller for client registration process.
 */
@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    /**
     * Gets the submit-registration page for the purpose of registering a {@link com.ecinema.app.domain.entities.User}
     * with {@link com.ecinema.app.domain.entities.Customer} authority. Adds to the model {@link RegistrationForm}
     * and {@link SecurityQuestions#getList()}.
     *
     * @param model            the model of the view
     * @param registrationForm the registration form
     * @return the view name
     */
    @GetMapping("/submit-customer-registration")
    public String showCustomerRegistrationPage(final Model model,
                                       @ModelAttribute("registrationForm") final RegistrationForm registrationForm) {
        addRegistrationPageAttributes(model, registrationForm);
        model.addAttribute("action", "/submit-customer-registration");
        return "submit-registration";
    }

    /**
     * Gets the submit-registration page for the purpose of registering a {@link com.ecinema.app.domain.entities.User}
     * with customized values for {@link UserAuthority}. At least one authority must be specified in the values
     * of {@link RegistrationForm#getAuthorities()} on submission.
     *
     * @param model            the model of the view
     * @param registrationForm the registration form
     * @return the view name
     */
    @GetMapping("/submit-management-customized-registration")
    public String showManagementCustomizedRegistrationPage(
            final Model model, @ModelAttribute("registrationForm") final RegistrationForm registrationForm) {
        addRegistrationPageAttributes(model, registrationForm);
        model.addAttribute("action", "/submit-management-customized-registration");
        return "submit-registration";
    }

    private void addRegistrationPageAttributes(final Model model, final RegistrationForm registrationForm) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Show submit registration page");
        model.addAttribute("registrationForm", registrationForm);
        logger.debug("Added blank registration form to model");
        List<String> securityQuestions = SecurityQuestions.getList();
        model.addAttribute("securityQuestions", securityQuestions);
        logger.debug("Added list of security questions to model");
        logger.debug("Security questions: " + securityQuestions);
        model.addAttribute("maxDate", LocalDate.now().minusYears(16));
        model.addAttribute("minDate", LocalDate.now().minusYears(120));
    }

    /**
     * Posts the {@link RegistrationForm} for registering a new {@link User} with {@link Customer} authority.
     *
     * @param redirectAttributes the redirect attributes
     * @param registrationForm   the registration form
     * @return the view name
     */
    @PostMapping("/submit-customer-registration")
    public String registerCustomer(final RedirectAttributes redirectAttributes,
                           @ModelAttribute("registrationForm") final RegistrationForm registrationForm) {
        registrationForm.getAuthorities().add(UserAuthority.CUSTOMER);
        return submitRegistration(redirectAttributes, registrationForm,
                                  "redirect:/submit-customer-registration");
    }

    /**
     * Posts the {@link RegistrationForm} for registering a new {@link User} that has been customized by
     * an {@link Admin} client. Must contain at least one {@link UserAuthority}.
     *
     * @param redirectAttributes the redirect attributes
     * @param registrationForm   the registration form
     * @return the view name
     */
    @PostMapping("/submit-management-customized-registration")
    public String registerManagementCustomizedRegistration(
            final RedirectAttributes redirectAttributes,
            @ModelAttribute("registrationForm") final RegistrationForm registrationForm) {
        if (registrationForm.getAuthorities().isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errors", List.of("Must specify at least one user authority"));
            redirectAttributes.addFlashAttribute("registrationForm", registrationForm);
            return "redirect:/submit-management-customized-registration";
        }
        return submitRegistration(redirectAttributes, registrationForm,
                                  "redirect:/submit-management-customized-registration");
    }

    /**
     * Posts the {@link RegistrationForm} that the client has filled out. The form is processed by
     * {@link RegistrationService#submitRegistrationForm(RegistrationForm)}. On success, the client is
     * redirected to the message-page get-mapping and is notified with an on-success message detailing
     * the next steps the client needs to take to complete the registration process.
     */
    private String submitRegistration(final RedirectAttributes redirectAttributes,
                                      final RegistrationForm registrationForm, final String returnOnError) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Post mapping: submit registration");
        try {
            registrationService.submitRegistrationForm(registrationForm);
            redirectAttributes.addFlashAttribute(
                    "message", "Registration request has been processed. \n Please see " +
                            "the email sent with a link to confirm your registration.");
            logger.debug("Successfully submitted registration form");
            return "redirect:/message-page";
        } catch (ClashException | InvalidArgsException | EmailException e) {
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            redirectAttributes.addFlashAttribute("registrationForm", registrationForm);
            logger.debug("Errors: " + e.getErrors());
            return returnOnError;
        }
    }

    /**
     * Gets the confirm-registration page and calls {@link RegistrationService#confirmRegistrationRequest(String)}
     * using the path variable "token". On success, the client is redirected to the message-page get-mapping
     * and is notified of being able to log into their newly-created account.
     *
     * @param model the model
     * @param token the token
     * @return the view name
     */
    @GetMapping("/confirm-registration/{token}")
    public String confirmRegistration(final Model model, @PathVariable("token") final String token) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Showing confirm registration page");
        logger.debug("Token: " + token);
        try {
            if (token == null || token.isBlank()) {
                throw new BadRuntimeVarException("Token cannot be empty");
            }
            registrationService.confirmRegistrationRequest(token);
            logger.debug("Successfully confirmed registration token");
            model.addAttribute("message",
                               "Successfully confirmed registration! You may now login.");
            model.addAttribute("redirectLink", "/login");
            model.addAttribute("redirectMessage", "Go to Login Page");
            return "message-page";
        } catch (NoEntityFoundException | BadRuntimeVarException e) {
            model.addAttribute("errors", e.getErrors());
            logger.debug("Errors: " + e.getErrors());
            return "error";
        }
    }

}
