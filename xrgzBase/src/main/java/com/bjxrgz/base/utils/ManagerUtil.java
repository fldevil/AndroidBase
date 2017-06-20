package com.bjxrgz.base.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

import com.bjxrgz.base.BaseApp;

/**
 * Created by Fan on 2017/5/15.
 * 获取系统Manager
 */

public class ManagerUtil {

    /**
     * 网络连接管理器
     */
    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) BaseApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    /**
     * 获取电话管理器
     */
    public static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) BaseApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取包管理器
     */
    public static PackageManager getPackageManager() {
        return BaseApp.getInstance().getPackageManager();
    }

    /**
     * 获取activity管理器
     */
    public static ActivityManager getActivityManager() {
        return (ActivityManager) BaseApp.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 获取wifi管理器
     */
    @SuppressLint("WifiManagerLeak")
    public static WifiManager getWifiManager() {
        return (WifiManager) BaseApp.getInstance().getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 获取输入管理器
     */
    public static InputMethodManager getInputManager() {
        return (InputMethodManager) BaseApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 获取剪贴板管理器
     */
    public static ClipboardManager getClipboardManager() {
        return (ClipboardManager) BaseApp.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 获取定位管理器
     */
    public static LocationManager getLocationManager() {
        return (LocationManager) BaseApp.getInstance().getSystemService(Context.LOCATION_SERVICE);
    }
}
