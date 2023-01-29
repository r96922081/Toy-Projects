package cron.model;

import java.time.LocalDateTime;

public class Time {
    public int month;
    public int dayOfMonth;
    public int hour;
    public int minute;
    public int dayOfWeek;
    public int year;

    public static Time GetNow() {
        Time time = new Time();
        LocalDateTime now = LocalDateTime.now();
        time.dayOfWeek = now.getDayOfWeek().getValue();
        time.month = now.getMonthValue();
        time.dayOfMonth = now.getDayOfMonth();
        time.hour = now.getHour();
        time.minute = now.getMinute();
        time.year = now.getYear();
        return time;
    }

    public Time Clone() {
        Time t = new Time();
        t.month = month;
        t.dayOfMonth = dayOfMonth;
        t.hour = hour;
        t.minute = minute;
        t.dayOfWeek = dayOfWeek;
        t.year = year;
        return t;
    }
}
