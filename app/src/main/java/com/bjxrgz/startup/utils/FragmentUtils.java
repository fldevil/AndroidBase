package com.bjxrgz.startup.utils;

import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.transition.AutoTransition;

/**
 * Created by Jiang on 2016/0/01
 * <p/>
 * Fragment处理类, 包括FragmentManager的获取
 */
public class FragmentUtils {

    public static void initBaseAttach(Fragment fragment) {
        // 只要进的动画就好，出的有时候执行不完全会bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fragment.setEnterTransition(new AutoTransition());
//            fragment.setExitTransition(new AutoTransition());
            fragment.setReenterTransition(new AutoTransition());
//            fragment.setReturnTransition(new AutoTransition());
        }
    }

    /**
     * 通过tag发现存在的fragment
     */
    public static Fragment findFragment(FragmentManager manager, String tag) {
        return manager.findFragmentByTag(tag);
    }

    /**
     * 通过replaceId发现存在的fragment
     */
    public static Fragment findFragment(FragmentManager manager, int replaceId) {
        return manager.findFragmentById(replaceId);
    }

    /**
     * v4.fragment
     */
    public static FragmentManager getSupportFragmentManager(FragmentActivity activity) {
        return activity.getSupportFragmentManager();
    }

    /**
     * v4.fragment
     * 双层fragment嵌套时，顶层fragment一定要用这个Manager，否则不会保存状态
     */
    public static FragmentManager getChildFragmentManager(Fragment fragment) {
        return fragment.getChildFragmentManager();
    }

    /**
     * 添加，会遮挡主后面的
     */
    public static void add(FragmentManager manager, Fragment fragment, int addID) {
        add(manager, fragment, addID, "", false);
    }

    public static void add(FragmentManager manager, Fragment fragment,
                           int addId, String tag, boolean stack) {
        if (fragment.isAdded()) {
            return;
        }
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        if (TextUtils.isEmpty(tag)) {
            transaction.add(addId, fragment);
        } else {
            transaction.add(addId, fragment, tag);
        }
        commonTrans(transaction, stack);
    }

    /**
     * 使用另一个Fragment替换当前的，实际上就是remove()然后add()的合体
     */
    public static void replace(FragmentManager manager, Fragment fragment, int replaceId) {
        replace(manager, fragment, replaceId, "", false);
    }

    public static void replace(FragmentManager manager, Fragment fragment,
                               int replaceId, String tag, boolean stack) {
        if (fragment.isVisible()) {
            return;
        }
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        if (TextUtils.isEmpty(tag)) {
            transaction.replace(replaceId, fragment);
        } else {
            transaction.replace(replaceId, fragment, tag);
        }
        commonTrans(transaction, stack);
    }

    /**
     * remove会执行到detach
     */
    public static void remove(FragmentManager manager, Fragment fragment, boolean stack) {
        if (fragment.isDetached()) {
            return;
        }
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        commonTrans(transaction, stack);
    }

    /**
     * 可以保存状态哦, 先add/replace/show再hide
     * 只执行onPrepareOptionsMenu
     */
    public static void hide(FragmentManager manager, Fragment fragment, boolean stack) {
        if (fragment.isHidden()) {
            return;
        }
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment);
        commonTrans(transaction, stack);
    }

    /**
     * show出来之后还是之前的状态, 先hide后show
     * 只执行onPrepareOptionsMenu和onResume
     */
    public static void show(FragmentManager manager, Fragment fragment, boolean stack) {
        if (!fragment.isHidden()) {
            return;
        }
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment);
        commonTrans(transaction, stack);
    }

    private static void commonTrans(FragmentTransaction transaction, boolean stack) {
        if (stack) {
            transaction.addToBackStack(null);
        }
        // commit方法一定要在onSaveInstance()之前调用，或者以下方法，或者handler.post
        transaction.commitAllowingStateLoss();
        // 异步执行,立即执行commit()提供的transaction。
//        manager.executePendingTransactions();
    }

}
