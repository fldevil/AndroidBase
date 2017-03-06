package com.bjxrgz.project.utils;

import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.ToastUtils;
import com.bjxrgz.project.service.UpdateService;
import com.bjxrgz.start.base.MyApp;
import com.bjxrgz.start.domain.HttpError;
import com.bjxrgz.start.utils.GsonUtils;

/**
 * Created by gg on 2017/2/28.
 * 符合本项目的工具类
 */
public class MyUtils {
    public static void httpFailure(int httpCode, String errorMessage) {
        String toast = "网络响应异常";
        switch (httpCode) {
            case 401: // 用户验证失败
                toast = "用户验证失败";
                // LoginActivity.goActivity();
                break;
            case 403: // API Key 不正确 或者没给
                toast = "Key错误";
                break;
            case 404: // 404
                toast = "资源未找到";
                break;
            case 409: // 用户版本过低, 应该禁止用户登录，并提示用户升级
                toast = "用户版本过低";
                UpdateService.goService(MyApp.get());
                break;
            case 410: // 用户被禁用,请求数据的时候得到该 ErrorCode, 应该退出应用
                toast = "用户被禁用";
                ActivityUtils.closeActivities();
                break;
            case 417: // 逻辑错误，必须返回错误信息
                HttpError httpError = GsonUtils.get().fromJson(errorMessage, HttpError.class);
                int errorCode = -1;
                if (httpError != null) {
                    errorCode = httpError.getErrorCode();
                    toast = httpError.getMessage();
                }
                switch (errorCode) {
                    case 1001: // 1001: 用户被锁定
                        ActivityUtils.closeTopActivity();
                        break;
                }
                break;
            case 500: // 500
                toast = "服务器异常";
                break;
        }
        ToastUtils.toast(toast);
    }
}
