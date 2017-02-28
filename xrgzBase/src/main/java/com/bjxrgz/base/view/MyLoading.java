package com.bjxrgz.base.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.bjxrgz.base.R;

/**
 * Created by Fan on 2016/10/17.
 * 等待对话框
 */

public class MyLoading extends ProgressDialog {

    public MyLoading(Context context) {
        super(context, R.style.DialogMyLoading);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_my_loading);
    }
}
