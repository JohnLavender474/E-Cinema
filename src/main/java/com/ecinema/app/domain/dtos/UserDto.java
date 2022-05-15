package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.utils.UtilMethods;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

/**
 * {@inheritDoc}
 * The type User dto.
 */
@Getter
@Setter
@ToString
public class UserDto implements AbstractDto {

    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDateTime creationDateTime;
    private LocalDateTime lastActivityDateTime;
    private Set<UserAuthority> userAuthorities =
            EnumSet.noneOf(UserAuthority.class);

    /**
     * Birthdate formatted string.
     *
     * @return the string
     */
    public String birthdateFormatted() {
        return UtilMethods.localDateFormatted(birthDate);
    }

    /**
     * Creation date time formatted string.
     *
     * @return the string
     */
    public String creationDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(creationDateTime);
    }

    /**
     * Last activity date time formatted string.
     *
     * @return the string
     */
    public String lastActivityDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(lastActivityDateTime);
    }

}
