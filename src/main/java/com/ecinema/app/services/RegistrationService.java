package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.RegistrationDto;
import com.ecinema.app.domain.dtos.UserDto;
import com.ecinema.app.domain.entities.Registration;
import com.ecinema.app.domain.forms.RegistrationForm;
import com.ecinema.app.domain.validators.RegistrationValidator;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.EmailException;
import com.ecinema.app.exceptions.InvalidArgsException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.RegistrationRepository;
import com.ecinema.app.util.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegistrationService extends AbstractEntityService<Registration, RegistrationRepository, RegistrationDto> {

    private final UserService userService;
    private final EmailService emailService;
    private final EncoderService encoderService;
    private final RegistrationValidator registrationValidator;

    public RegistrationService(RegistrationRepository repository, UserService userService,
                               EmailService emailService, EncoderService encoderService,
                               RegistrationValidator registrationValidator) {
        super(repository);
        this.userService = userService;
        this.emailService = emailService;
        this.encoderService = encoderService;
        this.registrationValidator = registrationValidator;
    }

    @Override
    public void onDelete(Registration entity) {}

    @Override
    public RegistrationDto convertToDto(Registration registration) {
        return null;
    }

    public Optional<RegistrationDto> findByToken(String token) {
        return repository.findByToken(token).map(this::convertToDto);
    }

    public List<RegistrationDto> findAllByEmail(String email) {
        return repository.findAllByEmail(email)
                .stream().map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public void deleteAllByEmail(String email) {
        repository.deleteAllByEmail(email);
    }

    public void deleteAllByCreationDateTimeBefore(LocalDateTime localDateTime) {
        repository.deleteAllByCreationDateTimeBefore(localDateTime);
    }

    public void submitRegistrationForm(RegistrationForm registrationForm)
            throws ClashException, InvalidArgsException, EmailException {
        submitRegistrationFormAndGetToken(registrationForm);
    }

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
        if (!registrationForm.getIsPasswordEncoded()) {
            registrationForm.setPassword(encoderService.encode(
                    registrationForm.getPassword()));
            registrationForm.setConfirmPassword(encoderService.encode(
                    registrationForm.getConfirmPassword()));
            registrationForm.setIsPasswordEncoded(true);
        }
        if (!registrationForm.getIsSecurityAnswer1Encoded()) {
            registrationForm.setSecurityAnswer1(encoderService.encode(
                    registrationForm.getSecurityAnswer1()));
            registrationForm.setIsSecurityAnswer1Encoded(true);
        }
        if (!registrationForm.getIsSecurityAnswer2Encoded()) {
            registrationForm.setSecurityAnswer2(encoderService.encode(
                    registrationForm.getSecurityAnswer2()));
            registrationForm.setIsSecurityAnswer2Encoded(true);
        }
        Registration registration = new Registration();
        registration.setCreationDateTime(LocalDateTime.now());
        registration.setToIRegistration(registrationForm);
        String token = UUID.randomUUID().toString();
        registration.setToken(token);
        emailService.sendFromBusinessEmail(
                registrationForm.getEmail(), buildEmail(token), "Confirm Account");
        repository.save(registration);
        logger.debug("Instantiated and saved new registration: " + registration);
        logger.debug("Registration form: " + registrationForm);
        return token;
    }

    public UserDto confirmRegistrationRequest(String token)
            throws NoEntityFoundException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Confirm registration request");
        Registration registration = repository.findByToken(token).orElseThrow(
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
        return "To confirm your ECinema registration, please click here: " +
                "http://localhost:8080/confirm-registration/" + token;
    }

}
