package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.enums.UserAuthority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private Set<UserAuthority> userAuthorities =
            EnumSet.noneOf(UserAuthority.class);
}
