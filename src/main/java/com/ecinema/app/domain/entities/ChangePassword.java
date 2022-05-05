package com.ecinema.app.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChangePassword extends AbstractEntity {

    @Column
    private Long userId;

    @Column
    private String token;

    @Column
    private String password;

    @Column
    private LocalDateTime creationDateTime;

    @Column
    private LocalDateTime expirationDateTime;

}
