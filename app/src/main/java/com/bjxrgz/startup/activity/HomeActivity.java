package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.manager.FileManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.MediaUtils;
import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
public class HomeActivity extends BaseViewActivity<HomeActivity> {

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


    }

    @Override
    protected void initData() {
    }

}
