package com.bjxrgz.startup.base;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bjxrgz.startup.manager.HttpManager;
import com.bjxrgz.startup.manager.PermManager;
import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.ShareManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.AppUtils;
import com.bjxrgz.startup.utils.DeviceUtils;
import com.bjxrgz.startup.utils.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;

public class MyApp extends MultiDexApplication {
    public static final boolean DEBUG = true; // 测试模式(上线为false)
    public static final boolean LOG = true; // 打印日志(上线为false)

    private static MyApp instance;  // MyApp实例

    private Handler mainHandler; // 主线程handler
    private ExecutorService threadPool; // 缓冲线程池
    private AppUtils.AppInfo appInfo; // app信息
    private DeviceUtils.DeviceInfo deviceInfo; // device信息

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initListener();

        ButterKnife.setDebug(LOG); // 注解
        LogUtils.initApp(); // 打印
        PushManager.initAPP(); // 推送
        ShareManager.initApp(); // 分享/授权
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // 大项目需要分包
    }

    public static MyApp get() {
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
        if (null == appInfo) {
            PermManager.requestApp(instance, new PermManager.PermissionListener() {
                @Override
                public void onAgree() {
                    appInfo = AppUtils.getAppInfo(instance);
                }
            });
        }
        return appInfo;
    }

    public DeviceUtils.DeviceInfo getDeviceInfo() {
        if (null == deviceInfo) { // 需要动态权限
            PermManager.requestDevice(instance, new PermManager.PermissionListener() {
                @Override
                public void onAgree() {
                    deviceInfo = DeviceUtils.getDeviceInfo();
                }
            });
        }
        return deviceInfo;
    }

    private void initListener() {
        // 监听当前app的内存 和 配置,可撤销
        registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onTrimMemory(int level) {
                LogUtils.d("杀死一个进程以求更多内存(level) ---> " + level);
            }

            @Override
            public void onLowMemory() {
                LogUtils.e("内存不足,清理内存以获取更多内存");
            }

            @Override
            public void onConfigurationChanged(Configuration newConfig) {
                LogUtils.d("配置发生变化");
                StringBuilder status = new StringBuilder();
                Configuration cfg = getResources().getConfiguration();
                status.append("fontScale:").append(cfg.fontScale).append("\n");
                status.append("hardKeyboardHidden:").append(cfg.hardKeyboardHidden).append("\n");
                status.append("keyboard:").append(cfg.keyboard).append("\n");
                status.append("keyboardHidden:").append(cfg.keyboardHidden).append("\n");
                status.append("locale:").append(cfg.locale).append("\n");
                status.append("mcc:").append(cfg.mcc).append("\n");
                status.append("mnc:").append(cfg.mnc).append("\n");
                status.append("navigation:").append(cfg.navigation).append("\n");
                status.append("navigationHidden:").append(cfg.navigationHidden).append("\n");
                status.append("orientation:").append(cfg.orientation).append("\n");
                status.append("screenHeightDp:").append(cfg.screenHeightDp).append("\n");
                status.append("screenWidthDp:").append(cfg.screenWidthDp).append("\n");
                status.append("screenLayout:").append(cfg.screenLayout).append("\n");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("densityDpi:").append(cfg.densityDpi).append("\n");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("smallestScreenWidthDp:").append(cfg.densityDpi).append("\n");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("touchscreen:").append(cfg.densityDpi).append("\n");
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    status.append("uiMode:").append(cfg.densityDpi).append("\n");
                }
                LogUtils.d(status.toString());
            }
        });
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
