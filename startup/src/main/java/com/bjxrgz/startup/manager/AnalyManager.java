package com.bjxrgz.startup.manager;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by gg on 2017/2/27.
 * 友盟统计
 */
public class AnalyManager {

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

}
