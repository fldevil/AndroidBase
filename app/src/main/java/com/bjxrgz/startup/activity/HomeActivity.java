package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.manager.APIManager;
import com.bjxrgz.startup.manager.XUtilsManager;
import com.bjxrgz.startup.utils.FragmentUtils;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe 主界面
 */
public class HomeActivity extends BaseViewActivity<HomeActivity> {

    public static void goActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(activity, intent);
    }

    @Override
    protected void initObject(Bundle savedInstanceState) {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initContentView(R.layout.activity_home);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        getData();
    }

    @Override
    protected void refreshData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 友盟统计
    }

    private void getData() {
        APIManager.demo(mActivity, new XUtilsManager.Callback() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
