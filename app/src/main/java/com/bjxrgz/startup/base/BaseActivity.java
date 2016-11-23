package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bjxrgz.startup.manager.PushManager;
import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.LogUtils;
import com.bjxrgz.startup.utils.NetUtils;

import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe Activity的基类
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    protected AppCompatActivity mActivity;
    protected FragmentManager mFragmentManager;
    protected ProgressDialog loading;
    protected boolean anim = true; // 跳转动画开关
    protected boolean log = false; // 是否打印生命周期
    protected String logTag = "BaseActivity";

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
        ButterKnife.bind(this);
    }

    protected abstract void initObject(Bundle savedInstanceState);

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    /**
     * ***********************************以下是生命周期***********************************
     * <p/>
     * 实例化控件，加载监听器
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logTag = getCls();
        ActivityUtils.initBaseCreate(this, anim);
        super.onCreate(savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
        mActivity = this; // 实例
        loading = ViewManager.createLoading(this);
        mFragmentManager = getSupportFragmentManager();
        initObject(savedInstanceState);
        initView(savedInstanceState);
    }

    /**
     * FragmentAttach的时候才会执行,之后会执行fragment的onCreate方法
     * 可以获取Fragment的引用，进行单方向通信
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 在window执行动画的时候activity不能draw，所以就有了下面的方法
     */
    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * setContentView()或者addContentView()方法执行完毕时就会调用该方法,不是onCreate
     * 可以在这里initData,但不能initListener,view只是加载出来，还没有实例化
     * <p/>
     * 尤其是换肤的时候，setTheme之后需要从新setContentView以下，
     * 所以控件需要重新实例化和加载监听器，这样可以直接调用create方法，
     * 如果里面有initData就不行了,但是每次换肤之后还是会执行onContentChanged
     * 所以需要接口里根据数据是不是null ,再来判断是否进行数据初始化
     */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        LogUtils.lifeCycle(log, logTag);
        initData();
    }

    /**
     * singTop模式下，在栈顶的activity启动本身的时候是不会再onCreate的，在这里接受
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * activity完全不可见--->可见时，从这里开始生命周期
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 前面不一定是onRestart
     */
    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 接受setResult回传的intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 权限处理回调
     *
     * @param requestCode  自己传的
     * @param permissions  对应的权限
     * @param grantResults grantResults[0] == PackageManager.PERMISSION_GRANTED 为拒绝
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * onCreate和onRestoreInstanceState是接受Bundle的
     * onSaveInstanceState是finish时保存传送Bundle的
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 当Activity彻底运行起来之后回调onPostCreate方法 也就是onCreate执行完后
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * activity部分不可见--->可见时，从这里开始生命周期
     */
    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.lifeCycle(log, logTag);
        PushManager.analysisOnResume(this);
        NetUtils.isAvailable(this);
    }

    /**
     * 有attach的fragment时，当fragment也Resume时被回调
     * 最好在这里执行已经存在的fragment的translate操作
     */
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * onResume执行完后执行
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * DecorView在这里才会有params,viewGroup在这里才能add
     * Window是WindowManager最顶层的视图，PhoneWindow是Window的唯一实现类
     * DecorView是window下的子视图,是所有应用窗口(Activity界面)的根View
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogUtils.lifeCycle(log, logTag);
        // View decorView = getWindow().getDecorView();
        // 控制DecorView的大小来控制activity的大小，可做窗口activity
        // setFinishOnTouchOutside(true);
    }

    /**
     * onCreateOptionsMenu：至调用一次，除非主动init
     * onPrepareOptionsMenu：每次显示Menu之前，都会去调用,不好用
     *
     * @return 捕获返回true, 返回false不会显示menu菜单，包括actionbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogUtils.lifeCycle(log, logTag);
        // 也引入menu的layout，也可动态add(记得每次先clear)
        // getMenuInflater().inflate(res,menu);
        // 内部调用onCreateOptionsMenu(Menu menu) ,用于动态变换menu选项
        // supportInvalidateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 完全可见--->部分遮挡
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.lifeCycle(log, logTag);
        PushManager.analysisOnPause(this);
    }

    /**
     * 当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用
     * recreate之后也会调用的，配合起来可以做主题更换功能
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 部分可见--->完全不可见
     */
    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * DecorView到这里没有params,viewGroup在这里之前remove
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 生命周期结束并不等于对象销毁，其他方法还是可以调用的
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * ***********************************以上是生命周期***********************************
     * <p/>
     * logTag获取
     */
    @SuppressWarnings("unchecked")
    private String getCls() {
        Class<T> cls = (Class<T>) (((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);
        return cls.getSimpleName();
    }

    /* 响应系统设置的事件 */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        StringBuilder status = new StringBuilder();
        Configuration cfg = getResources().getConfiguration();
        status.append("fontScale:").append(cfg.fontScale).append("\n");
        status.append("hardKeyboardHidden:").append(cfg.hardKeyboardHidden).append("\n");
        status.append("keyboard:").append(cfg.keyboard).append("\n");
        status.append("keyboardHidden:").append(cfg.keyboardHidden).append("\n");
        status.append("locale:").append(cfg.locale).append("\n");
        status.append("mcc:").append(cfg.mcc).append("\n");
        status.append("mnc:").append(cfg.mnc).append("\n");
        status.append("navigation:").append(cfg.navigation).append("\n");
        status.append("navigationHidden:").append(cfg.navigationHidden).append("\n");
        status.append("orientation:").append(cfg.orientation).append("\n");
        status.append("screenHeightDp:").append(cfg.screenHeightDp).append("\n");
        status.append("screenWidthDp:").append(cfg.screenWidthDp).append("\n");
        status.append("screenLayout:").append(cfg.screenLayout).append("\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            status.append("densityDpi:").append(cfg.densityDpi).append("\n");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            status.append("smallestScreenWidthDp:").append(cfg.densityDpi).append("\n");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            status.append("touchscreen:").append(cfg.densityDpi).append("\n");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            status.append("uiMode:").append(cfg.densityDpi).append("\n");
        }
        LogUtils.e(status.toString());
    }

    /**
     * 触摸拦截
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) { // 点击屏幕空白区域隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 选项菜单点击回调, 也可使用监听器 menuItem.setOnMenuItemClickListener();
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建上下文菜单 需主动注册registerForContextMenu(view); 也可注销
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * 监听上下文菜单选项事件
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    /**
     * 相当于KeyEvent.KEYCODE_BACK 返回键 , 相当于menu的返回事件
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
