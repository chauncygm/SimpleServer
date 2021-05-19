package game.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author gongshengjun
 * @date 2021/5/17 20:42
 */
public class TimeUtil {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static long OFFSET_TIME = 0L;

    private final static ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    public static void setOffsetTime(long offsetTime) {
        OFFSET_TIME = offsetTime;
    }

    public static long now() {
        return System.currentTimeMillis() + OFFSET_TIME;
    }

    public static long toMilli(LocalDateTime time) {
        return time.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    public static long getTime(String dateTime) {
        return toMilli(parseDateTime(dateTime));
    }

    public static LocalDateTime getTodayBeginTime() {
        return Instant.ofEpochSecond(now() / 1000L).atZone(DEFAULT_ZONE).toLocalDateTime();
    }
    public static LocalDateTime getDateTime(long milli) {
        return Instant.ofEpochMilli(milli).atZone(DEFAULT_ZONE).toLocalDateTime();
    }

    public static LocalDateTime parseDateTime(String dateTime) {
        return parseDateTime(dateTime, DATE_TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTime, String format) {
        return parseDateTime(dateTime, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime parseDateTime(String dateTime, DateTimeFormatter formatter) {
        return LocalDateTime.parse(dateTime, formatter);
    }

    public static String formatDateTime(long milli) {
        return formatDateTime(getDateTime(milli));
    }

    public static String formatDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DATE_TIME_FORMATTER);
    }
}
