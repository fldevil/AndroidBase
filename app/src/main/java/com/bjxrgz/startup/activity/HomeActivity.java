package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.domain.Home;
import com.bjxrgz.startup.manager.APIManager;
import com.bjxrgz.startup.manager.HttpManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
public class HomeActivity extends BaseViewActivity<HomeActivity> {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    private File result;

    @OnClick(R.id.image)
    public void onClick() {
    }

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
        APIManager service = HttpManager.getService(HttpManager.getEmptyHeader());
        HttpManager.enqueue(service.getHome2(), new HttpManager.CallBack<Home>() {
            @Override
            public void onSuccess(Home result) {

            }

            @Override
            public void onFailure() {

            }
        });
    }

}
