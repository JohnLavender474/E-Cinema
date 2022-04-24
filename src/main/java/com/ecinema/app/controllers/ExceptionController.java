package com.ecinema.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The type Exception controller.
 * https://www.thymeleaf.org/doc/articles/springsecurity.html
 */
@ControllerAdvice
public class ExceptionController {

    private final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    /**
     * Exception string.
     *
     * @param throwable the throwable
     * @param model     the model
     * @return the string
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(Throwable throwable, Model model) {
        logger.error("Exception during execution of SpringSecurity app", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }

}
