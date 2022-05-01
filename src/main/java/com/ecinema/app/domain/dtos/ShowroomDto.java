package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
public class ShowroomDto implements AbstractDto {
    private Long id;
    private Letter showroomLetter;
    private Set<ShowroomSeatDto> showroomSeatDTOs = new TreeSet<>();
}
