package com.user.project;

import com.bjxrgz.base.BaseApp;
import com.bjxrgz.base.utils.ActivityUtil;
import com.bjxrgz.base.utils.AnalyUtil;
import com.user.project.utils.ScanUtils;

import butterknife.ButterKnife;

public class MyApp extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();

        ButterKnife.setDebug(DEBUG); // 注解
        ActivityUtil.initApp(this);  // activity全局生命周期
        AnalyUtil.initApp(this);  // 友盟统计

//        PushUtils.initAPP(); // 推送
//        ShareUtils.initApp(this); // 分享/授权
        ScanUtils.initApp(this);
    }
}
