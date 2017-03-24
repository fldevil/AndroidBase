package com.bjxrgz.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;

import com.bjxrgz.base.base.BaseActivity;
import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.ToastUtils;
import com.bjxrgz.project.R;
import com.bjxrgz.project.utils.ViewUtils;

import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 主界面
 */
public class HomeActivity extends BaseActivity<HomeActivity> {

    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.img)
    ImageView img;

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
        ViewUtils.initTop(mActivity, "主页面");

    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.btn)
    public void onViewClicked() {

    }

    private Long lastExitTime = 0L; //最后一次退出时间

    /* 手机返回键 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Long nowTime = new Date().getTime();

            if (nowTime - lastExitTime > 2000) { // 第一次按
                ToastUtils.toast(R.string.press_again_exit);
            } else { // 返回键连按两次
                System.exit(0); // 真正退出程序
            }
            lastExitTime = nowTime;
        }
        return true;
    }

}
