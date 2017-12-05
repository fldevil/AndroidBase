package com.bjxrgz.base.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bjxrgz.base.BaseApp;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe 输入管理工具类
 */
public class InputUtil {

    /**
     * 动态隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view == null) return;
        InputMethodManager inputManager = ManagerUtil.getInputManager(activity);
        inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 动态隐藏软键盘
     */
    public static void hideSoftInput(EditText edit) {
        edit.clearFocus();
        InputMethodManager inputManager = ManagerUtil.getInputManager(edit.getContext());
        inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * 动态显示软键盘
     */
    public static void showSoftInput(EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = ManagerUtil.getInputManager(edit.getContext());
        inputManager.showSoftInput(edit, 0);
    }

    /**
     * 切换键盘显示与否状态
     */
    public static void toggleSoftInput(EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = ManagerUtil.getInputManager(edit.getContext());
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * **********************************剪切板************************************
     */

    /**
     * 复制文本到剪贴板
     */
    public static void copyText(Context context,String text) {
        ClipData myClip = ClipData.newPlainText("text", text);
        ManagerUtil.getClipboardManager(context).setPrimaryClip(myClip);
    }

    /**
     * 获取剪贴板的文本
     */
    public static CharSequence getCopy(Context context) {
        CharSequence copy = "";
        ClipData clip = ManagerUtil.getClipboardManager(context).getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0)
            copy = clip.getItemAt(0).coerceToText(BaseApp.getInstance());
        return copy;
    }

    /**
     * 复制uri到剪贴板
     */
    public static void copyUri(Context context,Uri uri) {
        ClipData clipData = ClipData.newUri(BaseApp.getInstance().getContentResolver(), "uri", uri);
        ManagerUtil.getClipboardManager(context).setPrimaryClip(clipData);
    }

    /**
     * 获取剪贴板的uri
     */
    public static Uri getUri(Context context) {
        Uri uri = null;
        ClipData clip = ManagerUtil.getClipboardManager(context).getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0)
            uri = clip.getItemAt(0).getUri();
        return uri;
    }

    /**
     * 复制意图到剪贴板
     */
    public static void copyIntent(Context context,Intent intent) {
        ClipData clipData = ClipData.newIntent("intent", intent);
        ManagerUtil.getClipboardManager(context).setPrimaryClip(clipData);
    }

    /**
     * 获取剪贴板的意图
     */
    public static Intent getIntent(Context context) {
        Intent intent = null;
        ClipData clip = ManagerUtil.getClipboardManager(context).getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0)
            intent = clip.getItemAt(0).getIntent();
        return intent;
    }
}
