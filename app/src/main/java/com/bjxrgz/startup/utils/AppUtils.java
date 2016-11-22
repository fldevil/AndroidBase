package com.bjxrgz.startup.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.webkit.MimeTypeMap;

import com.bjxrgz.startup.base.MyApp;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static u.aly.av.F;

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
        private String FilesDir; // SDCard/Android/data/包名/files/
        private String CacheDir; // SDCard/Android/data/包名/cache/

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
     * @param context 上下文(可以使其他的应用)
     * @return 当前应用的AppInfo
     */
    public static AppInfo getAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try { // packageName可换成其他的app包名
            pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
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
        String filesDir = getFilesDir(MyApp.getInstance(), "");
        String cacheDir = getCacheDir(MyApp.getInstance());
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
    private static String getFilesDir(Context context, String path) {
        if (isSDCardEnable()) {
            File filesDir = context.getExternalFilesDir(path);
            if (filesDir != null) {
                return filesDir.getAbsolutePath();
            }
        }
        return context.getFilesDir().getAbsolutePath();
    }

    /**
     * 如果SD卡存在，则获取
     * SDCard/Android/data/你的应用包名/cache/
     * 如果不存在，则获取
     * /data/data/<application package>/cache
     */
    private static String getCacheDir(Context context) {
        if (isSDCardEnable()) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                return cacheDir.getAbsolutePath();
            }
        }
        return context.getCacheDir().getAbsolutePath();
    }

    /**
     * 清除所有资源
     */
    public static void clearRes() {
        String resDir = MyApp.getInstance().getAppInfo().getResDir();
        List<File> fileList = FileUtils.listFilesAndDirInDir(resDir, true);
        for (File file : fileList) {
            FileUtils.deleteDir(file);
        }
    }

    /**
     * 清除缓存(Glide手动清)
     */
    public static void clearSys(Context context) {
        String filesDir = MyApp.getInstance().getAppInfo().getFilesDir();
        String cacheDir = MyApp.getInstance().getAppInfo().getCacheDir();
        File externalFilesDir = new File(filesDir);
        File externalCacheDir = new File(cacheDir);
        File internalFilesDir = context.getFilesDir();
        File internalCacheDir = context.getCacheDir();

        FileUtils.deleteFilesAndDirInDir(externalFilesDir);
        FileUtils.deleteFilesAndDirInDir(externalCacheDir);
        FileUtils.deleteFilesAndDirInDir(internalFilesDir);
        FileUtils.deleteFilesAndDirInDir(internalCacheDir);
    }

    /**
     * 外存总共空间
     */
    public static String getExternalTotal() {
        long totalSpace = Environment.getExternalStorageDirectory().getTotalSpace();
        return FileUtils.getFileSize(totalSpace);
    }

    /**
     * 外存剩余空间
     */
    public static String getExternalFree() {
        long freeSpace = Environment.getExternalStorageDirectory().getFreeSpace();
        return FileUtils.getFileSize(freeSpace);
    }

    /**
     * 外存使用空间
     */
    public static String getExternalUsable() {
        long usableSpace = Environment.getExternalStorageDirectory().getUsableSpace();
        return FileUtils.getFileSize(usableSpace);
    }

    /**
     * **************************************意图管理**********************************
     */
    /**
     * 打开app系统设置
     */
    public static Intent getSettingsIntent() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        String packageName = MyApp.getInstance().getAppInfo().getPackageName();
        intent.setData(Uri.parse("package:" + packageName));
        return intent;
    }

    /**
     * 获取安装App(支持6.0)的意图
     */
    public static Intent getInstallIntent(String filePath) {
        return getInstallIntent(FileUtils.getFileByPath(filePath));
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
     * 获取App信息的意图
     */
    public static Intent getInfoIntent(String packageName) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        return intent.setData(Uri.parse("package:" + packageName));
    }

    /**
     * 获取打开当前App的意图
     */
    public static Intent getOpenIntent(Context context) {
        return getIntentByPackageName(context, context.getPackageName());
    }

    /**
     * 判断App是否安装
     */
    public static boolean isInstall(Context context, String packageName) {
        return getIntentByPackageName(context, packageName) != null;
    }

    /**
     * 根据包名获取意图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
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
     * 判断当前App处于前台还是后台
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.GET_TASKS"/>}</p>
     * <p>并且必须是系统应用该方法才有效</p>
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        @SuppressWarnings("deprecation")
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

}
