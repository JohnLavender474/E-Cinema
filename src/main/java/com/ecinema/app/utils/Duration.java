package com.ecinema.app.utils;

import com.ecinema.app.exceptions.InvalidDurationException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/** Represents time duration in hours and minutes. */
@Getter
@NoArgsConstructor
public class Duration implements Comparable<Duration>, Serializable {

    private Integer hours;
    private Integer minutes;

    /**
     * Instantiates a new Duration with {@link #hours} equal to hours and {@link #minutes} equal to minutes.
     *
     * @param hours   the hours to assign to this.hours.
     * @param minutes the minutes to assign to this.minutes.
     * @throws InvalidDurationException thrown if the value of hours or minutes is less than zero.
     */
    public Duration(Integer hours, Integer minutes)
            throws InvalidDurationException {
        setHours(hours);
        setMinutes(minutes);
    }

    /**
     * Sets hours.
     *
     * @param hours the hours to assign to this.hours.
     * @throws InvalidDurationException thrown if the value of hours is less than zero.
     */
    public void setHours(Integer hours)
            throws InvalidDurationException {
        if (hours < 0) {
            throw new InvalidDurationException("Hours cannot be less than 0");
        }
        this.hours = hours;
    }

    /**
     * Sets minutes. While minutes is greater than or equal to 60, the following will be performed:
     *
     * {@code
     * while (minutes >= 60) {
     *     minutes -= 60;
     *     hours++;
     * }
     * }
     *
     * @param minutes the minutes to assign to this.minutes.
     * @throws InvalidDurationException the invalid duration exception
     */
    public void setMinutes(Integer minutes)
            throws InvalidDurationException {
        if (minutes < 0) {
            throw new InvalidDurationException("Minutes cannot be less than 0");
        }
        while (minutes >= 60) {
            minutes -= 60;
            hours++;
        }
        this.minutes = minutes;
    }

    @Override
    public String toString() {
        if (minutes < 10) {
            return hours + ":0" + minutes;
        } else {
            return hours + ":" + minutes;
        }
    }

    @Override
    public int compareTo(Duration o) {
        int comparison = hours.compareTo(o.getHours());
        if (comparison == 0) {
            comparison = minutes.compareTo(o.getMinutes());
        }
        return comparison;
    }

    @Override
    public int hashCode() {
        int hash = 21;
        hash += hours * 7;
        hash += minutes * 7;
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Duration duration &&
                hours.equals(duration.getHours()) &&
                minutes.equals(duration.getMinutes());
    }

    /**
     * Convert string representation of Duration into new Duration instance. String representation
     * is hours:minutes. See {@link #toString()}.
     *
     * @param str the String to convertToDto to a new Duration instance.
     * @return the new Duration instance.
     * @throws InvalidDurationException see {@link #setHours(Integer)} and {@link #setMinutes(Integer)}.
     */
    public static Duration strToDuration(String str)
            throws InvalidDurationException {
        String[] tokens = str.split(":");
        if (tokens.length != 2 || !UtilMethods.isDigitsOnly(tokens[0]) || !UtilMethods.isDigitsOnly(tokens[1])) {
            throw new InvalidDurationException("String cannot be converted to Duration");
        }
        Integer hours = Integer.parseInt(tokens[0]);
        Integer minutes = Integer.parseInt(tokens[1]);
        return new Duration(hours, minutes);
    }

    public static Duration randomDuration() {
        int hours = UtilMethods.randomIntBetween(0, 4);
        int minutes = UtilMethods.randomIntBetween(0, 59);
        return new Duration(hours, minutes);
    }

}
