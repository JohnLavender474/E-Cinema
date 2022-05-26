package com.ecinema.app.controllers;

import com.ecinema.app.TooManyPaymentCardsException;
import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.dtos.PaymentCardDto;
import com.ecinema.app.domain.forms.PaymentCardForm;
import com.ecinema.app.exceptions.InvalidArgumentException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.PaymentCardService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PaymentCardController {

    private final UserService userService;
    private final SecurityContext securityContext;
    private final PaymentCardService paymentCardService;
    private final Logger logger = LoggerFactory.getLogger(PaymentCardController.class);

    @ModelAttribute("paymentCardForm")
    public PaymentCardForm paymentCardForm() {
        return new PaymentCardForm();
    }

    @GetMapping("/payment-cards")
    public String showPaymentCardsPage(final Model model, final RedirectAttributes redirectAttributes) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Get mapping: payment cards");
        Long userId = securityContext.findIdOfLoggedInUser();
        logger.debug("User id: " + userId);
        List<PaymentCardDto> paymentCards = paymentCardService.findAllByCardUserWithId(userId);
        logger.debug("Payment cards: " + paymentCards);
        model.addAttribute("paymentCards", paymentCards);
        return "payment-cards";
    }

    @GetMapping("/add-payment-card")
    public String showAddPaymentCardPage(final Model model,
                                         @ModelAttribute("paymentCardForm") final PaymentCardForm paymentCardForm) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Get mapping: add payment card");
        model.addAttribute("paymentCardForm", paymentCardForm);
        model.addAttribute("action", "/add-payment-card");
        model.addAttribute("banner", "Add Payment Card");
        model.addAttribute("minDate", LocalDate.now());
        model.addAttribute("maxDate", LocalDate.now().plusYears(20));
        return "payment-card";
    }

    @PostMapping("/add-payment-card")
    public String addPaymentCard(final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("paymentCardForm") final PaymentCardForm paymentCardForm) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Post mapping: add payment card");
        logger.debug("Payment card form: " + paymentCardForm);
        return postPaymentCard(redirectAttributes, paymentCardForm,
                               "redirect:/add-payment-card",
                               "Successfully added new payment card");
    }

    @GetMapping("/edit-payment-card/{id}")
    public String showEditPaymentCardPage(final Model model, final RedirectAttributes redirectAttributes,
                                          @PathVariable("id") final Long paymentCardId) {
        try {
            logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
            logger.debug("Get mapping: edit payment card");
            logger.debug("Payment card id: " + paymentCardId);
            model.addAttribute("paymentCardId");
            PaymentCardForm paymentCardForm = paymentCardService.fetchAsForm(paymentCardId);
            logger.debug("Payment card form: " + paymentCardForm);
            model.addAttribute("paymentCardForm", paymentCardForm);
            model.addAttribute("banner", "Edit Payment Card");
            model.addAttribute("action", "/edit-payment-card");
            model.addAttribute("minDate", LocalDate.now());
            model.addAttribute("maxDate", LocalDate.now().plusYears(20));
            return "payment-card";
        } catch (NoEntityFoundException e) {
            logger.debug("Errors: " + e);
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            return "redirect:/payment-cards";
        }
    }

    @PostMapping("/edit-payment-card")
    public String editPaymentCard(final RedirectAttributes redirectAttributes,
                                  @ModelAttribute("paymentCardForm") final PaymentCardForm paymentCardForm) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Post mapping: edit payment card");
        logger.debug("Payment card form: " + paymentCardForm);
        return postPaymentCard(redirectAttributes, paymentCardForm,
                               "redirect:/edit-payment-card",
                               "Successfully edited payment card");
    }

    @PostMapping("/delete-payment-card/{id}")
    public String deletePaymentCard(final RedirectAttributes redirectAttributes,
                                    @PathVariable("id") final Long paymentCardId) {
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Post mapping: delete payment card");
        logger.debug("Payment card id: " + paymentCardId);
        try {
            if (paymentCardId == null) {
                throw new InvalidArgumentException("Payment card id cannot be null");
            }
            paymentCardService.delete(paymentCardId);
            redirectAttributes.addFlashAttribute("success",
                                                 "Successfully deleted payment card");
        } catch (NoEntityFoundException | InvalidArgumentException e) {
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
        }
        return "redirect:/payment-cards";
    }

    private String postPaymentCard(final RedirectAttributes redirectAttributes,
                                   final PaymentCardForm paymentCardForm, final String redirectOnError,
                                   final String onSuccessMessage) {
        try {
            Long userId = securityContext.findIdOfLoggedInUser();
            paymentCardForm.setUserId(userId);
            paymentCardService.submitPaymentCardForm(paymentCardForm);
            logger.debug("Successfully submitted payment card form");
            redirectAttributes.addFlashAttribute("success", onSuccessMessage);
            return "redirect:/payment-cards";
        } catch (NoEntityFoundException | TooManyPaymentCardsException | InvalidArgumentException e) {
            logger.debug("Errors: " + e);
            redirectAttributes.addFlashAttribute("errors", e.getErrors());
            logger.debug("Payment card form: " + paymentCardForm);
            redirectAttributes.addFlashAttribute("paymentCardForm", paymentCardForm);
            logger.debug("Redirect on error: " + redirectOnError);
            return redirectOnError;
        }
    }

}
