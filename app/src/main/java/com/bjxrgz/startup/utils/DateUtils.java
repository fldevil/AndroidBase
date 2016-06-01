package com.bjxrgz.startup.utils;

import android.content.Context;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by fd.meng on 2014/03/30
 * <p/>
 * describe 日期处理类
 */
public class DateUtils {

    public static final String DAY = "yyyy-MM-dd";
    public static final String TIME = "HH:mm:ss";
    public static final String ALL = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取日期格式
     */
    public static SimpleDateFormat getFormat(String type) {

        return new SimpleDateFormat(type, Locale.CHINA);
    }

    public static String getDateFormat4MMDD(Context context) {
        String f = "MM/dd";
        char[] t = DateFormat.getDateFormatOrder(context);
        if (t != null) {
            int posM = 0, posD = 0;
            for (int i = 0; i < t.length; i++) {
                if (t[i] == 'M') {
                    posM = i;
                } else if (t[i] == 'd') {
                    posD = i;
                }
            }
            if (posM > posD) {
                f = "dd/MM";
            } else {
                f = "MM/dd";
            }
        }
        return f;
    }

    /**
     * 获取当前时间
     */
    public static Date getCurrentDate() {

        return new Date();
    }

    public static int[] getCurrentCalendar() {
        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new int[]{year, month, day};
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

    public static String getString(long time, String sFormat) {
        Date date = new Date(time);
        return getString(date, sFormat);
    }

    public static String getString(Calendar calendar, String sFormat) {
        Date date = calendar.getTime();
        return getString(date, sFormat);
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

}
