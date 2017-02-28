package com.bjxrgz.project.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bjxrgz.project.R;
import com.bjxrgz.base.utils.ScreenUtils;
import com.bjxrgz.start.base.BaseActivity;
import com.bjxrgz.start.base.MyApp;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 启动界面
 */
public class WelcomeActivity extends BaseActivity<WelcomeActivity> {

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Override
    protected int initLayout(Bundle savedInstanceState) {
        ScreenUtils.hideStatusBar(mActivity);
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
    }


    @Override
    protected void initData() {
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
