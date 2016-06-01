package com.bjxrgz.startup.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.transition.ChangeBounds;
import android.transition.ChangeClipBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeScroll;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import java.util.List;

/**
 * Created by fd.meng on 2014/03/30
 * <p/>
 * AnimUtils: 动画处理类
 * 1.帧动画 AnimationDrawable ---> 是drawable的子类
 * 2.补间动画 Animation ---> 四大属性类
 * 3.属性动画 Animator ---> ValueAnimator(TimeAnimator + ObjectAnimator)
 * 4.切换动画 ViewAnimator ---> ViewFlipper + ViewSwitcher(ImageSwitcher + TextSwitcher)
 * 5.过渡动画 Transition ---> 5.0布局之间切换的高级动画
 */
public class AnimUtils {

    // 渐变property
    public static final String ALPHA = "alpha";
    // 缩放property
    public static final String SCALE_X = "scaleX";
    public static final String SCALE_Y = "scaleY";
    // 移动property
    public static final String TRANSLATE_X = "translationX";
    public static final String TRANSLATE_Y = "translationY";
    public static final String TRANSLATE_Z = "translationZ"; //看清楚有Y轴
    // 旋转property, 看清楚是轴，不是点，所有有立体效果
    public static final String ROTATION = "rotation";//以自身中心做中心点旋转
    public static final String ROTATION_X = "rotationX";//以X轴做中心线旋转
    public static final String ROTATION_Y = "rotationY";//以Y轴做中心线旋转
    // trans的标志
    public static final int TRANS_EXPLODE = 1;  // 随机边缘进出
    public static final int TRANS_SLIDE = 2;    // 指定边缘进出
    public static final int TRANS_FADE = 3;     // 透明度渐变进出

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>帧动画<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     *
     * @param image      加载帧动画的imageView
     * @param animListID res的drawable目录下定义：下面的true为重复播放
     *                   <animation-list android:oneshot="true" >
     *                   <item
     *                   android:drawable="@drawable/on_001"
     *                   android:duration="100"/>
     *                   .........
     *                   </animation-list>
     * @return Drawable可以强转成AnimationDrawable , animation.start(); animation.stop();
     */
    public static AnimationDrawable getAnimationDrawable(Context context, ImageView image, int animListID) {

        AnimationDrawable animation = (AnimationDrawable) ContextCompat.getDrawable(context, animListID);

        image.setImageDrawable(animation);

        return animation;
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>补间动画<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p/>
     * 渐变动画
     */
    public static AlphaAnimation getAlpha(float from, float to) {

        return new AlphaAnimation(from, to);
    }

    /**
     * 移动动画
     */
    public static TranslateAnimation getTranslate(float fromX, float toX,
                                                  float fromY, float toY) {
        return new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, fromX,
                Animation.RELATIVE_TO_SELF, toX,
                Animation.RELATIVE_TO_SELF, fromY,
                Animation.RELATIVE_TO_SELF, toY);
    }

    /**
     * 旋转动画
     */
    public static RotateAnimation getRotate(float from, float to, float pivotX, float pivotY) {

        return new RotateAnimation(from, to, Animation.RELATIVE_TO_SELF,
                pivotX, Animation.RELATIVE_TO_SELF, pivotY);
    }

    /**
     * 缩放动画
     */
    public static ScaleAnimation getScale(float fromX, float toX, float fromY,
                                          float toY, float pivotX, float pivotY) {

        return new ScaleAnimation(fromX, toX, fromY, toY,
                Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);
    }

    /**
     * 多种动画效果叠加, view.setAnimation(AnimationSet); set.start();
     */
    public static AnimationSet getAnimationSet(long offset, long duration, int repeat,
                                               boolean fill, int mode, Animation... items) {
        AnimationSet set = new AnimationSet(true);
        for (Animation param : items) {
            set.addAnimation(param);
        }

        set.setStartOffset(offset); // 开始延迟时间
        set.setDuration(duration); // 持续时间
        set.setRepeatCount(repeat); // 重复次数
        set.setFillAfter(fill); // 是否保持在结束的位置
        set.setRepeatMode(mode); // 重复模式？
        set.setInterpolator(new DecelerateInterpolator());//此处为减速

        return set;
    }

