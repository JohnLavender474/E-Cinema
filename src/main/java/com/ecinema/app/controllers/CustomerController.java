package com.ecinema.app.controllers;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.services.*;
import com.ecinema.app.domain.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final SecurityService securityService;
    private final UserService userService;

    @GetMapping("/customer")
    public String showCustomerPage(final Model model, final RedirectAttributes redirectAttributes) {
        UserDto userDto = securityService.findLoggedInUserDTO();
        if (userDto == null || !userDto.getUserRoles().contains(UserRole.CUSTOMER)) {
            redirectAttributes.addAttribute("failed", "Not authorized to view that page!");
            return "redirect:/error";
        }
        model.addAttribute("userRoles",
                           userService.userRolesAsListOfStrings(userDto.getId()));
        return "customer";
    }

}
