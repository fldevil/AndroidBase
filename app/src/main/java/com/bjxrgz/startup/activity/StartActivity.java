package com.bjxrgz.startup.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.utils.ScreenUtils;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe 启动界面
 */
public class StartActivity extends BaseActivity<StartActivity> {

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Override
    protected int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {
        ScreenUtils.hideStatusBar(mActivity);
        setContentView(R.layout.activity_start);
    }

    @Override
    protected void initData() {
        PushManager.setEnable(true); // 推送开关
        // UpdateService.goService(mActivity); // 检查更新
        goHome();
    }

    /* 跳转主页 */
    private void goHome() {
        MyApp.get().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);
            }
        }, 1000);
        // 立刻关闭当前页面会出现空白缝隙
        MyApp.get().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        }, 3000);
    }

}
