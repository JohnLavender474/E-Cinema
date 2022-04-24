package com.ecinema.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping({"/", "/home", "/index"})
    public String home1() {
        return "/index";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
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

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }

}
