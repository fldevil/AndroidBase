package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.ActivityUtils;

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
    protected void initObject(Bundle savedInstanceState) {

    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
        ViewManager.initTop(mActivity, "主页面");

    }

    @Override
    protected void initData() {
    }

}
