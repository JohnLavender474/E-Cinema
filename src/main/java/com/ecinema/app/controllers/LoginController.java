package com.ecinema.app.controllers;

import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.exceptions.PasswordMismatchException;
import com.ecinema.app.services.LoginService;
import com.ecinema.app.util.UtilMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The type Login controller.
 * https://www.thymeleaf.org/doc/articles/springsecurity.html
 */
@Controller
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Login get mapping");
        logger.debug("Authentication obj: " + authentication);
        return authentication == null || authentication instanceof AnonymousAuthenticationToken ?
                "login" : "redirect:/index";
    }

    @PostMapping("/perform-login")
    public String performLogin(@RequestParam("username") final String username,
                               @RequestParam("password") final String password) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Perform login post mapping");
        try {
            loginService.login(username, password);
            return "redirect:/index";
        } catch (NoEntityFoundException | PasswordMismatchException e) {
            logger.debug(e.toString());
            return "login";
        }
    }

    @GetMapping("/login-error")
    public String loginError(final Model model) {
        model.addAttribute("error", "Failed to login, bad credentials");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Login error get mapping");
        return authentication == null || authentication instanceof AnonymousAuthenticationToken ?
                "login" : "redirect:/index";
    }

}
