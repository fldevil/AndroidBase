package com.bjxrgz.base.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.format.Formatter;
import android.webkit.MimeTypeMap;

import com.bjxrgz.base.base.BaseApp;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016/10/12.
 * describe  App相关工具类
 */
public class AppUtils {

    /* 封装App信息的Bean类 */
    public static class AppInfo {

        private String name; // APP名称
        private Drawable icon; // 图标
        private String packageName; // 包名
        private String packagePath; // 包路径
        private String versionName; // 版本
        private int versionCode; // 版本
        private Signature[] signature; // 签名
        private String SHA1; // 签名的SHA1值
        private boolean isSystem; // 是否是用户级别
        private String resDir; // SDCard/包名/
        private String logDir; // SDCard/包名/log/
        private String FilesDir; // SDCard/Android/data/包名/files/ 或者是sys的
        private String CacheDir; // SDCard/Android/data/包名/cache/ 或者是sys的

        public String getFilesDir() {
            return FilesDir;
        }

        public void setFilesDir(String fileDir) {
            FilesDir = fileDir;
        }

        public String getCacheDir() {
            return CacheDir;
        }

        public void setCacheDir(String cacheDir) {
            CacheDir = cacheDir;
        }

        public String getResDir() {
            return resDir;
        }

        public void setResDir(String resDir) {
            this.resDir = resDir;
        }

        public String getLogDir() {
            return logDir;
        }

        public void setLogDir(String logDir) {
            this.logDir = logDir;
        }

        public String getSHA1() {
            return SHA1;
        }

        public void setSHA1(String SHA1) {
            this.SHA1 = SHA1;
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

        public String getPackageName() {
            return packageName;
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

        @Override
        public String toString() {
            return "AppInfo{" +
                    "name='" + name + '\'' +
                    ", icon=" + icon +
                    ", packageName='" + packageName + '\'' +
                    ", packagePath='" + packagePath + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", versionCode=" + versionCode +
                    ", signature=" + Arrays.toString(signature) +
                    ", SHA1='" + SHA1 + '\'' +
                    ", isSystem=" + isSystem +
                    ", resDir='" + resDir + '\'' +
                    ", logDir='" + logDir + '\'' +
                    ", FileDir='" + FilesDir + '\'' +
                    ", CacheDir='" + CacheDir + '\'' +
                    '}';
        }
    }

    /**
     * 获取当前App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
     *
     * @return 当前应用的AppInfo
     */
    public static AppInfo getAppInfo() {
        PackageManager pm = BaseApp.get().getPackageManager();
        PackageInfo pi = null;
        try { // packageName可换成其他的app包名
            pi = pm.getPackageInfo(BaseApp.get().getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi != null ? getBean(pm, pi) : null;
    }

    /**
     * 得到AppInfo的Bean
     *
     * @param pm 包的管理
     * @param pi 包的信息
     * @return AppInfo类
     */
    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
        String packageName = pi.packageName;
        Signature[] signatures = pi.signatures;
        ApplicationInfo ai = pi.applicationInfo;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        String appName = ai.loadLabel(pm).toString();
        Drawable appIcon = ai.loadIcon(pm);
        String sourceDir = ai.sourceDir;
        String sha1 = "";
        if (signatures != null && signatures.length > 0) {
            sha1 = EncryptUtils.encryptSHA1ToString(signatures[0].toByteArray()).replaceAll("(?<=[0-9A-F]{2})[0-9A-F]{2}", ":$0");
        }
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) == ApplicationInfo.FLAG_SYSTEM;
        String filesDir = getFilesDir("");
        String cacheDir = getCacheDir();
        String resDir = getResDir(packageName);
        FileUtils.createOrExistsDir(resDir); // 并创建
        String logDir = getLogDir(resDir);
        FileUtils.createOrExistsDir(logDir); // 并创建

        AppInfo appInfo = new AppInfo();
        appInfo.setName(appName);
        appInfo.setIcon(appIcon);
        appInfo.setPackageName(packageName);
        appInfo.setPackagePath(sourceDir);
        appInfo.setVersionName(versionName);
        appInfo.setVersionCode(versionCode);
        appInfo.setSignature(signatures);
        appInfo.setSHA1(sha1);
        appInfo.setSystem(isSystem);
        appInfo.setResDir(resDir);
        appInfo.setLogDir(logDir);
        appInfo.setFilesDir(filesDir);
        appInfo.setCacheDir(cacheDir);
        return appInfo;
    }

    /**
     * **********************************文件管理************************************
     */
    /**
     * 获取根目录
     */
    private static String getRootPath() {
        return Environment.getRootDirectory() + File.separator;
    }

    /**
     * 判断SD卡是否可用
     */
    private static boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取SD卡路径 一般是/storage/emulated/0/
     */
    private static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getPath() + File.separator;
    }

