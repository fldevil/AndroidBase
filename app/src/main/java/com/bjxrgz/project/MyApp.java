package com.bjxrgz.project;

import com.bjxrgz.startup.base.BaseApp;
import com.bjxrgz.startup.utils.LogUtils;

import butterknife.ButterKnife;

public class MyApp extends BaseApp {
    public static final boolean DEBUG = true; // 测试模式(上线为false)
    private static final boolean LOG = true; // 打印日志(上线为false)

    @Override
    public void onCreate() {
        super.onCreate();

        ButterKnife.setDebug(LOG); // 注解
        LogUtils.initApp(LOG); // 打印
//        PushUtils.initAPP(LOG); // 推送
//        ShareUtils.initApp(LOG); // 分享/授权
    }

}
