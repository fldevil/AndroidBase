package com.bjxrgz.project.utils;

import com.bjxrgz.base.domain.HttpError;
import com.bjxrgz.base.utils.ActivityUtils;
import com.bjxrgz.base.utils.GsonUtils;
import com.bjxrgz.base.utils.SPUtils;
import com.bjxrgz.base.utils.StringUtils;
import com.bjxrgz.base.utils.ToastUtils;
import com.bjxrgz.project.MyApp;
import com.bjxrgz.project.service.UpdateService;

/**
 * Created by gg on 2017/2/28.
 * 符合本项目的工具类
 */
public class MyUtils {

    public static final int REQUEST_CAMERA = 191;  // 相机
    public static final int REQUEST_PICTURE = 192;  // 图库
    public static final int REQUEST_CROP = 193;  // 裁剪

    public static boolean noLogin() {
        String userToken = SPUtils.getUser().getUserToken();
        return StringUtils.isEmpty(userToken);
    }

    public static void httpFailure(int httpCode, String errorMessage) {
        String toast = "网络响应异常";
        switch (httpCode) {
            case 401: // 用户验证失败
                toast = "用户验证失败";
//                LoginActivity.goActivity(MyApp.get());
                break;
            case 403: // API AliKey 不正确 或者没给
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
