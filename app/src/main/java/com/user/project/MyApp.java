package com.user.project;

import com.bjxrgz.base.base.BaseApp;

public class MyApp extends BaseApp {
    public static boolean DEBUG = true; // 测试模式(上线为false)

    @Override
    public void onCreate() {
        super.onCreate();

//        PushUtils.initAPP(LOG); // 推送
//        ShareUtils.initApp(LOG); // 分享/授权
    }

}
