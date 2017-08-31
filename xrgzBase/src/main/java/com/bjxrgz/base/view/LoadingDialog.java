package com.bjxrgz.base.view;

import android.app.Dialog;
import android.content.Context;

import com.bjxrgz.base.R;

/**
 * Created by Fan on 2016/10/17.
 * 等待对话框
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.DialogMyLoading);
        setContentView(R.layout.dialog_my_loading);
    }
}
