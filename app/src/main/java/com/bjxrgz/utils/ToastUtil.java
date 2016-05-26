package com.bjxrgz.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by fd.meng on 2014/03/30
 *
 * 处理 Toast
 *
 */
public class ToastUtil {

    /**
     * toast 文字
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, String message) {
        if (null != context && !TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * toast 文字
     *
     * @param context
     * @param message
     */
    public static void showToast(Context context, int message) {
        if (null != context && message != 0) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

}
