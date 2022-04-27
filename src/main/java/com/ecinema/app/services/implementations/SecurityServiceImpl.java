package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.User;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    public SecurityServiceImpl(UserService userService, DaoAuthenticationProvider daoAuthenticationProvider) {
        this.userService = userService;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @Override
    public void login(final String s, final String password)
            throws NoEntityFoundException {
        logger.info("Security Service login");
        User user = userService.findByUsernameOrEmail(s).orElseThrow(
                () -> new NoEntityFoundException("user", "authentication", s));
        for (GrantedAuthority authority : user.getAuthorities()) {
            logger.info("User has authority: " + authority.getAuthority());
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, password, user.getAuthorities());
        daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            securityContext.setAuthentication(usernamePasswordAuthenticationToken);
            logger.info(String.format("Auto login %s success!", user.getUsername()));
        } else {
            logger.info("Token is not authenticated");
        }
    }

    @Override
    public String findLoggedInUserEmail() {
        Object o = SecurityContextHolder.getContext().getAuthentication().getDetails();
        return o instanceof UserDetails userDetails ? userDetails.getUsername() : null;
    }

}
