package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.UserManager;
import com.bjxrgz.startup.manager.XUtilsManager;
import com.bjxrgz.startup.utils.DeviceUtils;
import com.bjxrgz.startup.utils.LogUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApp extends Application {

    public static final boolean IS_RELEASE = false; // 上线为true

    public static final boolean IS_LOG = true; // 上线为false

    public static final String LOG_TAG = "start_up"; // LogTag

    public static MyApp instance;  // 当前实例

    public final static ExecutorService threadPool = Executors.newCachedThreadPool(); // 线程池

    public final static Handler mainHandler = new Handler(Looper.getMainLooper()); // 主线程handler

    private List<Activity> activities = new LinkedList<>(); // 所有已启动的Activity

    public static String APP_VERSION_NAME = "1.0"; // APP版本名

    public static int APP_VERSION_CODE = 1;  // APP版本名

    public static String DEVICE_ID; // 设备id

    public static String DEVICE_TOKEN;  // 友盟 device token

    public static final String PLATFORM = "Android"; // 设备类型

    public static final String OS_VERSION = android.os.Build.VERSION.RELEASE; // Android系统版本

    public static String STORE_DIR; // sd卡目录中app的资源目录
    public static String LOG_FILE; // 日志文件

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtils.initApp(LOG_TAG, IS_LOG); // 日志初始化
        PushManager.initAPP(this); // 推送初始化
        XUtilsManager.initApp(this, MyApp.IS_LOG); // xUtils 初始化
        UserManager.initApp(this); // 初始化preference

        APP_VERSION_NAME = DeviceUtils.getVersionName(this);
        APP_VERSION_CODE = DeviceUtils.getVersionCode(this);
        DEVICE_ID = DeviceUtils.getDeviceId(this);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            STORE_DIR = Environment.getExternalStorageDirectory() + "/" + LOG_TAG + "/";
        } else {
            STORE_DIR = Environment.getRootDirectory() + "/" + LOG_TAG + "/";
        }
        LOG_FILE = STORE_DIR + "log/log.txt";
        // 监听所有activity的生命周期 ,可撤销
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtils.d(activity.getClass().getSimpleName());
                addActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtils.d(activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtils.d(activity.getClass().getSimpleName());
                removeActivity(activity);
            }
        });

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
    }

    /**
     * activity在onCreate是调用此方法
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * activity在onDestroy是调用此方法
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有activity，应用与一键退出
     */
    public void closeActivities() {
        for (Activity activity : activities) {
            if (activity != null)
                activity.finish();
        }
    }

    /**
     * 严核模式,允许在主线程进行网络操作,打印存在的隐患
     */
    private void initStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .penaltyDialog()
                .detectNetwork()
                .penaltyLog()
                .build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

}
