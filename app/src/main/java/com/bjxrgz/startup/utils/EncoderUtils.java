package com.bjxrgz.startup.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class EncoderUtils {

    private static byte[] back(byte[] s, int start, int end, int len) {
        byte[] d = new byte[2 * len];
        int j = 0;
        for (int i = start; i < end; i = i + 2) {
            d[j] = s[i];
            d[j + len] = s[i + 1];
            j++;
        }
        return d;
    }

    private static String back(String s) {
        StringBuilder d1 = new StringBuilder();
        StringBuilder d2 = new StringBuilder();
        for (int i = 0; i < s.length(); i = i + 2) {
            d1.append(s.charAt(i));
            d2.append(s.charAt(i + 1));
        }
        return d1.append(d2.toString()).toString();
    }

    private static byte[] hexStrToString(String source) {
        byte[] d = new byte[source.length() / 2];
        int j = 0;
        for (int i = 0; i < source.length(); i = i + 2) {
            try {
                d[j++] = (byte) (0xff & Integer.parseInt(source.substring(i, i + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return d;
    }

    public static void test() throws UnsupportedEncodingException {

        ArrayList<String> s = new ArrayList<String>();
        s.add("2");
        s.add("0");
        s.add("mengfanpp@gmail.com");
        s.add("aaaaaaaa");
        s.add("Android");
        s.add("000000000000000");
        s.add("generic");
        s.add("2.0");
        s.add("2.7");
        s.add("私は");
        s.add("");
        s.add("καθημερινά");
        s.add("");
        s.add("00:50:56:C0:00:01Intel Pentium III Xeon 处理器^BTCL63700DH4ITI");
        s.add("201001141336");
        String d = "";
        try {
            d = encodingString(s);
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        Log.d("Warn", d);
        String[] result = decodingString(d, s.size());

        s = new ArrayList<String>();
        s.add("走a过");
        s.add("也路过");
        s.add("也不于江湖");
        try {
            d = encodingString(s);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("Warn", d);
        result = decodingString(d, s.size());

        s = new ArrayList<String>();
        s.add("");
        s.add("");
        s.add("");
        s.add("");
        try {
            d = encodingString(s);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("Warn", d);
        result = decodingString(d, s.size());

        s = new ArrayList<String>();
        s.add("d41d8cd98f00b204e9800998ecf84");
        s.add("/Users/inorixu/iTaskBackup");
        s.add("0");
        s.add("5");
        s.add("0");
        s.add("");
        s.add("");
        s.add("");
        s.add("");
        s.add("");
        s.add("");
        s.add("");
        s.add("");
        s.add("mengfanpp@gmail.com");
        //s.add("281bd7f66140f5c3");
        //s.add("");
        d = encodingString(s);
        Log.d("Warn", d);
        result = decodingString(d, 15);
    }

    public static String encodingString2(ArrayList<String> source) {
        if (source.size() == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder(source.get(0));//
        StringBuilder buf2 = new StringBuilder(source.get(1));
        StringBuilder d = new StringBuilder();
        ArrayList<Integer> type = new ArrayList<Integer>();
        int status = 0;
        for (int i = 1; i < source.size(); ) {
            type.add(status);
            int n = buf.length();
            int m = buf2.length();
            int min = (m > n) ? n : m;
            if (min == 0 || min > 254) {
                type.add(255);
            } else {
                type.add(min);
            }
            //计算下一次的状态
            String temp1, temp2;
            if (m == n) {
                if (source.size() == (i + 2)) {
                    status = 5;
                } else {
                    status = 0;
                }
                temp1 = buf.toString();
                temp2 = buf2.toString();
                buf.delete(0, m);//清空buf
                buf2.delete(0, m);
                if (source.size() > (i + 1)) {//0
                    buf.append(source.get(i + 1));
                    if (status == 0) {
                        buf2.append(source.get(i + 2));
                    }
                }
                i = i + 2;
            } else if (n > m) {//第一个数据有剩余数据
                if (source.size() == (i + 1)) {
                    status = 3;
                } else {
                    status = 1;
                }
                temp1 = buf.substring(0, m);
                temp2 = buf2.toString();
                buf.delete(0, m);
                buf2.delete(0, m);
                if (status == 1) {
                    buf2.append(source.get(i + 1));
                }
                i++;
            } else {//第二个数据有剩余数据if(m > n)
                if (source.size() == (i + 1)) {
                    status = 4;
                } else {
                    status = 2;
                }
                temp1 = buf.toString();
                temp2 = buf2.substring(0, n);
                buf.delete(0, n);
                buf2.delete(0, n);
                if (status == 2) {
                    buf.append(source.get(i + 1));
                }
                i++;
            }
            StringBuilder temp = new StringBuilder();
            for (int j = 0; j < min; j++) {
                temp.append(temp1.charAt(j));
                temp.append(temp2.charAt(j));
            }
            d.append(temp.toString());
        }
        //compose
        if (buf2.toString() != null & buf2.toString().length() > 0) {
            buf.append(buf2.toString());
        }
        if (buf.toString() != null & buf.toString().length() > 0) {
            type.add(status);
            type.add(buf.toString().length());
            String temp = notToHexString(buf.toString());
            if (temp != null) {
                d.append(temp);
            }
        }
        d.append("TZ");
        //d.append(intArrayToString(type));
        String result = null;
        try {
            result = toHexString(d.toString().getBytes("UTF-8")) + intArrayToString(type);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    public static String encodingString(ArrayList<String> source) throws UnsupportedEncodingException {
        if (source.size() == 0) {
            return "";
        }
        byte[] buf = source.get(0).getBytes("UTF-8");
        byte[] buf2 = source.get(1).getBytes("UTF-8");
        ArrayList<Byte> d = new ArrayList<Byte>();
        ArrayList<Integer> type = new ArrayList<Integer>();
        int status = 0;
        for (int i = 1; i < source.size(); ) {
            type.add(status);
            int n = buf.length;
            int m = buf2.length;
            int min = (m > n) ? n : m;
            if (min == 0 || min > 254) {
                type.add(255);
            } else {
                type.add(min);
            }
            //计算下一次的状态
            byte[] temp1;
            byte[] temp2;
            if (m == n) {
                if (source.size() == (i + 2)) {
                    status = 5;
                } else {
                    status = 0;
                }
                temp1 = buf;
                temp2 = buf2;
                buf = null;
                buf2 = null;
                if (source.size() > (i + 1)) {//0
                    buf = source.get(i + 1).getBytes("UTF-8");
                    if (status == 0) {
                        buf2 = source.get(i + 2).getBytes("UTF-8");
                    }
                }
                i = i + 2;
            } else if (n > m) {//第一个数据有剩余数据
                if (source.size() == (i + 1)) {
                    status = 3;
                } else {
                    status = 1;
                }
                temp1 = copy(buf, 0, min);
                temp2 = buf2;
                buf = copy(buf, min, buf.length);
                buf2 = null;
                if (status == 1) {
                    buf2 = source.get(i + 1).getBytes("UTF-8");
                }
                i++;
            } else {//第二个数据有剩余数据if(m > n)
                if (source.size() == (i + 1)) {
                    status = 4;
                } else {
                    status = 2;
                }
                temp1 = buf;
                temp2 = copy(buf2, 0, min);
                buf = null;
                buf2 = copy(buf2, min, buf2.length);
                if (status == 2) {
                    buf = source.get(i + 1).getBytes("UTF-8");
                }
                i++;
            }
            for (int j = 0; j < min; j++) {
                d.add(temp1[j]);
                d.add(temp2[j]);
            }
        }
        //compose
        if (buf2 != null && buf2.length > 0) {
            buf = buf2;
        }
        if (buf != null && buf.length > 0) {
            type.add(status);
            type.add(buf.length);
            byte[] temp = notToHexString(buf).getBytes("UTF-8");
            if (temp != null) {
                for (int i = 0; i < temp.length; i++) {
                    d.add(temp[i]);
                }
            }
        }
        byte[] temp = "TZ".getBytes();
        for (int j = 0; j < temp.length; j++) {
            d.add(temp[j]);
        }
        //d.append(intArrayToString(type));
        String result = null;
        try {
            result = toHexString(d) + intArrayToString(type);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    private static byte[] copy(byte[] s, int start, int end) {
        if (start > end || start > s.length) {
            return null;
        }
        if (s.length < end) {
            end = s.length;
        }
        byte[] d = new byte[end - start];
        int j = 0;
        for (int i = start; i < end; i++) {
            d[j++] = s[i];
        }
        return d;
    }

    public static String[] decodingString2(String source, int n) {
        String s;
        try {
            byte[] bytes = hexStrToString(source);
            s = new String(bytes, "UTF-8");
            int tzPos = s.lastIndexOf("TZ");
            String types = s.substring(tzPos + 2);
            int g = types.length() / 2;
            int[] type = new int[g * 2];
            int j = 0;
            int lenPos = bytes.length - 2 * g - 2;
            for (int i = 0; i < 2 * g; i++) {
                if (i % 2 == 0) {
                    type[j++] = Integer.parseInt("" + types.charAt(i));
                } else {
                    if (i == 1) {
                        type[j++] = 255;
                    } else {
                        type[j++] = (bytes[lenPos + i] == -1) ? 255 : bytes[lenPos + i];
                    }
                }
            }

            int[] flags = new int[n];
            flags[0] = 1;
            flags[1] = 2;
            int k = 2;
            int preFlag = 2;
            for (int i = 2; i < type.length && k < n; k++, i = i + 2)//
            {
                if (type[i] == 0 || type[i] == 5) {
                    flags[k] = ((preFlag == 1) ? 2 : 1);
                    if ((k + 1) < n) {
                        flags[++k] = preFlag;
                    }
                } else if (type[i] == 1 || type[i] == 3) {
                    flags[k] = 2;
                } else {
                    flags[k] = 1;
                }
                preFlag = flags[k];
            }

            String[] d = new String[n];
            s = s.substring(0, tzPos);
            k = 0;
            int k1 = 2;
            int flag = 1;//1, 2
            int bufFlag = 0;
            for (j = 0; j < n; j++) {
                StringBuilder temp = new StringBuilder();
                flag = flags[j];
                int k0 = k;
                int i = k1;
                for (; i < type.length; i = i + 2) {
                    int opType = type[i];
                    int len = type[i + 1];
                    if (len == 255) {
                        len = 0;
                    }
                    String tb = back(s.substring(k, k + 2 * len));
                    k += (2 * len);
                    if (flag == 1) {
                        temp.append(tb.substring(0, len));
                        if (opType != 1 && opType != 3) {
                            break;
                        }
                    } else {//=2
                        temp.append(tb.substring(len));
                        if (opType != 2 && opType != 4) {
                            break;
                        }
                    }
                }//for i
                if (i >= type.length && bufFlag == 0) {//赋值
                    bufFlag = 1;
                    String buf = s.substring(k);
                    if (buf != null && !"".equals(buf)) {
                        String notChar = notToString(buf);
                        if (notChar != null) {
                            temp.append(notChar);
                        }
                    }
                }
                d[j] = temp.toString();
                if (((j + 1) < n) && ((flags[j + 1] == 1) ||
                        (flags[j] == 1 && (type[k1] == 1 || type[k1] == 3) && (k1 - 2) >= 0 && type[k1 - 2] == 2) ||
                        (flags[j] == 2 && flags[j + 1] == 2))) {
                    if ((k1 + 1) < type.length) {
                        int len = type[k1 + 1];
                        if (len == 255) {
                            len = 0;
                        }
                        k = k0 + 2 * len;
                    }
                    k1 = k1 + 2;
                } else {
                    k = k0;
                }
                if (k >= s.length()) {
                    return d;
                }
            }
            return d;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static int getTZLocation(byte[] s) throws UnsupportedEncodingException {
        int d = 0;
        byte[] tz = "TZ".getBytes("UTF-8");
        int len = tz.length;
        for (int i = s.length - 1; i >= 0; i--) {
            if (s[i] == tz[len - 1]) {
                int k = len - 2;
                int j = i - 1;
                for (; j >= 0 && k >= 0; j--, k--) {
                    if (s[j] != tz[k]) {
                        break;
                    }
                }
                if (k < 0) {
                    return j + 1;
                }

            }
        }
        return d;
    }

    public static String[] decodingString(String source, int n) {
        try {
            byte[] bytes = hexStrToString(source);
            int tzPos = getTZLocation(bytes);
            int g = (bytes.length - tzPos - 2) / 2;
            int[] type = new int[g * 2];
            int j = 0;
            int start = tzPos + 2;
            for (int i = 0; i < 2 * g; i++) {
                if (i % 2 == 0) {
                    type[j++] = bytes[start + i] - '0';
                } else {
                    if (i == 1) {
                        type[j++] = 255;
                    } else {
                        type[j++] = (bytes[start + i - 2] == -1) ? 255 : bytes[start + i - 2];
                    }
                }
            }

            int[] flags = new int[n];
            flags[0] = 1;
            flags[1] = 2;
            int k = 2;
            int preFlag = 2;
            for (int i = 2; i < type.length && k < n; k++, i = i + 2)//
            {
                if (type[i] == 0 || type[i] == 5) {
                    flags[k] = ((preFlag == 1) ? 2 : 1);
                    if ((k + 1) < n) {
                        flags[++k] = preFlag;
                    }
                } else if (type[i] == 1 || type[i] == 3) {
                    flags[k] = 2;
                } else {
                    flags[k] = 1;
                }
                preFlag = flags[k];
            }

            String[] d = new String[n];

            k = 0;
            int k1 = 2;
            int flag = 1;//1, 2
            int bufFlag = 0;
            for (j = 0; j < n; j++) {
                ArrayList<Byte> temp = new ArrayList<Byte>();
                flag = flags[j];
                int k0 = k;
                int i = k1;
                for (; i < type.length; i = i + 2) {
                    int opType = type[i];
                    int len = type[i + 1];
                    if (len == 255) {
                        len = 0;
                    }
                    byte[] tb = back(bytes, k, k + 2 * len, len);
                    k += (2 * len);
                    if (flag == 1) {
                        for (int p = 0; p < len; p++) {
                            temp.add(tb[p]);
                        }
                        if (opType != 1 && opType != 3) {
                            break;
                        }
                    } else {//=2
                        for (int p = len; p < tb.length; p++) {
                            temp.add(tb[p]);
                        }
                        if (opType != 2 && opType != 4) {
                            break;
                        }
                    }
                }//for i
                if (i >= type.length && bufFlag == 0) {//赋值
                    bufFlag = 1;
                    byte[] buf = copy(bytes, k, tzPos);
                    if (buf != null && buf.length > 0) {
                        ArrayList<Byte> notByte = notToString(buf);
                        if (notByte != null && notByte.size() > 0) {
                            for (int p = 0; p < notByte.size(); p++) {
                                temp.add(notByte.get(p));
                            }
                        }
                    }
                }
                d[j] = byteArrayToString(temp);
                if (((j + 1) < n) && ((flags[j + 1] == 1) ||
                        (flags[j] == 1 && (type[k1] == 1 || type[k1] == 3) && (k1 - 2) >= 0 && type[k1 - 2] == 2) ||
                        (flags[j] == 2 && flags[j + 1] == 2))) {
                    if ((k1 + 1) < type.length) {
                        int len = type[k1 + 1];
                        if (len == 255) {
                            len = 0;
                        }
                        k = k0 + 2 * len;
                    }
                    k1 = k1 + 2;
                } else {
                    k = k0;
                }
                if (k >= tzPos) {
                    return d;
                }
            }
            return d;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private static String byteArrayToString(ArrayList<Byte> s) throws UnsupportedEncodingException {
        byte[] d = new byte[s.size()];
        for (int i = 0; i < s.size(); i++) {
            d[i] = s.get(i);
        }
        return new String(d, "UTF-8");
    }

    private static String notToString(String s) {
        StringBuilder d = new StringBuilder();
        for (int i = 0; i < s.length(); i = i + 2) {
            try {
                byte temp = (byte) (0xff & Integer.parseInt(s.substring(i, i + 2), 16));
                int t = (~temp) & 0x000000FF;
                char dTemp = (char) t;
                d.append(dTemp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return d.toString();
    }

    ///????????
    private static ArrayList<Byte> notToString(byte[] s) {
        ArrayList<Byte> d = new ArrayList<Byte>();
        for (int i = 0; i < s.length; i = i + 2) {
            try {
                byte[] dTemp = new byte[]{s[i], s[i + 1]};
                String sTemp = new String(dTemp);
                byte temp = (byte) (0xff & Integer.parseInt(sTemp, 16));
                int t = (~temp) & 0x000000FF;
                d.add((byte) t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return d;
    }

    private static String notToHexString(String s) {
        try {
            byte[] bufs = s.getBytes("UTF-8");
            StringBuilder d = new StringBuilder();
            for (int i = 0; i < bufs.length; i++) {
                int t = (~bufs[i]) & 0x000000FF;
                String hex = Integer.toHexString(t);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                d.append(hex);
            }
            return d.toString();
        } catch (Exception e) {
        }
        return null;
    }

    //String hex = Integer.toHexString(b[i] & 0xFF);
    private static String intArrayToString(ArrayList<Integer> s) {
        StringBuilder d = new StringBuilder();
        String hex;
        for (int i = 0; i < s.size(); i++) {
            if (i % 2 == 0) {
                hex = Integer.toHexString(String.valueOf(s.get(i)).charAt(0));
            } else {
                hex = Integer.toHexString(s.get(i) & 0xFF);
            }
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            d.append(hex);
        }
        return d.toString();
    }

    private static String notToHexString(byte[] s) {
        try {
            StringBuilder d = new StringBuilder();
            for (int i = 0; i < s.length; i++) {
                int t = (~s[i]) & 0x000000FF;
                String hex = Integer.toHexString(t);
                if (hex.length() == 1) {
                    hex = '0' + hex;
                }
                d.append(hex);
            }
            return d.toString();
        } catch (Exception e) {
        }
        return null;
    }

    public static String toHexString(byte[] b) {
        StringBuilder d = new StringBuilder();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            d.append(hex);
        }
        return d.toString();
    }

    public static String toHexString(ArrayList<Byte> b) {
        StringBuilder d = new StringBuilder();
        for (int i = 0; i < b.size(); i++) {
            String hex = Integer.toHexString(b.get(i) & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            d.append(hex);
        }
        return d.toString();
    }
}
