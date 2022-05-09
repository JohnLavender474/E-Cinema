package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IShowroom;
import com.ecinema.app.domain.enums.Letter;
import lombok.*;

@Getter
@Setter
@ToString
public class ShowroomDto implements AbstractDto, IShowroom {
    private Long id;
    private Letter showroomLetter;
    private Integer numberOfRows;
    private Integer numberOfSeatsPerRow;
}
