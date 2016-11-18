package com.bjxrgz.startup.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.manager.PermissionsManager;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.ActivityUtils;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
public class HomeActivity extends BaseViewActivity<HomeActivity> {

    @BindView(R.id.iv)
    ImageView iv;

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, HomeActivity.class);
        ActivityUtils.startActivity(from, intent);
    }

    @Override
    protected void initObject(Bundle savedInstanceState) {
        PermissionsManager.requestEach(this,null, Manifest.permission.READ_PHONE_STATE);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initContentView(R.layout.activity_home);

        ViewManager.initTopLeft(mActivity, "这里是");

        iv.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    protected void initData() {

    }
}
