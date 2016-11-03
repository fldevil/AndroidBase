package com.bjxrgz.startup.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.bjxrgz.startup.R;

/**
 * Created by JiangZhiGuo on 2016-11-3.
 * describe 顶部下拉伸缩View
 */

public class MyZoomScrollView extends ScrollView implements View.OnTouchListener {

    private int layoutIndex = 2; // 第几层view的顶视图需要伸缩
    private float mFirstPosition = 0; // 记录首次按下位置
    private Boolean mScaling = false; // 是否正在放大
    private View dropZoomView; // 伸缩的view
    private int dropZoomViewWidth;
    private int dropZoomViewHeight;

    /* java构造时调用 */
    public MyZoomScrollView(Context context) {
        super(context);
    }

    /* xml构造时调用 */
    public MyZoomScrollView(Context context, AttributeSet set) {
        super(context, set, 0); // 有必要
    }

    /* xml构造，并有自定义attrs时调用 */
    public MyZoomScrollView(Context context, AttributeSet set, int defStyleAttr) {
        super(context, set, defStyleAttr);
        Resources.Theme theme = context.getTheme();
        int[] attrs = R.styleable.MyZoomScrollView;
        TypedArray typedArray = theme.obtainStyledAttributes(set, attrs, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount(); // 自定义属性的数量
        for (int i = 0; i < indexCount; i++) { // 循环遍历所有的属性
            int type = typedArray.getIndex(i);
            switch (type) { // 找到自己定义的属性(如果是dimen的话 需要dp转px)
                case R.styleable.MyZoomScrollView_layout_index: // 默认是第二层子view
                    layoutIndex = typedArray.getInteger(type, 2); // 赋值
                    if (layoutIndex < 2) {
                        layoutIndex = 2;
                    }
                    break;
            }
        }
        typedArray.recycle();
    }

    /* onInflate之后执行 初始化 */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOverScrollMode(OVER_SCROLL_NEVER); // 不允许滚动超出边界
        ViewGroup group = (ViewGroup) getChildAt(0);
        for (int i = 2; i < Integer.MAX_VALUE; i++) {
            if (i < layoutIndex) { // 继续递归
                group = (ViewGroup) group.getChildAt(0);
            } else { // 当前这层viewGroup的顶View需要伸缩
                dropZoomView = group.getChildAt(0);
                setOnTouchListener(this);
            }
        }
    }

    /* 触摸机制 */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (dropZoomViewWidth <= 0 || dropZoomViewHeight <= 0) {
            dropZoomViewWidth = dropZoomView.getMeasuredWidth();
            dropZoomViewHeight = dropZoomView.getMeasuredHeight();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP: // 手指离开后恢复图片
                mScaling = false;
                replyImage();
                break;
            case MotionEvent.ACTION_MOVE: // 手指按下时计算伸缩
                if (!mScaling) {
                    if (getScrollY() == 0) {
                        mFirstPosition = event.getY(); // 滚动到顶部时记录位置，否则正常返回
                    } else {
                        break;
                    }
                }
                int distance = (int) ((event.getY() - mFirstPosition) * 0.6); // 滚动距离乘以一个系数
                if (distance < 0) { // 当前位置比记录位置要小，正常返回
                    break;
                }

                // 处理放大
                mScaling = true;
                setZoom(1 + distance);
                return true; // 返回true表示已经完成触摸事件，不再处理
        }
        return false;
    }

    /* 回弹动画 */
    public void replyImage() {
        // 计算没伸缩时候的宽度
        final float distance = dropZoomView.getMeasuredWidth() - dropZoomViewWidth;
        // 属性动画(没有设置View？)
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration((long) (distance * 0.7));
        // 动画监听(这里是用setLayoutParams来模仿属性动画吗)
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                setZoom(distance - ((distance) * cVal));
            }
        });
        anim.start();
    }

    /* 伸缩 */
    public void setZoom(float s) {
        if (dropZoomViewHeight <= 0 || dropZoomViewWidth <= 0) {
            return;
        }
        ViewGroup.LayoutParams lp = dropZoomView.getLayoutParams();
        lp.width = (int) (dropZoomViewWidth + s); // 为什么宽高的伸缩规则不一样？
        lp.height = (int) (dropZoomViewHeight * ((dropZoomViewWidth + s) / dropZoomViewWidth));
        dropZoomView.setLayoutParams(lp);
    }
}
