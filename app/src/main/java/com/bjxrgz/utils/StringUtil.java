package com.bjxrgz.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fd.meng on 2014/03/30
 *
 * 字符串处理类
 *
 */
public class StringUtil {

	/**
	 * String to stream
	 * @param str
	 * @return
     */
	public static InputStream String2InputStream(String str){
	    ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
	    return stream;
	}

	/**
	 * stream to string
	 * @param is
	 * @return
     */
	public static String inputStream2String(InputStream is){
	    BufferedReader in = new BufferedReader(new InputStreamReader(is));
	    StringBuffer buffer = new StringBuffer();
	    String line = "";
	    try {
			while ((line = in.readLine()) != null){
			  buffer.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return buffer.toString();
	}

	/**
	 * 验证密码 6-16位，数字和字母组合
	 * @return 匹配成功
	 */
	public static boolean verifyPassword(String password){
		String reg = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9]{6,16}$";
		return password.matches(reg);
	}

	private static Pattern numericPattern = Pattern.compile("^[1-9]\\d*$");

	/**
	 * 判断是否数字表示
	 *
	 * @param src
	 *            源字符串
	 * @return 是否数字的标志
	 */
	public static boolean isNumber(String src) {
		boolean return_value = false;
		if (src != null && src.length() > 0) {
			Matcher m = numericPattern.matcher(src);
			if (m.find()) {
				return_value = true;
			}
		}
		return return_value;
	}
}
