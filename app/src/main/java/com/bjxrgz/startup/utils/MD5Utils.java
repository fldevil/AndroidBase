package com.bjxrgz.startup.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * md5 加密
 */
public class MD5Utils {

    /**
     * 第一种加密
     */
    public static String md5(String s) {
        char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] bytes = s.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            bytes = md.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;
            for (byte b : bytes) {
                chars[k++] = hexChars[b >>> 4 & 0xf];
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 第二种加密方式
     */
    public static String encode(String password) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(password.getBytes());// 对字符串加密,返回字节数组

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                int i = b & 0xff;// 获取字节的低八位有效值
                String hexString = Integer.toHexString(i);// 将整数转为16进制

                if (hexString.length() < 2) {
                    hexString = "0" + hexString;// 如果是1位的话,补0
                }

                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
