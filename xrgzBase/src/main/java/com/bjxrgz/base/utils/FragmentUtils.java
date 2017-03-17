package com.bjxrgz.base.utils;

import android.os.Build;
import android.support.v4.app.Fragment;
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
    private static final boolean anim = true; // 跳转动画开关

    public static void initAttach(Fragment fragment) {
        // 只要进的动画就好，出的有时候执行不完全会bug
        if (anim && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            fragment.setEnterTransition(new AutoTransition());
            // fragment.setExitTransition(new AutoTransition());
            fragment.setReenterTransition(new AutoTransition());
            // fragment.setReturnTransition(new AutoTransition());
        }
    }

    public static void initCreate(Fragment fragment) {
        fragment.setHasOptionsMenu(true);// Fragment与ActionBar和MenuItem集成
    }

    /**
     * 通过tag发现存在的fragment
     */
    public static Fragment find(FragmentManager manager, String tag) {
        return manager.findFragmentByTag(tag);
    }

    /**
     * 通过replaceId发现存在的fragment
     */
    public static Fragment find(FragmentManager manager, int replaceId) {
        return manager.findFragmentById(replaceId);
    }

    /**
     * 添加，会遮挡主后面的
     */
    public static void add(FragmentManager manager, Fragment fragment, int addID) {
        add(manager, fragment, addID, "", false);
    }

    public static void add(FragmentManager manager, Fragment fragment,
                           int addId, String tag, boolean stack) {
        if (fragment == null) return;
        if (fragment.isAdded()) return;
        FragmentTransaction transaction = manager.beginTransaction();
        if (TextUtils.isEmpty(tag)) {
            transaction.add(addId, fragment);
        } else {
            transaction.add(addId, fragment, tag);
        }
        commit(manager, transaction, stack, false);
    }

    /**
     * 使用另一个Fragment替换当前的，实际上就是remove()然后add()的合体
     */
    public static void replace(FragmentManager manager, Fragment fragment, int replaceId) {
        replace(manager, fragment, replaceId, "", false);
    }

    public static void replace(FragmentManager manager, Fragment fragment,
                               int replaceId, String tag, boolean stack) {
        if (fragment == null) return;
        if (fragment.isVisible()) return;
        if (fragment.isAdded()) { // isAdd 则显示(但不会刷新)
            show(manager, fragment, stack);
            return;
        }
        FragmentTransaction transaction = manager.beginTransaction();
        if (TextUtils.isEmpty(tag)) {
            transaction.replace(replaceId, fragment);
        } else {
            transaction.replace(replaceId, fragment, tag);
        }
        commit(manager, transaction, stack, false);
    }

    /**
     * remove会执行到detach
     */
    public static void remove(FragmentManager manager, Fragment fragment, boolean stack) {
        if (fragment == null) return;
        if (!fragment.isAdded()) return;
        if (fragment.isRemoving()) return;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        commit(manager, transaction, stack, false);
    }

    /**
     * 可以保存状态哦, 先add/replace/show再hide
     * 只执行onPrepareOptionsMenu
     */
    public static void hide(FragmentManager manager, Fragment fragment, boolean stack) {
        if (fragment == null) return;
        if (!fragment.isVisible()) return;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment);
        commit(manager, transaction, stack, false);
    }

    /**
     * show出来之后还是之前的状态, 先hide后show
     * 只执行onPrepareOptionsMenu和onResume
     */
    public static void show(FragmentManager manager, Fragment fragment, boolean stack) {
        if (fragment == null) return;
        if (!fragment.isAdded()) return;
        if (!fragment.isHidden()) return;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment);
        commit(manager, transaction, stack, false);
    }

    /* 事物提交 最后要commit */
    private static void commit(FragmentManager manager, FragmentTransaction transaction,
                               boolean stack, boolean multi) {
        if (stack) {
            transaction.addToBackStack(null); // 加入栈
            // transaction.commit();
            transaction.commitAllowingStateLoss(); // 允许状态丢失
            if (multi)  // 多次提交操作在同一个时间点一起执行
                manager.executePendingTransactions();
        } else {
            // transaction.commitNow();
            transaction.commitNowAllowingStateLoss(); // 允许状态丢失
        }
    }

    /* onBackPress里已经处理了 */
    public static boolean canGoBack(FragmentManager manager) {
        if (manager != null && manager.getBackStackEntryCount() > 0) {
            manager.popBackStack(); // fragment栈中有fragment时，回退fragment
            // manager.popBackStackImmediate();
            return true;
        }
        return false;
    }

}
