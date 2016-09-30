package com.bjxrgz.startup.utils;

import android.content.Context;
import android.text.format.DateFormat;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * describe 日期处理类
 */
public class DateUtils {

    public static final String FORMAT_CHINA_Y_M_D_H_M = "yyyy年MM月dd日 HH:mm";
    public static final String FORMAT_LINE_Y_M_D_H_M_S = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_LINE_M_D_H_M = "MM-dd HH:mm";

    public static final String FORMAT_LINE_Y_M_D = "yyyy-MM-dd";
    public static final String FORMAT_POINT_Y_M_D = "yyyy.MM.dd";
    public static final String FORMAT_CHINA_Y_M_D = "yyyy年MM月dd日";

    public static final String FORMAT_LINE_M_D = "MM-dd";
    public static final String FORMAT_POINT_M_D = "yyyy.MM";

    public static final String FORMAT_H_M_S = "HH:mm:ss";
    public static final String FORMAT_H_M = "HH:mm";
    public static final String FORMAT_M_S = "mm:ss";

    /**
     * 获取日期格式
     */
    public static SimpleDateFormat getFormat(String type) {

        return new SimpleDateFormat(type, Locale.CHINA);
    }

    /**
     * 获取当前时间
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * int year = c.get(Calendar.YEAR);
     * int month = c.get(Calendar.MONTH);
     * int day = c.get(Calendar.DAY_OF_MONTH);
     */
    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    public static String getCurrentString(String sFormat) {
        Date date = new Date();
        SimpleDateFormat format = getFormat(sFormat);
        return format.format(date);
    }

    public static long getCurrentLong() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * 获取Date类型时间
     */
    public static Date getDate(String time, String sFormat) {
        Date date = new Date();
        try {
            SimpleDateFormat format = getFormat(sFormat);
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date getDate(Calendar calendar) {
        return calendar.getTime();
    }

    public static Date getDate(long time) {
        return new Date(time);
    }

    /**
     * 获取Calendar类型时间
     */
    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static Calendar getCalendar(String time, String sFormat) {
        Date date = getDate(time, sFormat);
        return getCalendar(date);
    }

    public static Calendar getCalendar(long time) {
        Date date = new Date(time);
        return getCalendar(date);
    }

    /**
     * 获取String类型时间
     */
    public static String getString(Date date, String sFormat) {
        SimpleDateFormat format = getFormat(sFormat);
        return format.format(date);
    }

    /**
     * 获取String类型时间
     */
    public static String getString(long time, String sFormat) {
        Date date = new Date(time);
        return getString(date, sFormat);
    }

    /**
     * 获取String类型时间
     */
    public static String getString(Calendar calendar, String sFormat) {
        Date date = calendar.getTime();
        return getString(date, sFormat);
    }

    /**
     * 比较两个时间的大小
     *
     * @return <0 time1小于time2, =0 time1等于time2, >0 time1大于time2
     */
    public static int compareAAndB(long time1, long time2) {
        Date date1 = getDate(time1);
        Date date2 = getDate(time2);
        return date1.compareTo(date2);
    }

    /**
     * 获取long类型的时间
     */
    public static long getLong(Date date) {
        return date.getTime();
    }

    public static long getLong(Calendar calendar) {
        Date date = calendar.getTime();
        return date.getTime();
    }

    public static long getLong(String time, String sFormat) {
        Date date = getDate(time, sFormat);
        return date.getTime();
    }

    /**
     * 两段时间的月数差
     */
    public static int getMonthsBetween(Calendar d1, Calendar d2) {
        boolean swaped = false;
        if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
            swaped = true;
        }
        int months = d2.get(Calendar.MONTH) - d1.get(Calendar.MONTH);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                months += 12;
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        if (swaped) {
            months = -months;
        }
        return months;
    }

    /**
     * 两段时间的天数
     */
    public static int getDaysBetween(Calendar d1, Calendar d2) {
        boolean swaped = false;
        if (d1.after(d2)) {  // swap dates so that d1 is start and d2 is end
            Calendar swap = d1;
            d1 = d2;
            d2 = swap;
            swaped = true;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天�?
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        if (swaped) {
            days = -days;
        }
        return days;
    }

    /**
     * 两段时间的天数
     */
    public static int getDaysBetween(Date start_time, Date end_time) {
        long time = end_time.getTime() - start_time.getTime();
        int hours = (int) (time / 3600000);
        return hours / 24;
    }

    /**
     * 传入月份的天数
     */
    public static int getDaysOfMonth(Calendar c) {
        Calendar tmp = Calendar.getInstance();
        tmp.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
        tmp.roll(Calendar.DATE, false); // 将临时Calendar设置成当月最后一天
        return tmp.get(Calendar.DATE); // 获取最后一天
    }

    /**
     * 根据数字获取周几
     */
    public static String getWeekFromNumber(int week) {
        switch (week) {
            case Calendar.MONDAY:
                return MyApp.instance.getString(R.string.day1);
            case Calendar.TUESDAY:
                return MyApp.instance.getString(R.string.day2);
            case Calendar.WEDNESDAY:
                return MyApp.instance.getString(R.string.day3);
            case Calendar.THURSDAY:
                return MyApp.instance.getString(R.string.day4);
            case Calendar.FRIDAY:
                return MyApp.instance.getString(R.string.day5);
            case Calendar.SATURDAY:
                return MyApp.instance.getString(R.string.day6);
            case Calendar.SUNDAY:
                return MyApp.instance.getString(R.string.day7);
            default:
                return "";
        }
    }

    /**
     * 组合时间
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 返回09:00 - 10:00
     */
    public static String groupTime(long startTime, long endTime) {
        return String.format("%s-%s", DateUtils.getString(startTime, DateUtils.FORMAT_H_M), DateUtils.getString(endTime, DateUtils.FORMAT_H_M));
    }

    /**
     * 判断是否是同一天
     */
    public static boolean isSameDay(Calendar c1, Calendar c2) {
        boolean result = false;
        boolean isSameYear = c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR);
        boolean isSameMonth = c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH);
        boolean isSameDay = c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);

