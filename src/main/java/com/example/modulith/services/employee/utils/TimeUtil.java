package com.example.modulith.services.employee.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class TimeUtil {

    public final static String format1 = "MM/dd/yyyy HH:mm:ss.SSS";

    public final static String format2 = "yyyy-MM-dd HH:mm:ss.SSS";

    public final static String format3 = "MM/dd/yyyy";

    public final static String format4 = "MM/dd/yy";

    public final static String format5 = "yyyy/MM/dd";

    public final static String format6 = "M/d/yyyy";

    public final static String format7 = "MM/d/yyyy";

    public final static String format8 = "M/dd/yyyy";

    public final static String format9 = "MM-dd-yyyy";

    public final static String format10 = "yyyy-MM-dd";

    public final static String format11 = "MM-d-yyyy";

    public final static String format12 = "yyyy-MM-dd-HH-mm-ss-SSS";

    public final static String format13 = "MM/dd/yyy HH:mm";

    public final static String format14 = "yyyyMMddHHmmssSSS";

    public static final String format15 = "MM/dd/yyyy HH:mm:ss";

    public static final String format16 = "yyyy/MM/dd HH:mm:ss";

    public static final String format17 = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String format18 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public static final String format19 = "hh:mm a";

    public static final String format20 = "MM/dd";

    public final static String format22 = "MMM YYYY";

    public final static String format23 = "MMM dd, YYYY";

    public final static String format24 = "MM/dd/yyyy hh:mma";

    public final static String format27 = "MM_dd_yyyy HH:mm";

    public static final String format28 = "MM/yyyy";

    public final static String format_MMM = "MMM";

    public final static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public final static String dateFormatFull = "yyyy-MM-dd HH:mm:ss.SSS";

    public final static String formatMMddyyyy = "MMddyyyy";

    public final static String formatMMdd = "MM/dd";

    public final static String format21 = "yyyy-MM-dd_HH-mm";

    /**
     * 美国洛杉矶时区
     */
    public final static String US_LA_TIME_ZONE = "American/Los_Angeles";

    public final static String format25 = "yyyy-MM-dd'T'HH:mm:ss";

    public final static String format26 = "MM_dd";

    private final static String[] dateFormats = new String[]{"MM/dd/yyyy", "yyyy/MM/dd", "MM-dd-yyyy", "yyyy-MM-dd", "M/dd/yyyy", "M/d/yyyy", "MM/d/yyyy"};


    /**
     * Format a LocalTime object to a string using the given pattern.
     *
     * @param time   the LocalTime object
     * @param format the pattern to format the time
     * @return the formatted time string
     */
    public static String formatLocalTime(LocalTime time, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
        return time.format(formatter);
    }

    /**
     * Parse a string into a LocalTime object using the given pattern.
     *
     * @param timeString the time string
     * @param format     the pattern used in the time string
     * @return the parsed LocalTime object
     */
    public static LocalTime parseLocalTime(String timeString, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, Locale.ENGLISH);
        return LocalTime.parse(timeString, formatter);
    }

    /**
     * Get the current LocalTime in a specific format.
     *
     * @param format the pattern to format the current time
     * @return the formatted current time string
     */
    public static String getCurrentTimeFormatted(String format) {
        LocalTime now = LocalTime.now();
        return formatLocalTime(now, format);
    }

    /**
     * Calculate the difference between two LocalTime objects in minutes.
     *
     * @param start the start time
     * @param end   the end time
     * @return the difference in minutes
     */
    public static long getMinutesDifference(LocalTime start, LocalTime end) {
        return ChronoUnit.MINUTES.between(start, end);
    }

    /**
     * Add minutes to a given LocalTime.
     *
     * @param time    the original time
     * @param minutes the number of minutes to add
     * @return the new LocalTime after adding the minutes
     */
    public static LocalTime addMinutes(LocalTime time, int minutes) {
        return time.plusMinutes(minutes);
    }

    /**
     * Subtract minutes from a given LocalTime.
     *
     * @param time    the original time
     * @param minutes the number of minutes to subtract
     * @return the new LocalTime after subtracting the minutes
     */
    public static LocalTime subtractMinutes(LocalTime time, int minutes) {
        return time.minusMinutes(minutes);
    }
}