package com.ecinema.app.controllers;

import com.ecinema.app.domain.forms.RegistrationForm;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final SecurityService securityService;
    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(final Model model, final RedirectAttributes redirectAttributes) {
        if (securityService.userIsLoggedIn()) {
            redirectAttributes.addFlashAttribute("errors", List.of("Already logged in"));
            return "redirect:/error";
        }
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(final Model model, final RedirectAttributes redirectAttributes,
                           @ModelAttribute("registrationForm") final RegistrationForm registrationForm) {
        return null;
    }

}
