package com.ecinema.app.domain.contracts;

import java.time.LocalDateTime;

/**
 * The interface Screening.
 */
public interface IScreening {

    /**
     * Gets movie id.
     *
     * @return the movie id
     */
    Long getMovieId();

    /**
     * Gets showroom id.
     *
     * @return the showroom id
     */
    Long getShowroomId();

    /**
     * Gets showtime.
     *
     * @return the showtime
     */
    LocalDateTime getShowtime();

    /**
     * Sets showtime.
     *
     * @param showtime the showtime
     */
    void setShowtime(LocalDateTime showtime);

    /**
     * Showtime formatted string.
     *
     * @return the string
     */
    default String showtimeFormatted() {
        int hour = getShowtime().getHour();
        String amOrPm;
        if (hour < 12) {
            amOrPm = "AM";
        } else {
            amOrPm = "PM";
            hour -= 12;
        }
        return getShowtime().getMonth() + "-" +
                getShowtime().getDayOfMonth() + "-" +
                getShowtime().getYear() + "; " +
                hour + ":" + getShowtime().getMinute() + amOrPm;
    }

}
