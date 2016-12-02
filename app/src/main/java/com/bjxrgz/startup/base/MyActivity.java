package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.NetUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Activity的基类
 */

public abstract class MyActivity<T> extends BaseActivity<T> {

    private Unbinder unbinder;
    protected ProgressDialog loading;
    protected View rootView;

    protected abstract void initObject(Bundle savedInstanceState);

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    /* 子类重写类似方法 实现跳转 */
    public static void goActivity(Activity from) {
        Intent intent = new Intent(from, BaseActivity.class);
        // intent.putData();
        ActivityUtils.startActivity(from, intent);
    }

    /* 重写setContentView 在initView()里调用 */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        rootView = LayoutInflater.from(this).inflate(layoutResID, null);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = ViewManager.createLoading(this);
        initObject(savedInstanceState);
        initView(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetUtils.isAvailable(this);
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
}
