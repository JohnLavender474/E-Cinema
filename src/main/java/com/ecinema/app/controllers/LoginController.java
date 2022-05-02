package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.exceptions.PasswordMismatchException;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.utils.UtilMethods;
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
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Login get mapping");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logger.debug("Authentication obj: " + authentication);
        return authentication == null || authentication instanceof AnonymousAuthenticationToken ?
                "login" : "redirect:/index";
    }

    @PostMapping("/perform-login")
    public String performLogin(final HttpSession httpSession,
                               @RequestParam("username") final String username,
                               @RequestParam("password") final String password) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Perform login post mapping");
        try {
            UserDto userDto = securityService.login(username, password);
            logger.debug("User DTO: " + userDto);
            httpSession.setAttribute("user", userDto);
            return "redirect:/index";
        } catch (NoEntityFoundException | PasswordMismatchException e) {
            logger.debug(e.toString());
            return "login";
        }
    }

    @GetMapping("/login-error")
    public String loginError(final Model model) {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Login error get mapping");
        model.addAttribute("error", "Failed to login, bad credentials");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication instanceof AnonymousAuthenticationToken ?
                "login" : "redirect:/index";
    }

}
