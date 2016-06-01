package com.bjxrgz.startup.utils;

import android.util.Log;

import com.bjxrgz.startup.base.MyApp;

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
        if (MyApp.PRINT_LOG) {
            switch (level) {
                case Log.DEBUG:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.DEBUG)) {
                        Log.d(tag, message);
                    }
                    break;
                case Log.INFO:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.INFO)) {
                        Log.i(tag, message);
                    }
                    break;
                case Log.ERROR:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.e(tag, message);
                    }
                    break;
                case Log.VERBOSE:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.VERBOSE)) {
                        Log.v(tag, message);
                    }
                    break;
                case Log.WARN:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.WARN)) {
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
        if (MyApp.PRINT_LOG) {
            if (message == null) {
                message = "";
            }
            switch (level) {
                case Log.DEBUG:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.DEBUG)) {
                        Log.d(tag, message, exc);
                    }
                    break;
                case Log.INFO:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.INFO)) {
                        Log.i(tag, message, exc);
                    }
                    break;
                case Log.ERROR:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.ERROR)) {
                        Log.e(tag, message, exc);
                    }
                    break;
                case Log.VERBOSE:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.VERBOSE)) {
                        Log.v(tag, message, exc);
                    }
                    break;
                case Log.WARN:
                    if (Log.isLoggable(MyApp.LOG_TAG, Log.WARN)) {
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

}
