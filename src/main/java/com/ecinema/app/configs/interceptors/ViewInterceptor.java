package com.ecinema.app.configs.interceptors;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.enums.UserAuthority;
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
        if (modelAndView != null) {
            addAccountDropdownMenu(modelAndView);
        }
    }

    private void addAccountDropdownMenu(final ModelAndView modelAndView) {
        List<Pair<String, String>> dropdownMenu = new ArrayList<>();
        UserDto userDto = securityService.findLoggedInUserDTO();
        if (userDto == null) {
            dropdownMenu.add(new Pair<>("Login", "/login"));
        } else {
            if (userDto.getUserAuthorities().contains(UserAuthority.CUSTOMER)) {
                dropdownMenu.add(new Pair<>("Profile", "/profile"));
                dropdownMenu.add(new Pair<>("Tickets", "/tickets"));
                dropdownMenu.add(new Pair<>("Payment Cards", "/payment-cards"));
            }
            if (userDto.getUserAuthorities().contains(UserAuthority.MODERATOR) ||
                    userDto.getUserAuthorities().contains(UserAuthority.ADMIN)) {
                dropdownMenu.add(new Pair<>("Management", "/management"));
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