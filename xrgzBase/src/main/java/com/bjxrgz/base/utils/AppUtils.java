package com.bjxrgz.base.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.bjxrgz.base.base.BaseApp;

import java.io.File;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016/10/12.
 * describe  App相关工具类
 */
public class AppUtils {
    private static AppUtils instance;

    private String name; // APP名称
    private Drawable icon; // 图标
    private String packageName; // 包名
    private String packagePath; // 包路径
    private String versionName; // 版本
    private int versionCode; // 版本
    private Signature[] signature; // 签名
    private boolean isSystem; // 是否是用户级别
    private String resDir; // SDCard/包名/
    private String logDir; // SDCard/包名/log/
    private String filesDir; // SDCard/Android/data/包名/files/ 或者是sys的
    private String cacheDir; // SDCard/Android/data/包名/cache/ 或者是sys的

    /* 获取当前App信息 */
    public static AppUtils get() {
        if (instance != null) return instance;
        PermUtils.requestApp(BaseApp.get(), null);
        instance = new AppUtils();
        PackageManager pm = BaseApp.get().getPackageManager();
        try { // packageName可换成其他的app包名
            PackageInfo pi = pm.getPackageInfo(BaseApp.get().getApplicationContext().getPackageName(), 0);
            ApplicationInfo ai = pi.applicationInfo;
            Signature[] signatures = pi.signatures;
            boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags)
                    == ApplicationInfo.FLAG_SYSTEM;
            // 赋初始值
            instance.setName(ai.loadLabel(pm).toString());
            instance.setIcon(ai.loadIcon(pm));
            instance.setPackageName(pi.packageName);
            instance.setPackagePath(ai.sourceDir);
            instance.setVersionCode(pi.versionCode);
            instance.setVersionName(pi.versionName);
            instance.setSignature(signatures);
            instance.setSystem(isSystem);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return instance;
    }

    /**
     * 如果SD卡存在，则获取 SDCard/Android/data/你的应用的包名/files/
     * 如果不存在，则获取 /data/data/<application package>/files
     */
    public String getFilesDir() {
        if (!StringUtils.isEmpty(filesDir)) return filesDir;
        BaseApp baseApp = BaseApp.get();
        if (isSDCardEnable()) {
            File filesDir = baseApp.getExternalFilesDir("");
            if (filesDir != null) return filesDir.getAbsolutePath();
        }
        setFilesDir(baseApp.getFilesDir().getAbsolutePath());
        return filesDir;
    }

    public void setFilesDir(String fileDir) {
        filesDir = fileDir;
    }

    /**
     * 如果SD卡存在，则获取 SDCard/Android/data/你的应用包名/cache/
     * 如果不存在，则获取 /data/data/<application package>/cache
     */
    public String getCacheDir() {
        if (!StringUtils.isEmpty(cacheDir)) return cacheDir;
        BaseApp baseApp = BaseApp.get();
        if (isSDCardEnable()) {
            File cacheDir = baseApp.getExternalCacheDir();
            if (cacheDir != null) return cacheDir.getAbsolutePath();
        }
        setCacheDir(baseApp.getCacheDir().getAbsolutePath());
        return cacheDir;
    }

    public void setCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    /**
     * 自定义资源路径(部分手机有差别)
     */
    public String getResDir() {
        if (!StringUtils.isEmpty(resDir)) return resDir;
        String resDir;
        if (isSDCardEnable()) {
            resDir = getSDCardPath() + name + File.separator;
        } else {
            resDir = getRootPath() + name + File.separator;
        }
        setResDir(resDir);
        FileUtils.createOrExistsDir(resDir); // 并创建
        return resDir;
    }

    public void setResDir(String resDir) {
        this.resDir = resDir;
    }

    /**
     * 自定义Log路径
     */
    public String getLogDir() {
        if (!StringUtils.isEmpty(logDir)) return logDir;
        setLogDir(resDir + "log" + File.separator);
        FileUtils.createOrExistsDir(logDir); // 并创建
        return logDir;
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
    }

    public Signature[] getSignature() {
        return signature;
    }

    public void setSignature(Signature[] signature) {
        this.signature = signature;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 获取根目录
     */
    public String getRootPath() {
        return Environment.getRootDirectory() + File.separator;
    }

    /**
     * 判断SD卡是否可用
     */
    public boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡路径 一般是/storage/emulated/0/
     */
    public String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    /**
     * 清除所有资源
     */
    public void clearRes() {
        String resDir = getResDir();
        FileUtils.deleteFilesAndDirInDir(resDir);
    }

    /**
     * 清除缓存(Glide手动清)
     */
    public void clearSys() {
        BaseApp baseApp = BaseApp.get();
        String filesDir = getFilesDir();
        String cacheDir = getCacheDir();
        File externalFilesDir = new File(filesDir);
        File externalCacheDir = new File(cacheDir);
        File internalFilesDir = baseApp.getFilesDir();
        File internalCacheDir = baseApp.getCacheDir();

        FileUtils.deleteFilesAndDirInDir(externalFilesDir);
        FileUtils.deleteFilesAndDirInDir(externalCacheDir);
        FileUtils.deleteFilesAndDirInDir(internalFilesDir);
        FileUtils.deleteFilesAndDirInDir(internalCacheDir);
    }

    /**
     * ***********************************外存***********************************
     * <p>
     * 外存总共空间
     */
    public static String getExternalTotal() {
        long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
        return FileUtils.getFileSize(totalSpace);
    }

    /**
     * 外存使用空间
     */
    public static String getExternalUsable() {
        long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();
        return FileUtils.getFileSize(usableSpace);
    }

    /**
     * 外存剩余空间
     */
    public static String getExternalFree() {
        long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        return FileUtils.getFileSize(freeSpace);
    }

    /**
     * 判断App在前台运行
     * <uses-permission android:name="android.permission.GET_TASKS" />
     */
    public static boolean isAppOnForeground() {
        BaseApp baseApp = BaseApp.get();
        ActivityManager activityManager = (ActivityManager)
                baseApp.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses =
                activityManager.getRunningAppProcesses();
        if (appProcesses != null && appProcesses.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.importance ==
                        ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && appProcess.processName.equals(baseApp.getPackageName()))
                    return true;
            }
        }
        return false;
    }

}
