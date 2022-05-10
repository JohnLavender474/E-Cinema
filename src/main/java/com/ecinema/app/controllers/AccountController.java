package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SecurityService securityService;

    @GetMapping("/account")
    public String navigateToAccount(final RedirectAttributes redirectAttributes) {
        UserDto userDto = securityService.findLoggedInUserDTO();
        if (userDto == null) {
            redirectAttributes.addFlashAttribute("error", "FATAL ERROR: Forced to logout");
            return "redirect:/logout";
        }
        if (userDto.getUserRoles().contains(UserRole.ADMIN)) {
            return "redirect:/admin";
        } else if (userDto.getUserRoles().contains(UserRole.MODERATOR)) {
            return "redirect:/moderator";
        } else if (userDto.getUserRoles().contains(UserRole.CUSTOMER)) {
            return "redirect:/customer";
        } else {
            redirectAttributes.addAttribute("error", "No privileges assigned to account");
            return "redirect:/error";
        }
    }

}
