package com.bjxrgz.project.utils;

import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.ToastUtils;
import com.bjxrgz.project.service.UpdateService;
import com.bjxrgz.start.base.MyApp;

/**
 * Created by gg on 2017/2/28.
 * 符合本项目的工具类
 */
public class MyUtils {
    public static void httpFailure(int httpCode, int errorCode, String errorMessage) {
        ToastUtils.toast(errorMessage);
        switch (httpCode) {
            case 401:
                // login
                break;
            case 409:
                UpdateService.goService(MyApp.get());
                break;
            case 410:
                ActivityUtils.closeActivities();
                break;
            case 417:
                switch (errorCode) {
                    case 1001:
                        ActivityUtils.closeTopActivity();
                        break;
                }
                break;
        }
    }
}
