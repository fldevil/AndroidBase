package com.bjxrgz.startup.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
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
    private static final int kitkatAnimIn = android.R.anim.fade_in; // 4.4下的跳转效果
    private static final int kitkatAnimOut = android.R.anim.fade_out; // 4.4下的跳转效果
    private static List<Activity> activities = new LinkedList<>(); // 所有已启动的Activity

    public static void initCreate(Activity activity) {
        Window window = activity.getWindow(); // 软键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// 键盘不会遮挡输入框
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // 不自动弹键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // 总是隐藏键盘
        ScreenUtils.requestPortrait(activity); // 竖屏
        if (activity instanceof AppCompatActivity) { // titleBar
            ScreenUtils.requestNoTitle((AppCompatActivity) activity);
        }
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 动画
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            window.setEnterTransition(new Fade());
            window.setExitTransition(new Fade());
        }
    }

    /*内存，进程，服务，任务*/
    private static ActivityManager getActivityManager(Context context) {
        return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
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
                from.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
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
                to.overridePendingTransition(kitkatAnimIn, kitkatAnimOut);
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
        } else {
            activity.finish();
        }
    }

    /**
     * ***********************************运存***********************************
     * <p>
     * 获取手机内存信息
     */
    private static ActivityManager.MemoryInfo getMemoryInfo(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(memoryInfo);
        return memoryInfo;
    }

    /**
     * 获取总共运存
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static String getTotalMem(Context context) {
        return Formatter.formatFileSize(context, getMemoryInfo(context).totalMem);
    }

    /**
     * 获取可用运存
     */
    public static String getAvailMem(Context context) {
        return Formatter.formatFileSize(context, getMemoryInfo(context).availMem);
    }

    /**
     * 系统内存不足的阀值，即临界值
     */
    public static String getThreshold(Context context) {
        return Formatter.formatFileSize(context, getMemoryInfo(context).threshold);
    }

    /**
     * 如果当前可用内存 <= threshold，该值为真
     */
    public static boolean isLowMemory(Context context) {
        return getMemoryInfo(context).availMem <= getMemoryInfo(context).threshold;
    }


    /**
     * ***********************************进程***********************************
     * <p>
     * 获取运行的进程信息
     */
    public static List<ActivityManager.RunningAppProcessInfo> getRunningProcesses(Context context) {
        return getActivityManager(context).getRunningAppProcesses();
    }

    /**
     * packageName 系统进程是杀不死的，只能杀死用户进程。
     */
    public static void killBackgroundProcess(Context context, String packageName) {
        getActivityManager(context).killBackgroundProcesses(packageName);
    }

    /**
     * 进程名，默认是包名或者由android:process=””属性指定
     */
    public static String getProcessName(ActivityManager.RunningAppProcessInfo processInfo) {
        return processInfo.processName;
    }

    /**
     * 进程ID
     */
    public static int getProcessPid(ActivityManager.RunningAppProcessInfo processInfo) {
        return processInfo.pid;
    }

    /**
     * 进程所在的用户ID
     */
    public static int getProcessUid(ActivityManager.RunningAppProcessInfo processInfo) {
        return processInfo.uid;
    }

    /**
     * 运行在该进程下的所有应用程序包名
     */
    public static String[] getProcessPkgList(ActivityManager.RunningAppProcessInfo processInfo) {
        return processInfo.pkgList;
    }

    /**
     * 获取指定process的运存
     */
    public static int getRunMemory(Context context, ActivityManager.RunningAppProcessInfo processInfo) {
        Debug.MemoryInfo[] memoryInfo = getActivityManager(context)
                .getProcessMemoryInfo(new int[]{processInfo.pid});
        return memoryInfo[0].getTotalPrivateDirty() * 1024;
    }

    /**
     * ***********************************服务***********************************
     * <p>
     * 获取运行的服务信息
     */
    public static List<ActivityManager.RunningServiceInfo> getRunningServices(Context context, int maxNum) {
        return getActivityManager(context).getRunningServices(maxNum);
    }

    /**
     * 进程名，默认是包名或者由属性android：process指定
     */
    public static String getName(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.process;
    }

    /**
     * 获得该Service的组件信息 包含了packageName / serviceName信息
     */
    public static ComponentName getComponentName(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.service;
    }

    /**
     * runningServiceInfo 停止该服务
     */
    public static void stopService(Context context, ActivityManager.RunningServiceInfo runningServiceInfo) {
        Intent intent = new Intent();
        intent.setComponent(getComponentName(runningServiceInfo));
        context.stopService(intent);
    }

    /**
     * 若为true，则该服务在后台执行
     */
    public static boolean isForeground(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.foreground;
    }

    /**
     * 如果不为0，表示该service所在的进程ID号( PS:为0的话我也不清楚 - - 求指点)
     */
    public static int getServicePid(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.pid;
    }

    /**
     * 用户ID 类似于Linux的用户权限，例如root等
     */
    public static int getServiceUid(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.uid;
    }

    /**
     * 服务第一次被激活的时间, 包括启动和绑定方式
     */
    public static long getActiveSince(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.activeSince;
    }

    /**
     * 服务运行期间，出现死机的次数
     */
    public static int getCrashCount(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.crashCount;
    }

    /**
     * 如果该Service是通过Bind方法方式连接，则clientCount代表了service连接客户端的数目
     */
    public static int getClientCount(ActivityManager.RunningServiceInfo runningServiceInfo) {
        return runningServiceInfo.clientCount;
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
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, 0);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        int size = context.getPackageManager().queryIntentActivities(intent, 0).size();
        return !(resolveInfo == null || componentName == null || size == 0);
    }

}
