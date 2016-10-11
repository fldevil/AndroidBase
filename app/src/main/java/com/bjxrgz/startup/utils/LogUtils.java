package com.bjxrgz.startup.utils;

import android.text.TextUtils;
import android.util.Log;

import com.bjxrgz.startup.base.MyApp;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

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

    public static void initApp(String LOG_TAG, boolean IS_LOG) {
        Logger.Settings settings = Logger.init(LOG_TAG); // 打印tag
        settings.setMethodCount(3);// 3以上才能显示调用方法
        settings.hideThreadInfo(); // 隐藏线程显示
        if (IS_LOG) { // log开关
            settings.setLogLevel(LogLevel.FULL);
        } else {
            settings.setLogLevel(LogLevel.NONE);
        }
    }

    /**
     * 调试
     */
    public static void d(String print) {
        Logger.d(print);
    }

    public static void d(String tag, String print) {
        Logger.d(tag, print);
    }

    public static void d(String print, int methodCount) {
        Logger.d(print, methodCount);
    }

    /**
     * 警告
     */
    public static void e(String print) {
        Logger.e(print);
    }

    public static void e(String tag, String print) {
        Logger.e(tag, print);
    }

    public static void e(String print, int methodCount) {
        Logger.e(print, methodCount);
    }

    /**
     * 异常
     */
    public static void e(Exception e) {
        Logger.e(e);
    }

    public static void e(Throwable ex) {
        Logger.e(ex.toString());
    }

    public static void e(String tag, Exception e) {
        Logger.e(tag, e);
    }

    public static void e(String tag, Throwable ex) {
        Logger.e(tag, ex.toString());
    }

    /**
     * 实体类
     */
    public static void json(String json) {
        Logger.json(json);
    }

    public static void json(String tag, String json) {
        Logger.json(tag, json);
    }

    public static void json(String json, int methodCount) {
        Logger.json(json, methodCount);
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
