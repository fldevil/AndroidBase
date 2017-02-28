package com.bjxrgz.base.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.AppUtils;
import com.bjxrgz.base.utils.DeviceUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseApp extends MultiDexApplication {
    protected static BaseApp instance;  // MyApp实例
    protected Handler mainHandler; // 主线程handler
    protected ExecutorService threadPool; // 缓冲线程池
    protected AppUtils.AppInfo appInfo; // app信息
    protected DeviceUtils.DeviceInfo deviceInfo; // device信息

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initActivityLife();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // 大项目需要分包
    }

    public static BaseApp get() {
        return instance;
    }

    public Handler getHandler() {
        if (null == mainHandler) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    public ExecutorService getThread() {
        if (null == threadPool) {
            threadPool = Executors.newCachedThreadPool();
        }
        return threadPool;
    }

    public AppUtils.AppInfo getAppInfo() {
        return AppUtils.getAppInfo();
    }

    public DeviceUtils.DeviceInfo getDeviceInfo() {
        return DeviceUtils.getDeviceInfo();
    }

    private void initActivityLife() {
        // 监听所有activity生命周期,可撤销
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityUtils.addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                ActivityUtils.removeActivity(activity);
            }
        });
    }
}
