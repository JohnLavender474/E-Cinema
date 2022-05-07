package com.ecinema.app.domain.forms;

import com.ecinema.app.domain.contracts.IScreening;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

@Data
public class ScreeningForm implements IScreening, Serializable {

    private Long movieId = 0L;
    private Long showroomId = 0L;
    private Integer showtimeHour = 0;
    private Integer showtimeMinute = 0;
    private Integer showtimeDay = 1;
    private Integer showtimeYear = 2022;
    private Month showtimeMonth = Month.JANUARY;

    @Override
    public LocalDateTime getShowtime() {
        return LocalDateTime.of(
                LocalDate.of(showtimeYear, showtimeMonth, showtimeDay),
                LocalTime.of(showtimeHour, showtimeMinute));
    }

    @Override
    public void setShowtime(LocalDateTime showtime) {
        setShowtimeYear(showtime.getYear());
        setShowtimeMonth(showtime.getMonth());
        setShowtimeDay(showtime.getDayOfMonth());
        setShowtimeHour(showtime.getHour());
        setShowtimeMinute(showtime.getMinute());
    }

}
