package com.ecinema.app.controllers;

import com.ecinema.app.beans.SecurityContext;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.forms.UserProfileForm;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.services.UserService;
import com.ecinema.app.util.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;
    private final SecurityContext securityContext;
    private final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @GetMapping("/user-profile")
    public String showUserProfilePage(final Model model, final RedirectAttributes redirectAttributes) {
        Long userId = securityContext.findIdOfLoggedInUser();
        if (userId == null) {
            redirectAttributes.addFlashAttribute(
                    "errors", List.of("FATAL ERROR: Forced to logout"));
            return "redirect:/logout";
        }
        UserDto userDto = userService.findById(userId);
        model.addAttribute("user", userDto);
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Get mapping: profile page");
        logger.debug("User DTO: " + userDto);
        return "user-profile";
    }

    @GetMapping("/edit-user-profile")
    public String showEditUserProfilePage(final Model model, final RedirectAttributes redirectAttributes) {
        Long userId = securityContext.findIdOfLoggedInUser();
        if (userId == null) {
            redirectAttributes.addFlashAttribute(
                    "errors", List.of("FATAL ERROR: Forced to logout"));
            return "redirect:/logout";
        }
        UserDto userDto = userService.findById(userId);
        UserProfileForm userProfileForm = new UserProfileForm();
        userProfileForm.setFirstName(userDto.getFirstName());
        userProfileForm.setLastName(userDto.getLastName());
        userProfileForm.setBirthDate(userDto.getBirthDate());
        model.addAttribute("userId", userDto.getId());
        model.addAttribute("profileForm", userProfileForm);
        return "edit-user-profile";
    }

    @PostMapping("/edit-user-profile/{id}")
    public String editUserProfile(final Model model, final RedirectAttributes redirectAttributes,
                                  @ModelAttribute("profileForm") final UserProfileForm userProfileForm,
                                  @PathVariable("id") final Long userId) {
        try {
            userProfileForm.setUserId(userId);
            userService.editUserProfile(userProfileForm);
            redirectAttributes.addFlashAttribute("sucess", "Successfully edited profile");
            return "redirect:/profile";
        } catch (InvalidArgsException e) {
            model.addAttribute("errors", e.getErrors());
            return "edit-user-profile";
        } catch (NoEntityFoundException e) {
            e.getErrors().add("FATAL ERROR: Forced to logout");
            redirectAttributes.addAttribute("errors", e.getErrors());
            return "redirect:/logout";
        }
    }

}
