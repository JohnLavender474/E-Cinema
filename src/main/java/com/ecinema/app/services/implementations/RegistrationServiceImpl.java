package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.Registration;
import com.ecinema.app.repositories.RegistrationRepository;
import com.ecinema.app.services.EmailSenderService;
import com.ecinema.app.services.RegistrationService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.dtos.UserDto;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.EmailException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.forms.RegistrationForm;
import com.ecinema.app.validators.RegistrationValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * {@inheritDoc}
 * The type Registration request service.
 */
@Service
@Transactional
public class RegistrationServiceImpl extends AbstractServiceImpl<Registration,
        RegistrationRepository> implements RegistrationService {

    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RegistrationValidator registrationValidator;

    /**
     * Instantiates a new Registration request service.
     *
     * @param repository                the repository
     * @param userService               the user service
     * @param emailSenderService        the email sender service
     * @param passwordEncoder           the password encoder
     * @param registrationValidator the registration form validator
     */
    public RegistrationServiceImpl(RegistrationRepository repository, UserService userService,
                                   EmailSenderService emailSenderService, BCryptPasswordEncoder passwordEncoder,
                                   RegistrationValidator registrationValidator) {
        super(repository);
        this.userService = userService;
        this.emailSenderService = emailSenderService;
        this.passwordEncoder = passwordEncoder;
        this.registrationValidator = registrationValidator;
    }

    /**
     * No implementation for this service.
     *
     * @param registration the {@link Registration} being deleted.
     */
    @Override
    protected void onDelete(Registration registration) {}

    @Override
    public Optional<Registration> findByToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public List<Registration> findAllByEmail(String email) {
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
            throws ClashException, InvalidArgsException, EmailException {
        if (userService.existsByEmail(registrationForm.getEmail())) {
            throw new ClashException(
                    "User with email " + registrationForm.getEmail() + " already exists");
        }
        if (userService.existsByUsername(registrationForm.getUsername())) {
            throw new ClashException(
                    "User with username " + registrationForm.getUsername() + " already exists");
        }
        List<String> errors = new ArrayList<>();
        registrationValidator.validate(registrationForm, errors);
        if (!errors.isEmpty()) {
            throw new InvalidArgsException(errors);
        }
        String token = UUID.randomUUID().toString();
        emailSenderService.sendFromBusinessEmail(registrationForm.getEmail(),
                                                 buildEmail(token), "Confirm Account");
        Registration registration = new Registration();
        registration.setToken(token);
        registration.setUserRoles(registrationForm.getUserRoles());
        String encodedPassword = passwordEncoder.encode(registrationForm.getPassword());
        registration.setPassword(encodedPassword);
        registration.setConfirmPassword(encodedPassword);
        registration.setEmail(registrationForm.getEmail());
        registration.setUsername(registrationForm.getUsername());
        registration.setFirstName(registrationForm.getFirstName());
        registration.setLastName(registrationForm.getLastName());
        registration.setSecurityQuestion1(registrationForm.getSecurityQuestion1());
        registration.setSecurityAnswer1(registrationForm.getSecurityAnswer1());
        registration.setSecurityQuestion2(registrationForm.getSecurityQuestion2());
        registration.setSecurityAnswer2(registrationForm.getSecurityAnswer2());
        save(registration);
        return token;
    }

    @Override
    public UserDto confirmRegistrationRequest(String token)
            throws NoEntityFoundException, ClashException {
        Registration registration = findByToken(token).orElseThrow(
                () -> new NoEntityFoundException("registration request", "token", token));
        if (userService.existsByEmail(registration.getEmail())) {
            throw new ClashException("Someone else has already claimed the email you provided in the time " +
                                             "between now and when you requested registration.");
        }
        if (userService.existsByUsername(registration.getUsername())) {
            throw new ClashException("Someone else has already claimed the username you provided in the time " +
                                             "between now and when you requested registration.");
        }
        UserDto userDTO = userService.register(registration);
        deleteAllByEmail(registration.getEmail());
        return userDTO;
    }

    private String buildEmail(String token) {
        return "To confirm your account, please click here: " +
                "http://localhost:8080/confirmRegistration?token=" + token;
    }

}
