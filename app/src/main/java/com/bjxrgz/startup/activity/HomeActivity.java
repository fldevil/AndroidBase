package com.bjxrgz.startup.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bjxrgz.startup.R;
import com.bjxrgz.startup.base.BaseViewActivity;
import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.manager.APIManager;
import com.bjxrgz.startup.manager.XUtilsManager;
import com.bjxrgz.startup.utils.ActivityUtils;
import com.bjxrgz.startup.utils.FileUtils;
import com.bjxrgz.startup.utils.MediaUtils;
import com.bjxrgz.startup.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by JiangZhiGuo on 2016/06/01
 * <p>
 * describe 主界面
 */
public class HomeActivity extends BaseViewActivity<HomeActivity> {

    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.image2)
    ImageView image2;
    private File result;

    @OnClick(R.id.image)
    public void onClick() {
//        String fileName = StringUtils.getRandom(5) + ".png";
//        String resDir = MyApp.instance.appInfo.getResDir();
//        result = new File(resDir, fileName);
//        FileUtils.createOrExistsFile(result);
//        Intent intent = MediaUtils.getCameraIntent(result);
        Intent intent = MediaUtils.getPictureIntent();
        startActivityForResult(intent, 10);
    }

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
//        String week = TimeUtils.getWeek(TimeUtils.getCurrentDate());
//        int weekIndex = TimeUtils.getWeekIndex(TimeUtils.getCurrentDate());
//        boolean leapYear = TimeUtils.isLeapYear(2016);
//        LogUtils.e(week + "\n" + weekIndex + "\n" + leapYear);

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
//        getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
//            Bitmap bitmapFile = MediaUtils.getCameraBitmap(result);
//            image2.setImageBitmap(bitmapFile);

            Bitmap bitmap = MediaUtils.getPictureBitmap(mActivity, data);
            image1.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void refreshData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 友盟统计
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 友盟统计
    }

    private void getData() {
        APIManager.demo(mActivity, new XUtilsManager.Callback() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

}
