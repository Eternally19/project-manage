package com.hciot.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateFormat {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        int year = 2019, month = 12;
        String beginTime = getMonthBeginTime(year, month);
        System.out.println(beginTime);

        String endTime = getMonthEndTime(year, month);
        System.out.println(endTime);

        String yearBeginTime = getYearBeginTime(2019);
        System.out.println(yearBeginTime);

        String yearEndTime = getYearEndTime(2020);
        System.out.println(yearEndTime);
    }

    public static String getYearBeginTime(Integer year) {
        YearMonth yearMonth = YearMonth.of(year, 1);
        LocalDate localDate = yearMonth.atDay(1);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.of("Asia/Shanghai"));

        return sdf.format(Date.from(zonedDateTime.toInstant()));
    }

    public static String getYearEndTime(Integer year) {
        YearMonth yearMonth = YearMonth.of(year, 12);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        LocalDateTime localDateTime = endOfMonth.atTime(23, 59, 59, 999);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
        return sdf.format(Date.from(zonedDateTime.toInstant()));
    }

    public static String getMonthBeginTime(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localDate = yearMonth.atDay(1);
        LocalDateTime startOfDay = localDate.atStartOfDay();
        ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.of("Asia/Shanghai"));

        return sdf.format(Date.from(zonedDateTime.toInstant()));
    }

    public static String getMonthEndTime(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate endOfMonth = yearMonth.atEndOfMonth();
        LocalDateTime localDateTime = endOfMonth.atTime(23, 59, 59, 999);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
        return sdf.format(Date.from(zonedDateTime.toInstant()));
    }


}
