package com.bjxrgz.startup.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjxrgz.startup.base.MyApp;

/**
 * Created by JiangZhiGuo on 2016-11-4.
 * describe Toast构造类
 */

public class ToastUtils {

    /**
     * 显示自定义view的toast
     */
    public static void show(View view, int bgColor, int gravity, int duration) {
        Toast toast = getToast(bgColor, gravity, duration);
        if (null != view) {
            toast.setView(view);
        }
        toast.show();
    }

    /**
     * 显示text或者是img的toast
     */
    public static void show(String show, int textColor, float textSize, ImageView image,
                            int bgColor, int gravity, int duration) {
        Toast toast = getToast(bgColor, gravity, duration);
        if (!TextUtils.isEmpty(show)) {
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setText(show);
            if (0 != textColor) {
                tv.setTextColor(textColor);
            }
            if (0 != textSize) {
                tv.setTextSize(textSize);
            }
        }
        if (null != image) {
            LinearLayout layout = (LinearLayout) toast.getView();
            layout.addView(image, 0);
        }
        toast.show();
    }

    /**
     * @param bgColor  eg:Color.BLUE
     * @param gravity  eg:Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL
     * @param duration eg:Toast.LENGTH_SHORT
     */
    private static Toast getToast(int bgColor, int gravity, int duration) {
        Toast toast = new Toast(MyApp.instance);
        if (0 != bgColor) {
            LinearLayout layout = (LinearLayout) toast.getView();
            layout.setBackgroundColor(bgColor);
        }
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(duration);
        return toast;
    }

}
