package com.bjxrgz.startup.manager;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.view.MyLoading;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe 符合项目样式的View管理类
 */

public class ViewManager {

    /**
     * 等待对话框 默认DialogUtils.createLoading();
     */
    public static ProgressDialog createLoading(Context context) {
        ProgressDialog loading = new MyLoading(context);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(true);
        return loading;
    }

    /**
     * Toast 也可以ToastUtils来构造
     */
    public static void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(MyApp.instance, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(int message) {
        if (0 != message) {
            Toast.makeText(MyApp.instance, message, Toast.LENGTH_SHORT).show();
        }
    }

}
