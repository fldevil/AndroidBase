package com.bjxrgz.startup.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Activity管理工具类
 */
public class ActivityUtils {

    protected static boolean anim = true; // 跳转动画开关
    // 4.4版本下的跳转效果 5.0以上的在baseActivity里就设定好了
    private static final int kitkatAnimIn = android.R.anim.fade_in;
    private static final int kitkatAnimOut = android.R.anim.fade_out;
    // 所有已启动的Activity
    private static List<Activity> activities = new LinkedList<>();

    /**
     * activity在onCreate是调用此方法
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * activity在onDestroy是调用此方法
     */
    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 关闭所有activity，应用与一键退出
     */
    public static void closeActivities() {
        for (Activity activity : activities) {
            if (activity != null)
                activity.finish();
        }
        activities.clear();
    }

    /**
     * Context启动activity
     */
    public static void startActivity(Context from, Intent intent) {
        if (from instanceof Activity) {
            Activity activity = (Activity) from;
            if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            } else {
                activity.startActivity(intent);
                if (anim) { // 4.4跳转效果
                    activity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
                }
            }
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            from.startActivity(intent);
        }
    }

    /**
     * fragment启动activity
     */
    public static void startActivity(Fragment fragment, Activity parentActivity, Intent intent) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(parentActivity).toBundle());
        } else {
            fragment.startActivity(intent);
        }
    }

    /**
     * 启动activity，setResult设置回传的resultCode和intent
     */
    public static void startActivity(Activity activity, Intent intent, int requestCode) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityForResult(intent, requestCode, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivityForResult(intent, requestCode);
            if (anim) // 4.4跳转效果
                activity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
        }
    }

    /**
     * Fragment启动activity，setResult设置回传的resultCode和intent
     */
    public static void startActivity(Fragment fragment, Activity activity, Intent intent, int requestCode) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fragment.startActivityForResult(intent, requestCode,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivityForResult(intent, requestCode);
            if (anim) // 4.4跳转效果
                activity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
        }
    }

    /**
     * 多层fragment时，第二级fragment是无法在startActivityForResult上时候收到回传intent的
     */
    public static void startActivityForFragment(AppCompatActivity activity, Fragment fragment, Intent intent, int requestCode) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startActivityFromFragment(fragment, intent, requestCode,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        } else {
            activity.startActivityFromFragment(fragment, intent, requestCode);
            if (anim) // 4.4跳转效果
                activity.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
        }
    }

    /**
     * 关闭当前activity ,finishActivity(requestCode)为关闭源activity
     */
    public static void finish(Activity activity, boolean removeTask) {
        if (removeTask) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.finishAndRemoveTask();
            }
        } else
            activity.finish();
    }

    /**
     * 判断是否存在Activity
     *
     * @param context     上下文
     * @param packageName 包名
     * @param className   activity全路径类名
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isExistActivity(Context context, String packageName, String className) {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        return !(context.getPackageManager().resolveActivity(intent, 0) == null ||
                intent.resolveActivity(context.getPackageManager()) == null ||
                context.getPackageManager().queryIntentActivities(intent, 0).size() == 0);
    }

}
