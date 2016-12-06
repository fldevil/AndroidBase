package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.NetUtils;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Activity的基类
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    protected AppCompatActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected String tag = "BaseActivity";

    private Unbinder unbinder;
    protected ProgressDialog loading;
    protected View rootView;

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

    protected abstract void initObject(Bundle savedInstanceState);

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        tag = getCls();
        ActivityUtils.initCreate(this);
        super.onCreate(savedInstanceState);
        mActivity = this; // 实例
        mFragmentManager = getSupportFragmentManager();
        loading = ViewManager.createLoading(this);
        initObject(savedInstanceState);
        initView(savedInstanceState);
    }

    /* setContentView()或者addContentView()方法执行完毕时就会调用该方法
     可以在这里initData,但不能initListener,view只是加载出来，还没有实例化 */
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

    /* 获取当前类的 */
    @SuppressWarnings("unchecked")
    private String getCls() {
        Class<T> cls = (Class<T>) (((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);
        return cls.getSimpleName();
    }

}
