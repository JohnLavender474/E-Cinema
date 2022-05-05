package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.ChangePassword;
import com.ecinema.app.domain.entities.User;
import com.ecinema.app.domain.forms.ChangePasswordForm;
import com.ecinema.app.exceptions.*;
import com.ecinema.app.repositories.ChangePasswordRepository;
import com.ecinema.app.services.ChangePasswordService;
import com.ecinema.app.services.EmailService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.validators.PasswordValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The type Change password request service.
 */
@Service
public class ChangePasswordServiceImpl extends AbstractServiceImpl<ChangePassword,
        ChangePasswordRepository> implements ChangePasswordService {

    public static final long EXPIRATION_MINUTES = 30L;
    public static final int MAX_TOKEN_INSTANTATIONS = 5;

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordValidator passwordValidator;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Instantiates a new Abstract service.
     *
     * @param repository the repository
     */
    public ChangePasswordServiceImpl(ChangePasswordRepository repository,
                                     UserService userService,
                                     EmailService emailService,
                                     PasswordValidator passwordValidator,
                                     BCryptPasswordEncoder passwordEncoder) {
        super(repository);
        this.userService = userService;
        this.emailService = emailService;
        this.passwordValidator = passwordValidator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void onDelete(ChangePassword entity) {}

    @Override
    public void submitChangePasswordForm(ChangePasswordForm changePasswordForm)
            throws NoEntityFoundException, InvalidArgsException, EmailException {
        List<String> errors = new ArrayList<>();
        passwordValidator.validate(changePasswordForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        Long userId = userService.findIdByEmail(changePasswordForm.getEmail()).orElseThrow(
                () -> new NoEntityFoundException("user", "email", changePasswordForm.getEmail()));
        String encryptedPassword = passwordEncoder.encode(changePasswordForm.getPassword());
        LocalDateTime creationDateTime = LocalDateTime.now();
        LocalDateTime expirationDateTime = creationDateTime.plusMinutes(EXPIRATION_MINUTES);
        ChangePassword changePassword = new ChangePassword();
        changePassword.setPassword(encryptedPassword);
        changePassword.setUserId(userId);
        changePassword.setCreationDateTime(creationDateTime);
        changePassword.setExpirationDateTime(expirationDateTime);
        String token;
        int tokenInstantiationAttempts = 0;
        while (true) {
            token = UUID.randomUUID().toString();
            if (existsByToken(token)) {
                tokenInstantiationAttempts++;
            } else {
                break;
            }
            if (tokenInstantiationAttempts >= MAX_TOKEN_INSTANTATIONS) {
                throw new BadInstantiationException(
                        "Failed to instantiate token due to too many clashes with existing tokens");
            }
        }
        changePassword.setToken(token);
        sendRequestEmail(changePasswordForm.getEmail(), token,
                         creationDateTime, expirationDateTime);
        save(changePassword);
    }

    @Override
    public void confirmChangePassword(String token)
            throws NoEntityFoundException, ExpirationException {
        ChangePassword changePassword = repository.findByToken(token).orElseThrow(
                () -> new NoEntityFoundException("change password request", "token", token));
        if (changePassword.getExpirationDateTime().isBefore(LocalDateTime.now())) {
            throw new ExpirationException("The token you have request has already expired");
        }
        User user = userService.findById(changePassword.getUserId()).orElseThrow(
                () -> new NoEntityFoundException("user", "id", changePassword.getUserId()));
        user.setPassword(changePassword.getPassword());
        userService.save(user);
        repository.deleteAllByUserId(user.getId());
        sendConfirmationEmail(user.getEmail());
    }

    @Override
    public boolean existsByToken(String token) {
        return repository.existsByToken(token);
    }

    private void sendRequestEmail(String email, String token, LocalDateTime creationDateTime,
                                  LocalDateTime expirationDateTime)
            throws EmailException {
        String emailBody = "A request has been made to change your password.\n" +
                "If you would like to confirm this request, then click the link below:\n" +
                "https//:localhost:8080/change-password-confirm/" + token + "\n\n" +
                "This request was made at " + creationDateTime.toString() + "\n" +
                "and expires at " + expirationDateTime.toString();
        emailService.sendFromBusinessEmail(email, emailBody, "Change Password Request");
    }

    private void sendConfirmationEmail(String email)
            throws EmailException {
        String emailBody = "The password for your account has successfully been changed.";
        emailService.sendFromBusinessEmail(email, emailBody, "Change Password Confirmation");
    }

}
