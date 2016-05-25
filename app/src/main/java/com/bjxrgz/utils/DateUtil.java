package com.bjxrgz.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;

public class DateUtil {
	public static final String defaultDateFormatStr = "yyyy-MM-dd";
	public static final String defaultTimeFormatStr = "HH:mm";

	public static String dateTimeFormatStr = "yyyy-MM-dd HH:mm:ss Z";
	public static final SimpleDateFormat dateTimeFormater = new SimpleDateFormat(dateTimeFormatStr);
	
	/**
	 * 得到当前时间 yyyy-MM-dd HH:mm:ss Z 格式的字符串
	 * @return
	 */
	public static String getCurrentTimeStr(){
		return dateTimeFormater.format(new Date());
	}
	
	/**
	 * Obtains time which the string represents, transforms the string to time
	 * 
	 * @param calendar
	 *            Assigns the time
	 * @param sFormat
	 *            Time of return form [ MM/dd/yyyy HH:mm ]
	 * @return String
	 */
	public static String getStrFromDateTime(Calendar calendar, String sFormat) {
		SimpleDateFormat s = new SimpleDateFormat(sFormat);

		String sDateNew = "";
		try {
			sDateNew = s.format(calendar.getTime());
		} catch (Exception e) {
			
		}

		return sDateNew;
	}
	
	/**
	 * Obtains time which the string represents, transforms the string to time
	 * 
	 * @param date
	 *            Assigns the time
	 * @param sFormat
	 *            Time of return form [ MM/dd/yyyy HH:mm ]
	 * @return String
	 */
	public static String getStrFromDateTime(Date date, String sFormat) {
		SimpleDateFormat s = new SimpleDateFormat(sFormat);

		String sDateNew = "";
		try {
			sDateNew = s.format(date);
		} catch (Exception e) {
		}

		return sDateNew;
	}
	
	/**
	 * 
	 * @param date
	 * @return String[MM/dd/yyyy]
	 */
	public static String getStrFromDateTime(Date date) {
		return getStrFromDateTime(date, "MM/dd/yyyy");
	}
	
	/**
	 * Obtains time which the string represents, transforms the string to time
	 * 
	 * @param sDateTime
	 *            Certain form time string, for example:2006-08-01 16:00:18
	 * @param sFormat
	 *            String form, like yyyy-MM-dd, yyyy-MM-dd HH:mm:ss and so on
	 * 
	 * @return Calendar
	 */
	public static Calendar getDateTimeFromStr(String sDateTime, String sFormat) {

		if (sFormat == null || sFormat.trim().equals("")) {
			sFormat = "yyyy-MM-dd HH:mm:ss";
		}

		Calendar c = null;

		try {
			SimpleDateFormat format = new SimpleDateFormat(sFormat);
			c = Calendar.getInstance();
			c.setTime(format.parse(sDateTime));
		} catch (Exception e) {
			c = null;
		}

		return c;
	}
	
	/**
	 * Obtains time which the string represents, transforms the string to time
	 * 
	 * @param sDateTime
	 *            yyyy-MM-dd HH:mm:ss form time string, for example: 2006-08-01
	 *            16:00:18
	 * 
	 * @return Calendar
	 */
	public static Calendar getDateTimeFromStr(String sDateTime) {
		return getDateTimeFromStr(sDateTime, null);
	}
	
	/**
	 * get today, eg: 2007-05-16 14:32:30, return 2007-5-16 00:00:00
	 * 
	 * @return
	 * @author admin
	 * @since 2007-5-16
	 */
	public static Date getToday() {
		Calendar c = Calendar.getInstance();
		String dateStr = DateUtil.getStrFromDateTime(c, "yyyy/MM").concat("/").concat(
				String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		Date date = DateUtil.getDateTimeFromStr(dateStr, "yyyy/MM/dd").getTime();
		return date;
	}
	
	public static int getMonthsBetween (Calendar d1, Calendar d2) {		
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
        
        if(swaped){
        	months = -months;
        }
        
        return months;
    }
	
	public static int getDaysBetween (Calendar d1, Calendar d2) {
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
        
        if(swaped){
        	days = -days;
        }
        
        return days;
    }
	
	 //2009/5/3 & 2009/5/6 -> 3 
	public static int getDaysBetween(Date start_time, Date end_time) {
    	int hours = (int)((end_time.getTime() - start_time.getTime())/3600000);
    	return hours/24;
    }
	
	public static int getDaysBetweenIgnorTime(Date start_time, Date end_time) {
    	Calendar c1 = Calendar.getInstance();
    	c1.setTime(start_time);
    	
    	Calendar c2 = Calendar.getInstance();
    	c2.setTime(end_time);
    	
    	return getDaysBetween(c1, c2);
    }
	
	public static int getMonthMaxinum(Calendar c){
		Calendar temp = Calendar.getInstance();
		temp.setTime(c.getTime());
		temp.set(Calendar.DAY_OF_MONTH, 1);
		temp.add(Calendar.MONTH, 1);
		temp.add(Calendar.DAY_OF_MONTH, -1);
		return temp.get(Calendar.DAY_OF_MONTH);
	}
	
	public static Calendar clearTime(Date dateTime){		
		Calendar tmp = Calendar.getInstance();
		tmp.setTimeInMillis(dateTime.getTime());
				
		return clearTime(tmp);
	}
	
	public static Calendar clearTime(Calendar dateTime){
		Calendar tmp = Calendar.getInstance();
		tmp.clear();
		
		tmp.set(Calendar.YEAR, dateTime.get(Calendar.YEAR));
		tmp.set(Calendar.MONTH, dateTime.get(Calendar.MONTH));
		tmp.set(Calendar.DAY_OF_MONTH, dateTime.get(Calendar.DAY_OF_MONTH));
		
		return tmp;
	}
	
	public static String getDateFormat4MMDD(Context context){
		String f = "MM/dd";
		char[] t = DateFormat.getDateFormatOrder(context);
		
		if(t != null){
			int posM = 0, posD = 0;
			for(int i = 0; i < t.length; i++){
				if(t[i] == 'M'){
					posM = i; 
				}else if(t[i] == 'd'){
					posD = i;
				}
			}
			
			if(posM > posD){
				f = "dd/MM";
			}else{
				f = "MM/dd";
			}
		}
		
		return f;
	}
}
