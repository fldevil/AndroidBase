package com.bjxrgz.startup.activity;

import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.manager.PushManager;

import org.xutils.view.annotation.ContentView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 欢迎界面，启动界面
 */
@ContentView(R.layout.activity_start)
public class StartActivity extends BaseActivity<StartActivity> {

    @Override
    protected void create(Bundle savedInstanceState) {
        PushManager.setPush(true);

        goHome();
    }

    private void goHome() {
        MyApp.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);

            }
        }, 1500);
        MyApp.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        }, 3000);
    }

}
