package com.bjxrgz.startup.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjxrgz.startup.utils.FragmentUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe Fragment的基类
 */

public abstract class BaseFragment<T> extends Fragment {

    public BaseActivity mActivity;
    public BaseFragment mFragment;
    public FragmentManager mFragmentManager;
    public String tag = "BaseFragment";
    public Bundle mBundle;
    public View rootView;
    private Unbinder unbinder;

    /* 子类重写类似方法 获取对象 */
    public static BaseFragment newFragment() {
        Bundle bundle = new Bundle();
        // bundle.putData();
        return BaseFragment.newInstance(BaseFragment.class, bundle);
    }

    /* 初始layout */
    protected abstract int initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /* 实例化View */
    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    /* 初始Data */
    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onAttach(Context context) {
        tag = getCls();
        mFragment = this;
        super.onAttach(context);
        if (context instanceof FragmentActivity) {
            mActivity = (BaseActivity) context;
            mFragmentManager = mActivity.getSupportFragmentManager();
        }
        FragmentUtils.initAttach(this);
    }

    /* Activity中的onAttachFragment执行完后会执行 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments(); // 取出Bundle
        FragmentUtils.initCreate(this);
    }

    /* 在这里返回绑定并View,从stack返回的时候也是先执行这个方法 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            int layoutId = initLayout(inflater, container, savedInstanceState);
            rootView = inflater.inflate(layoutId, container, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    /* 一般在这里进行控件的实例化,加载监听器, 参数view就是fragment的layout */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

    /* activity的onCreate执行完成后才会调用 */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }

    /*Fragment中的布局被移除时调用*/
    @Override
    public void onDestroyView() {
        rootView = getView();
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /* 反射生成对象 */
    protected static <T> T newInstance(Class<T> clz, Bundle args) {
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

}
