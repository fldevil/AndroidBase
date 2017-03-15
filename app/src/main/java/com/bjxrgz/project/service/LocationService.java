package com.bjxrgz.project.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bjxrgz.base.utils.DeviceUtils;
import com.bjxrgz.base.utils.PermUtils;
import com.bjxrgz.project.utils.MapUtils;

public class LocationService extends Service {

    public static void goService(final Context from) {
        PermUtils.requestMap(from, new PermUtils.PermissionListener() {
            @Override
            public void onAgree() {
                Intent intent = new Intent(from, LocationService.class);
                from.startService(intent);
            }
        });
    }

    @Override
    public void onCreate() {
        MapUtils.get().initLocation(this);

        AMapLocationListener locationListener = MapUtils.get().getAMapLocationListener(new MapUtils.LocationCallBack() {
            @Override
            public void onSuccess(AMapLocation aMapLocation) {
                double longitude = aMapLocation.getLongitude();
                double latitude = aMapLocation.getLatitude();
                String province = aMapLocation.getProvince();
                String city = aMapLocation.getCity();
                String district = aMapLocation.getDistrict();
                String address = aMapLocation.getAddress();

                DeviceUtils deviceUtils = DeviceUtils.get();
                deviceUtils.setLongitude(longitude);
                deviceUtils.setLatitude(latitude);
                deviceUtils.setProvince(province);
                deviceUtils.setCity(city);
                deviceUtils.setDistrict(district);
                deviceUtils.setAddress(address);
            }

            @Override
            public void onFailed(AMapLocation aMapLocation) {
                int errorCode = aMapLocation.getErrorCode();
            }
        });
        MapUtils.get().startLocation(locationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
