package com.bjxrgz.base.utils;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Settings;

/**
 * 日志管理工具类
 */
public class LogUtil {

    /**
     * 初始化日志框架
     */
    public static void initApp(String tag, boolean debug) {
        Settings settings = Logger.init(tag);
        settings.hideThreadInfo();
        if (debug) {
            settings.logLevel(LogLevel.FULL);
        } else {
            settings.logLevel(LogLevel.NONE);
        }
    }

    public static void i(String message, Object... args) {
        Logger.i(message, args);
    }

    public static void i(String tag, String message, Object... args) {
        Logger.t(tag).i(message, args);
    }

    public static void d(String message, Object... args) {
        Logger.d(message, args);
    }

    public static void d(String tag, String message, Object... args) {
        Logger.t(tag).d(message, args);
    }

    public static void e(String message, Object... args) {
        Logger.e(message, args);
    }

    public static void e(String tag, String message, Object... args) {
        Logger.t(tag).e(message, args);
    }

    public static void e(Throwable throwable, String message, Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void e(String tag, Throwable throwable, String message, Object... args) {
        Logger.t(tag).e(throwable, message, args);
    }

    public static void json(String json) {
        Logger.json(json);
    }

    public static void json(String tag, String json) {
        Logger.t(tag).e(json);
    }
}
