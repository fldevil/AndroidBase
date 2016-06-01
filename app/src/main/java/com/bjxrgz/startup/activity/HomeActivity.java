package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

@ContentView(R.layout.activity_home)
public class HomeActivity extends BaseActivity<HomeActivity> {

    public static void goActivity(Activity activity) {

        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(activity, intent, true);
    }

    @Override
    protected void create(Bundle savedInstanceState) {

    }

    @Override
    protected void destroy() {

    }
}
