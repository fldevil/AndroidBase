package com.bjxrgz.base.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;

/**
 * 日志管理工具类
 */
public class LogUtil {

    /**
     * 初始化日志框架
     */
    public static void initApp(String tag, final boolean debug) {
        FormatStrategy prettyFormatStrategy = PrettyFormatStrategy.newBuilder()
                .tag(tag)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(prettyFormatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return debug;
            }
        });
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

    public static void writeLogFile(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        File logFile = new File(AppUtil.getLogDir(context), "log.txt");
        if (FileUtil.createOrExistsDir(logFile)) {
            FileUtil.writeFileFromString(logFile, content + "\r\n", true);
        }
    }
}
