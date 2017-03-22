package com.bjxrgz.base.base;

import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.LogUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;

public class BaseApp extends MultiDexApplication {
    protected static BaseApp instance;  // MyApp实例
    protected Handler mainHandler; // 主线程handler
    protected ExecutorService threadPool; // 缓冲线程池

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this); // 大项目需要分包
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initListener();
        ActivityUtils.initApp();
        ButterKnife.setDebug(true); // 注解
        LogUtils.initApp(); // 打印
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

    }

}
