package com.bjxrgz.start.base;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Build;

import com.bjxrgz.base.base.BaseApp;
import com.bjxrgz.base.utils.AppUtils;
import com.bjxrgz.base.utils.DeviceUtils;
import com.bjxrgz.start.utils.LogUtils;
import com.bjxrgz.start.utils.PermUtils;

import butterknife.ButterKnife;

public class MyApp extends BaseApp {
    public static final boolean DEBUG = true; // 测试模式(上线为false)
    private static final boolean LOG = true; // 打印日志(上线为false)

    @Override
    public void onCreate() {
        super.onCreate();
        initListener();

        ButterKnife.setDebug(LOG); // 注解
        LogUtils.initApp(LOG); // 打印
//        PushUtils.initAPP(LOG); // 推送
//        ShareUtils.initApp(LOG); // 分享/授权
    }

    @Override
    public AppUtils.AppInfo getAppInfo() {
        if (null == appInfo) {
            PermUtils.requestApp(instance, new PermUtils.PermissionListener() {
                @Override
                public void onAgree() {
                    appInfo = MyApp.super.getAppInfo();
                }
            });
        }
        return appInfo;
    }

    @Override
    public DeviceUtils.DeviceInfo getDeviceInfo() {
        if (null == deviceInfo) { // 需要动态权限
            PermUtils.requestDevice(instance, new PermUtils.PermissionListener() {
                @Override
                public void onAgree() {
                    deviceInfo = MyApp.super.getDeviceInfo();
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

    }

}
