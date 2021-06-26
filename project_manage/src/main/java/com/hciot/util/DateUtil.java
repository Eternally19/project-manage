package com.hciot.util;

import com.hciot.entity.Process;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateUtil {

    public DateUtil() {
    }

    public static Calendar increaseHour(String addHour) {
        Calendar cal=Calendar.getInstance();
        if (addHour.equals("12h")) {
            cal.add(java.util.Calendar.HOUR_OF_DAY,12);
        }else if (addHour.equals("24h")) {
            cal.add(java.util.Calendar.HOUR_OF_DAY,24);
        }else if (addHour.equals("36h")) {
            cal.add(java.util.Calendar.HOUR_OF_DAY,36);
        }else if (addHour.equals("48h")) {
            cal.add(java.util.Calendar.HOUR_OF_DAY,48);
        }
        return cal;
    }

    public static void saveProcessEndTime(Process process) {
        Calendar cal=Calendar.getInstance();
        if (process.getSelectTime().equals("12h")) {
            cal.add(java.util.Calendar.HOUR_OF_DAY,12);
            process.setEndTime(cal.getTime());
        }else if (process.getSelectTime().equals("24h")) {
            cal.add(java.util.Calendar.HOUR_OF_DAY,24);
            process.setEndTime(cal.getTime());
        }else if (process.getSelectTime().equals("36h")) {
            cal.add(java.util.Calendar.HOUR_OF_DAY,36);
            process.setEndTime(cal.getTime());
        }
    }

    public static void editProcessEndTime(Process process) {
        Calendar cal=Calendar.getInstance();
        if (!"".equals(process.getSelectTime()) && process.getSelectTime() != null){
            if (process.getSelectTime().equals("12h")) {
                cal.add(java.util.Calendar.HOUR_OF_DAY,12);
                process.setEndTime(cal.getTime());
                process.setStatus("延期");
            }else if (process.getSelectTime().equals("24h")) {
                cal.add(java.util.Calendar.HOUR_OF_DAY,24);
                process.setEndTime(cal.getTime());
                process.setStatus("延期");
            }else if (process.getSelectTime().equals("36h")) {
                cal.add(java.util.Calendar.HOUR_OF_DAY,36);
                process.setEndTime(cal.getTime());
                process.setStatus("延期");
            }
        }

    }

    public static void editHourByStatus(Process process) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(process.getEndTime());
        if (process.getStatus().equals("12h")) {
            cal.add(java.util.Calendar.HOUR,12);
        }else if (process.getStatus().equals("24h")) {
            cal.add(java.util.Calendar.HOUR,24);
        }else if (process.getStatus().equals("36h")) {
            cal.add(java.util.Calendar.HOUR,36);
        }else if (process.getStatus().equals("0h")) {
            cal.add(java.util.Calendar.HOUR,0);
        }else if (process.getStatus().equals("-12h")) {
            cal.add(java.util.Calendar.HOUR,-12);
        }else if (process.getStatus().equals("-24h")) {
            cal.add(java.util.Calendar.HOUR,-24);
        }else if (process.getStatus().equals("-36h")) {
            cal.add(java.util.Calendar.HOUR,-36);
        }
        process.setEndTime(cal.getTime());
    }


    public static void editHourBySelectTime(Date endTime,Process process) {
        Calendar cal=Calendar.getInstance();
        cal.setTime(endTime);
        editBySelectTime(process,cal);
        process.setEndTime(cal.getTime());
    }

    public static void editHourBySelectTime(Page<Process> page, Process process) {
        Calendar cal=Calendar.getInstance();
        List<Process> processes = page.toList();
        if (processes.size()==1) {
            cal.setTime(processes.get(0).getEndTime());
        }else {
            for (int i = 0; i < processes.size(); i++) {
                if (i+1 != processes.size()) {
                    if (processes.get(i+1).getId().equals(process.getId())) {
                        cal.setTime(processes.get(i).getEndTime());
                        break;
                    }
                }
            }
        }
        editBySelectTime(process,cal);
        process.setEndTime(cal.getTime());
    }

    private static void editBySelectTime(Process process, Calendar cal) {
        if (process.getSelectTime().equals("12h")) {
            cal.add(java.util.Calendar.HOUR,12);
        }else if (process.getSelectTime().equals("24h")) {
            cal.add(java.util.Calendar.HOUR,24);
        }else if (process.getSelectTime().equals("36h")) {
            cal.add(java.util.Calendar.HOUR,36);
        }
    }


}



