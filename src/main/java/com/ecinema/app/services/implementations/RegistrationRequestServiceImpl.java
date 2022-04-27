package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.RegistrationRequest;
import com.ecinema.app.entities.User;
import com.ecinema.app.repositories.RegistrationRequestRepository;
import com.ecinema.app.services.EmailSenderService;
import com.ecinema.app.services.RegistrationRequestService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.utils.exceptions.ClashException;
import com.ecinema.app.utils.exceptions.EmailException;
import com.ecinema.app.utils.exceptions.InvalidArgException;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import com.ecinema.app.utils.forms.RegistrationForm;
import com.ecinema.app.utils.validators.RegistrationFormValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The type Registration request service.
 */
@Service
@Transactional
public class RegistrationRequestServiceImpl extends AbstractServiceImpl<RegistrationRequest,
        RegistrationRequestRepository> implements RegistrationRequestService {

    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegistrationFormValidator registrationFormValidator;

    /**
     * Instantiates a new Registration request service.
     *
     * @param repository                the repository
     * @param userService               the user service
     * @param emailSenderService        the email sender service
     * @param passwordEncoder           the password encoder
     * @param registrationFormValidator the registration form validator
     */
    public RegistrationRequestServiceImpl(RegistrationRequestRepository repository, UserService userService,
                                          EmailSenderService emailSenderService, BCryptPasswordEncoder passwordEncoder,
                                          RegistrationFormValidator registrationFormValidator) {
        super(repository);
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.registrationFormValidator = registrationFormValidator;
    }

    /**
     * No implementation for this service.
     *
     * @param registrationRequest the {@link RegistrationRequest} being deleted.
     */
    @Override
    protected void onDelete(RegistrationRequest registrationRequest) {}

    @Override
    public Optional<RegistrationRequest> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public List<RegistrationRequest> findAllByEmail(String email) {
        return repository.findAllByEmail(email);
    }

    @Override
    public void deleteAllByEmail(String email) {
        repository.deleteAllByEmail(email);
    }

    @Override
    public void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime) {
        repository.deleteAllByCreationDateTimeBefore(localDateTime);
    }

    @Override
    public String submitRegistrationRequestAndGetToken(RegistrationForm registrationForm)
            throws ClashException, InvalidArgException, EmailException {
        if (userService.existsByEmail(registrationForm.getEmail())) {
            throw new ClashException(
                    "User with email " + registrationForm.getEmail() + " already exists");
        }
        if (userService.existsByUsername(registrationForm.getUsername())) {
            throw new ClashException(
                    "User with username " + registrationForm.getUsername() + " already exists");
        }
        List<String> errors = new ArrayList<>();
        registrationFormValidator.validate(registrationForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgException(errors);
        }
        String token = UUID.randomUUID().toString();
        emailSenderService.sendFromBusinessEmail(registrationForm.getEmail(),
                                                 buildEmail(token), "Confirm Account");
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setToken(token);
        registrationRequest.setUserRoles(registrationForm.getUserRoles());
        String encodedPassword = passwordEncoder.encode(registrationForm.getPassword());
        registrationRequest.setPassword(encodedPassword);
        registrationRequest.setEmail(registrationForm.getEmail());
        registrationRequest.setUsername(registrationForm.getEmail());
        registrationRequest.setFirstName(registrationForm.getFirstName());
        registrationRequest.setLastName(registrationForm.getLastName());
        registrationRequest.setSecurityQuestion1(registrationForm.getSecurityQuestion1());
        registrationRequest.setSecurityAnswer1(registrationForm.getSecurityAnswer1());
        registrationRequest.setSecurityQuestion2(registrationForm.getSecurityQuestion2());
        registrationRequest.setSecurityAnswer2(registrationForm.getSecurityAnswer2());
        save(registrationRequest);
        return token;
    }

    @Override
    public void confirmRegistrationRequest(String token)
            throws NoEntityFoundException {
        RegistrationRequest registrationRequest = findByToken(token).orElseThrow(
                () -> new NoEntityFoundException("registration request", "token", token));
        if (userService.existsByEmail(registrationRequest.getEmail())) {
            throw new ClashException("Someone else has already claimed the email you provided in the time " +
                                             "between now and when you requested registration.");
        }
        if (userService.existsByUsername(registrationRequest.getUsername())) {
            throw new ClashException("Someone else has already claimed the username you provided in the time " +
                                             "between now and when you requested registration.");
        }
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        user.setFirstName(registrationRequest.getFirstName());
        user.setLastName(registrationRequest.getLastName());
        user.setSecurityQuestion1(registrationRequest.getSecurityQuestion1());
        user.setSecurityAnswer1(registrationRequest.getSecurityAnswer1());
        user.setSecurityQuestion2(registrationRequest.getSecurityQuestion2());
        user.setSecurityAnswer2(registrationRequest.getSecurityAnswer2());
        user.setCreationDateTime(LocalDateTime.now());
        user.setLastActivityDateTime(LocalDateTime.now());
        user.setIsAccountEnabled(true);
        user.setIsAccountLocked(false);
        user.setIsAccountExpired(false);
        user.setIsCredentialsExpired(false);
        userService.save(user);
        userService.addUserRoleDefToUser(user, registrationRequest.getUserRoles());
        deleteAllByEmail(registrationRequest.getEmail());
    }

    private String buildEmail(String token) {
        return "To confirm your account, please click here: " +
                "http://localhost:8080/confirmRegistration?token=" + token;
    }

}
