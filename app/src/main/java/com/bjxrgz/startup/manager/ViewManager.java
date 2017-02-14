package com.bjxrgz.startup.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.view.MyLoading;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe 符合项目样式的View管理类
 */
public class ViewManager {

    private static Toast toast;

    /* 等待对话框 默认DialogUtils.createLoading(); */
    public static ProgressDialog createLoading(Context context) {
        ProgressDialog loading = new MyLoading(context);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(true);
        return loading;
    }

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

    public static void initTop(Activity activity, String title) {
        TextView tvCenter = (TextView) activity.findViewById(R.id.tvCenter);
        tvCenter.setVisibility(View.VISIBLE);
        tvCenter.setText(title);
    }

}