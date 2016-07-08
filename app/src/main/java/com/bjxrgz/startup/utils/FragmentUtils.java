package com.bjxrgz.startup.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by fd.meng on 2014/03/30
 * <p/>
 * Fragment处理类, 包括FragmentManager的获取
 */
public class FragmentUtils {

    /**
     * 通过tag发现存在的fragment, 另一个方法是通过xml的id获得fragment
     */
    public static Fragment findFragment(FragmentManager manager, String tag) {

        return manager.findFragmentByTag(tag);
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
    public static void add(FragmentManager manager, Fragment fragment,
                           int addID, String tag, boolean stack) {
        if (fragment.isAdded()) {
            return;
        }
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(addID, fragment, tag);
        if (stack)
            transaction.addToBackStack(null);
        // commit方法一定要在Activity.onSaveInstance()之前调用
        transaction.commit();
    }

    /**
     * remove会执行到detach
     */
    public static void remove(FragmentManager manager, Fragment fragment, boolean stack) {
        if (!fragment.isAdded()) {
            return;
        }
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        if (stack)
            transaction.addToBackStack(null);
        // commit方法一定要在Activity.onSaveInstance()之前调用
        transaction.commit();
    }

    /**
     * 使用另一个Fragment替换当前的，实际上就是remove()然后add()的合体
     */
    public static void replace(FragmentManager manager, Fragment fragment,
                               int replaceId, String tag, boolean stack) {
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(replaceId, fragment, tag);
        if (stack)
            transaction.addToBackStack(null);
        // commit方法一定要在Activity.onSaveInstance()之前调用
        transaction.commit();
        // 异步执行？
        // manager.executePendingTransactions();
    }

    /**
     * 使用另一个Fragment替换当前的，实际上就是remove()然后add()的合体
     */
    public static void replace(FragmentManager manager, Fragment fragment, int replaceId) {
        // fragment事务，被忘了commit
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(replaceId, fragment);
        // commit方法一定要在Activity.onSaveInstance()之前调用
        transaction.commit();
        // 异步执行？
        // manager.executePendingTransactions();
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
        if (stack)
            transaction.addToBackStack(null);
        // commit方法一定要在Activity.onSaveInstance()之前调用
        transaction.commit();
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
        if (stack)
            transaction.addToBackStack(null);
        // commit方法一定要在Activity.onSaveInstance()之前调用
        transaction.commit();
    }

}
