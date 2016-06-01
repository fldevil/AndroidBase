package com.bjxrgz.startup.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * 字符串处理类
 */
public class StringUtils {

    /**
     * 验证密码 6-16位，数字和字母组合
     */
    public static boolean verifyPassword(String password) {
        String reg = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,16}$";
        return password.matches(reg);
    }

    /**
     * 判断是否为数字
     */
    public static boolean isNumber(String src) {
        if (!TextUtils.isEmpty(src))
            return false;
        Pattern pattern = Pattern.compile("^[1-9]\\d*$");
        Matcher m = pattern.matcher(src);
        return m.find();
    }

    /**
     * 判断是否为邮箱
     */
    public static boolean isEmail(String src) {
        if (!TextUtils.isEmpty(src))
            return false;
        Pattern p = Pattern.compile("\\w+@\\w+(\\.\\w+)+");
        Matcher m = p.matcher(src);
        return m.matches();
    }

//    /**
//     * 判断是否为手机号 11位
//     */
//    public static boolean isMobileNumber(String src) {
//        if (!TextUtils.isEmpty(src))
//            return false;
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
//        Pattern p2 = Pattern.compile("^1[3|4|5|8]\\d{9}$");
//        Pattern p3 = Pattern.compile("^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\\d{8}$");
//        Matcher m = p3.matcher(src);
//        return m.matches();
//    }

}
