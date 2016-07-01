package com.bjxrgz.startup.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 通用WebView
 */
public class MyWebView extends WebView {

    // 缓存目录
    private String cacheDir = getContext().getFilesDir().getAbsolutePath() + "web_cache";

    public MyWebView(Context context) {
        super(context);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        // 还可以(can)goBack / (can)goForward

        // 设置监听
        this.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            // 打开内部连接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });


        //使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        if (isInEditMode()) {
            return;
        }
        requestFocusFromTouch();//支持获取手势焦点
        setHorizontalScrollBarEnabled(false);// 支持横向滚动条

        // settings信息
        WebSettings settings = getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setJavaScriptEnabled(true);// 支持JavaScript
        settings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        settings.supportMultipleWindows();  //多窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.setAllowFileAccess(true);  //设置可以访问文件
        settings.setNeedInitialFocus(true); //当webView调用requestFocus时为webView设置节点
        settings.setDomStorageEnabled(true);//开启DOM storage API功能
        settings.setDatabaseEnabled(true); //开启database storage 功能
        // 缩放设置
        settings.setBuiltInZoomControls(true); // 支持缩放
        settings.setSupportZoom(true);         //支持缩放
        settings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小
        // 图片加载设置
        settings.setUseWideViewPort(true); //将图片调整到适合webView的大小
        settings.setLoadsImagesAutomatically(true);// 支持自动加载图片
        settings.setBlockNetworkImage(false);
        // 缓存设置
        settings.setAppCachePath(cacheDir);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); // 优先使用缓存
    }
}
