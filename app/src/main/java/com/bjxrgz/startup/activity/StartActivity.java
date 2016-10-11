package com.bjxrgz.startup.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.manager.PushManager;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe 欢迎界面，启动界面
 */
public class StartActivity extends BaseViewActivity<StartActivity> {

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Override
    protected void initObject(Bundle savedInstanceState) {
        PushManager.setPush(true); // 推送开关
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        initContentView(R.layout.activity_start);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        goHome(); // 跳转主页
    }

    @Override
    protected void refreshData() {
    }

    private void goHome() {
        MyApp.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);
            }
        }, 1500);
        // 立刻关闭当前页面会出现空白缝隙
        MyApp.mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        }, 3000);
    }
}
