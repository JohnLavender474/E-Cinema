package com.ecinema.app.domain.forms;

import lombok.Data;

import java.io.Serializable;
import java.time.Month;

@Data
public class ScreeningForm implements Serializable {
    private Long movieId = 0L;
    private Long showroomId = 0L;
    private Integer showtimeHour = 0;
    private Integer showtimeMinute = 0;
    private Integer showtimeDay = 1;
    private Integer showtimeYear = 2022;
    private Month showtimeMonth = Month.JANUARY;
}
