package com.bjxrgz.startup.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.transition.AutoTransition;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

/**
 * Created by Jiang on 2016/06/01
 * <p/>
 * 处理   popWindow
 */
public class PopUtils {

    /**
     * @param width  WindowManager.LayoutParams.WRAP_CONTENT
     * @param height WindowManager.LayoutParams.WRAP_CONTENT
     */
    private static PopupWindow getPop(View view, int width, int height, int anim) {
        PopupWindow pop = new PopupWindow(view, width, height);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (0 != anim) {
            pop.setAnimationStyle(anim);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pop.setEnterTransition(new AutoTransition());
            pop.setExitTransition(new AutoTransition());
        }
        pop.setOutsideTouchable(true); // 点击其他地方消失
        // 这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        // 代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        // PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        pop.setTouchable(true);
        pop.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });
        return pop;
    }

    /**
     * 创建适配的PopupWindow，不要多次创建
     */
    public static PopupWindow create(View popView) {
        return getPop(popView, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT, 0);
    }

    /**
     * 创建指定宽高的PopupWindow，不要多次创建
     */
    public static PopupWindow create(View popView, int width, int height) {
        return getPop(popView, width, height, 0);
    }

    /**
     * 显示popupWindow ,location可用viewUtils获取
     */
    public static void show(PopupWindow pop, int gravity, View parent, int offsetX, int offsetY) {
        if (pop != null && !pop.isShowing()) {
            pop.showAtLocation(parent, gravity, offsetX, offsetY);
        }
    }

    /**
     * 显示popupWindow ,会依附在anchor的下方 还有偏移量(有动画)
     */
    public static void show(PopupWindow pop, View anchor, int offsetX, int offsetY) {
        if (pop != null && !pop.isShowing()) {
            pop.showAsDropDown(anchor, offsetX, offsetY);
        }
    }

    /**
     * 更新popupWindow宽高
     */
    public static void update(PopupWindow pop, int width, int height) {
        if (pop != null) {
            pop.update(width, height);
        }
    }

    public static void update(PopupWindow pop, int x, int y, int width, int height) {
        if (pop != null) {
            pop.update(x, y, width, height);
        }
    }

    /**
     * popupWindow 移除的popupWindow
     */
    public static void dismiss(PopupWindow pop) {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
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
