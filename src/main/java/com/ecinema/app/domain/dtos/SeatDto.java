package com.ecinema.app.domain.dtos;

import com.ecinema.app.utils.Letter;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class SeatDto implements AbstractDto, Comparable<SeatDto> {

    private Long id;
    private Letter rowLetter;
    private Integer seatNumber;

    @Override
    public int compareTo(SeatDto o) {
        int comp = rowLetter.compareTo(o.getRowLetter());
        if (comp == 0) {
            comp = seatNumber.compareTo(o.getSeatNumber());
        }
        return comp;
    }

    @Override
    public int hashCode() {
        int hash = 7 * rowLetter.hashCode();
        hash += 7 * seatNumber.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof SeatDto seat &&
                seat.getRowLetter().equals(rowLetter) &&
                seat.getSeatNumber().equals(seatNumber);
    }

}
