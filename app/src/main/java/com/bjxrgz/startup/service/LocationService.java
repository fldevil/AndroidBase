package com.bjxrgz.startup.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.manager.MapManager;
import com.bjxrgz.startup.utils.DeviceUtils;

public class LocationService extends Service {

    public static void goService(Context from) {
        Intent intent = new Intent(from, LocationService.class);
        from.startService(intent);
    }

    @Override
    public void onCreate() {
        MapManager.get().initLocation(this);

        AMapLocationListener locationListener = MapManager.get().getAMapLocationListener(new MapManager.LocationCallBack() {
            @Override
            public void onSuccess(AMapLocation aMapLocation) {
                double longitude = aMapLocation.getLongitude();
                double latitude = aMapLocation.getLatitude();
                DeviceUtils.DeviceInfo deviceInfo = MyApp.get().getDeviceInfo();
                deviceInfo.setLongitude(longitude);
                deviceInfo.setLatitude(latitude);
            }

            @Override
            public void onFailed(AMapLocation aMapLocation) {
                int errorCode = aMapLocation.getErrorCode();
            }
        });
        MapManager.get().startLocation(locationListener);
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