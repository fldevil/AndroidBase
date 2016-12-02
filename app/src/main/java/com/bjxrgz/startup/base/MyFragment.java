package com.bjxrgz.startup.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bjxrgz.startup.manager.ViewManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by JiangZhiGuo on 2016-12-2.
 * describe
 */

public abstract class MyFragment<T> extends BaseFragment<T> {

    private Unbinder unbinder;
    protected ProgressDialog loading;
    public View rootView;

    /* 子类重写类似方法 获取对象 */
    public static BaseFragment newFragment() {
        Bundle bundle = new Bundle();
        // bundle.putData();
        return MyFragment.newInstance(MyFragment.class, bundle);
    }

    protected abstract void initObject(Bundle savedInstanceState);

    protected abstract int layoutId(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    protected abstract void initData(Bundle savedInstanceState);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading = ViewManager.createLoading(mActivity);
        initObject(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            int layoutId = layoutId(inflater, container, savedInstanceState);
            rootView = inflater.inflate(layoutId, container, false);
            unbinder = ButterKnife.bind(mFragment, rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
    }

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
}
