package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.contracts.IProfile;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class UserProfileForm implements IProfile, Serializable {
    private Long userId = 0L;
    private String firstName = "";
    private String lastName = "";
    private LocalDate birthDate = LocalDate.now();
}
