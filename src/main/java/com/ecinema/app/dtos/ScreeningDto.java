package com.ecinema.app.dtos;

import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
public class ScreeningDto implements Serializable {
    private Long id;
    private String movieTitle;
    private Letter showroomLetter;
    private LocalDateTime showDateTime;
    private Set<ScreeningSeatDto> screeningSeats = new TreeSet<>();
}
