package com.user.project.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bjxrgz.base.BaseApp;
import com.bjxrgz.base.utils.ScreenUtil;
import com.user.project.R;
import com.user.project.base.BaseActivity;

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
        ScreenUtil.hideStatusBar(mActivity);
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
        BaseApp.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.goActivity(mActivity);
            }
        }, 1000);
        // 立刻关闭当前页面会出现空白缝隙
        BaseApp.getInstance().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.finish();
            }
        }, 3000);
    }

}
