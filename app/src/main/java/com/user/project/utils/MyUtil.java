package com.user.project.utils;

import com.bjxrgz.base.utils.ActivityUtil;
import com.bjxrgz.base.utils.ConstantUtil;
import com.bjxrgz.base.utils.GsonUtil;
import com.bjxrgz.base.utils.StringUtil;
import com.bjxrgz.base.utils.ToastUtil;
import com.user.project.R;
import com.user.project.domain.HttpError;

/**
 * Created by gg on 2017/2/28.
 * 符合本项目的工具类
 */
public class MyUtil {
    public static final long IMG_SIZE = ConstantUtil.KB * 200; // 图片最大尺寸

    public static final int REQUEST_CAMERA = 191;  // 相机
    public static final int REQUEST_PICTURE = 192;  // 图库
    public static final int REQUEST_CROP = 193;  // 裁剪
    public static final int REQUEST_SCAN = 194;  // 扫描

    public static boolean noLogin() {
        String userToken = SPUtil.getUser().getUserToken();
        return StringUtil.isEmpty(userToken);
    }

    public static void httpFailure(int httpCode, String errorMessage) {
        switch (httpCode) {
            case -1: //请求异常
                ToastUtil.showShortToast(errorMessage);
                break;
            case 401: // 用户验证失败
                ToastUtil.showShortToast(R.string.http_response_error_401);
//                LoginActivity.goActivity(MyApp.get());
                break;
            case 403: // API AliKey 不正确 或者没给
                ToastUtil.showShortToast(R.string.http_response_error_403);
                break;
            case 404: // 404
                ToastUtil.showShortToast(R.string.http_response_error_404);
                break;
            case 409: // 用户版本过低, 应该禁止用户登录，并提示用户升级
                ToastUtil.showShortToast(R.string.http_response_error_409);
                SPUtil.clearUser();
                break;
            case 410: // 用户被禁用,请求数据的时候得到该 ErrorCode, 应该退出应用
                ToastUtil.showShortToast(R.string.http_response_error_410);
                ActivityUtil.closeActivities();
                break;
            case 417: // 逻辑错误，必须返回错误信息
                HttpError httpError = GsonUtil.get().fromJson(errorMessage, HttpError.class);
                int errorCode = -1;
                if (httpError != null) {
                    ToastUtil.showShortToast(httpError.getMessage());
                    errorCode = httpError.getErrorCode();
                }
                switch (errorCode) {
                    case 1001: // 1001: 用户被锁定
                        ActivityUtil.closeTopActivity();
                        break;
                }
                break;
            case 500: // 500
                ToastUtil.showShortToast(R.string.http_response_error_500);
                break;
            default: // 其他错误
                ToastUtil.showShortToast(R.string.http_response_error);
                break;
        }
    }

}
