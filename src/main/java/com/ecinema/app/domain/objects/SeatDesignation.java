package com.ecinema.app.domain.objects;

import com.ecinema.app.domain.enums.Letter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDesignation {
    private Letter rowLetter;
    private Integer seatNumber;
}
