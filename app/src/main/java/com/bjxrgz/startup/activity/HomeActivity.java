package com.bjxrgz.startup.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.view.MyWebView;

import butterknife.BindView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
@RuntimePermissions
public class HomeActivity extends BaseViewActivity<HomeActivity> {

    @BindView(R.id.mwv)
    MyWebView mwv;

    public static void goActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        ActivityUtils.startActivity(activity, intent);
    }

    @Override
    protected void initObject(Bundle savedInstanceState) {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initContentView(R.layout.activity_home);
        mwv.load("http://www.hao123.com");

    }

    @Override
    protected void initData() {
    }

    @Override
    public void onBackPressed() {
        if (mwv.canFinish()) {
            super.onBackPressed();
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {

    }

    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {

    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {

    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {

    }
}
