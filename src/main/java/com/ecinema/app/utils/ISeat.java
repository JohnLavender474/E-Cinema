package com.ecinema.app.utils;

import java.util.Comparator;

/**
 * The interface Seat.
 */
public interface ISeat {

    Comparator<ISeat> comparator = Comparator
            .comparing(ISeat::getRowLetter)
            .thenComparingInt(ISeat::getSeatNumber);

    /**
     * Gets row letter.
     *
     * @return the row letter
     */
    Letter getRowLetter();

    /**
     * Gets seat number.
     *
     * @return the seat number
     */
    Integer getSeatNumber();

}
