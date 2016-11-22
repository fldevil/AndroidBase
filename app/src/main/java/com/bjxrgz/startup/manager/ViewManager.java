package com.bjxrgz.startup.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.view.MyLoading;

/**
 * Created by JiangZhiGuo on 2016-10-31.
 * describe 符合项目样式的View管理类
 */

public class ViewManager {

    private static Toast toast;

    /* 等待对话框 默认DialogUtils.createLoading(); */
    public static ProgressDialog createLoading(Context context) {
        ProgressDialog loading = new MyLoading(context);
        loading.setCanceledOnTouchOutside(false);
        loading.setCancelable(true);
        return loading;
    }

    /* Toast 单例 可覆盖 */
    public static void showToast(CharSequence message) {
        if (toast == null) {
            toast = Toast.makeText(MyApp.getInstance(), message, Toast.LENGTH_SHORT);
        } else {
            TextView tv = (TextView) toast.getView().findViewById(android.R.id.message);
            tv.setText(message);
        }
        toast.show();
    }

    /* Toast 也可以ToastUtils来构造 */
    public static void showToast(int message) {
        String show = MyApp.getInstance().getString(message);
        showToast(show);
    }

    /* 顶部标题栏 */
    public static void initTopTitle(Activity activity, String title) {
        Toolbar tbTop = (Toolbar) activity.findViewById(R.id.tbTop);
        TextView tvCenter = (TextView) activity.findViewById(R.id.tvCenter);
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(tbTop);
        }
        tvCenter.setVisibility(View.VISIBLE);
        tvCenter.setText(title);
    }

    public static void initTopLeft(Activity activity, String title, String left) {
        initTopTitle(activity, title);
        TextView tvLeft = (TextView) activity.findViewById(R.id.tvLeft);
        tvLeft.setVisibility(View.VISIBLE);
        tvLeft.setText(left);
    }

    public static void initTopLeft(Activity activity, String title) {
        initTopTitle(activity, title);
        ImageView ivLeft = (ImageView) activity.findViewById(R.id.ivLeft);
        ivLeft.setVisibility(View.VISIBLE);
    }

    public static void initTopRight(Activity activity, String title, String right) {
        initTopTitle(activity, title);
        TextView tvRight = (TextView) activity.findViewById(R.id.tvRight);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(right);
    }

    public static void initTopRight(Activity activity, String title) {
        initTopTitle(activity, title);
        ImageView ivRight = (ImageView) activity.findViewById(R.id.ivRight);
        ivRight.setVisibility(View.VISIBLE);
    }

}