    /**
     * 补间动画是最后和view绑定和start
     */
    public static void startAnimation(View view, Animation animation) {
        // view.setAnimation(animation);
        // animation.start();
        view.startAnimation(animation);
    }

    /**
     * 监听开始, 结束, 重复
     */
    public static void setListener(Animation animation, Animation.AnimationListener listener) {

        animation.setAnimationListener(listener);
    }

    /**
     * 补间动画group使用，ViewGroup.setLayoutAnimation(lac);
     */
    public static LayoutAnimationController getGroupAnimation(AnimationSet set, float delay) {

        LayoutAnimationController lac = new LayoutAnimationController(set);
        lac.setDelay(delay); // 开始延迟时间
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL); // 正常顺序
        lac.setInterpolator(new LinearInterpolator()); // 正常速率

        return lac;
    }

    /**
     * ViewGroup启动当时不一样
     */
    public static void startLayoutAnimation(ViewGroup group,
                                            LayoutAnimationController controller) {

        group.setLayoutAnimation(controller);
        group.startLayoutAnimation();
    }

    /**
     * 监听开始, 结束, 重复, 看清楚和view的加载不一样，调用者不一样
     */
    public static void setLayoutAnimationListener(ViewGroup group,
                                                  Animation.AnimationListener listener) {

        group.setLayoutAnimationListener(listener);
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>属性动画<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     *
     * @param view     执行动画的view，补间动画可以最后设置view
     * @param property 看上面的标志
     * @param values   除了起始值和终点值之外，还可以有过渡值
     */
    public static ObjectAnimator getObject(View view, String property, float... values) {

        return ObjectAnimator.ofFloat(view, property, values);
    }

    /**
     * set.start(); set.pause(); set.cancel(); set.end(); ...
     */
    public static AnimatorSet getObjectSet(boolean together, long startDelay,
                                           long duration, Animator... items) {
        AnimatorSet set = new AnimatorSet();

        set.setStartDelay(startDelay); // 开始延迟时间
        set.setDuration(duration); // 持续时间
        set.setInterpolator(new DecelerateInterpolator());//此处为减速
        if (together) {
            set.playTogether(items); // 一起执行
        } else {
            set.playSequentially(items); // 一个一个执行
        }
        return set;
    }

    /**
     * 属性动画group使用，ViewGroup.setLayoutTransition(transition);
     */
    public static LayoutTransition getGroupAnimator(long duration, Animator appear,
                                                    Animator disappear) {
        LayoutTransition transition = new LayoutTransition();

        transition.setDuration(duration);
        transition.setAnimator(LayoutTransition.APPEARING, appear);
        transition.setAnimator(LayoutTransition.DISAPPEARING, disappear);

        return transition;
    }

    /**
     * android5.0的水波纹效果也是用的Animator实现的
     * android:background="?android:attr/selectableItemBackground"波纹有边界
     * android:background="?android:attr/selectableItemBackgroundBorderless"波纹超出边界
     * android:colorControlHighlight：设置波纹颜色
     *
     * @param view        可以在View上做水波纹
     * @param centerX     这里的(0,0)是view的左上角
     * @param centerY     (view.getWidth() / 2，view.getHeight() / 2)为中心点
     * @param startRadius 圆形开始的半径
     * @param endRadius   结束时候的半径
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Animator getCircular(View view, int centerX, int centerY,
                                       float startRadius, float endRadius) {

        return ViewAnimationUtils.createCircularReveal(view,
                centerX, centerY, startRadius, endRadius);
    }

    /**
     * 监听开始，结束，取消，重复
     */
    public static void addListener(ValueAnimator animator, Animator.AnimatorListener listener) {

        animator.addListener(listener);
    }

    public static void removeListeners(ValueAnimator animator) {

        animator.removeAllListeners();
    }

    /**
     * ObjectAnimator继承自ValueAnimator吧，但ValueAnimator没有操作view四大属性的方法
     * 看看底下这个方法，就是给ValueAnimator用的，可以更灵活的操作view的各种属性
     */
    public static void addUpdateListener(ValueAnimator animator,
                                         ValueAnimator.AnimatorUpdateListener listener) {

        animator.addUpdateListener(listener);
    }

    public static void removeUpdateListeners(ValueAnimator animator) {

        animator.removeAllUpdateListeners();
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>切换动画<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p/>
     * 除了可以xml里添加切换的view之外，还可以代码中动态添加
     */
    public static void addViewAnimatorChild(ViewAnimator animator, View child, int index,
                                            ViewGroup.LayoutParams params) {

        animator.addView(child, index, params);
    }

    /**
     * 开始幻灯片效果 flipper.startFlipping() 暂停 flipper.stopFlipping()
     * 也可animator.showNext() 和 animator.showPrevious()
     */
    public static ViewFlipper loadFlipper(ViewFlipper flipper, Context context,
                                          int delay, boolean auto) {
        // 也可以AnimationUtils.load创建动画
        flipper.setInAnimation(AnimationUtils.makeInAnimation(context, true));
        flipper.setOutAnimation(AnimationUtils.makeOutAnimation(context, true));
        flipper.setFlipInterval(delay);// 切换时间间隔
        flipper.setAutoStart(auto); // 是否自动幻灯片

        return flipper;
    }

    /**
     * 可通过switcher.setImageDrawable(list.get(X)) 来进行切换
     * 也可animator.showNext() 和 animator.showPrevious()
     */
    public static ImageSwitcher loadImageSwitcher(ImageSwitcher switcher,
                                                  final Context context, List<Drawable> list) {
        // 也可以AnimationUtils.load创建动画
        switcher.setInAnimation(AnimationUtils.makeInAnimation(context, true));
        switcher.setOutAnimation(AnimationUtils.makeOutAnimation(context, true));

        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(context);
            }
        });
        if (list != null && list.size() != 0)
            switcher.setImageDrawable(list.get(0)); // 开始时的展现

        return switcher;
    }

    /**
     * 可通过switcher.setCurrentText(list.get(X)) 来进行切换
     * 也可animator.showNext() 和 animator.showPrevious()
     */
    public static TextSwitcher LoadTextSwitcher(TextSwitcher switcher,
                                                final Context context, List<String> list) {
        // 也可以AnimationUtils.load创建动画
        switcher.setInAnimation(AnimationUtils.makeInAnimation(context, true));
        switcher.setOutAnimation(AnimationUtils.makeOutAnimation(context, true));

        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new TextView(context);
            }
        });
        if (list != null && list.size() != 0)
            switcher.setCurrentText(list.get(0)); // 开始时的展现

        return switcher;
    }

    /**
     * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>过渡动画<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     * <p/>
     * 用于切换的场景,就是布局
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Scene getScene(ViewGroup parent, int layoutID, Context context) {

        return Scene.getSceneForLayout(parent, layoutID, context);
    }

    /**
     * 获取自定义的Trans,也可以auto生成
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Transition getTrans(long duration, boolean together, int mode) {
        TransitionSet set = new TransitionSet();
        set.setDuration(duration);
        set.setInterpolator(new LinearInterpolator());

        if (together)
            set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        else
            set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        if (mode == TRANS_EXPLODE)
            set.addTransition(new Explode());
        else if (mode == TRANS_FADE)
            set.addTransition(new Fade(Fade.OUT)).addTransition(new Fade(Fade.IN));
        else if (mode == TRANS_SLIDE)
            set.addTransition(new Slide(Gravity.BOTTOM));

        set.addTransition(new ChangeBounds()); // 捕捉到边界
        set.addTransition(new ChangeTransform()); // 捕捉到旋转
        set.addTransition(new ChangeImageTransform());  // 捕捉Matrix
        set.addTransition(new ChangeClipBounds()); //  捕捉裁剪
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            set.addTransition(new ChangeScroll()); // 捕捉滚动
        }
        return set;
    }

    /**
     * 监听开始、结束、取消、暂停、结束
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void addListener(Transition transition, Transition.TransitionListener listener) {

        transition.addListener(listener);
    }

    /**
     * 使用默认的new AutoTransition()来启动的
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void goTrans(Scene scene) {

        TransitionManager.go(scene);
    }

    /**
     * 使用的自定义的Transition来启动的
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void goTrans(Scene scene, Transition transition) {

        TransitionManager.go(scene, transition);
    }

}
