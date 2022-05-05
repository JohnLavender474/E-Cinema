package com.ecinema.app.controllers;

import com.ecinema.app.domain.forms.ChangePasswordForm;
import com.ecinema.app.services.ChangePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final ChangePasswordService changePasswordService;

    @GetMapping("/change-password")
    public String showForgotPasswordPage(final Model model) {
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "change-password";
    }

    @PostMapping("/change-password-request")
    public String forgotPassword(final RedirectAttributes redirectAttributes,
                                 @ModelAttribute("changePasswordForm") final ChangePasswordForm changePasswordForm) {
        changePasswordService.submitChangePasswordForm(changePasswordForm);
        redirectAttributes.addFlashAttribute(
                "success", "An email with further instructions has been sent!\n"
                        + "Your password will not be changed until you click on the token link in the email");
        return "message-page";
    }

    @GetMapping("/change-password-confirm/{token}")
    public String confirmChangePassword(final Model model, @PathVariable("token") final String token) {
        changePasswordService.confirmChangePassword(token);
        model.addAttribute("success", "Success, your password has now been changed!");
        return "message-page";
    }

}
