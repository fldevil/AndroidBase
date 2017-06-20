package com.user.project.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjxrgz.base.utils.FragmentUtil;

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

    protected Context mContext;
    protected BaseActivity mActivity;
    protected BaseFragment mFragment;
    protected FragmentManager mFragmentManager;
    protected Bundle mBundle;
    public String tag = "BaseFragment";
    private Unbinder unbinder;

    /* 初始layout */
    protected abstract int initLayout();

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
            mContext = context.getApplicationContext();
            mActivity = (BaseActivity) context;
            mFragmentManager = mActivity.getSupportFragmentManager();
        }
        FragmentUtil.initAttach(this);
    }

    /* Activity中的onAttachFragment执行完后会执行 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments(); // 取出Bundle
        FragmentUtil.initCreate(this);
    }

    /* 在这里返回绑定并View,从stack返回的时候也是先执行这个方法 */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutId = initLayout();
        return inflater.inflate(layoutId, container, false);
    }

    /* 一般在这里进行控件的实例化,加载监听器, 参数view就是fragment的layout */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
    }

    /* activity的onCreate执行完成后才会调用 */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
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
