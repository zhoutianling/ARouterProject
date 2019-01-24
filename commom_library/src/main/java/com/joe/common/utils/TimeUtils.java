package com.joe.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.joe.common.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


public class TimeUtils {

    public static String timeTo12(String time, Context context) {
        if (TextUtils.isEmpty(time)) {
            return null;
        }
        String[] arr = time.split(":");
        int hour = Integer.parseInt(arr[0]);
        int min = Integer.parseInt(arr[1]);
        String hourS;
        String minS;
        String day;
        if (hour == 0) {
            hour = hour + 12;
            day = context.getString(R.string.timer_pm);
        } else if (hour <= 12) {
            day = context.getString(R.string.timer_am);
        } else {
            hour = hour - 12;
            day = context.getString(R.string.timer_pm);
        }
        hourS = hour > 9 ? hour + "" : "0" + hour;
        minS = min > 9 ? min + "" : "0" + min;

        return hourS + ":" + minS + " " + day;
    }

    /***
     * 根据起始时间判断是否是当天
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isCurrentDay(String startTime, String endTime) {
        if (!startTime.contains(":") || !endTime.contains(":")) return true;
        String[] arrStart = startTime.split(":");
        String[] arrEnd = endTime.split(":");
        int startHour = Integer.parseInt(arrStart[0]);
        int endHour = Integer.parseInt(arrEnd[0]);

        int startMin = Integer.parseInt(arrStart[1]);
        int endMin = Integer.parseInt(arrEnd[1]);

        //如果开始小时大于结束时间就是跨天
        if (startHour - endHour > 0) {
            return false;
            //如果开始小时等于结束小时就比较分,如果开始分钟大于结束分钟也是跨天
        } else if (startHour - endHour == 0) {
            if (startMin - endMin >= 0) {
                return false;
            }
        } else {
            return true;
        }
        return true;

    }

    public static int getDisableWeek(int enableWeek) {
        return 127 - enableWeek;
    }

    public static String hourMinTo24(int hour, int min) {
        String hourS = hour > 9 ? hour + "" : "0" + hour;
        String minS = min > 9 ? min + "" : "0" + min;
        return hourS + ":" + minS;
    }

    public static String getCurrent24() {
        GregorianCalendar d = new GregorianCalendar();
        int hours = d.get(Calendar.HOUR_OF_DAY);
        int minute = d.get(Calendar.MINUTE);
        return hourMinTo24(hours, minute);
    }

    public static String getCurrent24(int hour) {
        GregorianCalendar d = new GregorianCalendar();
        int hours = d.get(Calendar.HOUR_OF_DAY) + hour;
        int minute = d.get(Calendar.MINUTE);
        return hourMinTo24(hours, minute);
    }

    public static String getCurrentMin24(int minutes) {
        GregorianCalendar d = new GregorianCalendar();
        int hour = d.get(Calendar.HOUR_OF_DAY);
        int minute = d.get(Calendar.MINUTE) + minutes;
        return hourMinTo24(hour, minute);
    }

    public static int getCurrentWeek() {
        GregorianCalendar d = new GregorianCalendar();
        int week = d.get(Calendar.DAY_OF_WEEK) - 1;
        return (int) Math.pow(2, week);
    }

    /***
     * 获取系统默认时区
     * @return
     */
    public static String getDefTimeZone() {
        return TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT);
    }

    public static String weeksToText(List<String> weeks, Context context) {
        if (weeks == null || weeks.size() != 7) {
            return null;
        }

        boolean isAllDay = true;
        boolean isWorkDay = true;
        boolean isWeekend = true;
        StringBuilder sb = new StringBuilder();

        for (int i = 0, j = weeks.size(); i < j; i++) {
            if (weeks.get(i).endsWith(String.valueOf(1)))
                sb.append(context.getResources().getStringArray(R.array.timer_weeks)[i]).append(" ");

            //每天
            if (!weeks.get(i).equals("1")) {
                isAllDay = false;
            }

            //工作日
            if (weeks.get(0).equals("1") || weeks.get(weeks.size() - 1).equals("1")) {
                isWorkDay = false;
            } else if (0 < i && i < (weeks.size() - 1) && weeks.get(i).equals("0")) {
                isWorkDay = false;
            }

            //周末
            if (!weeks.get(0).equals("1") || !weeks.get(weeks.size() - 1).equals("1")) {
                isWeekend = false;
            } else if (0 < i && i < (weeks.size() - 1) && weeks.get(i).equals("1")) {
                isWeekend = false;
            }
        }

        if (sb.length() == 0) {
            sb.append(context.getString(R.string.timer_once));
        } else if (isAllDay) {
            return context.getResources().getString(R.string.timer_all_day);
        } else if (isWorkDay) {
            return context.getResources().getString(R.string.timer_work_day);
        } else if (isWeekend) {
            return context.getResources().getString(R.string.timer_weekend_day);
        }

        return sb.toString();
    }

    // true: 保存时间戳大于当前时间戳 false: other
    public static boolean matchTimeMillis(byte[] save, int startIndex) {
        GregorianCalendar saveC = new GregorianCalendar();
        saveC.set(unsignedByte(save[startIndex]) << 8 | unsignedByte(save[startIndex + 1]),
                unsignedByte(save[startIndex + 2]) - 1, unsignedByte(save[startIndex + 3]),
                unsignedByte(save[startIndex + 4]), unsignedByte(save[startIndex + 5]));
        long saveTime = saveC.getTimeInMillis();
        long currTime = System.currentTimeMillis();
        return saveTime > currTime;
    }

    /***
     * 把服务端传过来的值转换成包含的星期
     * @param num
     * @param leftPad
     * @return
     */
    public static List<Integer> leftToList(int num, int leftPad) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < leftPad; i++) {
            if ((num & (1 << i)) != 0) {
                list.add(i);
            }
        }
        return list;
    }

    private static int unsignedByte(byte b) {
        return b & 0xff;
    }
}
