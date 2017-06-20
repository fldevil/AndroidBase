package com.user.project.func;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bjxrgz.base.BaseApp;
import com.bjxrgz.base.utils.LogUtil;

/**
 * Created by Fan on 2017/5/23.
 * 定位
 */

public class LocationApp {

    private LocationListener locationListener;

    public interface LocationListener {
        void onLocationSuccess(AMapLocation aMapLocation);
    }

    public LocationApp(LocationListener locationListener) {
        init();
    }

    private void init() {
        AMapLocationClient aMapLocationClient = new AMapLocationClient(BaseApp.getInstance());
        aMapLocationClient.setLocationListener(aMapLocationListener);
        aMapLocationClient.setLocationOption(getClientOption());
        aMapLocationClient.startLocation();
    }

    /**
     * 定位配置对象
     */
    private AMapLocationClientOption getClientOption() {
        //声明mLocationOption对象,初始化定位参数
        AMapLocationClientOption clientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会。
        if (clientOption.isOnceLocationLatest()) {
            clientOption.setOnceLocationLatest(true);
        }
        //设置是否返回地址信息（默认返回地址信息）
        clientOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        clientOption.setMockEnable(false);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        //设置是否只定位一次,默认为false
        clientOption.setOnceLocation(true);
        //设置定位间隔,单位毫秒,默认为2000ms
        clientOption.setInterval(10000);
        return clientOption;
    }

    /**
     * 定位监听
     */
    private AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    LogUtil.i(aMapLocation.toStr());
                    locationListener.onLocationSuccess(aMapLocation);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    LogUtil.e("location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
}
