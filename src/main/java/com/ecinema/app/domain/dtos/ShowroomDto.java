package com.ecinema.app.domain.dtos;

import com.ecinema.app.domain.contracts.IShowroom;
import com.ecinema.app.domain.enums.Letter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ShowroomDto implements AbstractDto, IShowroom {
    private Long id;
    private Letter showroomLetter;
    private Integer numberOfRows;
    private Integer numberOfSeatsPerRow;
}
