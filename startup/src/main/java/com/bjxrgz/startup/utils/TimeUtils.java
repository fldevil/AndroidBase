package com.bjxrgz.startup.utils;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.bjxrgz.startup.utils.ConstantUtils.DAY;
import static com.bjxrgz.startup.utils.ConstantUtils.HOUR;
import static com.bjxrgz.startup.utils.ConstantUtils.MIN;
import static com.bjxrgz.startup.utils.ConstantUtils.MONTH;
import static com.bjxrgz.startup.utils.ConstantUtils.MSEC;
import static com.bjxrgz.startup.utils.ConstantUtils.SEC;
import static com.bjxrgz.startup.utils.ConstantUtils.YEAR;

/**
 * Created by fd.meng on 2014/03/30
 * <p/>
 * describe 日期处理类
 */
public class TimeUtils {

    /**
     * 流水号
     */
    public static String genBillTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.CHINA);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 获取日期格式
     */
    public static SimpleDateFormat getFormat(String type) {
        return new SimpleDateFormat(type, Locale.getDefault());
    }

    /**
     * 获取当前时间
     * int year = c.get(Calendar.YEAR);
     * int month = c.get(Calendar.MONTH);
     * int day = c.get(Calendar.DAY_OF_MONTH);
     */
    public static Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    public static Date getCurrentDate() {
        return new Date();
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
     * 毫秒时间戳单位转换（单位：unit）
     */
    private static long milliseconds2Unit(long milliseconds, ConstantUtils.TimeUnit unit) {
        switch (unit) {
            case MSEC:
                return milliseconds / MSEC;
            case SEC:
                return milliseconds / SEC;
            case MIN:
                return milliseconds / MIN;
            case HOUR:
                return milliseconds / HOUR;
            case DAY:
                return milliseconds / DAY;
        }
        return -1;
    }

    /**
     * 获取间隔时间 eg: 1分钟，1小时，1天，
     */
    public static String getUnit(long time) {
        if (time >= 0 && time < MIN) {
            return time / SEC + MyApp.get().getString(R.string.second);

        } else if (time > MIN && time < HOUR) {
            return time / MIN + MyApp.get().getString(R.string.minute);

        } else if (time > HOUR && time < DAY) {
            return time / HOUR + MyApp.get().getString(R.string.hour);

        } else if (time > DAY && time < MONTH) {
            return time / DAY + MyApp.get().getString(R.string.day);

        } else if (time > MONTH && time < YEAR) {
            return time / MONTH + MyApp.get().getString(R.string.month);

        } else if (time > YEAR) {
            return time / YEAR + MyApp.get().getString(R.string.year);
        }
        return "error time";
    }

    /**
     * ********************************天*****************************
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
     * ***********************************周******************************
     * 获取星期
     */
    public static String getWeek(Date time) {
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(time);
    }

    /**
     * 获取星期 <p>注意：周日的Index才是1，周六为7</p>
     */
    public static int getWeekIndex(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据Index获取星期
     */
    public static String getWeekFromNumber(int week) {
        switch (week) {
            case Calendar.MONDAY:
                return MyApp.get().getString(R.string.day1);
            case Calendar.TUESDAY:
                return MyApp.get().getString(R.string.day2);
            case Calendar.WEDNESDAY:
                return MyApp.get().getString(R.string.day3);
            case Calendar.THURSDAY:
                return MyApp.get().getString(R.string.day4);
            case Calendar.FRIDAY:
                return MyApp.get().getString(R.string.day5);
            case Calendar.SATURDAY:
                return MyApp.get().getString(R.string.day6);
            case Calendar.SUNDAY:
                return MyApp.get().getString(R.string.day7);
            default:
                return "";
        }
    }

    /**
     * 获取本周第一天的日期
     */
    public static Calendar getFirstDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1);
        }
        return calendar;
    }

    /**
     * 获取传入日期的周第一天
     */
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
     * 得到某一天的该星期的第一日
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

    /**
     * 获取上周周日
     */
    public static Calendar getSundayInLastWeek(int value) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.set(Calendar.DAY_OF_WEEK, value);
        return cal;
    }

    /**
     * ***********************************月********************************8
     * 传入月份的天数
     */
    public static int getDaysNumOfMonth(Calendar c) {
        Calendar tmp = Calendar.getInstance();
        tmp.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1);
        tmp.roll(Calendar.DATE, false); // 将临时Calendar设置成当月最后一天
        return tmp.get(Calendar.DATE); // 获取最后一天
    }

    /**
     * 获取月份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param time Date类型时间
     * @return 1...5
     */
    public static int getWeekOfMonth(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.WEEK_OF_MONTH);
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
     * ***********************************年**********************************
     * 判断闰年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    /**
     * 获取年份中的第几周
     * <p>注意：国外周日才是新的一周的开始</p>
     *
     * @param time Date类型时间
     * @return 1...54
     */
    public static int getWeekOfYear(Date time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
