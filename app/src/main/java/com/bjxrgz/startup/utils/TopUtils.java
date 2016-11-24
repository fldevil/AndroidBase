package com.bjxrgz.startup.utils;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bjxrgz.startup.R;

/**
 * Created by JiangZhiGuo on 2016-11-23.
 * describe
 */

public class TopUtils {

    /* 废弃 */
//    private static Toolbar setTop(AppCompatActivity activity) {
//        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.tbTop);
//        if (toolbar != null) {
//            // 设置top
//            activity.setSupportActionBar(toolbar);
//            // 获取top
//            ActionBar actionBar = activity.getSupportActionBar();
//            if (actionBar != null) {
//                actionBar.setDisplayShowTitleEnabled(false);
//            }
//        }
//        return toolbar;
//    }

    public static class Builder {

        private Toolbar toolbar;
        private TextView tvCenter;
        private TextView tvLeft;
        private TextView tvRight;

        public Builder(AppCompatActivity activity) {
            toolbar = (Toolbar) activity.findViewById(R.id.tbTop);
            tvCenter = (TextView) activity.findViewById(R.id.tvCenter);
            tvLeft = (TextView) activity.findViewById(R.id.tvLeft);
            tvRight = (TextView) activity.findViewById(R.id.tvRight);
        }

        public void setTitleLeft(String title, int color) {
            if (toolbar != null) {
                toolbar.setTitle(title);
                toolbar.setTitleTextColor(color);
            }
        }

        public void setTitleLeft(int title, int color) {
            if (toolbar != null) {
                toolbar.setTitle(title);
                toolbar.setTitleTextColor(color);
            }
        }

        public void setTitleCenter(String title, int color) {
            if (tvCenter != null) {
                tvCenter.setVisibility(View.VISIBLE);
                tvCenter.setTextColor(color);
                tvCenter.setText(title);
            }
        }

        public void setTitleCenter(int title, int color) {
            if (tvCenter != null) {
                tvCenter.setVisibility(View.VISIBLE);
                tvCenter.setTextColor(color);
                tvCenter.setText(title);
            }
        }

        public void setNavigateLeft(String left, int color, View.OnClickListener listener) {
            if (tvLeft != null) {
                tvLeft.setVisibility(View.VISIBLE);
                tvLeft.setTextColor(color);
                tvLeft.setText(left);
                tvLeft.setOnClickListener(listener);
            }
        }

        public void setNavigateLeft(int left, int color, View.OnClickListener listener) {
            if (tvLeft != null) {
                tvLeft.setVisibility(View.VISIBLE);
                tvLeft.setTextColor(color);
                tvLeft.setText(left);
                tvLeft.setOnClickListener(listener);
            }
        }

        public void setNavigateRight(String right, int color, View.OnClickListener listener) {
            if (tvRight != null) {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setTextColor(color);
                tvRight.setText(right);
                tvRight.setOnClickListener(listener);
            }
        }

        public void setNavigateRight(int right, int color, View.OnClickListener listener) {
            if (tvRight != null) {
                tvRight.setVisibility(View.VISIBLE);
                tvRight.setTextColor(color);
                tvRight.setText(right);
                tvRight.setOnClickListener(listener);
            }
        }

        /* 设置左上角的填充菜单 */
        public void setNavigateLeft(int leftIconId, View.OnClickListener listener) {
            if (toolbar != null) {
                toolbar.setNavigationIcon(leftIconId);
                toolbar.setNavigationOnClickListener(listener);
            }
        }

        /* 设置右上角的填充菜单 (优先级1) */
        public void setMenu(int menuId, Toolbar.OnMenuItemClickListener listener) {
            if (toolbar != null) {
                toolbar.inflateMenu(menuId);
                toolbar.setOnMenuItemClickListener(listener);
            }
        }
    }
}
