package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.exceptions.PasswordMismatchException;
import com.ecinema.app.services.SecurityService;
import com.ecinema.app.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);

    public SecurityServiceImpl(UserService userService, BCryptPasswordEncoder passwordEncoder,
                               DaoAuthenticationProvider daoAuthenticationProvider) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @Override
    public void login(final String s, final String password)
            throws NoEntityFoundException, PasswordMismatchException {
        logger.debug("Security Service login method");
        UserDetails user = userService.loadUserByUsername(s);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordMismatchException(s);
        }
        for (GrantedAuthority authority : user.getAuthorities()) {
            logger.debug("User has authority: " + authority.getAuthority());
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        user, password, user.getAuthorities());
        daoAuthenticationProvider.authenticate(usernamePasswordAuthenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            logger.debug(String.format("Auto login %s success!", user.getUsername()));
            securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        }
    }

    @Override
    public UserDto findLoggedInUserDTO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object o = authentication.getPrincipal();
        return o instanceof User user ? userService.convertToDto(user) : null;
    }

    @Override
    public Long findIdOfLoggedInUser() {
        UserDto userDto = findLoggedInUserDTO();
        return userDto != null ? userDto.getId() : null;
    }

}
