package com.bjxrgz.startup.manager;

import android.content.Context;
import android.text.TextUtils;

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

    private static String DEVICE_TOKEN; // 友盟标识
    private static PushAgent mPushAgent;

    public static void initAPP(Context context, boolean isLog) {
        // 获取mPushAgent
        mPushAgent = PushAgent.getInstance(context.getApplicationContext());
        // 统计应用启动数据
        mPushAgent.onAppStart();
        mPushAgent.setDebugMode(false);
        // 注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                DEVICE_TOKEN = deviceToken;
                LogUtils.d("registerPush", deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtils.e(s + "\n" + s1);
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
                    getDeviceToken();
                }

                @Override
                public void onFailure(String s, String s1) {
                    LogUtils.e(s + "\n" + s1);
                }
            });
        } else {
            mPushAgent.disable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(String s, String s1) {
                    LogUtils.e(s + "\n" + s1);
                }
            });
        }
    }

    /**
     * 主动获取DeviceToken
     */
    public static String getDeviceToken() {
        if (TextUtils.isEmpty(DEVICE_TOKEN)){
            DEVICE_TOKEN = mPushAgent.getRegistrationId();
        }
        LogUtils.d("deviceToken", DEVICE_TOKEN);
        return DEVICE_TOKEN;
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
