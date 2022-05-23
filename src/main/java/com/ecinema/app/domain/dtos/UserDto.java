package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.domain.contracts.IProfile;
import com.ecinema.app.domain.enums.UserAuthority;
import com.ecinema.app.util.UtilMethods;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserDto extends AbstractDto implements IProfile {

    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDateTime creationDateTime;
    private LocalDateTime lastActivityDateTime;
    private Set<UserAuthority> userAuthorities =
            EnumSet.noneOf(UserAuthority.class);

    public boolean isCustomer() {
        return userAuthorities.contains(UserAuthority.CUSTOMER);
    }

    public boolean isModerator() {
        return userAuthorities.contains(UserAuthority.MODERATOR);
    }

    public boolean isAdmin() {
        return userAuthorities.contains(UserAuthority.ADMIN);
    }

    public String birthdateFormatted() {
        return UtilMethods.localDateFormatted(birthDate);
    }

    public String creationDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(creationDateTime);
    }

    public String lastActivityDateTimeFormatted() {
        return UtilMethods.localDateTimeFormatted(lastActivityDateTime);
    }

}
