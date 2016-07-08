package com.bjxrgz.startup.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.transition.AutoTransition;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.bjxrgz.startup.utils.DialogUtils;
import com.bjxrgz.startup.utils.LogUtils;

import org.xutils.x;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe Fragment的基类,主要用于log日志和初始化工作
 */
public abstract class BaseFragment extends Fragment {

    public FragmentActivity mActivity;
    public FragmentManager mFragmentManager;
    protected Bundle mBundle;// 接受数据的Bundle
    private String cls = "BaseFragment";// 子类的类名
    private BaseInterface.BaseActivityView mListener;// 这个监听器是向activity传数据的
    protected ProgressDialog pb;

    /**
     * 两种方法获取当前fragment的实例
     * 1.反射生成对象
     */
    public static <T> T newInstance(Class<T> clz, Bundle args) {
        T fragment = null;
        try {
            // 获取Fragment内名为setArguments的函数
            Method setArguments = clz.getMethod("setArguments", Bundle.class);
            // 走的无参构造函数
            fragment = clz.newInstance();
            // 往Bundle里传值，这里是子类的类名
            args.putString("cls", fragment.getClass().getSimpleName());
            // 执行setArguments方法
            setArguments.invoke(fragment, args);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    /**
     * 2.子类重写类似方法 获取对象
     */
    public static BaseFragment newBaseFragment() {
        Bundle bundle = new Bundle();
        BaseFragment baseFragment = BaseFragment.newInstance(BaseFragment.class, bundle);
        // 设置bundle的内容...
        bundle.putString("cls", "手动写当前类名字");
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    /**
     * 将bundle传送给activity数据,在需要回传的地方调用(fragment类中调用)
     */
    public void goActivity(Bundle bundle) {
        if (mListener != null) {
            mBundle = mListener.toActivity(bundle);
        }
    }

    protected abstract View createView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState);

    protected abstract void viewCreate(View view, @Nullable Bundle savedInstanceState);

    protected abstract void detach();

    /**
     * **********************************以下是生命周期*******************************
     * <p>
     * 在xml中定义的，onInflate方法第一个被调用,否则直接下一步
     */
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        LogUtils.log(Log.DEBUG, cls, "----->onInflate");
        super.onInflate(context, attrs, savedInstanceState);

    }

    /**
     * 旧传值方式，互相获取对象引用进行数据传送
     * 新传值方式：借用mvp的思想, 接口化，双方互相回调接口实现数据传送
     */
    @Override
    public void onAttach(Context context) {
        LogUtils.log(Log.DEBUG, cls, "----->onAttach");
        super.onAttach(context);
        if (context instanceof BaseInterface.BaseActivityView) {
            mListener = (BaseInterface.BaseActivityView) context;
        } else {
            LogUtils.log(Log.DEBUG, cls, "no implement Activity2FragmentListener");
        }
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
            mFragmentManager = mActivity.getSupportFragmentManager();
        }
        // 过渡动画效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.setEnterTransition(new AutoTransition());
            this.setExitTransition(new AutoTransition());
            this.setReenterTransition(new AutoTransition());
            this.setReturnTransition(new AutoTransition());
        }
    }

    /**
     * Activity中的onAttachFragment执行完后会执行 ,最好在这里接受bundle
     * 这个方法之后在detach之后才会执行，看清楚要不要在这里初始化数据
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.log(Log.DEBUG, cls, "----->onCreate");
        setHasOptionsMenu(true);// Fragment与ActionBar和MenuItem集成
        pb = DialogUtils.createProgress(getContext(), null, "请稍候.....", false, false, null);

        if (getArguments() != null) {
            mBundle = getArguments();
            cls = mBundle.getString("cls");
        }
    }

    /**
     * 在这里返回绑定并View,从stack返回的时候也是先执行这个方法
     * 可以通过--->getView来辨别是否已创建过view
     * 这里的做法是个onDestroyView配套的
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getView();
        if (view == null) {
            LogUtils.log(Log.DEBUG, cls, "----->onCreateView == null");
            View view1 = createView(inflater, container, savedInstanceState);
            x.view().inject(this, view1); // xUtils在fragment的初始化
            return view1;
        } else {
            LogUtils.log(Log.DEBUG, cls, "----->onCreateView == exist");
            x.view().inject(this, view); // xUtils在fragment的初始化
            return view;
        }
    }


    /**
     * 一般在这里进行控件的实例化,加载监听器, 参数view就是fragment的layout
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtils.log(Log.DEBUG, cls, "----->onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        viewCreate(view, savedInstanceState);
    }

    /**
     * activity的onCreate执行完成后才会调用
     * 比如activity的onCreate是提取数据的，那么可以在这里获取activity中的数据
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtils.log(Log.DEBUG, cls, "----->onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 当保存的额状态全部读取完毕后，这里可以检查数据是否正确
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        LogUtils.log(Log.DEBUG, cls, "----->onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * 全部遮挡--->到显示
     */
    @Override
    public void onStart() {
        LogUtils.log(Log.DEBUG, cls, "----->onStart");
        super.onStart();
    }

    /**
     * 部分遮挡--->到显示
     */
    @Override
    public void onResume() {
        LogUtils.log(Log.DEBUG, cls, "----->onResume");
        super.onResume();
    }

    /**
     * 一般都会在activity中处理
     * 当fragment被hide之类的时候，需要连带着它的Menu一起消失 setMenuVisibility(visibility);
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        LogUtils.log(Log.DEBUG, cls, "----->onCreateOptionsMenu");
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 完全可见--->部分遮挡
     */
    @Override
    public void onPause() {
        LogUtils.log(Log.DEBUG, cls, "----->onPause");
        super.onPause();
    }

    /**
     * 非人为关闭时调用
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        LogUtils.log(Log.DEBUG, cls, "----->onSaveInstanceState");
    }

    /**
     * 部分可见--->完全不可见
     */
    @Override
    public void onStop() {
        LogUtils.log(Log.DEBUG, cls, "----->onStop");
        super.onStop();
    }

    /**
     * Fragment中的布局被移除时调用。
     * 加入stack的时候，在onDestroyView之后就没了，不执行后面的方法了
     */
    @Override
    public void onDestroyView() {
        LogUtils.log(Log.DEBUG, cls, "----->onDestroyView");
        // viewpager有四个fragment 当滑动到第四页的时候 第一页执行onDestroyView(),但没有执行onDestroy。
        // 他依然和activity关联。当在滑动到第一页的时候又执行了 onCreateView()。会出现重复加载view的局面
        // 这里的做法是个onCreateView配套的
        View view = getView();
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        super.onDestroyView();
    }

    /**
     * 相当于finish
     */
    @Override
    public void onDestroy() {
        LogUtils.log(Log.DEBUG, cls, "----->onDestroy");
        super.onDestroy();
    }

    /**
     * 和activity脱离关系
     */
    @Override
    public void onDetach() {
        LogUtils.log(Log.DEBUG, cls, "----->onDetach");
        super.onDetach();

        detach();
    }

    /**
     * **************************************以上是生命周期**************************************
     * <p>
     * 捕获是否hide的状态，可以做更新操作
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtils.log(Log.DEBUG, cls, "onHiddenChanged");
        super.onHiddenChanged(hidden);
    }

    /**
     * 第二层的fragment，想要返回值的话，需要特殊的启动技巧
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.log(Log.DEBUG, cls, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        LogUtils.log(Log.DEBUG, cls, "onCreateAnimation");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * 上下文菜单是独立的，和activity无关联，功能健全
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        LogUtils.log(Log.DEBUG, cls, "onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        LogUtils.log(Log.DEBUG, cls, "onContextItemSelected");
        return super.onContextItemSelected(item);
    }

}