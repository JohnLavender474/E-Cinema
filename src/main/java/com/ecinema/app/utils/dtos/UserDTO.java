package com.ecinema.app.utils.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * {@inheritDoc}
 * The type User dto.
 */
@Data
public class UserDTO implements Serializable {
    private Long id;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
}
