package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.contracts.IShowroom;
import com.ecinema.app.domain.enums.Letter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * The type Showroom form.
 */
@Getter
@Setter
public class ShowroomForm implements IShowroom, Serializable {
    private Letter showroomLetter = Letter.A;
    private Integer numberOfRows = 1;
    private Integer numberOfSeatsPerRow = 1;
}
