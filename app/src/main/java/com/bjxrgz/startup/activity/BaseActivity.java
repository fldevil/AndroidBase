package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bjxrgz.startup.R;
import com.bjxrgz.utils.DeviceUtil;

/**
 * Created by fd.meng on 2016/05/26
 *
 * 基类
 *
 */
public class BaseActivity extends AppCompatActivity {

    protected Activity activity;
    protected ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!DeviceUtil.isNetworkAvailable(this)){
            Toast.makeText(this, getString(R.string.no_network_title),Toast.LENGTH_SHORT).show();
        }
    }
}
