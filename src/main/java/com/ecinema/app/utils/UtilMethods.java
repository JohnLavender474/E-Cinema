package com.ecinema.app.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.concurrent.ThreadLocalRandom;

public class UtilMethods {

    public static LocalDateTime randomDateTime() {
        return LocalDateTime.of(randomDate(), randomTime());
    }

    public static LocalDate randomDate() {
        long minDay = LocalDate.of(2022, Month.JANUARY, 1).toEpochDay();
        long maxDay = LocalDate.of(2022, Month.APRIL, 18).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static LocalTime randomTime() {
        int randomHour = ThreadLocalRandom.current().nextInt(0, 23 + 1);
        int randomMinute = ThreadLocalRandom.current().nextInt(0, 59 + 1);
        return LocalTime.of(randomHour, randomMinute);
    }

}
