package com.ecinema.app.configs.interceptors;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserRole;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.utils.Pair;
import com.ecinema.app.utils.UtilMethods;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * The type View interceptor.
 */
@RequiredArgsConstructor
public class ViewInterceptor implements HandlerInterceptor {

    private final SecurityService securityService;
    private final Logger logger = LoggerFactory.getLogger(ViewInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        addAccountDropdownMenu(modelAndView);
    }

    private void addAccountDropdownMenu(final ModelAndView modelAndView) {
        List<Pair<String, String>> dropdownMenu = new ArrayList<>();
        UserDto userDto = securityService.findLoggedInUserDTO();
        if (userDto == null) {
            dropdownMenu.add(new Pair<>("Login", "/login"));
        } else {
            if (userDto.getUserRoles().contains(UserRole.CUSTOMER)) {
                dropdownMenu.add(new Pair<>("Customer Account", "/customer"));
            }
            if (userDto.getUserRoles().contains(UserRole.MODERATOR)) {
                dropdownMenu.add(new Pair<>("Moderator Account", "/moderator"));
            }
            if (userDto.getUserRoles().contains(UserRole.ADMIN)) {
                dropdownMenu.add(new Pair<>("Admin Account", "/admin"));
            }
            dropdownMenu.add(new Pair<>("Logout", "/logout"));
        }
        modelAndView.addObject("dropdownMenu", dropdownMenu);
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Controller Advice: account dropdown menu");
        logger.debug("User DTO: " + userDto);
        logger.debug("Dropdown menu: " + dropdownMenu);
    }

}
