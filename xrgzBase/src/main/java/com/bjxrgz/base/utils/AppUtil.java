package com.bjxrgz.base.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.bjxrgz.base.BaseApp;

import java.io.File;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016/10/12.
 * describe  App相关工具类
 */
public class AppUtil {

    /**
     * 获取app的名称
     */
    public static String getAppName() {
        PackageManager pm = ManagerUtil.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            ApplicationInfo ai = pi.applicationInfo;
            return ai.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取app的图标
     */
    public static Drawable getAppIcon() {
        PackageManager pm = ManagerUtil.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            ApplicationInfo ai = pi.applicationInfo;
            return ai.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用包名
     */
    public static String getPackageName() {
        PackageManager pm = ManagerUtil.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            return pi.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用安装路径
     */
    public static String getSourceDir() {
        PackageManager pm = ManagerUtil.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            ApplicationInfo ai = pi.applicationInfo;
            return ai.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用版本名称
     */
    public static String getVersionName() {
        PackageManager pm = ManagerUtil.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用版本号
     */
    public static int getVersionCode() {
        PackageManager pm = ManagerUtil.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(BaseApp.getInstance().getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 自定义资源路径(部分手机有差别)
     */
    public static String getResDir() {
        String dir;
        if (isSDCardEnable()) {
            dir = getSDCardPath() + getPackageName() + File.separator;
        } else {
            dir = getRootPath() + getPackageName() + File.separator;
        }
        FileUtil.createOrExistsDir(dir); // 并创建
        return dir;
    }

    /**
     * 自定义Log路径
     */
    public static String getLogDir() {
        String dir = getResDir() + "log" + File.separator;
        FileUtil.createOrExistsDir(dir); // 并创建
        return dir;
    }

    /**
     * 如果SD卡存在，则获取 SDCard/Android/data/你的应用的包名/files/
     * 如果不存在，则获取 /data/data/<application package>/files
     */
    public static String getFilesDir(Context context, String dirName) {
        File dir;
        if (isSDCardEnable()) {
            dir = context.getExternalFilesDir(dirName);
        } else {
            dir = context.getFilesDir();
        }
        if (dir != null) {
            return dir.getAbsolutePath();
        }
        return "";
    }

    /**
     * 如果SD卡存在，则获取 SDCard/Android/data/你的应用包名/cache/
     * 如果不存在，则获取 /data/data/<application package>/cache
     */
    public static String getCacheDir(Context context) {
        File dir;
        if (isSDCardEnable()) {
            dir = context.getExternalCacheDir();
        } else {
            dir = context.getCacheDir();
        }
        if (dir != null) {
            return dir.getAbsolutePath();
        }
        return "";
    }

    /**
     * 获取根目录
     */
    public static String getRootPath() {
        return Environment.getRootDirectory() + File.separator;
    }

    /**
     * 获取SD卡路径 一般是/storage/emulated/0/
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    /**
     * 判断SD卡是否可用
     */
    public static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * ***********************************外存***********************************
     * <p>
     * 外存总共空间
     */
    public static String getExternalTotal() {
        long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
        return FileUtil.getFileSize(totalSpace);
    }

    /**
     * 外存使用空间
     */
    public static String getExternalUsable() {
        long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();
        return FileUtil.getFileSize(usableSpace);
    }

    /**
     * 外存剩余空间
     */
    public static String getExternalFree() {
        long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        return FileUtil.getFileSize(freeSpace);
    }

    /**
     * 判断App在前台运行
     * <uses-permission android:name="android.permission.GET_TASKS" />
     */
    public static boolean isAppOnForeground() {
        ActivityManager activityManager = ManagerUtil.getActivityManager();
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                activityManager.getRunningAppProcesses();
        if (appProcesses != null && appProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance ==
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && appProcess.processName.equals(getPackageName()))
                    return true;
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public static void AppExit() {
        ActivityUtil.closeActivities();
        ActivityManager activityManager = ManagerUtil.getActivityManager();
        activityManager.killBackgroundProcesses(getPackageName());
        System.exit(0);
    }
}
