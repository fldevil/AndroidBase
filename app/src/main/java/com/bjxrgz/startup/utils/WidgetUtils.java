package com.bjxrgz.startup.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * Created by fd.meng on 2014/03/30
 * <p/>
 * 处理  Toast, SnackBar, popWindow, popMenu,
 */
public class WidgetUtils {

    /**
     * Toast
     */
    public static void showToast(Context context, String message) {
        if (null != context && !TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showToast(Context context, int message) {
        if (null != context && message != 0) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * SnackBar
     */
    public static void showSnaker(View view, String message, String action,
                                  View.OnClickListener listener) {

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(action, listener).show();
    }

    public static void showSnaker(View view, int message, String action,
                                  View.OnClickListener listener) {

        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction(action, listener).show();
    }

    /**
     * 创建适配的PopupWindow，不要多次创建
     */
    public static PopupWindow creatPopWindow(View popView) {
        PopupWindow popupWindow = new PopupWindow(popView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        //还必须设置一个背景图，这里是透明背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);//点击其他地方消失
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupWindow.setEnterTransition(new AutoTransition());
            popupWindow.setExitTransition(new AutoTransition());
        }
        return popupWindow;
    }

    /**
     * 创建指定宽高的PopupWindow，不要多次创建
     */
    public static PopupWindow creatPopWindow(View popView, int width, int height) {
        PopupWindow popupWindow = new PopupWindow(popView, width, height);
        //还必须设置一个背景图，这里是透明背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);//点击其他地方消失
        return popupWindow;
    }

    /**
     * 显示popupWindow ,location可用viewUtils获取
     */
    public static void showPopWindow(PopupWindow popupWindow, int gravity, View parent, int[] location) {
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAtLocation(parent, gravity, location[0], location[1]);
        }
    }

    /**
     * 显示popupWindow ,会依附在anchor的上面
     */
    public static void showPopWindow(PopupWindow popupWindow, View anchor) {
        if (popupWindow != null && !popupWindow.isShowing()) {
            popupWindow.showAsDropDown(anchor);
        }
    }

    /**
     * 更新popupWindow宽高
     */
    public static void updatePopWindow(PopupWindow popupWindow, int width, int height) {
        if (popupWindow != null) {
            popupWindow.update(width, height);
        }
    }

    /**
     * popupWindow 移除的popupWindow
     */
    public static void dismissPopWindow(PopupWindow popupWindow) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 创建PopMenu 调用menu.show()显示
     */
    public static PopupMenu createPopMenu(Context context, View view, int menuID,
                                          PopupMenu.OnMenuItemClickListener listener) {
        PopupMenu menu = new PopupMenu(context, view);
        menu.getMenuInflater().inflate(menuID, menu.getMenu());
        menu.setOnMenuItemClickListener(listener);
        return menu;
    }

}
