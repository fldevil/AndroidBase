package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.UserManager;
import com.bjxrgz.startup.manager.XUtilsManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.AppUtils;
import com.bjxrgz.startup.utils.DeviceUtils;
import com.bjxrgz.startup.utils.LogUtils;

import butterknife.ButterKnife;

public class MyApp extends Application {

    public static final boolean IS_RELEASE = false; // 上线为true

    public static final boolean IS_LOG = true; // 上线为false

    public static MyApp instance;  // 当前实例

    public AppUtils.AppInfo appInfo; // app信息

    public DeviceUtils.DeviceInfo deviceInfo; // device信息

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        appInfo = AppUtils.getAppInfo(instance);
        deviceInfo = DeviceUtils.getDeviceInfo(instance);

        LogUtils.initApp(IS_LOG); // 日志初始化
        ButterKnife.setDebug(IS_LOG); // 懒人框架打印
        UserManager.initApp(this); // 初始化preference
        PushManager.initAPP(this, IS_LOG); // 推送初始化
        XUtilsManager.initApp(this, MyApp.IS_LOG); // xUtils 初始化

        initListener();
    }

    private void initListener() {
        // 监听当前app的内存 ,可撤销
        registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int level) {
                LogUtils.d("杀死一个进程以求更多内存(level) ---> " + level);
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                LogUtils.d("配置发生变化");
            }

            @Override
            public void onLowMemory() {
                LogUtils.e("内存不足,清理内存以获取更多内存");
            }
        });
        // 监听所有activity,可撤销
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                LogUtils.d(activity.getClass().getSimpleName());
                ActivityUtils.addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
//                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
//                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
//                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
//                LogUtils.d(activity.getClass().getSimpleName());
                ActivityUtils.removeActivity(activity);
            }
        });
    }

}
