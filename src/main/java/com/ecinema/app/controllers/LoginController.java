package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.exceptions.PasswordMismatchException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * The type Login controller.
 * https://www.thymeleaf.org/doc/articles/springsecurity.html
 */
@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final SecurityService securityService;

    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        logger.info("Login get mapping");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken ?
                "login" : "redirect:/index";
    }

    @PostMapping("/perform-login")
    public String performLogin(final HttpSession httpSession,
                               @RequestParam("username") final String username,
                               @RequestParam("password") final String password) {
        logger.info("Perform login post mapping");
        try {
            UserDto userDto = securityService.login(username, password);
            httpSession.setAttribute("user", userDto);
            return "redirect:/index";
        } catch (NoEntityFoundException | PasswordMismatchException e) {
            logger.info(e.toString());
            return "login";
        }
    }

    @GetMapping("/login-error")
    public String loginError(final Model model) {
        logger.info("Login error get mapping");
        model.addAttribute("error", "Failed to login, bad credentials");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken ?
                "login" : "redirect:/index";
    }

}
