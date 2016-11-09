package com.bjxrgz.startup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.service.UpdateService;
import com.bjxrgz.startup.utils.ScreenUtils;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe 启动界面
 */
public class StartActivity extends BaseViewActivity<StartActivity> {

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Override
    protected void initObject(Bundle savedInstanceState) {
        PushManager.setEnable(true); // 推送开关
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        ScreenUtils.hideStatusBar(mActivity);
        initContentView(R.layout.activity_start);
    }

    @Override
    protected void initData() {
//        update();
        goHome();
    }

    /* 检查更新 */
    private void update() {
        Intent intent = new Intent(this, UpdateService.class);
        startService(intent);
    }

    /* 跳转主页 */
    private void goHome() {
        MyApp.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);
            }
        }, 1500);
        // 立刻关闭当前页面会出现空白缝隙
        MyApp.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        }, 3000);
    }

}
