package com.bjxrgz.startup.activity;

import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;
import com.bjxrgz.startup.base.MyApp;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import org.xutils.view.annotation.ContentView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe 欢迎界面，启动界面
 */
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity<WelcomeActivity> {

    @Override
    protected void create(Bundle savedInstanceState) {
        initPush();

    }


    @Override
    protected void destroy() {

    }

    private void initPush() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();
        MyApp.DEVICE_TOKEN = UmengRegistrar.getRegistrationId(this);
    }
}
