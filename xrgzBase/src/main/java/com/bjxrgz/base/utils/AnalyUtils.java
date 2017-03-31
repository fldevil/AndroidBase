package com.bjxrgz.base.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by gg on 2017/2/27.
 * 友盟统计(是友盟的单独的服务)
 */
public class AnalyUtils {

    /* c收集奔溃日志 */
    public static void initApp(Context context) {
        MobclickAgent.setScenarioType(context, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setCatchUncaughtExceptions(true);
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

    /* 应该有账号之类的统计 */
}
