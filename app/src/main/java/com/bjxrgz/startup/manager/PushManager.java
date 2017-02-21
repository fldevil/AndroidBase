package com.bjxrgz.startup.manager;

import android.content.Context;
import android.text.TextUtils;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.AppUtils;
import com.bjxrgz.startup.utils.LogUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by JiangZhiGuo on 2016/8/5.
 * describe 友盟管理工具类 (不用的话，删掉PushSDK module 并去掉manifest下的配置)
 */
public class PushManager {

    private static String DEVICE_TOKEN; // 友盟标识
    private static PushAgent mPushAgent;

    public static void initAPP() {
        Context context = MyApp.get().getApplicationContext();
        // 获取mPushAgent
        mPushAgent = PushAgent.getInstance(context);
        // 统计应用启动数据
        mPushAgent.onAppStart();
        // 打印日志
        mPushAgent.setDebugMode(MyApp.LOG);
        // 检查配置文件,会弹错误信息的toast
//        mPushAgent.setPushCheck(isLog);
        // 收集奔溃日志
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);
        // 应用在前台时否显示通知
        mPushAgent.setNotificaitonOnForeground(true);
        // 通知栏按数量显示
        mPushAgent.setDisplayNotificationNumber(10);
        // 设置重复提醒的最小间隔秒数
        mPushAgent.setMuteDurationSeconds(1);
        // 开启免打扰模式 “23:00”到“6:00”
        mPushAgent.setNoDisturbMode(23, 0, 6, 0);
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
     * 开关,在startActivity中调用
     */
    public static void setEnable(boolean open) {
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
        if (TextUtils.isEmpty(DEVICE_TOKEN)) {
            DEVICE_TOKEN = mPushAgent.getRegistrationId();
        }
        LogUtils.d("deviceToken", DEVICE_TOKEN);
        return DEVICE_TOKEN;
    }

    /**
     * 数据统计(崩溃日志) 在activity中的OnResume中调用
     */
    public static void analysisOnResume(Context context) {
        MobclickAgent.onResume(context);
    }

    /**
     * 数据统计(崩溃日志) 在activity中的OnPause中调用
     */
    public static void analysisOnPause(Context context) {
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
     * 自定义通知打开动作
     * 1.只能在MyApp中调用 ,如果在Activity中调用此接口，若应用进程关闭，则设置的接口会无效
     * 2.UmengNotificationClickHandler是在BroadcastReceiver中被调用，因此若需启动Activity，
     * 需为Intent添加Flag：Intent.FLAG_ACTIVITY_NEW_TASK，否则无法启动Activity。
     */
    public static void initCustomAction() {
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            // 后台发送自定义消息时，回调这个方法
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                // 如果回调这个方法,数据是放在msg.custom里的
                // 处理过程参照下面回调方法
            }

            // 后台发送正常消息时，回调这个方法
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                // 一般这里会先进行判断  是正常打开app，还是跳转到指定的页面
                if (!AppUtils.isAppOnForeground()) {
                    // 如果数据在uMessage.extra里，那么友盟会自动把这些数据封装带启动的intent里
                    // 如果数据不在extra里，那么需要额外的处理
                    super.launchApp(context, uMessage);
                } else {
                    // 直接打开Activity操作
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
    }

}
