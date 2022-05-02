package com.ecinema.app.domain.forms;

import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

/**
 * The type Showroom form.
 */
@Getter
@Setter
public class ShowroomForm implements Serializable {
    private Letter showroomLetter = Letter.A;
    private Map<Letter, Integer> seatMap = new EnumMap<>(Letter.class);
}
