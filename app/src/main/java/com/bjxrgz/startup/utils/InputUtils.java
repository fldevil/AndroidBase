package com.bjxrgz.startup.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by JiangZhiGuo on 2016/10/13.
 * describe 输入管理工具类
 */
public class InputUtils {

    /**
     * 不自动遮挡弹出等
     */
    public static void initNoAuto(Activity activity) {
        Window window = activity.getWindow();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// 键盘不会遮挡输入框
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); // 不自动弹键盘
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); // 总是隐藏键盘
    }

    /**
     * 动态隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 动态隐藏软键盘
     */
    public static void hideSoftInput(Context context, EditText edit) {
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * 动态显示软键盘
     */
    public static void showSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(edit, 0);
    }

    /**
     * 切换键盘显示与否状态
     */
    public static void toggleSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * **********************************剪切板************************************
     */
    public static ClipboardManager getClipboardManager(Context context) {

        return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    /**
     * 复制文本到剪贴板
     */
    public static void copyText(Context context, String text) {
        ClipData myClip = ClipData.newPlainText("text", text);
        getClipboardManager(context).setPrimaryClip(myClip);
    }

    /**
     * 获取剪贴板的文本
     */
    public static CharSequence getCopy(Context context) {
        ClipData clip = getClipboardManager(context).getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(context);
        }
        return "";
    }

    /**
     * 复制uri到剪贴板
     */
    public static void copyUri(Context context, Uri uri) {
        ClipData clipData = ClipData.newUri(context.getContentResolver(), "uri", uri);
        getClipboardManager(context).setPrimaryClip(clipData);
    }

    /**
     * 获取剪贴板的uri
     */
    public static Uri getUri(Context context) {
        ClipData clip = getClipboardManager(context).getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getUri();
        }
        return null;
    }

    /**
     * 复制意图到剪贴板
     */
    public static void copyIntent(Context context, Intent intent) {
        ClipData clipData = ClipData.newIntent("intent", intent);
        getClipboardManager(context).setPrimaryClip(clipData);
    }

    /**
     * 获取剪贴板的意图
     */
    public static Intent getIntent(Context context) {
        ClipData clip = getClipboardManager(context).getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).getIntent();
        }
        return null;
    }
}
