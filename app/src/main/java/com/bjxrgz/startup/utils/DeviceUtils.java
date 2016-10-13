package com.bjxrgz.startup.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

/**
 * Created by jiang on 2016/10/12
 * <p/>
 * describe  设备相关工具类
 */
public class DeviceUtils {

    /**
     * 封装设备信息的Bean类
     */
    public static class DeviceInfo {

        private String deviceId; // 当前移动终端的唯一标识(IMEI),如果是GSM网络，返回IMEI；如果是CDMA网络，返回MEID
        private String macAddress; // MAC地址
        private String model; // 设备型号
        private String manufacturer; // 设备厂商
        private String platform; // 平台(Android)
        private String osVersion; // Android版本号
        private boolean isPhone; // 是否是手机
        private boolean isTable; // 是否是手表
        private String phoneNumber; // 手机号
        private String simSerial; // sim卡序号

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSimSerial() {
            return simSerial;
        }

        public void setSimSerial(String simSerial) {
            this.simSerial = simSerial;
        }

        public boolean isPhone() {
            return isPhone;
        }

        public void setPhone(boolean phone) {
            isPhone = phone;
        }

        public boolean isTable() {
            return isTable;
        }

        public void setTable(boolean table) {
            isTable = table;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getMacAddress() {
            return macAddress;
        }

        public void setMacAddress(String macAddress) {
            this.macAddress = macAddress;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        @Override
        public String toString() {
            return "DeviceInfo{" +
                    "deviceId='" + deviceId + '\'' +
                    ", macAddress='" + macAddress + '\'' +
                    ", model='" + model + '\'' +
                    ", manufacturer='" + manufacturer + '\'' +
                    ", platform='" + platform + '\'' +
                    ", osVersion='" + osVersion + '\'' +
                    ", isPhone=" + isPhone +
                    ", isTable=" + isTable +
                    ", phoneNumber='" + phoneNumber + '\'' +
                    ", simSerial='" + simSerial + '\'' +
                    '}';
        }
    }

    public static DeviceInfo getDeviceInfo(Context context) {
        String deviceId = PhoneUtils.getDeviceId(context);
        String macAddress = getMacAddress(context);
        boolean phone = PhoneUtils.isPhone(context);
        boolean table = isTable(context);
        String phoneNumber = PhoneUtils.getPhoneNumber(context);
        String simSerial = PhoneUtils.getSimSerial(context);

        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setDeviceId(deviceId);
        deviceInfo.setMacAddress(macAddress);
        deviceInfo.setModel(Build.MODEL);
        deviceInfo.setManufacturer(Build.MANUFACTURER);
        deviceInfo.setPlatform("Android");
        deviceInfo.setOsVersion(android.os.Build.VERSION.RELEASE);
        deviceInfo.setPhone(phone);
        deviceInfo.setTable(table);
        deviceInfo.setPhoneNumber(phoneNumber);
        deviceInfo.setSimSerial(simSerial);
        return deviceInfo;
    }

    /**
     * 物理地址
     */
    private static String getMacAddress(Context context) {
        String macAddress = "";
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) {
            macAddress = info.getMacAddress();
            if (macAddress != null) {
                macAddress.replace(":", "");
            }
        }
        return macAddress;
    }

    public static boolean isTable(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

}
