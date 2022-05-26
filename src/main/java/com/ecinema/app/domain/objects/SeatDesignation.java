package com.ecinema.app.domain.objects;

import com.ecinema.app.domain.enums.Letter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatDesignation implements Serializable {

    private Letter rowLetter;
    private Integer seatNumber;

    @Override
    public String toString() {
        return rowLetter + "-" + seatNumber;
    }

}
