package com.bjxrgz.startup.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;

/**
 * Created by Jiang on 2014/07/01
 * <p>
 * 处理  view的增删，更新，状态，以及监听事件
 */
public class ViewUtils {

    /**
     * 窗口视图操作
     */
    public static WindowManager getWindowManager(Context context) {

        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 剪切view , 还有设置四大属性的方法，太多了 不封装了
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void clipView(View view, ViewOutlineProvider provider) {
        // 设置Outline , provider在外部自己实现
        view.setOutlineProvider(provider);
        // 剔除Outline以外的view ,可以起裁剪作用
        view.setClipToOutline(true);
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>增删<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * activity中添加视图，会删除就视图,
     * 所有的视图都删除，换肤要注意了
     */
    public static void setContentView(Activity activity, View view, ViewGroup.LayoutParams params) {
        if (params == null)
            activity.setContentView(view);
        else
            activity.setContentView(view, params);
    }

    /**
     * activity中添加视图，不会删除就视图
     */
    public static void addContentView(Activity activity, View view, ViewGroup.LayoutParams params) {

        activity.addContentView(view, params);
    }

    /**
     * window添加view
     */
    public static void addWindowView(Context context, View view,
                                     WindowManager.LayoutParams params) {

        getWindowManager(context).addView(view, params);
    }

    /**
     * window移除view
     */
    public static void removeWindowView(Context context, View view) {

        getWindowManager(context).removeView(view);
    }

    /**
     * ViewGroup添加view
     */
    public static void addView(ViewGroup group, View child, ViewGroup.LayoutParams params) {

        group.addView(child, params);
    }

    /**
     * ViewGroup删除view
     */
    public static void removeView(ViewGroup group, View child) {
        if (child != null)
            group.removeView(child);
        else
            group.removeAllViews();
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>更新<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 刷新，以下更新分为params更新和location更新
     */
    public static void initView(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (view.isInLayout()) {
                view.requestLayout();
            }
        }
        view.invalidate();
    }

    /**
     * 更新view在window的Params
     */
    public static void setParamsInWindow(Context context, View view,
                                         WindowManager.LayoutParams params) {

        getWindowManager(context).updateViewLayout(view, params);
    }

    /**
     * 更新view在Group的Params
     */
    public static void setParamsIngroup(ViewGroup group, View child,
                                        ViewGroup.LayoutParams params) {

        group.updateViewLayout(child, params);
    }

    /**
     * 更新view的Params
     */
    public static void setParams(View view, ViewGroup.LayoutParams params) {

        view.setLayoutParams(params);// 可以get
    }

    /**
     * 获取在window的Location
     */
    public static int[] getLocationInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    /**
     * 获取在parent的Location
     */
    public static int[] getLocation(View view) {
        int x = (int) view.getX();
        int y = (int) view.getY();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int z = (int) view.getZ();
            return new int[]{x, y, z};
        }
        return new int[]{x, y};
    }

    /**
     * 设置在parent的Location
     */
    public static void setLocation(View view, int[] location) {
        view.setX(location[0]);
        view.setY(location[1]);
        if (location.length == 3 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setZ(location[2]); // 这里的海拔是增加了的海拔,只是相对于parent的海拔
            // view.setElevation 是真正的海拔指定
        }
    }

    /**
     * 获取在parent的view的中心Location
     */
    public static int[] getLocationPivot(View view) {
        int pivotX = (int) view.getPivotX();
        int pivotY = (int) view.getPivotY();
        return new int[]{pivotX, pivotY};
    }

    /**
     * 设置在parent的view的中心Location
     */
    public static void setLocationPivot(View view, int[] location) {
        view.setPivotX(location[0]);
        view.setPivotY(location[1]);
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>状态<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 可见度设置
     */
    public static void setVisiable(View view, int visibility) {
        view.getVisibility();
        if (visibility == View.VISIBLE) {
            if (!view.isShown())
                view.setVisibility(visibility);
        }
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            if (view.isShown())
                view.setVisibility(visibility);
        }
    }

    /**
     * 选中操作
     */
    public static void setSelected(View view, boolean selected) {
        if (!view.isEnabled())
            view.setEnabled(true);
        view.setSelected(selected);
    }

    /**
     * 按下操作
     */
    public static void setPress(View view, boolean pressed) {
        if (!view.isEnabled())
            view.setEnabled(true);
        view.setPressed(pressed);
    }

    /**
     * 设置焦点
     */
    public static void setFocus(View view, boolean focus) {
        if (!view.isFocusable()) {
            view.setFocusable(true);
        }
        if (!view.isInTouchMode()) {
            view.setFocusableInTouchMode(true);
        }
        if (focus) {
            if (!view.isFocused()) {
                view.requestFocus();
            }
        } else {
            if (view.isFocused())
                view.clearFocus();
        }
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>事件<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p>
     * 点击事件
     */
    public static void setClickListener(View view, View.OnClickListener listener) {
        if (!view.isClickable())
            view.setClickable(true);

        view.setOnClickListener(listener);
    }

    /**
     * 长按事件，ContextMenu, 拖拽, ActionMode
     */
    public static void setLongClickListener(View view, View.OnLongClickListener listener) {
        if (!view.isLongClickable())
            view.setLongClickable(true);

        view.setOnLongClickListener(listener);
    }

    /**
     * 触摸事件 ,可以外部调用监听事件，也可以内部重写onTouchEvent方法
     */
    public static void setTouchListener(View view, View.OnTouchListener listener) {
        if (!view.isFocusable()) {
            view.setFocusable(true);
        }
        if (!view.isInTouchMode()) {
            view.setFocusableInTouchMode(true);
        }
        view.setOnTouchListener(listener);
    }

    /**
     * 手势操作 , 注意view的众多事件重写方法中，唯独没有这个
     * 1.onDown轻触 2.onShowPress轻触未松开 3.onSingleTapUp轻触已松开
     * 4.onScroll滑动 5.onLongPress长按 6.onFling快速滑动
     */
    public static void setGestureListener(Context context, View view, GestureDetector.OnGestureListener listener) {

        final GestureDetector detector = new GestureDetector(context, listener);

        setTouchListener(view, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
    }

    /**
     * 拖拽事件 ,也可以内部重写onDragEvent方法 ,拖拽范围仅仅在parent边界之内
     */
    public static void setDragListener(final View child, final ViewGroup parent,
                                       final DragCallBack callBack) {
        // 1.首先在外部给被拖拽的view设置一个TAG
        if (child.getTag() == null) {
            child.setTag(child.toString());
        }
        // 2.先设置长按事件，触发拖拽事件
        setLongClickListener(child, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 3.然后是在LongClick事件里创建一个剪切板，供移动使用
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);

                // 4.其次是创建一个被拖拽的view的阴影
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

                // 5.最后是开始拖拽效果，执行onDragEvent()，切记需要父布局调用
                parent.startDrag(dragData, myShadow, null, 0);
                return true;
            }
        });
        // 6.实现触发内容
        final int[] start_x = new int[2];
        final int[] start_y = new int[2];
        parent.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                // 记住这里的view不是父布局，而是被拖拽的view
                ViewGroup.LayoutParams params = child.getLayoutParams();
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        // 开始，一般无操作
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        // 接触点进入view之内, event.getX()有值
                        start_x[0] = (int) event.getX();
                        start_y[0] = (int) event.getY();
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        // 接触点移动,并且接触点在view之内时调用, event.getX()有值
                        start_x[1] = (int) event.getX();
                        start_y[1] = (int) event.getY();

                        callBack.move(params, start_x[1], start_y[1]);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        // 接触点移出view边界，一般无操作
                        break;
                    case DragEvent.ACTION_DROP:
                        // 接触点在view之内，并且在view边界之内, event.getX()有值
                        int move_x = start_x[1] - start_x[0];
                        int move_y = start_y[1] - start_y[0];

                        callBack.drop(params, move_x, move_y);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        // 结束，一般无操作
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 拖拽回调事件
     */
    public interface DragCallBack {

        /**
         * 别忘了强转params成parent的类型,移动过程中的位置
         */
        void move(ViewGroup.LayoutParams params, int inX, int inY);

        /**
         * 别忘了强转params成parent的类型,移动了多少距离
         * layoutParams.leftMargin += move_x;
         * layoutParams.topMargin += move_y;
         * btn4.setLayoutParams(layoutParams);
         */
        void drop(ViewGroup.LayoutParams params, int byX, int byY);
    }

}
