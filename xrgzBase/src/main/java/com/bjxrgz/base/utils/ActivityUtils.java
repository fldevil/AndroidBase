package com.bjxrgz.base.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Window;
import android.view.WindowManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe Activity管理工具类
 */
public class ActivityUtils {

    private static boolean anim = true; // 跳转动画开关
    private static final int animIn = android.R.anim.fade_in; // 4.4下的进场效果
    private static final int animOut = android.R.anim.fade_out; // 4.4下的退场效果
    private static List<Activity> activities = new LinkedList<>(); // 所有已启动的Activity

    public static void initSuperCreate(Activity activity) {
        Window window = activity.getWindow(); // 软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// 键盘不会遮挡输入框
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // 不自动弹键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // 总是隐藏键盘
        ScreenUtils.requestPortrait(activity); // 竖屏
        if (activity instanceof AppCompatActivity) { // titleBar
            ScreenUtils.requestNoTitle((AppCompatActivity) activity);
        }
        // 专门的跳转方式才会有过场效果
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setEnterTransition(new Fade()); // 下一个activity进场
            window.setExitTransition(new Fade()); //  当前activity向后时退场
            // window.setReenterTransition(slideIn); // 上一个activity进场
            // window.setReturnTransition(slideOut); // 当前activity向前时退场
        }
    }

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
     * 关闭顶部activity
     */
    public static void closeTopActivitie() {
        Activity top = activities.get(activities.size() - 1);
        if (top != null) {
            top.finish();
        }
    }

    /**
     * 关闭所有activity，应用于一键退出
     */
    public static void closeActivities() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
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
                try { // 有些机型会报错
                    activity.startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                } catch (Exception e) {
                    e.printStackTrace();
                    activity.startActivity(intent);
                }
            } else {
                activity.startActivity(intent);
                if (anim) { // 4.4跳转效果
                    activity.overridePendingTransition(animIn, animOut);
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
    public static void startActivity(Fragment from, Activity to, Intent intent) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { // 有些机型会报错
                from.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(to).toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                from.startActivity(intent);
            }
        } else {
            from.startActivity(intent);
        }
    }

    /**
     * 启动activity，setResult设置回传的resultCode和intent
     */
    public static void startActivity(Activity from, Intent intent, int requestCode) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { // 有些机型会报错
                from.startActivityForResult(intent, requestCode,
                        ActivityOptions.makeSceneTransitionAnimation(from).toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                from.startActivityForResult(intent, requestCode);
            }
        } else {
            from.startActivityForResult(intent, requestCode);
            if (anim) { // 4.4跳转效果
                from.overridePendingTransition(animIn, animOut);
            }
        }
    }

    /**
     * Fragment启动activity，setResult设置回传的resultCode和intent
     */
    public static void startActivity(Fragment from, Activity to, Intent intent, int requestCode) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try { // 有些机型会报错
                from.startActivityForResult(intent, requestCode,
                        ActivityOptions.makeSceneTransitionAnimation(to).toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                from.startActivityForResult(intent, requestCode);
            }
        } else {
            from.startActivityForResult(intent, requestCode);
            if (anim) { // 4.4跳转效果
                to.overridePendingTransition(animIn, animOut);
            }
        }
    }

    /**
     * 多层fragment时，第二级fragment是无法在startActivityForResult上时候收到回传intent的
     */
    public static void startActivityForFragment(AppCompatActivity activity, Fragment fragment, Intent intent, int requestCode) {
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                activity.startActivityFromFragment(fragment, intent, requestCode,
                        ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            } catch (Exception e) {
                e.printStackTrace();
                activity.startActivityFromFragment(fragment, intent, requestCode);
            }
        } else {
            activity.startActivityFromFragment(fragment, intent, requestCode);
            if (anim) // 4.4跳转效果
                activity.overridePendingTransition(animIn, animOut);
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
        } else {
            activity.finish();
        }
    }

}
