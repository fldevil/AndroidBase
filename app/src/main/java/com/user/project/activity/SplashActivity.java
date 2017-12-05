package com.user.project.activity;

import android.widget.ImageView;

import com.bjxrgz.base.utils.ScreenUtil;
import com.user.project.R;
import com.user.project.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * @author Fan
 * 启动界面
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.ivWelcome)
    ImageView ivWelcome;

    @Override
    protected int initLayout() {
        ScreenUtil.hideStatusBar(mActivity);
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
    }


    @Override
    protected void initData() {
        goHome();
    }

    /* 跳转主页 */
    private void goHome() {
        Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> HomeActivity.goActivity(mActivity));

        // 立刻关闭当前页面会出现空白缝隙
        Observable.timer(3,TimeUnit.SECONDS).subscribe(aLong -> mActivity.finish());
    }

}
