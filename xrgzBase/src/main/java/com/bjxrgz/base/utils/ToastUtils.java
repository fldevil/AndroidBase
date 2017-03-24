package com.bjxrgz.base.utils;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bjxrgz.base.R;
import com.bjxrgz.base.base.BaseApp;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe toast工具类
 */
public class ToastUtils {
    private static ToastUtils instance;
    private static Toast toast;

    /* Toast 单例 可覆盖 */
    public static ToastUtils get() {
        if (instance == null) {
            synchronized (ToastUtils.class) {
                if (instance == null) {
                    instance = new ToastUtils();
                }
            }
        }
        if (toast == null) {
            synchronized (ToastUtils.class) {
                if (toast == null) {
                    toast = getToast();
                }
            }
        }
        return instance;
    }

    /* 自定义Toast */
    private static Toast getToast() {
        View inflate = LayoutInflater.from(BaseApp.get()).inflate(R.layout.toast, null);
        Toast toast = new Toast(BaseApp.get());
        toast.setView(inflate);
        return toast;
    }

    public void show(final CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        BaseApp.get().getHandler().post(new Runnable() {
            @Override
            public void run() {
                TextView tvToast = (TextView) toast.getView().findViewById(R.id.tvToast);
                tvToast.setText(message);
                toast.show();
            }
        });
    }

    public void show(int resId) {
        if (resId == 0) return;
        String toast = BaseApp.get().getString(resId);
        show(toast);
    }
}