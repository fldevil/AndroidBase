package com.bjxrgz.startup.base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import com.bjxrgz.startup.utils.DialogUtils;
import com.bjxrgz.startup.utils.LogUtils;
import com.bjxrgz.startup.utils.NetUtils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe Activity的基类,主要用于log日志和初始化工作
 */
public abstract class BaseActivity<T> extends AppCompatActivity {

    private BaseInterface.BaseFragmentView mListener;// 这个监听器是向fragment传数据的
    protected Bundle fromFragment = new Bundle();// 接受到的fragment的数据
    protected Activity mActivity;
    protected ProgressDialog pb;

    // 4.4版本下的跳转效果 5.0以上的在baseActivity里就设定好了
    private static final int kitkatAnimIn = android.R.anim.fade_in;
    private static final int kitkatAnimOut = android.R.anim.fade_out;

    /**
     * 获取T的名称,不能用getLocalClassName代替
     */
    @SuppressWarnings("unchecked")
    protected String getCls() {
        Class<T> cls = (Class<T>) (((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);

        return cls.getSimpleName();
    }

    /**
     * 子类重写类似方法 实现跳转
     */
    public static void goBaseActivity(Activity activity) {
        Intent intent = new Intent();
        // intent.setClass
        startActivity(activity, intent, true);
    }

    /**
     * 将bundle传送给fragment数据,并接受回传bundle 在需要回传的地方调用(activity类中调用)
     */
    public void goFragment(Bundle bundle) {
        if (mListener != null) {
            fromFragment = mListener.toFragment(bundle);
        }
    }

    protected abstract void create(Bundle savedInstanceState);

    protected abstract void destroy();

    /**
     * ***********************************以下是生命周期***********************************
     * <p>
     * 实例化控件，加载监听器
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 无actionBar, 全屏是window.addFlags
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        // 写死竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 布局动画, 要在initTheme前面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setEnterTransition(new Fade());
            window.setExitTransition(new Fade());
        }
        // SkinUtils.initTheme(this);// 换肤操作
        pb = DialogUtils.createProgress(this, null, "请稍候.....", false, false, null);
        super.onCreate(savedInstanceState);
        mActivity = this; // 实例

        create(savedInstanceState); // 抽象方法
    }

    /**
     * FragmentAttach的时候才会执行,之后会执行fragment的onCreate方法
     * 可以获取Fragment的引用，进行单方向通信
     */
    @Override
    public void onAttachFragment(Fragment fragment) {
        LogUtils.log(Log.DEBUG, getCls(), "--->onAttachFragment");
        super.onAttachFragment(fragment);
        if (fragment instanceof BaseInterface.BaseActivityView) {
            mListener = (BaseInterface.BaseFragmentView) fragment;
        } else {
            LogUtils.log(Log.DEBUG, getCls(), "--->no implement Activity2FragmentListener");
        }
    }

    /**
     * setContentView()或者addContentView()方法执行完毕时就会调用该方法,不是onCreate
     * 可以在这里initData,但不能initListener,view只是加载出来，还没有实例化
     * <p>
     * 尤其是换肤的时候，setTheme之后需要从新setContentView以下，
     * 所以控件需要重新实例化和加载监听器，这样可以直接调用create方法，
     * 如果里面有initData就不行了,但是每次换肤之后还是会执行onContentChanged
     * 所以需要接口里根据数据是不是null ,再来判断是否进行数据初始化
     */
    @Override
    public void onContentChanged() {
        LogUtils.log(Log.DEBUG, getCls(), "--->onContentChanged");
        super.onContentChanged();
    }

    /**
     * singTop模式下，在栈顶的activity启动本身的时候是不会再onCreate的，在这里接受
     */
    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.log(Log.DEBUG, getCls(), "--->onNewIntent");
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
        LogUtils.log(Log.DEBUG, getCls(), "--->onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * onCreate和onRestoreInstanceState是接受Bundle的
     * onSaveInstanceState是finish时保存传送Bundle的
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        LogUtils.log(Log.DEBUG, getCls(), "--->onActivityResult");
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 当Activity彻底运行起来之后回调onPostCreate方法 也就是onCreate执行完后
     */
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.log(Log.DEBUG, getCls(), "--->onPostCreate");
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
        LogUtils.log(Log.DEBUG, getCls(), "--->onPostResume");
        super.onPostResume();
    }

    /**
     * DecorView在这里才会有params,viewGroup在这里才能add
     * Window是WindowManager最顶层的视图，PhoneWindow是Window的唯一实现类
     * DecorView是window下的子视图,是所有应用窗口(Activity界面)的根View
     */
    @Override
    public void onAttachedToWindow() {
        LogUtils.log(Log.DEBUG, getCls(), "--->onAttachedToWindow");
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
        LogUtils.log(Log.DEBUG, getCls(), "--->onCreateOptionsMenu");
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
        LogUtils.log(Log.DEBUG, getCls(), "--->onSaveInstanceState");
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
        LogUtils.log(Log.DEBUG, getCls(), "--->onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    /**
     * 生命周期结束并不等于对象销毁，其他方法还是可以调用的
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroy();
    }

    /**
     * ***********************************以上是生命周期***********************************
     * <p>
     * 有attach的fragment时，当fragment也Resume时被回调
     * 最好在这里执行已经存在的fragment的translate操作
     */
    @Override
    protected void onResumeFragments() {
        LogUtils.log(Log.DEBUG, getCls(), "onResumeFragments");
        super.onResumeFragments();
    }

    /**
     * 在window执行动画的时候activity不能draw，所以就有了下面的方法
     */
    @Override
    public void onEnterAnimationComplete() {
        LogUtils.log(Log.DEBUG, getCls(), "onEnterAnimationComplete");
        super.onEnterAnimationComplete();
    }

    /**
     * 选项菜单点击回调, 也可使用监听器 menuItem.setOnMenuItemClickListener();
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogUtils.log(Log.DEBUG, getCls(), "onOptionsItemSelected--->" + item.getTitle());
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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        LogUtils.log(Log.DEBUG, getCls(), "onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    /**
     * 监听上下文菜单选项事件
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        LogUtils.log(Log.DEBUG, getCls(), "onContextItemSelected--->position == " + info.position);
        return super.onContextItemSelected(item);
    }

    /**
     * 相当于KeyEvent.KEYCODE_BACK 返回键 , 相当于menu的返回事件
     */
    @Override
    public void onBackPressed() {
        LogUtils.log(Log.DEBUG, getCls(), "onBackPressed");
        super.onBackPressed();
    }

    /**
     * activity启动activity
     */
    public static void startActivity(Activity activity, Intent intent, boolean anim) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivity(intent);
            if (anim) // 4.4跳转效果
                activity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
        }
    }

    /**
     * fragment启动activity
     */
    public static void startActivity(Fragment fragment, Activity parentActivity,
                                     Intent intent, boolean anim) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(parentActivity).toBundle());
        } else {
            fragment.startActivity(intent);
            // if (anim) // 4.4跳转效果
            // parentActivity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
        }
    }

    /**
     * context启动activity
     */
    public static void startActivity(Context context, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 启动activity，setResult设置回传的resultCode和intent
     */
    public static void startActivityForResult(Activity activity, Intent intent,
                                              int requestCode, boolean anim) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityForResult(intent, requestCode,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivityForResult(intent, requestCode);
            if (anim) // 4.4跳转效果
                activity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
        }
    }

    /**
     * 多层fragment时，第二级fragment是无法在startActivityForResult上时候收到回传intent的
     */
    public static void startActivityFromFragment(AppCompatActivity activity, Fragment fragment,
                                                 Intent intent, int requestCode, boolean anim) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityFromFragment(fragment, intent, requestCode,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivityFromFragment(fragment, intent, requestCode);
            if (anim) // 4.4跳转效果
                activity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
        }
    }

    /**
     * 关闭当前activity ,finishActivity(requestCode)为关闭源activity
     */
    public static void finish(Activity activity, boolean removeTask) {
        if (removeTask) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.finishAndRemoveTask();
            }
        } else
            activity.finish();
    }

}