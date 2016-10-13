package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.AnimUtils;
import com.bjxrgz.startup.utils.DialogUtils;
import com.bjxrgz.startup.utils.InputUtils;
import com.bjxrgz.startup.utils.NetUtils;
import com.bjxrgz.startup.utils.ScreenUtils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe Activity的基类
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    protected Activity mActivity;
    protected FragmentManager mFragmentManager;
    protected ProgressDialog loading;
    protected String logTag = "BaseActivity";

    /**
     * 子类重写类似方法 实现跳转
     */
    public static void goActivity(Activity activity) {
        Intent intent = new Intent();
        // intent.setClass
        ActivityUtils.startActivity(activity, intent);
    }

    @SuppressWarnings("unchecked")
    protected String getCls() {
        Class<T> cls = (Class<T>) (((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);
        return cls.getSimpleName();
    }

    protected abstract void create(Bundle savedInstanceState);

    /**
     * ***********************************以下是生命周期***********************************
     * <p/>
     * 实例化控件，加载监听器
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logTag = getCls();
        ScreenUtils.requestNoTitle(this);
        ScreenUtils.requestPortrait(this);
        InputUtils.initNoAuto(this);
        AnimUtils.initBaseActivity(this);
        super.onCreate(savedInstanceState);
        mActivity = this; // 实例
        loading = DialogUtils.createLoading(this, getString(R.string.wait), true);
        mFragmentManager = getSupportFragmentManager();
        create(savedInstanceState); // 抽象方法
    }

    /**
     * FragmentAttach的时候才会执行,之后会执行fragment的onCreate方法
     * 可以获取Fragment的引用，进行单方向通信
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
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
    }

    /**
     * singTop模式下，在栈顶的activity启动本身的时候是不会再onCreate的，在这里接受
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    /**
     * activity完全不可见--->可见时，从这里开始生命周期
     */
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 前面不一定是onRestart
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 接受setResult回传的intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * onCreate和onRestoreInstanceState是接受Bundle的
     * onSaveInstanceState是finish时保存传送Bundle的
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 当Activity彻底运行起来之后回调onPostCreate方法 也就是onCreate执行完后
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    /**
     * activity部分不可见--->可见时，从这里开始生命周期
     */
    @Override
    protected void onResume() {
        super.onResume();
        NetUtils.isAvailable(this);
    }

    /**
     * onResume执行完后执行
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * DecorView在这里才会有params,viewGroup在这里才能add
     * Window是WindowManager最顶层的视图，PhoneWindow是Window的唯一实现类
     * DecorView是window下的子视图,是所有应用窗口(Activity界面)的根View
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
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
    }

    /**
     * 当系统“未经你许可”时销毁了你的activity，则onSaveInstanceState会被系统调用
     * recreate之后也会调用的，配合起来可以做主题更换功能
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 部分可见--->完全不可见
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * DecorView到这里没有params,viewGroup在这里之前remove
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 生命周期结束并不等于对象销毁，其他方法还是可以调用的
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * ***********************************以上是生命周期***********************************
     * <p/>
     * 有attach的fragment时，当fragment也Resume时被回调
     * 最好在这里执行已经存在的fragment的translate操作
     */
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) { // 点击屏幕空白区域隐藏软键盘
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * 在window执行动画的时候activity不能draw，所以就有了下面的方法
     */
    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();
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
