package com.bjxrgz.startup.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

/**
 * Created by JiangZhiGuo on 2016/8/5.
 * describe 推送管理
 */
public class PushManager {

    // umeng
    public static final String UMENG_KEY = " 579f12c067e58eb387000a70";
    public static final String UMENG_MESSAGE_SECRET = "259ac76441a3aac7788694b9579cfec2";
    public static final String APP_MASTER_SECRET = "pbmenpubcbytew3hsq5fvnu3u7ukiwdr";

    public static PushAgent mPushAgent;

    public static void initAPP(Context context) {
        // 获取mPushAgent
        mPushAgent = PushAgent.getInstance(context.getApplicationContext());
        // 统计应用启动数据
        mPushAgent.onAppStart();
        // 注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                MyApp.DEVICE_TOKEN = deviceToken;
                LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "PushManager(register)--->" + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.log(Log.ERROR, MyApp.LOG_TAG, "PushManager(register)--->" + s + "-" + s1);
            }
        });
    }

    /**
     * 开关
     */
    public static void setPush(boolean open) {
        if (open) {
            mPushAgent.enable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "PushManager--->enable(success)");
                }

                @Override
                public void onFailure(String s, String s1) {
                    LogUtils.log(Log.ERROR, MyApp.LOG_TAG, "PushManager--->enable(Failure)--->" + s + "-" + s1);
                }
            });
        } else {
            mPushAgent.disable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "PushManager--->disable(success)");
                }

                @Override
                public void onFailure(String s, String s1) {
                    LogUtils.log(Log.ERROR, MyApp.LOG_TAG, "PushManager--->disable(Failure)--->" + s + "-" + s1);
                }
            });
        }
    }

    /**
     * 主动获取DeviceToken
     */
    public static void getDeviceToken() {
        if (TextUtils.isEmpty(MyApp.DEVICE_TOKEN)) {
            MyApp.DEVICE_TOKEN = mPushAgent.getRegistrationId();
        }
        LogUtils.log(Log.DEBUG, MyApp.LOG_TAG, "PushManager--->getDeviceToken--->" + MyApp.DEVICE_TOKEN);
    }

    /**
     * umeng 统计 在activity中的OnResume中调用
     */
    public static void mobclickOnResume(Context context) {
        MobclickAgent.onResume(context);
    }

    /**
     * umeng 统计 在activity中的OnPause中调用
     */
    public static void mobclickOnPause(Context context) {
        MobclickAgent.onPause(context);
    }

    /**
     * 设置声音
     */
    public static void setAudio(boolean open) {
        if (open) { // 正常
            mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        } else { // 静音
            mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        }
    }

    /**
     * 设置震动
     */
    public static void setVibrate(boolean open) {
        if (open) { // 震动
            mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        } else { // 不震动
            mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_DISABLE);
        }
    }

    /**
     * 设置推广渠道
     */
    public static void sethannel(String channl) {
        mPushAgent.setMessageChannel(channl);
    }

}
