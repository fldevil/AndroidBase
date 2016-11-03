package com.bjxrgz.startup.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.bjxrgz.startup.manager.ViewManager;
import com.bjxrgz.startup.utils.FragmentUtils;
import com.bjxrgz.startup.utils.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p/>
 * describe Fragment的基类
 */
public abstract class BaseFragment<T> extends Fragment {

    public FragmentActivity mActivity;
    public BaseFragment mFragment;
    public FragmentManager mFragmentManager;
    protected Bundle mBundle;// 接受数据的Bundle
    protected ProgressDialog loading;
    protected boolean log = false; // 是否打印生命周期
    protected String logTag = "BaseFragment";
    private Unbinder unbinder; // ButterKnife

    /* 子类重写类似方法 获取对象 */
    public static BaseFragment newFragment() {
        Bundle bundle = new Bundle();
        // bundle.putData();
        return BaseFragment.newInstance(BaseFragment.class, bundle);
    }

    protected abstract void initObject(Bundle savedInstanceState);

    protected abstract int createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    /**
     * **********************************以下是生命周期*******************************
     * <p/>
     * 在xml中定义的，onInflate方法第一个被调用,否则直接下一步
     */
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        logTag = getCls();
        super.onInflate(context, attrs, savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 旧传值方式，互相获取对象引用进行数据传送
     * 新传值方式：借用mvp的思想, 接口化，双方互相回调接口实现数据传送
     */
    @Override
    public void onAttach(Context context) {
        logTag = getCls();
        super.onAttach(context);
        LogUtils.lifeCycle(log, logTag);
        FragmentUtils.initBaseAttach(this);
        mFragment = this;
        if (context instanceof FragmentActivity) {
            mActivity = (FragmentActivity) context;
            mFragmentManager = mActivity.getSupportFragmentManager();
        }
    }

    /**
     * Activity中的onAttachFragment执行完后会执行 ,最好在这里接受bundle
     * 这个方法之后在detach之后才会执行，看清楚要不要在这里初始化数据
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
        FragmentUtils.initBaseCreate(this);
        loading = ViewManager.createLoading(mActivity);
        mBundle = getArguments(); // 取出Bundle
        initObject(savedInstanceState);
    }

    /**
     * 在这里返回绑定并View,从stack返回的时候也是先执行这个方法
     * 可以通过--->getView来辨别是否已创建过view
     * 这里的做法是个onDestroyView配套的
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.lifeCycle(log, logTag);
        View view = getView();
        if (view == null) {
            int rootRes = createView(inflater, container, savedInstanceState);
            view = inflater.inflate(rootRes, container, false);
            unbinder = ButterKnife.bind(mFragment, view);
        }
        return view;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        LogUtils.lifeCycle(log, logTag);
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    /**
     * 一般在这里进行控件的实例化,加载监听器, 参数view就是fragment的layout
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
        initView(view, savedInstanceState);
    }

    /**
     * activity的onCreate执行完成后才会调用
     * 比如activity的onCreate是提取数据的，那么可以在这里获取activity中的数据
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
        initData(savedInstanceState);
    }

    /**
     * 当保存的额状态全部读取完毕后，这里可以检查数据是否正确
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 全部遮挡--->到显示
     */
    @Override
    public void onStart() {
        super.onStart();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 第二层的fragment，想要返回值的话，需要特殊的启动技巧
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 部分遮挡--->到显示
     */
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 捕获是否hide的状态，可以做更新操作
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 一般都会在activity中处理
     * 当fragment被hide之类的时候，需要连带着它的Menu一起消失 setMenuVisibility(visibility);
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 完全可见--->部分遮挡
     */
    @Override
    public void onPause() {
        super.onPause();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 非人为关闭时调用
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 部分可见--->完全不可见
     */
    @Override
    public void onStop() {
        super.onStop();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * Fragment中的布局被移除时调用。
     * 加入stack的时候，在onDestroyView之后就没了，不执行后面的方法了
     */
    @Override
    public void onDestroyView() {
        // viewpager有四个fragment 当滑动到第四页的时候 第一页执行onDestroyView(),但没有执行onDestroy。
        // 他依然和activity关联。当在滑动到第一页的时候又执行了 onCreateView()。会出现重复加载view的局面
        // 这里的做法是个onCreateView配套的
        View view = getView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        super.onDestroyView();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 相当于finish
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.lifeCycle(log, logTag);
    }

    /**
     * 和activity脱离关系
     */
    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.lifeCycle(log, logTag);
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * **************************************以上是生命周期**************************************
     * <p/>
     * 反射生成对象
     */
    private static <T> T newInstance(Class<T> clz, Bundle args) {
        T fragment = null;
        try {
            // 获取Fragment内名为setArguments的函数
            Method setArguments = clz.getMethod("setArguments", Bundle.class);
            fragment = clz.newInstance();  // 走的无参构造函数
            if (args == null) { // 往Bundle里传值，这里是子类的类名
                args = new Bundle();
            }
            setArguments.invoke(fragment, args); // 执行setArguments方法
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
     * logTag获取
     */
    @SuppressWarnings("unchecked")
    protected String getCls() {
        Class<T> cls = (Class<T>) (((ParameterizedType) (this.getClass()
                .getGenericSuperclass())).getActualTypeArguments()[0]);
        return cls.getSimpleName();
    }

    /**
     * 上下文菜单是独立的，和activity无关联，功能健全
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

}