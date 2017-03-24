package com.bjxrgz.base.utils;

import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bjxrgz.base.base.BaseApp;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe toast工具类
 */
public class ToastUtils {
    private static Toast toast;

    /* Toast 单例 可覆盖 */
    public static void toast(final CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            BaseApp.get().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (toast == null) {
                        toast = Toast.makeText(BaseApp.get(), message, Toast.LENGTH_SHORT);
                    } else {
                        TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
                        tv.setText(message);
                    }
                    toast.show();
                }
            });
        }
    }

    public static void toast(int message) {
        String toast = BaseApp.get().getString(message);
        toast(toast);
    }

    /* 应该有自定义的方法 */
}