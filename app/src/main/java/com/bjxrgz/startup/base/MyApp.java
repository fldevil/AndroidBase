package com.bjxrgz.startup.base;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bjxrgz.startup.manager.PermissionsManager;
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

    /* 大项目需要分包 */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /* 初始化 */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        LogUtils.initApp(LOG); // 打印
        ButterKnife.setDebug(LOG); // 注解
        PushManager.initAPP(this, LOG); // 推送
        ShareManager.initApp(this, LOG); // 分享/授权

        initListener();
    }

    public static MyApp getInstance() {
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
            PermissionsManager.request(instance, new PermissionsManager.PermissionListener() {
                @Override
                public void onAgree() {
                    appInfo = AppUtils.getAppInfo(instance);
                }

                @Override
                public void onRefuse() {
//                    getAppInfo(); // 拒绝，不再提醒，就是死循环
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        }
        return appInfo;
    }

    public DeviceUtils.DeviceInfo getDeviceInfo() {
        if (null == deviceInfo) { // 需要动态权限
            PermissionsManager.request(instance, new PermissionsManager.PermissionListener() {
                @Override
                public void onAgree() {
                    deviceInfo = DeviceUtils.getDeviceInfo(instance);
                }

                @Override
                public void onRefuse() {
//                    getDeviceInfo(); // 拒绝，不再提醒，就是死循环
                }
            }, Manifest.permission.READ_PHONE_STATE);
        }
        return deviceInfo;
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
        // 监听所有activity生命周期,可撤销
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
