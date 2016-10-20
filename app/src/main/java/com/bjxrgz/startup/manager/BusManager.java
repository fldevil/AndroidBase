package com.bjxrgz.startup.manager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe 同步/异步管理工具类
 */
public class BusManager {

    /**
     * 在activity/fragment的onStart中注册
     */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /**
     * 在activity/fragment的onStop中注销
     */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 注册之后才会触发此方法,注销之后不触发此方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public static void onEvent(Object object) {

    }
}
