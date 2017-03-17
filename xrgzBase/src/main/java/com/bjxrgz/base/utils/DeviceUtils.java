package com.bjxrgz.base.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.bjxrgz.base.base.BaseApp;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by jiang on 2016/10/12
 * <p/>
 * describe  设备相关工具类
 */
public class DeviceUtils {
    private static DeviceUtils instance;
    private static TelephonyManager telephonyManager;

    private String deviceId; // GSM网络，返回IMEI；CDMA网络，返回MEID
    private String macAddress; // MAC地址
    private String model; // 设备型号
    private String manufacturer; // 设备厂商
    private String platform; // 平台(Android)
    private String osVersion; // Android版本号
    private boolean isPhone; // 是否是手机
    private boolean isTable; // 是否是手表
    private String phoneNumber; // 手机号
    private String simSerial; // sim卡序号
    private String ipAddress; // ip地址 eg:127.168.x.x
    // location
    private double longitude; // 经度
    private double latitude; // 纬度
    private String province; // 省信息
    private String city; // 城市信息
    private String district; // 区
    private String address; // 详细地址

    /* 获取当前Device信息 */
    public static DeviceUtils get() {
        if (instance != null) return instance;
        PermUtils.requestDevice(BaseApp.get(), null); // 权限请求
        instance = new DeviceUtils();
        telephonyManager = (TelephonyManager) BaseApp.get()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return instance;
    }

    /**
     * 获取手机的IMIE(DeviceId)
     */
    @SuppressLint("HardwareIds")
    public String getDeviceId() {
        if (!StringUtils.isEmpty(deviceId)) return deviceId;
        String deviceId;
        if (isPhone()) {
            deviceId = telephonyManager.getDeviceId();
        } else {
            ContentResolver contentResolver = BaseApp.get().getContentResolver();
            deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
        }
        setDeviceId(deviceId);
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @SuppressLint("HardwareIds")
    public String getPhoneNumber() {
        if (!StringUtils.isEmpty(phoneNumber)) return phoneNumber;
        setPhoneNumber(telephonyManager.getLine1Number());
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @SuppressLint("HardwareIds")
    public String getSimSerial() {
        if (!StringUtils.isEmpty(simSerial)) return simSerial;
        setSimSerial(telephonyManager.getSimSerialNumber());
        return simSerial;
    }

    public void setSimSerial(String simSerial) {
        this.simSerial = simSerial;
    }

    /**
     * 物理地址
     */
    @SuppressLint("HardwareIds")
    public String getMacAddress() {
        if (!StringUtils.isEmpty(macAddress)) return macAddress;
        WifiManager wifi = (WifiManager) BaseApp.get()
                .getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            setMacAddress(info.getMacAddress());
        }
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public boolean isPhone() {
        setPhone(telephonyManager.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE);
        return isPhone;
    }

    public void setPhone(boolean phone) {
        isPhone = phone;
    }

    public boolean isTable() {
        int screenLayout = BaseApp.get().getResources().getConfiguration().screenLayout;
        boolean xlarge = ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE);
        setTable((xlarge || large));
        return isTable;
    }

    public void setTable(boolean table) {
        isTable = table;
    }

    public String getManufacturer() {
        if (!StringUtils.isEmpty(manufacturer)) return manufacturer;
        setManufacturer(Build.MANUFACTURER);
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        if (!StringUtils.isEmpty(model)) return model;
        setModel(Build.MODEL);
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlatform() {
        if (!StringUtils.isEmpty(platform)) return platform;
        setPlatform("Android");
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOsVersion() {
        if (!StringUtils.isEmpty(osVersion)) return osVersion;
        setOsVersion(Build.VERSION.RELEASE);
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getIpAddress() {
        if (!StringUtils.isEmpty(ipAddress)) return ipAddress;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet6Address) continue; // skip ipv6
                    String ip = ia.getHostAddress();
                    String host = "127.0.0.1";
                    if (!host.equals(ip)) {
                        setIpAddress(ip);
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getAddress() {
        if (StringUtils.isEmpty(address)) {
            address = "";
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        if (StringUtils.isEmpty(district)) {
            district = "";
        }
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        if (StringUtils.isEmpty(province)) {
            province = "";
        }
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        if (StringUtils.isEmpty(city)) {
            city = "";
        }
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

}
