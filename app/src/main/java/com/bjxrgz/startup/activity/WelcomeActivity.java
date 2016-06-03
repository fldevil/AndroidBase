package com.bjxrgz.startup.activity;

import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;
import com.umeng.message.PushAgent;

import org.xutils.view.annotation.ContentView;
/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 欢迎界面，启动界面
 */
@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity<WelcomeActivity> {

    @Override
    protected void create(Bundle savedInstanceState) {
        // 开启推送服务
        // String device_token = UmengRegistrar.getRegistrationId(this);
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable();

    }

    @Override
    protected void destroy() {

    }

}
