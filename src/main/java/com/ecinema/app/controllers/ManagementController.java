package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.services.*;
import com.ecinema.app.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The type Admin controller.
 */
@Controller
@RequiredArgsConstructor
public class ManagementController {

    private final UserService userService;
    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(ManagementController.class);

    /**
     * Show admin page string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/management")
    public String showAdminPage(final Model model) {
        UserDto userDto = securityService.findLoggedInUserDTO();
        List<String> userAuthorities = userService.userAuthoritiesAsListOfStrings(userDto.getId());
        if (userDto.getUserAuthorities().contains(UserAuthority.MODERATOR)) {
            model.addAttribute("moderator", true);
        }
        if (userDto.getUserAuthorities().contains(UserAuthority.ADMIN)) {
            model.addAttribute("admin", true);
        }
        model.addAttribute("userAuthorities", userAuthorities);
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: Admin page");
        logger.debug("User authorities: " + userAuthorities);
        return "management";
    }

}
