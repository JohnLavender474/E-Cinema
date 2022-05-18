package com.ecinema.app.configs.interceptors;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The type User dto interceptor.
 */
@RequiredArgsConstructor
public class UserDtoInterceptor implements HandlerInterceptor {

    private final SecurityService securityService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        UserDto userDto = securityService.findLoggedInUserDTO();
        modelAndView.addObject("user", userDto);
    }

}
