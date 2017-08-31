package com.user.project.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.bjxrgz.base.utils.ActivityUtil;
import com.bjxrgz.base.utils.AnalyUtil;
import com.bjxrgz.base.utils.DialogUtil;
import com.bjxrgz.base.utils.NetUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected BaseActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected Intent mIntent;
    protected Dialog mLoading;

    private Unbinder unbinder;

    /* 初始layout(setContent之前调用) */
    protected abstract int initLayout();

    /* 实例化View */
    protected abstract void initView();

    /* 初始Data */
    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.initSuperCreate(this);
        mActivity = this;
        mContext = getApplicationContext();
        mFragmentManager = getSupportFragmentManager();
        mLoading = DialogUtil.createLoading(this);
        mIntent = getIntent();
        setContentView(initLayout()); // 这之后 页面才会加载出来
    }

    /* setContentView()或addContentView()后调用,view只是加载出来，没有实例化.
     * 为了页面的加载速度，不要在setContentView里做过多的操作 */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder = ButterKnife.bind(this); // 每次setContentView之后都要bind一下
        initView(); // 二次setContentView之后控件是以前view的，所以要重新实例化一次
        initData(); // 二次setContentView的话，可以不用获取数据 只加载数据
    }

    @Override
    protected void onStart() {
        super.onStart();
        NetUtil.isAvailable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyUtil.analysisOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyUtil.analysisOnPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /* 触摸事件 */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) { // 点击屏幕空白区域隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }
}
