package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.ActivityUtils;

import java.util.Date;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
public class HomeActivity extends BaseActivity<HomeActivity> {

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, HomeActivity.class);
        ActivityUtils.startActivity(from, intent);
    }

    @Override
    protected int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        ViewManager.initTop(mActivity, "主页面");

    }

    @Override
    protected void initData() {
    }


    private Long lastExitTime = 0L; //最后一次退出时间

    /* 手机返回键 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Long nowTime = new Date().getTime();

            if (nowTime - lastExitTime > 2000) { // 第一次按
                ViewManager.toast("再按一次退出");
            } else { // 返回键连按两次
                System.exit(0); // 真正退出程序
            }
            lastExitTime = nowTime;
        }
        return true;
    }

}
