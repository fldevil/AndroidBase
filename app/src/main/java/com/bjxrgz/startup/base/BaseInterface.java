package com.bjxrgz.startup.base;

import android.os.Bundle;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe Interface的基类,主要用于Activity和fragment的通信
 */
public interface BaseInterface {

    /**
     * fragment实现这个接口的子接口，在toFragment里接受来自activity的bundle
     * activity定义这个接口的子接口，主动调用toFragment传值给fragment
     */
    interface BaseFragmentView {

        Bundle toFragment(Bundle bundle);
    }

    /**
     * activity实现这个接口的子接口，在toActivity里接受来自fragment的bundle
     * fragment定义这个接口的子接口，主动调用toActivity传值给activity
     */
    interface BaseActivityView {

        Bundle toActivity(Bundle bundle);
    }

    /**
     * APIManager里的接口定义
     */
    interface Callback<T> {

        void onSuccess(T result);

        void onFinished();

    }

}

