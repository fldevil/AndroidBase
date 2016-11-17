package com.bjxrgz.startup.manager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe EventBus管理工具类
 */
public class BusManager {

    public static final int TYPE_NORMAL = 0;

    /* 注册之后才会触发此方法,注销之后不触发此方法 */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BusManager.Event event) {
        int type = event.getType();
        if (type == BusManager.TYPE_NORMAL){
            // 具体操作
        }
    }

    /* 在activity/fragment的onStart中注册,参数为引用 */
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    /* 在activity/fragment的onStop中注销,参数为引用 */
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    /* 像所有订阅者推送消息 */
    public static void post(Event event) {
        EventBus.getDefault().post(event);
    }

    public static class Event {
        private int type;
        private Object object;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getObject() {
            return object;
        }

        public void setObject(Object object) {
            this.object = object;
        }
    }

}
