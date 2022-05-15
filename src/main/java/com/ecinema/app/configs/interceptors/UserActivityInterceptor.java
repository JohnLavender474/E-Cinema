package com.ecinema.app.configs.interceptors;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class UserActivityInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final SecurityService securityService;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        if (securityService.userIsLoggedIn()) {
            UserDto userDto = securityService.findLoggedInUserDTO();
            userService.updateLastActivityDateTimeOfUserWithId(userDto.getId());
        }
    }

}
