package com.bjxrgz.startup.utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Calendar;

/**
 * Created by fd.meng on 2014/03/30
 * <p/>
 * DialogUtils: 获取对话框 alert，progress，单选，多选，日期
 */
public class DialogUtils {

    /**
     * 弹出对话框
     */
    public static void show(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    /**
     * 撤销对话框
     */
    public static void dismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    /**
     * 进度对话框 , 可loading可progress
     */
    public static ProgressDialog createProgress(Context context, String title, String message,
                                                boolean horizontal, boolean cancel,
                                                DialogInterface.OnCancelListener listener) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        if (horizontal) {
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMax(100);
            dialog.setProgress(0);
        }
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancel);
        dialog.setOnCancelListener(listener);
        return dialog;
    }

    /**
     * 警告对话框
     */
    public static AlertDialog createAlert(Context context, String title,
                                          String message, View view, String positive,
                                          final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(view);
        if (TextUtils.isEmpty(positive)) {
            positive = "确定";
        }
        builder.setPositiveButton(positive, listener);
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    /**
     * 单选对话框
     */
    public static AlertDialog createSingle(Context context, String title, String[] items,
                                           int checkedId, String positive,
                                           DialogInterface.OnClickListener choiceListener,
                                           DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, checkedId, choiceListener);
        builder.setPositiveButton(positive, positiveListener);
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    /**
     * 多选对话框
     */
    public static AlertDialog createMulti(Context context, String title, String[] items,
                                          final boolean[] checkedState, String positive,
                                          DialogInterface.OnMultiChoiceClickListener choiceListener,
                                          DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (TextUtils.isEmpty(positive)) {
            positive = "确定";
        }
        builder.setMultiChoiceItems(items, checkedState, choiceListener);
        builder.setPositiveButton(positive, positiveListener);
        builder.setNegativeButton("取消", null);
        return builder.create();
    }

    /**
     * 日期对话框
     * Calendar转化时和这里的monthOfYear是对应的, 手写的传出需要monthOfYear + 1
     */
    public static DatePickerDialog showDaterPicker(Context context, int[] date,
                                                   DatePickerDialog.OnDateSetListener listener) {
        if (date == null) { // 获取当前时间
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            date = new int[]{year, month, day};
        }
        return new DatePickerDialog(context, listener, date[0], date[1], date[2]);
    }

    /**
     * 设置透明度
     */
    public static void setAlpha(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.alpha = 0.5f;
        window.setAttributes(lp);
    }

    /**
     * 设置暗黑背景层
     */
    public static void setDimamount(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();

        lp.dimAmount = 1.0f;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
