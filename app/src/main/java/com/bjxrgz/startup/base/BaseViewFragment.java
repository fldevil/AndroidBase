package com.bjxrgz.startup.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseViewFragment<T> extends BaseFragment<T> {

    private Unbinder unbinder;

    protected abstract int initContentView(LayoutInflater inflater, ViewGroup container,
                                           Bundle savedInstanceState);

    protected abstract void initObject(View view, @Nullable Bundle savedInstanceState);

    protected abstract void initView(View view, @Nullable Bundle savedInstanceState);

    protected abstract void initData(View view, @Nullable Bundle savedInstanceState);

    protected abstract void refreshData();

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootRes = initContentView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(rootRes, container, false);
        if (rootView != null) {
            unbinder = ButterKnife.bind(mFragment, rootView);
        }
        return rootView;
    }

    @Override
    protected void viewCreate(View view, @Nullable Bundle savedInstanceState) {
        initObject(view, savedInstanceState);
        initView(view, savedInstanceState);
        initData(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    //
//    @ViewInject(R.id.tvTopTitle)
//    private TextView tvTopTitle;
//    // left
//    @ViewInject(R.id.tvTopLeft)
//    protected TextView tvTopLeft;
//    @ViewInject(R.id.ibTopLeft)
//    protected ImageButton ibTopLeft;
//    // right
//    @ViewInject(R.id.tvTopRightShall)
//    protected TextView tvTopRightShall;
//    @ViewInject(R.id.tvTopRightDark)
//    protected TextView tvTopRightDark;
//    @ViewInject(R.id.ivAdd)
//    protected ImageView ivAdd;
//    @ViewInject(R.id.cbTopRight)
//    protected CheckBox cbTopRight;
//
//    @Event(value = {R.id.ibTopLeft, R.id.tvTopLeft}, type = View.OnClickListener.class)
//    private void onClick(View view) {
//        mFragmentManager.popBackStack();
//    }
//
//    protected void initBaseView(String topTitle) {
//        tvTopTitle.setText(topTitle);
//    }
//
//    protected void initTop(String topTitle) {
//        tvTopTitle.setText(topTitle);
//    }
//
//    protected void initTopBack(String title) {
//        ibTopLeft.setVisibility(View.VISIBLE);
//        initTop(title);
//    }
//
//    protected void initTopBack(String title, String back) {
//        tvTopLeft.setVisibility(View.VISIBLE);
//        tvTopLeft.setText(back);
//        initTop(title);
//    }
//
//    protected void initTopRightShall(String title, String right) {
//        tvTopRightShall.setVisibility(View.VISIBLE);
//        tvTopRightShall.setText(right);
//        initTop(title);
//    }
//
//    protected void initTopRightDark(String title, String right) {
//        tvTopRightDark.setVisibility(View.VISIBLE);
//        tvTopRightDark.setText(right);
//        initTop(title);
//    }
//
//    protected void initTopAdd(String title) {
//        ibTopLeft.setVisibility(View.VISIBLE);
//        ivAdd.setVisibility(View.VISIBLE);
//        initTop(title);
//    }
//
//    protected void initTopCheck(String title) {
//        ibTopLeft.setVisibility(View.VISIBLE);
//        cbTopRight.setVisibility(View.VISIBLE);
//        initTop(title);
//    }

}
