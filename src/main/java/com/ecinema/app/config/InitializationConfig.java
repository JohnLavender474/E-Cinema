package com.ecinema.app.config;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.entities.User;
import com.ecinema.app.entities.UserRoleDef;
import com.ecinema.app.services.*;
import com.ecinema.app.utils.UtilMethods;
import com.ecinema.app.utils.constants.SecurityQuestions;
import com.ecinema.app.utils.constants.UserRole;
import com.ecinema.app.utils.forms.RegistrationForm;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.EnumMap;
import java.util.Map;

/**
 * Initializes persistence on app start.
 */
@Component
public class InitializationConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Map<UserRole, UserRoleDefService<? extends UserRoleDef>> userRoleDefServices =
            new EnumMap<>(UserRole.class);

    /**
     * Instantiates a new Startup config.
     *
     * @param userService                the user service
     * @param adminRoleDefService        the admin role def service
     * @param adminTraineeRoleDefService the admin trainee role def service
     * @param customerRoleDefService     the customer role def service
     * @param moderatorRoleDefService    the moderator role def service
     * @param passwordEncoder            the password encoder
     */
    public InitializationConfig(UserService userService,
                                AdminRoleDefService adminRoleDefService,
                                AdminTraineeRoleDefService adminTraineeRoleDefService,
                                CustomerRoleDefService customerRoleDefService,
                                ModeratorRoleDefService moderatorRoleDefService,
                                BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        userRoleDefServices.put(UserRole.ADMIN, adminRoleDefService);
        userRoleDefServices.put(UserRole.ADMIN_TRAINEE, adminTraineeRoleDefService);
        userRoleDefServices.put(UserRole.CUSTOMER, customerRoleDefService);
        userRoleDefServices.put(UserRole.MODERATOR, moderatorRoleDefService);
    }

    /**
     * Called on app ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        userService.deleteAll();
        defineRootAdmin();
    }

    private void defineRootAdmin() {
        User root = new User();
        initRoot(root);
        initRootAdminRoleDef(root);
        initRootModeratorRoleDef(root);
    }

    private void initRoot(User root) {
        root.setUsername("ROOT");
        root.setEmail("ecinema.app.474@gmail.com");
        String encodedPassword = passwordEncoder.encode("password123!");
        root.setPassword(encodedPassword);
        root.setConfirmPassword(encodedPassword);
        root.setFirstName("Jim");
        root.setLastName("Montgomery");
        root.setBirthDate(LocalDate.of(1998, Month.JULY, 9));
        root.setSecurityQuestion1(SecurityQuestions.SQ1);
        String answer1 = "Bowser";
        String answer1Formatted = UtilMethods.removeWhitespace(answer1.toLowerCase());
        String encodedAnswer1 = passwordEncoder.encode(answer1Formatted);
        root.setSecurityAnswer1(encodedAnswer1);
        root.setSecurityQuestion2(SecurityQuestions.SQ5);
        String answer2 = "root beer";
        String answer2Formatted = UtilMethods.removeWhitespace(answer2.toLowerCase());
        String encodedAnswer2 = passwordEncoder.encode(answer2Formatted);
        root.setSecurityAnswer2(encodedAnswer2);
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
        adminRoleDef.setIsRoleValid(true);
        root.getUserRoleDefs().put(UserRole.ADMIN, adminRoleDef);
        AdminRoleDefService adminRoleDefService =
                (AdminRoleDefService) userRoleDefServices.get(UserRole.ADMIN);
        adminRoleDefService.save(adminRoleDef);
    }

    private void initRootModeratorRoleDef(User root) {
        ModeratorRoleDef moderatorRoleDef = new ModeratorRoleDef();
        moderatorRoleDef.setUser(root);
        moderatorRoleDef.setIsRoleValid(true);
        root.getUserRoleDefs().put(UserRole.MODERATOR, moderatorRoleDef);
        ModeratorRoleDefService moderatorRoleDefService =
                (ModeratorRoleDefService) userRoleDefServices.get(UserRole.MODERATOR);
        moderatorRoleDefService.save(moderatorRoleDef);
    }

}
