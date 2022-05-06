package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.forms.ChangePasswordForm;
import com.ecinema.app.exceptions.EmailException;
import com.ecinema.app.exceptions.ExpirationException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.ChangePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class PasswordController {

    private final ChangePasswordService changePasswordService;

    @GetMapping("/change-password")
    public String showForgotPasswordPage(final HttpSession httpSession, final Model model) {
        UserDto userDto = (UserDto) httpSession.getAttribute("user");
        model.addAttribute("loggedIn", userDto != null);
        ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        if (userDto != null) {
            changePasswordForm.setEmail(changePasswordForm.getEmail());
        }
        model.addAttribute("changePasswordForm", changePasswordForm);
        return "change-password";
    }

    @PostMapping("/change-password")
    public String showChangePasswordPage(final Model model, final RedirectAttributes redirectAttributes,
                                         @ModelAttribute("changePasswordForm")
                                         final ChangePasswordForm changePasswordForm) {
        try {
            changePasswordService.submitChangePasswordForm(changePasswordForm);
            redirectAttributes.addFlashAttribute(
                    "message", "An email with further instructions has been sent!\n"
                            + "Your password will not be changed until you click on the token link in the email");
            return "redirect:/message-page";
        } catch (NoEntityFoundException | InvalidArgsException | EmailException e) {
            model.addAttribute("errors", e.getErrors());
            return "change-password";
        }
    }

    @GetMapping("/change-password-confirm/{token}")
    public String confirmChangePassword(final Model model, @PathVariable("token") final String token) {
        try {
            changePasswordService.confirmChangePassword(token);
            model.addAttribute("message", "Success, your password has now been changed!");
            return "message-page";
        } catch (NoEntityFoundException | ExpirationException e) {
            model.addAttribute("errors", e.getErrors());
            return "error";
        }
    }

}
