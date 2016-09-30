package com.bjxrgz.startup.utils;

import android.content.Context;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * 字符串处理类
 */
public class StringUtils {

    /**
     * 格式化文件大小
     */
    public static String getFileSize(Context context, long fileLength) {

        return Formatter.formatFileSize(context, fileLength);
    }

    public static String getRandom(int length) {
        String random = UUID.randomUUID().toString().replace("-", "");
        return random.substring(random.length() - length, random.length());
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     */
    public static int getLength(String validateStr) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < validateStr.length(); i++) {
            /* 获取一个字符 */
            String temp = validateStr.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 验证密码 6-16位，数字和字母组合
     */
    public static boolean isPassword(String str) {
        String reg = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,16}$";
        return str.matches(reg);
    }

    /**
     * 判断是否为数字
     */
    public static boolean isNumber(String src) {
        if (isEmpty(src))
            return false;
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher m = pattern.matcher(src);
        return m.matches();
    }

    /**
     * 判断字符串是否为日期格式
     */
    public static boolean isDate(String src) {
        if (isEmpty(src))
            return false;
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(src);
        return m.matches();
    }

    /**
     * 判断是否为邮箱
     */
    public static boolean isEmail(String src) {
        if (isEmpty(src))
            return false;
        Pattern pattern = Pattern.compile("\\w+@\\w+(\\.\\w+)+");
        Matcher m = pattern.matcher(src);
        return m.matches();
    }

    /**
     * 判断是否为手机号 11位
     */
    public static boolean isMobileNumber(String src) {
        if (TextUtils.isEmpty(src))
            return false;
        Pattern pattern = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\\d{8}$");
        Matcher m = pattern.matcher(src);
        return m.matches();
    }

    /**
     * 判断是否为身份证号
     */
    public static boolean isIdCard(String IDStr) {
        String[] ValCodeArr = {"1", "0", "x", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        // ================ 号码的长度 15位或18位 ================
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            return false;
        }
        // ================ 数字 除最后以为都为数字 ================
        String Ai = "";
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (!isNumber(Ai)) {
            return false;
        }
        // ================ 出生年月是否有效 ================
        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (!isDate(strYear + "-" + strMonth + "-" + strDay)) {
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return false; // 身份证生日不在有效范围。
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false; // 身份证月份无效
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false; // 身份证日期无效
        }
        // ================ 地区码时候有效 ================
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
            return false;
        }
        // ================ 判断最后一位的值 ================
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (!Ai.equals(IDStr)) {
                return false; // 身份证无效，不是合法的身份证号码
            }
        } else {
            return false;
        }
        return true;
    }

    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

}
