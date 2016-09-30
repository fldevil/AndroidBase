package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity<HomeActivity> {

    public static void goActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(activity, intent);
    }

    @Override
    protected void create(Bundle savedInstanceState) {

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
}
