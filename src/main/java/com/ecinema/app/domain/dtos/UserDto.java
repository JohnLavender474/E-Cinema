package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.UserRole;
import lombok.Data;

import java.util.EnumSet;
import java.util.Set;

/**
 * {@inheritDoc}
 * The type User dto.
 */
@Data
public class UserDto implements AbstractDto {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private Set<UserRole> userRoles =
            EnumSet.noneOf(UserRole.class);
}
