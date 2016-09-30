package com.bjxrgz.startup.utils;

import android.text.TextUtils;
import android.util.Log;

import com.bjxrgz.startup.base.MyApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * 日志处理
 */
public class LogUtils {

    /**
     * 记录文本日志 , 默认Log.d
     */
    public static void log(int level, String tag, String message) {
        if (message == null) {
            message = "";
        }
        if (MyApp.IS_LOG) {
            switch (level) {
                case Log.DEBUG:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.d(tag, message);
                    }
                    break;
                case Log.INFO:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.i(tag, message);
                    }
                    break;
                case Log.ERROR:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.e(tag, message);
                    }
                    break;
                case Log.VERBOSE:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.v(tag, message);
                    }
                    break;
                case Log.WARN:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.w(tag, message);
                    }
                    break;
                default:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.d(tag, message);
                    }
                    break;
            }
        }
    }

    /**
     * 记录Exception日志 , 默认Log.d
     */
    public static void log(int level, String tag, String message, Exception exc) {
        if (MyApp.IS_LOG) {
            if (message == null) {
                message = "";
            }
            switch (level) {
                case Log.DEBUG:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.d(tag, message, exc);
                    }
                    break;
                case Log.INFO:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.i(tag, message, exc);
                    }
                    break;
                case Log.ERROR:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.e(tag, message, exc);
                    }
                    break;
                case Log.VERBOSE:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.v(tag, message, exc);
                    }
                    break;
                case Log.WARN:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.w(tag, message, exc);
                    }
                    break;
                case -1:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.DEBUG)) {
                        Log.d(tag, message);
                    }
                    break;
                default:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.d(tag, message, exc);
                    }
                    break;
            }
        }
    }

    /**
     * ************************************日志******************************
     * <p>
     * 记录日志
     */
    public static void writeLogFile(boolean printLog, String content, boolean isEncrypt) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (printLog) {
            writeFile(MyApp.LOG_FILE, content, isEncrypt);
        }
    }

    /**
     * 写日志文件
     */
    private static void writeFile(String name, String content, boolean isEncrypt) {

        OutputStreamWriter osw = null;
        try {
            File file = new File(name);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            } else if (file.length() > 1024 * 60) {// 60k
                file.delete();
                file.createNewFile();
            }
            osw = new OutputStreamWriter(new FileOutputStream(file, true));
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss|SSS");
            String time = "---" + (sdf.format(new Date()));
            String logStr = content + time;
            if (isEncrypt) {
                logStr = Base64Utils.encode(logStr.getBytes("utf-8"));
            }
            osw.write(logStr + "\n");
        } catch (Exception ex) {
            Log.e(MyApp.LOG_TAG, ((ex.getMessage() == null) ? "" : ex.getMessage()));
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    Log.e(MyApp.LOG_TAG, ((e.getMessage() == null) ? "" : e.getMessage()));
                }
            }
        }
    }

    /**
     * 写日志
     */
    public static void writeLogFile(boolean printLog, Exception e) {
        if (printLog) {
            try {
                e.printStackTrace(new PrintStream(MyApp.LOG_FILE));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (Exception ex) {
                Log.e(MyApp.LOG_TAG, ((ex.getMessage() == null) ? "" : ex.getMessage()));
            }
        }
    }

}
