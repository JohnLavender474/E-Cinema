package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.Registration;
import com.ecinema.app.domain.forms.RegistrationForm;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.EmailException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.RegistrationRepository;
import com.ecinema.app.services.EmailService;
import com.ecinema.app.services.EncoderService;
import com.ecinema.app.services.RegistrationService;
import com.ecinema.app.services.UserService;
import com.ecinema.app.domain.validators.RegistrationValidator;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * {@inheritDoc}
 * The type Registration request service.
 */
@Service
@Transactional
public class RegistrationServiceImpl extends AbstractServiceImpl<Registration,
        RegistrationRepository> implements RegistrationService {

    private final UserService userService;
    private final EmailService emailService;
    private final EncoderService encoderService;
    private final RegistrationValidator registrationValidator;

    /**
     * Instantiates a new Registration request service.
     *
     * @param repository            the repository
     * @param userService           the user service
     * @param emailService          the email sender service
     * @param registrationValidator the registration form validator
     */
    public RegistrationServiceImpl(RegistrationRepository repository, UserService userService,
                                   EmailService emailService, EncoderService encoderService,
                                   RegistrationValidator registrationValidator) {
        super(repository);
        this.userService = userService;
        this.emailService = emailService;
        this.encoderService = encoderService;
        this.registrationValidator = registrationValidator;
    }

    @Override
    protected void onDelete(Registration registration) {}

    @Override
    public void onDeleteInfo(Long id, Collection<String> info) {}

    @Override
    public void onDeleteInfo(Registration entity, Collection<String> info) {}

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
    public void submitRegistrationForm(RegistrationForm registrationForm)
            throws ClashException, InvalidArgsException, EmailException {
        submitRegistrationFormAndGetToken(registrationForm);
    }

    @Override
    public String submitRegistrationFormAndGetToken(RegistrationForm registrationForm)
            throws ClashException, InvalidArgsException, EmailException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Submit registration request and get token");
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
        logger.debug("Registration form passed validation checks");
        Registration registration = new Registration();
        registration.setCreationDateTime(LocalDateTime.now());
        registration.setToIRegistration(registrationForm);
        registration.setUserAuthorities(registrationForm.getUserAuthorities());
        String encodedPassword = registration.getIsPasswordEncoded() ? registration.getPassword() :
                encoderService.encode(registration.getPassword());
        registration.setPassword(encodedPassword);
        registration.setConfirmPassword(encodedPassword);
        registration.setIsPasswordEncoded(true);
        String encodedAnswer1 = registration.getIsSecurityAnswer1Encoded() ? registration.getSecurityAnswer1() :
                encoderService.removeWhiteSpace_SetToAllUpperCase_AndThenEncode(registration.getSecurityAnswer1());
        registration.setSecurityAnswer1(encodedAnswer1);
        registration.setIsSecurityAnswer1Encoded(true);
        String encodedAnswer2 = registration.getIsSecurityAnswer2Encoded() ? registration.getSecurityAnswer2() :
                encoderService.removeWhiteSpace_SetToAllUpperCase_AndThenEncode(registration.getSecurityAnswer2());
        registration.setSecurityAnswer2(encodedAnswer2);
        registration.setIsSecurityAnswer2Encoded(true);
        String token = UUID.randomUUID().toString();
        registration.setToken(token);
        emailService.sendFromBusinessEmail(
                registrationForm.getEmail(), buildEmail(token), "Confirm Account");
        save(registration);
        logger.debug("Instantiated and saved new registration: " + registration);
        logger.debug("Registration form: " + registrationForm);
        return token;
    }

    @Override
    public UserDto confirmRegistrationRequest(String token)
            throws NoEntityFoundException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Confirm registration request");
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
        UserDto userDTO = userService.register(registration,
                                               registration.getIsPasswordEncoded(),
                                               registration.getIsSecurityAnswer1Encoded(),
                                               registration.getIsSecurityAnswer2Encoded());
        deleteAllByEmail(registration.getEmail());
        logger.debug("Registered new user and returned user dto: " + userDTO);
        return userDTO;
    }

    private String buildEmail(String token) {
        return "To confirm your account, please click here: " +
                "http://localhost:8080/confirm-registration?token=" + token;
    }

}
