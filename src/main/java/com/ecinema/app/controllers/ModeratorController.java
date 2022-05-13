package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.domain.enums.UserAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ModeratorController {

    private final SecurityService securityService;
    private final UserService userService;

    @GetMapping("/moderator")
    public String showModeratorPage(final Model model, final RedirectAttributes redirectAttributes) {
        UserDto userDto = securityService.findLoggedInUserDTO();
        if (userDto == null || !userDto.getUserAuthorities().contains(UserAuthority.MODERATOR)) {
            redirectAttributes.addAttribute("failed", "Not authorized to view that page!");
            return "redirect:/error";
        }
        model.addAttribute("userRoles",
                           userService.userAuthoritiesAsListOfStrings(userDto.getId()));
        return "moderator";
    }

}
