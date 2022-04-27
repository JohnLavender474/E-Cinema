package com.ecinema.app.controllers;

import com.ecinema.app.services.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);
    private final SecurityService securityService;

    public TestController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping({"/", "/home", "/index"})
    public String home1() {
        return "/index";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") final String username,
                      @RequestParam("password") final String password) {
        logger.debug("Login post mapping method accessed");
        securityService.login(username, password);
        return "index";
    }

    @GetMapping("/customer")
    public String customer() {
        return "/customer";
    }

    @GetMapping("/moderator")
    public String moderator() {
        return "/moderator";
    }

    @GetMapping("/admin_trainee")
    public String adminTrainee() {
        return "/admin_trainee";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin";
    }

}
