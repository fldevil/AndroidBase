package com.bjxrgz.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {

	public static InputStream String2InputStream(String str){
	    ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
	    return stream;
	}
	
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
	
}
