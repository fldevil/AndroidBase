package com.bjxrgz.startup.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by fd.meng on 2014/03/30
 * <p>
 * describe 设备判断，像素，运存，SIM卡，deviceID，版本，软键盘，PhoneState
 */
public class DeviceUtils {

    /**
     * SIM信息，服务商，数据连接
     */
    public static TelephonyManager getTelephonyManager(Context context) {

        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取分辨率对象 ,可以获取密度 ,以及屏幕宽高
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {

        return context.getResources().getDisplayMetrics();
    }

    /**
     * 内存，进程，服务，任务
     */
    public static ActivityManager getActivityManager(Context context) {

        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 应用包，应用程序，<<对应manifests文件>>
     */
    public static PackageManager getPackageManager(Context context) {

        return context.getPackageManager();
    }

    /**
     * 软键盘的管理
     */
    public static InputMethodManager getInputMethodManager(Context context) {

        return ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
    }

    /**
     * 剪切板
     */
    public static ClipboardManager getClipboardManager(Context context) {

        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 环境
     */
    public static Locale getLocale(Context context) {

        return context.getResources().getConfiguration().locale;
    }

    /**
     * ************************************设备判断************************************
     * <p>
     * 判断当前设备是否为手机
     */
    public static boolean isPhone(Context context) {

        return getTelephonyManager(context).getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 判断当前设备是否为Table
     */
    public static boolean isTable(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    /**
     * ************************************像素************************************
     * px 转 sp
     */
    public static float px2sp(Context context, Float px) {

        return px / getDisplayMetrics(context).scaledDensity;
    }

    /**
     * sp 转 px
     */
    public static float sp2px(Context context, Float sp) {

        return sp * getDisplayMetrics(context).scaledDensity;
    }

    /**
     * px 转 dp
     */
    public static float px2dp(Context context, float px) {
        // dp = px /设备密度
        return px / getDisplayMetrics(context).density;
    }

    /**
     * dp 转 px
     */
    public static int dp2px(Context context, int dp) {
        // 细节,四舍五入
        return (int) (dp * getDisplayMetrics(context).density + 0.5f);
    }

    /**
     * ************************************运存************************************
     * <p>
     * 获取手机运存信息
     */
    public static ActivityManager.MemoryInfo getMemoryInfo(Context context) {

        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();

        getActivityManager(context).getMemoryInfo(memoryInfo);

        return memoryInfo;
    }

    /**
     * 获取总共运存
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static String getTotalMem(Context context) {

        return Formatter.formatFileSize(context, getMemoryInfo(context).totalMem);
    }

    /**
     * 获取可用运存
     */
    public static String getAvailMem(Context context) {

        return Formatter.formatFileSize(context, getMemoryInfo(context).availMem);
    }

    /**
     * ************************************SIM卡************************************
     * <p>
     * SIM_STATE_UNKNOWN 　 未知状态
     * SIM_STATE_ABSENT 　 未插卡
     * SIM_STATE_READY 　 准备就绪
     * SIM_STATE_PIN_REQUIRED 　 需要PIN码，需要SIM卡PIN码解锁
     * SIM_STATE_PUK_REQUIRED 　需要PUK码，需要SIM卡PUK码解锁
     * SIM_STATE_NETWORK_LOCKED 网络被锁定，需要网络PIN解锁
     */
    public static int getSimState(Context context) {

        return getTelephonyManager(context).getSimState();
    }

    /**
     * SIM卡的序列号(IMEI)
     */
    public static String getSimNumber(Context context) {
        if (!isPhone(context)) {
            return "请换成手机";
        }
        if (getSimState(context) != TelephonyManager.SIM_STATE_READY) {
            return "请检查SIM卡状态";
        }
        return getTelephonyManager(context).getSimSerialNumber();
    }

    /**
     * 返回手机号码，对于GSM网络来说即MSISDN
     */
    public static String getPhoneNumber(Context context) {
        if (!isPhone(context)) {
            return "请换成手机";
        }
        if (getSimState(context) != TelephonyManager.SIM_STATE_READY) {
            return "请检查SIM卡状态";
        }
        return getTelephonyManager(context).getLine1Number();
    }

    /**
     * ************************************Device************************************
     * <p>
     * 返回当前移动终端的唯一标识(IMEI) 如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
     */
    public static String getDeviceId(Context context) {
        String deviceId;
        if (isPhone(context)) {
            deviceId = getTelephonyManager(context).getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = "000000000000000";
        }
        return deviceId;
    }

    /**
     * 生成 GUID
     */
    public static String generateGUID(Context context, int objectId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        String sDateNew = format.format(Calendar.getInstance().getTime());

        return getTelephonyManager(context).getDeviceId() + "_" + sDateNew + "_" + String.valueOf(objectId);
    }

    /**
     * 返回用户唯一标识，比如GSM网络的IMSI编号
     */
    public static String getSubscriberId(Context context) {

        return getTelephonyManager(context).getSubscriberId();
    }

    /**
     * 返回移动终端的软件版本，例如：GSM手机的IMEI/SV码。
     */
    public static String getDeviceSoftwareVersion(Context context) {

        return getTelephonyManager(context).getDeviceSoftwareVersion();
    }

    /**
     * *********************************Package*********************************
     * <p>
     * 返回PackageInfo对象, 相当于Manifest里的manifest节点
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        try {
            return getPackageManager(context).getPackageInfo(packageName, 0);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是versionName
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
        if (packageInfo == null) {
            return "找不到包信息";
        }
        return packageInfo.versionName;
    }

    /**
     * 是versionCode
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
        if (packageInfo == null) {
            return 0;
        }
        return packageInfo.versionCode;
    }

    /**
     * 获取应用大小，单位已经换算好
     */
    public static String getApplicationSize(Context context) {
        PackageInfo packageInfo = getPackageInfo(context, context.getPackageName());
        if (packageInfo == null) {
            return "找不到包信息";
        }
        long length = new File(packageInfo.applicationInfo.sourceDir).length();

        return Formatter.formatFileSize(context, length);
    }

    /**
     * *********************************软键盘*********************************
     * <p>
     * 隐藏软键盘
     */
    public static void hideSoftInputMode(Context context, View windowToken) {

        getInputMethodManager(context).hideSoftInputFromWindow(windowToken.getWindowToken(), 0);
    }

    /**
     * 弹出软键盘
     */
    public static void showSoftInputMode(Context context, View windowToken) {
        getInputMethodManager(context).showSoftInput(windowToken, InputMethodManager.SHOW_FORCED);
    }

    /**
     * ************************************剪切板*************************************
     */
    public static void copyText(Context context, String label, String text) {

        ClipData myClip = ClipData.newPlainText(label, text);

        getClipboardManager(context).setPrimaryClip(myClip);
    }

    /**
     * ************************************环境*************************************
     * 是否为英语环境
     */
    public static boolean isEN(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("en");
    }

    /**
     * 是否为中文环境
     */
    public static boolean isZH(Context context) {
        String language = getLocale(context).getLanguage();
        return language.endsWith("zh");
    }

    /**
     * ************************************PhoneState************************************
     * <p>
     * 返回电话状态
     * CALL_STATE_IDLE 无任何状态时
     * CALL_STATE_OFFHOOK 接起电话时
     * CALL_STATE_RINGING 电话进来时
     */
    public static int getCallState(Context context) {

        return getTelephonyManager(context).getCallState();
    }

    /**
     * 获取数据活动状态
     * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据
     * DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据
     * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据
     * DATA_ACTIVITY_NONE 数据连接状态：活动，但无数据发送和接受
     */
    public static int getDataActivity(Context context) {

        return getTelephonyManager(context).getDataActivity();
    }

    /**
     * 获取数据连接状态
     * DATA_CONNECTED 数据连接状态：已连接
     * DATA_CONNECTING 数据连接状态：正在连接
     * DATA_DISCONNECTED 数据连接状态：断开
     * DATA_SUSPENDED 数据连接状态：暂停
     */
    public static int getDataState(Context context) {

        return getTelephonyManager(context).getDataState();
    }

    /**
     * 监听网络信号强度
     */
    public static void startListenStrength(Context context, PhoneStateListener listener) {

        getTelephonyManager(context).listen(listener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }

    /**
     * 监听来电状态
     */
    public static void startListenCall(Context context, PhoneStateListener listener) {

        getTelephonyManager(context).listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 停止PhoneState所有监听
     */
    public static void stopListen(Context context, PhoneStateListener listener) {

        getTelephonyManager(context).listen(listener, PhoneStateListener.LISTEN_NONE);
    }

}
