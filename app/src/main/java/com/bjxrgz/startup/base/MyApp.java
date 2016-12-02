package com.bjxrgz.startup.base;

import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.ShareManager;
import com.bjxrgz.startup.utils.LogUtils;

import butterknife.ButterKnife;

public class MyApp extends BaseApp {

    public static final boolean DEBUG = true; // 测试模式(上线为false)
    public static final boolean LOG = true; // 打印日志(上线为false)

    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.initApp(LOG); // 打印
        ButterKnife.setDebug(LOG); // 注解
        PushManager.initAPP(this, LOG); // 推送
        ShareManager.initApp(this, LOG); // 分享/授权
    }

}
