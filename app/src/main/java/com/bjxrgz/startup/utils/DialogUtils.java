package com.bjxrgz.startup.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bjxrgz.startup.R;

import java.util.Calendar;

/**
 * Created by Jiang on 2016/10/13
 * <p/>
 * DialogUtils: 对话框(alert，progress)，单选，多选，日期
 */
public class DialogUtils {

    /**
     * 自定义对话框
     */
    public static Dialog createCustom(Context context, int theme, View view) {
        final Dialog dialog = new Dialog(context, theme);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            dialog.setContentView(view, lp);
        } else {
            dialog.setContentView(view);
        }
        return dialog;
    }

    /**
     * 自定义对话框
     */
    public static Dialog createCustom(Activity activity, int theme, View view, float height, float width) {
        final Dialog dialog = new Dialog(activity, theme);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        DisplayMetrics d = activity.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (height != 0) {
            lp.height = (int) (d.heightPixels * height); // 高度设置为屏幕的0.x
        }
        if (width != 0) {
            lp.width = (int) (d.widthPixels * width); // 高度设置为屏幕的0.x
        }
        dialog.setContentView(view, lp);
        return dialog;
    }

    /**
     * 等待对话框
     */
    public static ProgressDialog createLoading(Context context, int theme, String title,
                                               String message, boolean cancel) {
        ProgressDialog loading;
        if (theme == 0) {
            loading = new ProgressDialog(context);
        } else {
            loading = new ProgressDialog(context, theme);
        }
        if (!TextUtils.isEmpty(title)) {
            loading.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            loading.setMessage(message);
        }
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(cancel);
        return loading;
    }

    /**
     * 进度对话框
     */
    public static ProgressDialog createProgress(Context context, int theme, String title,
                                                String message, boolean cancel, int max, int start,
                                                DialogInterface.OnCancelListener listener) {
        ProgressDialog progress;
        if (theme == 0) {
            progress = new ProgressDialog(context);
        } else {
            progress = new ProgressDialog(context, theme);
        }
        if (!TextUtils.isEmpty(title)) {
            progress.setTitle(title);
        }
        if (!TextUtils.isEmpty(message)) {
            progress.setMessage(message);
        }
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setMax(max);
        progress.setProgress(start);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(cancel);
        progress.setOnCancelListener(listener);
        return progress;
    }

    /**
     * 警告对话框
     */
    public static AlertDialog createAlert(Context context, String title, String message,
                                          String positive, String negative,
                                          final DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (TextUtils.isEmpty(title)) {
            title = context.getString(R.string.prompt);
        }
        builder.setTitle(title);
        builder.setMessage(message);
        if (TextUtils.isEmpty(positive)) {
            positive = context.getString(R.string.confirm);
        }
        if (TextUtils.isEmpty(negative)) {
            negative = context.getString(R.string.cancel);
        }
        builder.setPositiveButton(positive, positiveListener);
        builder.setNegativeButton(negative, null);
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
        builder.setNegativeButton(R.string.cancel, null);
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
            positive = context.getString(R.string.confirm);
        }
        builder.setMultiChoiceItems(items, checkedState, choiceListener);
        builder.setPositiveButton(positive, positiveListener);
        builder.setNegativeButton(R.string.cancel, null);
        return builder.create();
    }

    /**
     * 创建系统日期选择对话框
     */
    public static DatePickerDialog showDatePicker(Context context, Calendar calendar,
                                                  DatePickerDialog.OnDateSetListener onDateSetListener) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog picker = new DatePickerDialog(context, onDateSetListener, year, month, day);
        picker.show();
        return picker;
    }

    /**
     * 创建系统时间选择对话框 24小时
     */
    public static TimePickerDialog show24TimePicker(Context context, Calendar calendar,
                                                    TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog picker = new TimePickerDialog(context, onTimeSetListener, hour, minute, true);
        picker.show();
        return picker;
    }

    /**
     * 创建系统时间选择对话框 12小时
     */
    public static TimePickerDialog show12TimePicker(Context context, Calendar calendar,
                                                    TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog picker = new TimePickerDialog(context, onTimeSetListener, hour, minute, false);
        picker.show();
        return picker;
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
