package com.bjxrgz.base.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.bjxrgz.base.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by gg on 2017/3/24.
 * 定位相关工具类
 */
public class LocationUtil {
    private static LocationUtil instance;
    private static OnLocationChangeListener mListener;
    private static MyLocationListener myLocationListener;
    // info
    private double longitude; // 经度
    private double latitude; // 纬度
    private String address; // 详细地址
    private String province; // 省信息
    private String city; // 城市信息
    private String district; // 区

    public static LocationUtil get() {
        if (instance == null) {
            synchronized (LocationUtil.class) {
                if (instance == null) {
                    instance = new LocationUtil();
                }
            }
        }
        return instance;
    }

    public String getAddress() {
        if (StringUtil.isEmpty(address)) {
            address = "";
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        if (StringUtil.isEmpty(district)) {
            district = "";
        }
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        if (StringUtil.isEmpty(province)) {
            province = "";
        }
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        if (StringUtil.isEmpty(city)) {
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

    /**
     * 判断Gps是否可用
     */
    public static boolean isGpsEnabled() {
        return ManagerUtil.getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断定位是否可用
     */
    public static boolean isLocationEnabled() {
        LocationManager lm = ManagerUtil.getLocationManager();
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 注册
     * <p>使用完记得调用{@link #unregister()}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>}</p>
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>}</p>
     * <p>如果{@code minDistance}为0，则通过{@code minTime}来定时更新；</p>
     * <p>{@code minDistance}不为0，则以{@code minDistance}为准；</p>
     * <p>两者都为0，则随时刷新。</p>
     *
     * @param minTime     位置信息更新周期（单位：毫秒）
     * @param minDistance 位置变化最小距离：当位置距离变化超过此值时，将更新位置信息（单位：米）
     * @param listener    位置刷新的回调接口
     * @return {@code true}: 初始化成功<br>{@code false}: 初始化失败
     */
    public boolean register(Activity activity, long minTime, long minDistance, OnLocationChangeListener listener) {
        if (listener == null) return false;
        mListener = listener;
        if (!isLocationEnabled()) {
            ToastUtil.showShortToast(R.string.cannot_location_please_open_service);
            return false;
        }
        LocationManager mLocationManager = ManagerUtil.getLocationManager();
        String provider = mLocationManager.getBestProvider(getCriteria(), true);
        // 权限检查
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // 再次请求
            PermissionUtil.requestLocation(activity, null);
            return true;
        }
        Location location = mLocationManager.getLastKnownLocation(provider);
        if (location != null) {
            setNativeLocation(location);
            listener.getLastKnownLocation(location);
        }
        if (myLocationListener == null) {
            myLocationListener = new MyLocationListener();
        }
        mLocationManager.requestLocationUpdates(provider,
                minTime, minDistance, myLocationListener);
        return true;
    }


    /**
     * 注销
     */
    public static void unregister() {
        if (myLocationListener != null) {
            ManagerUtil.getLocationManager().removeUpdates(myLocationListener);
            myLocationListener = null;
        }
    }

    /**
     * 设置定位参数
     *
     * @return {@link Criteria}
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        //设置是否要求速度
        criteria.setSpeedRequired(false);
        // 设置是否允许运营商收费
        criteria.setCostAllowed(false);
        //设置是否需要方位信息
        criteria.setBearingRequired(false);
        //设置是否需要海拔信息
        criteria.setAltitudeRequired(false);
        // 设置对电源的需求
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        return criteria;
    }

    /* 设置本地缓存地址信息 */
    private void setNativeLocation(Location location) {
        if (location == null) return;
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * 根据经纬度获取地理位置
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return {@link Address}
     */
    public static Address getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据经纬度获取所在国家
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在国家
     */
    public static String getCountryName(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getCountryName();
    }

    /**
     * 根据经纬度获取所在地
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在地
     */
    public static String getLocality(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getLocality();
    }

    /**
     * 根据经纬度获取所在街道
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @return 所在街道
     */
    public String getStreet(Context context, double latitude, double longitude) {
        Address address = getAddress(context, latitude, longitude);
        return address == null ? "unknown" : address.getAddressLine(0);
    }

    private class MyLocationListener implements LocationListener {
        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        @Override
        public void onLocationChanged(Location location) {
            if (mListener != null) {
                setNativeLocation(location);
                mListener.onLocationChanged(location);
            }
        }

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (mListener != null) {
                mListener.onStatusChanged(provider, status, extras);
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    LogUtil.d("onStatusChanged", "当前GPS状态为可见状态");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    LogUtil.d("onStatusChanged", "当前GPS状态为服务区外状态");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    LogUtil.d("onStatusChanged", "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * provider被enable时触发此函数，比如GPS被打开
         */
        @Override
        public void onProviderEnabled(String provider) {
        }

        /**
         * provider被disable时触发此函数，比如GPS被关闭
         */
        @Override
        public void onProviderDisabled(String provider) {
        }
    }

    public interface OnLocationChangeListener {

        /**
         * 获取最后一次保留的坐标
         *
         * @param location 坐标
         */
        void getLastKnownLocation(Location location);

        /**
         * 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
         *
         * @param location 坐标
         */
        void onLocationChanged(Location location);

        /**
         * provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
         *
         * @param provider 提供者
         * @param status   状态
         * @param extras   provider可选包
         */
        void onStatusChanged(String provider, int status, Bundle extras);//位置状态发生改变
    }
}