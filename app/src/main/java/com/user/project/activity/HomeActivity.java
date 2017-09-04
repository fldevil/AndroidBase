package com.user.project.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;

import com.bjxrgz.base.utils.ActivityUtil;
import com.bjxrgz.base.utils.ToastUtil;
import com.user.project.R;
import com.user.project.base.BaseActivity;
import com.user.project.func.UpdateApp;
import com.user.project.http.BaseObserver;
import com.user.project.http.HttpManager;
import com.user.project.utils.ViewUtils;

import java.util.Date;

import okhttp3.ResponseBody;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * describe 主界面
 */
public class HomeActivity extends BaseActivity {

    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, HomeActivity.class);
        ActivityUtil.startActivity(from, intent);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        ViewUtils.initTop(mActivity, "主页面");
    }

    @Override
    protected void initData() {
        new UpdateApp(mActivity,true);
    }

    private Long lastExitTime = 0L; //最后一次退出时间

    /* 手机返回键 */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Long nowTime = new Date().getTime();

            if (nowTime - lastExitTime > 2000) { // 第一次按
                ToastUtil.showShortToast(R.string.press_again_exit);
            } else { // 返回键连按两次
                System.exit(0); // 真正退出程序
            }
            lastExitTime = nowTime;
            return true;
        }
        return false;
    }

}
