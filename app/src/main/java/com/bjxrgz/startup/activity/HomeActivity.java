package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.manager.FileManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.MediaUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
public class HomeActivity extends BaseViewActivity<HomeActivity> {

    @BindView(R.id.btnCamera)
    Button btnCamera;
    @BindView(R.id.btnPicture)
    Button btnPicture;
    @BindView(R.id.ivReturn)
    ImageView ivReturn;

    public static void goActivity(Activity activity) {
        Intent intent = new Intent(activity, HomeActivity.class);
        ActivityUtils.startActivity(activity, intent);
    }

    @Override
    protected void initObject(Bundle savedInstanceState) {
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initContentView(R.layout.activity_home);


    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 222) {
                Bitmap pictureBitmap = MediaUtils.getPictureBitmap(mActivity, data);
                ivReturn.setImageBitmap(pictureBitmap);
            }
        }
    }

    @OnClick({R.id.btnCamera, R.id.btnPicture})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCamera:

                break;
            case R.id.btnPicture:
                File pictureFile = FileManager.createBitmapFile();
                Intent pictureCropIntent = MediaUtils.getPictureCropIntent(pictureFile, 500, 500);
                ActivityUtils.startActivity(mActivity, pictureCropIntent, 222);
                break;
        }
    }

}
