package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseActivity;

import org.xutils.view.annotation.ContentView;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 广告界面
 */
@ContentView(R.layout.activity_advert)
public class AdvertActivity extends BaseActivity<AdvertActivity> {

    public static void goActivity(Activity activity) {
        Intent intent = new Intent(activity, AdvertActivity.class);
        startActivity(activity, intent, true);
    }

    @Override
    protected void create(Bundle savedInstanceState) {

    }

    @Override
    protected void destroy() {

    }

}