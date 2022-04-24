package com.ecinema.app.config;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.User;
import com.ecinema.app.services.AdminRoleDefService;
import com.ecinema.app.services.CustomerRoleDefService;
import com.ecinema.app.services.ModeratorRoleDefService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.utils.constants.SecurityQuestions;
import com.ecinema.app.utils.constants.UserRole;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

/**
 * The type Startup config.
 */
@Component
public class DateInitializationConfig {

    private final UserService userService;
    private final AdminRoleDefService adminRoleDefService;
    private final CustomerRoleDefService customerRoleDefService;
    private final ModeratorRoleDefService moderatorRoleDefService;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Instantiates a new Startup config.
     *
     * @param userService             the user service
     * @param adminRoleDefService     the admin role def service
     * @param customerRoleDefService  the customer role def service
     * @param moderatorRoleDefService the moderator role def service
     * @param passwordEncoder         the password encoder
     */
    public DateInitializationConfig(UserService userService,
                                    AdminRoleDefService adminRoleDefService,
                                    CustomerRoleDefService customerRoleDefService,
                                    ModeratorRoleDefService moderatorRoleDefService,
                                    BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.adminRoleDefService = adminRoleDefService;
        this.customerRoleDefService = customerRoleDefService;
        this.moderatorRoleDefService = moderatorRoleDefService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * App ready.
     *
     */
    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        /*
        userService.deleteAll();
        User root = new User();
        initRoot(root);
        initRootAdminRoleDef(root);
        initRootModeratorRoleDef(root);
         */
    }

    private void initRoot(User root) {
        root.setEmail("ecinema.app.474@gmail.com");
        root.setPassword(passwordEncoder.encode("password123!"));
        root.setFirstName("Jim");
        root.setLastName("Montgomery");
        root.setBirthDate(LocalDate.of(1998, Month.JULY, 9));
        root.setSecurityQuestion1(SecurityQuestions.SQ1);
        root.setSecurityAnswer1(passwordEncoder.encode( "Bowser"));
        root.setSecurityQuestion2(SecurityQuestions.SQ5);
        root.setSecurityAnswer2(passwordEncoder.encode("root beer"));
        root.setCreationDateTime(LocalDateTime.now());
        root.setLastActivityDateTime(LocalDateTime.now());
        root.setIsAccountEnabled(true);
        root.setIsAccountLocked(false);
        root.setIsAccountExpired(false);
        root.setIsCredentialsExpired(false);
        userService.save(root);
    }

    private void initRootAdminRoleDef(User root) {
        AdminRoleDef adminRoleDef = new AdminRoleDef();
        adminRoleDef.setUser(root);
        root.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        adminRoleDefService.save(adminRoleDef);
    }

    private void initRootModeratorRoleDef(User root) {

    }

}
