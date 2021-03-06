package com.bjxrgz.base.utils;

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

import com.bjxrgz.base.R;
import com.bjxrgz.base.view.LoadingDialog;

import java.util.Calendar;

/**
 * Created by Jiang on 2016/10/13
 * <p/>
 * DialogUtil: 对话框(alert，progress)，单选，多选，日期
 */
public class DialogUtil {

    /**
     * 等待对话框 (一般用于baseActivity)
     */
    public static Dialog createLoading(Context context) {
        Dialog dialog = new LoadingDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        return dialog;
    }

    /**
     * 自定义对话框
     *
     * @param theme R.style.DialogCustom
     * @param view  LayoutInflater.from(activity).inflate(layoutId, null);
     */
    public static Dialog createCustom(Activity activity, int theme, View view,
                                      float height, float width) {
        final Dialog dialog = new Dialog(activity, theme);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        DisplayMetrics d = activity.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        if (height != 0) {  // 高度设置为屏幕的0.x（减去statusBar高度）
            lp.height = (int) (d.heightPixels * height) - ScreenUtil.getStatusBarHeight(activity);
        }
        if (width != 0) {  // 宽度设置为屏幕的0.x
            lp.width = (int) (d.widthPixels * width);
        }
        dialog.setContentView(view, lp);
        return dialog;
    }

    /**
     * 警告对话框
     */
    public static AlertDialog createAlert(Context context, String title, String message,
                                          String positive, String negative,
                                          final DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positive, positiveListener);
        builder.setNegativeButton(negative, null);
        return builder.create();
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
        if (!TextUtils.isEmpty(title))
            loading.setTitle(title);
        if (!TextUtils.isEmpty(message))
            loading.setMessage(message);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(cancel);
        return loading;
    }

    /**
     * 进度对话框(没有message)
     */
    public static ProgressDialog createProgress(Context context, int theme, String title,
                                                boolean cancel, int max, int start,
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
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setMax(max);
        progress.setProgress(start);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(cancel);
        progress.setOnCancelListener(listener);
        return progress;
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
    public static void setAlpha(Dialog dialog, float alpha) {
        Window window = dialog.getWindow();
        if (window == null) return;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
    }

    /**
     * 设置暗黑背景层
     */
    public static void setDimamount(Dialog dialog, float alpha) {
        Window window = dialog.getWindow();
        if (window == null) return;
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = alpha;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
