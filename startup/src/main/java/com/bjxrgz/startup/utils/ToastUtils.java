package com.bjxrgz.startup.utils;

import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bjxrgz.startup.base.MyApp;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe toast工具类
 */
public class ToastUtils {

    private static Toast toast;

    /* Toast 单例 可覆盖 */
    public static void toast(final CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            MyApp.get().getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (toast == null) {
                        toast = Toast.makeText(MyApp.get(), message, Toast.LENGTH_SHORT);
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
        String show = MyApp.get().getString(message);
        toast(show);
    }

}