    /**
     * 自定义资源路径(部分手机有差别)
     */
    private static String getResDir(String name) {
        String resDir;
        if (isSDCardEnable()) {
            resDir = getSDCardPath() + name + File.separator;
        } else {
            resDir = getRootPath() + name + File.separator;
        }
        return resDir;
    }

    /**
     * 自定义Log路径
     */
    private static String getLogDir(String resDir) {
        return resDir + "log" + File.separator;
    }

    /**
     * 如果SD卡存在，则获取
     * SDCard/Android/data/你的应用的包名/files/
     * 如果不存在，则获取
     * /data/data/<application package>/files
     */
    private static String getFilesDir(String path) {
        BaseApp baseApp = BaseApp.get();
        if (isSDCardEnable()) {
            File filesDir = baseApp.getExternalFilesDir(path);
            if (filesDir != null) {
                return filesDir.getAbsolutePath();
            }
        }
        return baseApp.getFilesDir().getAbsolutePath();
    }

    /**
     * 如果SD卡存在，则获取
     * SDCard/Android/data/你的应用包名/cache/
     * 如果不存在，则获取
     * /data/data/<application package>/cache
     */
    private static String getCacheDir() {
        BaseApp baseApp = BaseApp.get();
        if (isSDCardEnable()) {
            File cacheDir = baseApp.getExternalCacheDir();
            if (cacheDir != null) {
                return cacheDir.getAbsolutePath();
            }
        }
        return baseApp.getCacheDir().getAbsolutePath();
    }

    /**
     * 清除所有资源
     */
    public static void clearRes() {
        String resDir = BaseApp.get().getAppInfo().getResDir();
        List<File> fileList = FileUtils.listFilesAndDirInDir(resDir, true);
        for (File file : fileList) {
            FileUtils.deleteDir(file);
        }
    }

    /**
     * 清除缓存(Glide手动清)
     */
    public static void clearSys() {
        BaseApp baseApp = BaseApp.get();
        String filesDir = baseApp.getAppInfo().getFilesDir();
        String cacheDir = baseApp.getAppInfo().getCacheDir();
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
     * ***********************************运存***********************************
     * <p>
     * 内存，进程，服务，任务
     */
    private static ActivityManager getActivityManager() {
        return (ActivityManager) BaseApp.get().getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 获取手机内存信息
     */
    private static ActivityManager.MemoryInfo getMemoryInfo() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        getActivityManager().getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /**
     * 获取总共运存
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static String getTotalMem() {
        return Formatter.formatFileSize(BaseApp.get(), getMemoryInfo().totalMem);
    }

    /**
     * 获取可用运存
     */
    public static String getAvailMem() {
        return Formatter.formatFileSize(BaseApp.get(), getMemoryInfo().availMem);
    }

    /**
     * 如果当前可用内存 <= threshold，该值为真
     */
    public static boolean isLowMemory() {
        return getMemoryInfo().availMem <= getMemoryInfo().threshold;
    }

    /**
     * **************************************意图管理**********************************
     * 打开app系统设置
     */
    public static Intent getSettingsIntent() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String packageName = BaseApp.get().getAppInfo().getPackageName();
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * 获取App信息的意图
     */
    public static Intent getInfoIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        return intent.setData(Uri.parse("package:" + packageName));
    }

    /**
     * 跳转应用市场的意图
     */
    public static Intent getMarketIntent() {
        String str = "market://details?id=" + BaseApp.get().getPackageName();
        return new Intent("android.intent.action.VIEW", Uri.parse(str));
    }

    /**
     * 获取安装App(支持6.0)的意图
     */
    public static Intent getInstallIntent(File file) {
        if (file == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String type;
        if (Build.VERSION.SDK_INT < 23) {
            type = "application/vnd.android.package-archive";
        } else {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(FileUtils.getFileExtension(file));
        }
        return intent.setDataAndType(Uri.fromFile(file), type);
    }

    /**
     * 获取卸载App的意图
     */
    public Intent getUninstallIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取打开当前App的意图
     */
    public static Intent getOpenIntent() {
        BaseApp baseApp = BaseApp.get();
        return baseApp.getPackageManager().getLaunchIntentForPackage(baseApp.getPackageName());
    }

    /**
     * 回到Home
     */
    public static Intent getHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
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
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        && appProcess.processName.equals(baseApp.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否存在Activity
     *
     * @param packageName 包名
     * @param className   activity全路径类名
     */
    public static boolean isActivityExist(String packageName, String className) {
        PackageManager packageManager = BaseApp.get().getPackageManager();
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);
        ComponentName componentName = intent.resolveActivity(packageManager);
        int size = packageManager.queryIntentActivities(intent, 0).size();
        return !(resolveInfo == null || componentName == null || size == 0);
    }

    /**
     * 判断服务是否运行
     *
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     */
    public static boolean isServiceWork(String serviceName) {
        ActivityManager myAM = (ActivityManager) BaseApp.get()
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList != null && myList.size() > 0) {
            for (ActivityManager.RunningServiceInfo serviceInfo : myList) {
                if (serviceInfo.service.getClassName().equals(serviceName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