        if (isSameYear && isSameMonth && isSameDay) {
            result = true;
        }
        return result;
    }

    /**
     * 获取本周第一天的日期
     */
    public static Calendar setToFirstDay() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar;
    }

    /**
     * 获取上周周日
     */
    public static Calendar getWeeksOfDay(int value) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.set(Calendar.DAY_OF_WEEK, value);
        return cal;
    }


    /**
     * 得到某一天的该星期的第一日 00:00:00
     *
     * @param date           当前日期
     * @param firstDayOfWeek 一个星期的第一天为星期几
     */
    public static Calendar getFirstDayOfWeek(Date date, int firstDayOfWeek) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.setFirstDayOfWeek(firstDayOfWeek);//设置一星期的第一天是哪一天
        cal.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);//指示一个星期中的某天
        cal.set(Calendar.HOUR_OF_DAY, 0);//指示一天中的小时。HOUR_OF_DAY 用于 24 小时制时钟。例如，在 10:04:15.250 PM 这一时刻，HOUR_OF_DAY 为 22。
        cal.set(Calendar.MINUTE, 0);//指示一小时中的分钟。例如，在 10:04:15.250 PM 这一时刻，MINUTE 为 4。
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar getFirstDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.setFirstDayOfWeek(Calendar.SUNDAY);//设置一星期的第一天是哪一天
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//指示一个星期中的某天
        cal.set(Calendar.HOUR_OF_DAY, 0);//指示一天中的小时。HOUR_OF_DAY 用于 24 小时制时钟。例如，在 10:04:15.250 PM 这一时刻，HOUR_OF_DAY 为 22。
        cal.set(Calendar.MINUTE, 0);//指示一小时中的分钟。例如，在 10:04:15.250 PM 这一时刻，MINUTE 为 4。
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    /**
     * 获取间隔时间 eg: 1分钟，1小时，1天，
     */
    public static String getInterval(long time) {
        long seconds = 1000;
        long minute = seconds * 60;
        long hour = minute * 60;
        long day = hour * 24;
        long month = day * 30;
        long year = month * 12;

        if (time >= 0 && time < minute) {
            return time / seconds + MyApp.instance.getString(R.string.second);

        } else if (time > minute && time < hour) {
            return time / minute + MyApp.instance.getString(R.string.minute);

        } else if (time > hour && time < day) {
            return time / hour + MyApp.instance.getString(R.string.hour);

        } else if (time > day && time < month) {
            return time / day + MyApp.instance.getString(R.string.day);

        } else if (time > month && time < year) {
            return time / month + MyApp.instance.getString(R.string.month);

        } else if (time > year) {
            return time / year + MyApp.instance.getString(R.string.year);
        }
        return "未知时间";
    }

    /**
     * 流水号
     */
    public static String genBillNum() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
        return simpleDateFormat.format(new Date());
    }

}
