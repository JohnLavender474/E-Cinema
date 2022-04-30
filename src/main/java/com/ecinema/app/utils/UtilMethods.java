package com.ecinema.app.utils;

import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class UtilMethods {

    private static final Random random = new Random(System.currentTimeMillis());

    public static Random getRandom() {
        return random;
    }

    public static int randomIntBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static <T> List<T> findAllThatCollectionContainsIfAny(
            Collection<T> checkIfThisContains, Collection<T> checkIfOtherContainsOfThis) {
        List<T> list = new ArrayList<>();
        for (T t : checkIfOtherContainsOfThis) {
            if (checkIfThisContains.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    public static <T> List<T> findAllKeysThatMapContainsIfAny(Map<T, ?> map, Collection<T> collection) {
        return findAllThatCollectionContainsIfAny(map.keySet(), collection);
    }

    public static boolean isAlphabeticalOnly(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isAlphabetic(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDigitsOnly(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAlphaAndDigitsOnly(String s) {
        for (char c : s.toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static int numSpecialChars(String s) {
        int num = 0;
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c) && !Character.isAlphabetic(c)) {
                num++;
            }
        }
        return num;
    }

    public static String removeSubstrings(String s, String... targets) {
        String str = "";
        for (String target : targets) {
            str = s.replace(target, "");
        }
        return str;
    }

    public static String removeWhitespace(String s) {
        return s.replace("\\s", "");
    }

    public static LocalDateTime randomDateTime() {
        return LocalDateTime.of(randomDate(), randomTime());
    }

    public static LocalDate randomDate() {
        long minDay = LocalDate.of(2022, Month.JANUARY, 1).toEpochDay();
        LocalDate localDate = LocalDate.now();
        long maxDay = LocalDate.of(localDate.getYear(), localDate.getMonth(), localDate.getDayOfMonth()).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    public static LocalTime randomTime() {
        int randomHour = ThreadLocalRandom.current().nextInt(0, 23 + 1);
        int randomMinute = ThreadLocalRandom.current().nextInt(0, 59 + 1);
        return LocalTime.of(randomHour, randomMinute);
    }

}
