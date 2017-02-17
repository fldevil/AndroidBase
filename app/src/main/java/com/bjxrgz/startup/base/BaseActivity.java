package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.FragmentUtils;
import com.bjxrgz.startup.utils.NetUtils;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Activity的基类
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    public AppCompatActivity mActivity;
    public FragmentManager mFragmentManager;
    public String tag = "BaseActivity";
    public ProgressDialog loading;
    public Intent mIntent;
    private Unbinder unbinder;

    /* 子类重写类似方法 实现跳转 */
    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, BaseActivity.class);
        // intent.putData();
        ActivityUtils.startActivity(from, intent);
    }

    /* 初始layout(setContent之前调用) */
    protected abstract int initLayout(Bundle savedInstanceState);

    /* 实例化View */
    protected abstract void initView();

    /* 初始Data */
    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = getCls();
        mActivity = this;
        ActivityUtils.initSuperCreate(mActivity);
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        loading = ViewManager.createLoading(this); // Fragment也调用父Activity的Loading
        mIntent = getIntent();
        setContentView(initLayout(savedInstanceState)); // 这之后 页面才会加载出来
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
        NetUtils.isAvailable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PushManager.analysisOnResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PushManager.analysisOnPause(this);
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

    /* 菜单事件 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: // 返回键
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!FragmentUtils.goBack(mFragmentManager)) {
            super.onBackPressed();
        }
    }

    /* 获取当前类的 */
    @SuppressWarnings("unchecked")
    private String getCls() {
        Class<T> cls = (Class<T>) (((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);
        return cls.getSimpleName();
    }

}
