package com.bjxrgz.base.utils;

import android.widget.Toast;

import com.bjxrgz.base.BaseApp;

/**
 * @author Fan
 *         toast 工具类
 */
public class ToastUtil {
    private ToastUtil() {
    }

    /**
     * 长时Toast
     */
    public static void showLongToast(String msg) {
        Toast.makeText(BaseApp.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时Toast
     */
    public static void showLongToast(int msg) {
        Toast.makeText(BaseApp.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时Toast
     */
    public static void showShortToast(String msg) {
        Toast.makeText(BaseApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时Toast
     */
    public static void showShortToast(int msg) {
        Toast.makeText(BaseApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}