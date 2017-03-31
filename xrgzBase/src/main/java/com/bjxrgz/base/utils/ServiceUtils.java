package com.bjxrgz.base.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by gg on 2017/3/13.
 * 服务管理
 */
public class ServiceUtils {

    /**
     * 判断服务是否运行
     *
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     */
    public static boolean isServiceWork(Context context, String serviceName) {
        ActivityManager myAM = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList != null && myList.size() > 0) {
            for (ActivityManager.RunningServiceInfo serviceInfo : myList) {
                if (serviceInfo.service.getClassName().equals(serviceName)) return true;
            }
        }
        return false;
    }

}
