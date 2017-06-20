package com.bjxrgz.base.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.bjxrgz.base.BaseApp;

/**
 * Created by jiang on 2016/10/12
 * <p/>
 * describe  设备相关工具类
 */
public class DeviceUtil {

    /**
     * 判断当前设备是否是手机
     */
    public static boolean isPhone() {
        TelephonyManager telephonyManager = ManagerUtil.getTelephonyManager();
        return telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取设备唯一标识
     */
    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        String deviceId;
        TelephonyManager telephonyManager = ManagerUtil.getTelephonyManager();
        if (isPhone()) {
            deviceId = telephonyManager.getDeviceId();
        } else {
            ContentResolver contentResolver = BaseApp.getInstance().getContentResolver();
            deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }

    /**
     * 获取设备mac地址
     */
    @SuppressLint("HardwareIds")
    public static String getMacAddress() {
        String macAddress = "";
        WifiManager wifi = ManagerUtil.getWifiManager();
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            macAddress = info.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取设备手机号
     */
    @SuppressLint("HardwareIds")
    public static String getPhoneNumber() {
        String phoneNumber;
        TelephonyManager telephonyManager = ManagerUtil.getTelephonyManager();
        phoneNumber = telephonyManager.getLine1Number();
        return phoneNumber;
    }

    /**
     * 获取设备型号
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 获取设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备平台
     */
    public static String getPlatform() {
        return "Android";
    }

    /**
     * 获取系统版本号
     */
    public static String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取sim卡序列号
     */
    @SuppressLint("HardwareIds")
    public static String getSimSerial() {
        String simSerial;
        TelephonyManager telephonyManager = ManagerUtil.getTelephonyManager();
        simSerial = telephonyManager.getSimSerialNumber();
        return simSerial;
    }
}
