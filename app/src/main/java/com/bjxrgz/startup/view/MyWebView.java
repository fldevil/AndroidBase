package com.bjxrgz.startup.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bjxrgz.startup.base.MyApp;
import com.bjxrgz.startup.utils.FileUtils;

/**
 * 通用WebView
 */
public class MyWebView extends WebView {

    private static final boolean supportJS = true; // 是否支持js
    private static final boolean supportZoom = true; // 是否支持缩放
    private static final boolean supportCache = false; // 是否支持缓存

    private OnScrollChangedCallback mOnScrollChangedCallback;
    private WebSettings settings;
    private String cacheDir; // 缓存目录
    private String cookie; // cookie

    public MyWebView(Context context) {
        super(context);
        init(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {

        // WebViewClient可以辅助WebView处理各种通知,请求等事件
        this.setWebViewClient(new WebViewClient() {
            // 页面打开时
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            // 页面关闭时
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager instance = CookieManager.getInstance();
                cookie = instance.getCookie(url);
                super.onPageFinished(view, url);
            }

            // 打开内部连接
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        // WebChromeClient专门用来辅助WebView处理js的对话框,网站title,网站图标,加载进度条等
        this.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        // 使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        if (isInEditMode()) {
            return;
        }
        requestFocusFromTouch(); //支持获取手势焦点

        settings = getSettings();
        // JS
        settings.setJavaScriptEnabled(supportJS);// 支持JS
        settings.setJavaScriptCanOpenWindowsAutomatically(supportJS);// 支持通过JS打开新窗口
        // 缩放
        settings.setSupportZoom(supportZoom); // 支持缩放
        settings.setBuiltInZoomControls(supportZoom); // 手势缩放
        settings.setDisplayZoomControls(false); // 按钮缩放
        settings.setLoadWithOverviewMode(!supportZoom); // 直接调整至屏幕的大小
        // 缓存
        settings.setAppCacheEnabled(supportCache);
        String resDir = MyApp.appInfo.getResDir();
        cacheDir = resDir + "web_cache";
        FileUtils.createOrExistsFile(cacheDir);
        settings.setAppCachePath(cacheDir);
        if (supportCache) { // 优先使用缓存
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        } else { // 不用缓存
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        // 图片
        settings.setUseWideViewPort(true); // 将图片调整到适合webView的大小
        settings.setLoadsImagesAutomatically(true);// 支持自动加载图片
        settings.setBlockNetworkImage(false);
        // 数据库
        settings.setDomStorageEnabled(true);// 开启DOM storage API功能
        settings.setDatabaseEnabled(true); // 开启database storage 功能
        // 其他
        settings.setDefaultTextEncodingName("UTF-8");
        settings.supportMultipleWindows();  // 多窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        settings.setAllowFileAccess(true);  // 设置可以访问文件
        settings.setNeedInitialFocus(true); // 当webView调用requestFocus时为webView设置节点
    }

    /* 这里的dx和dy代表的是x轴和y轴上的偏移量，你也可以自己把l, t, oldl, oldt四个参数暴露出来 */
    public interface OnScrollChangedCallback {
        void onScroll(int scrollX, int scrollY);
    }

    /* 滚动监听 */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
        }
    }

    /* 设置滚动监听 */
    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /* 外部调用 */
    public void load(String url) {
        loadUrl(url);
    }

    /* 带cookie的加载url */
    public void loadCookie(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);  // cookies是要设置的cookie字符串
        CookieSyncManager.getInstance().sync();
    }

    /* 刷新界面 */
    public void refresh() {
        reload();
    }

    public void clearCache() {
        FileUtils.deleteDir(cacheDir);
    }

    /* 滚动到顶部 */
    public void scrollTop() {
        setScrollY(0);
    }

    /* 缩放页面 */
    public void setZoom(int scale) {
        setInitialScale(scale); // 25最小缩放等级
    }

    /* 缩放字体 */
    public void setTextZoom(int size) {
        settings.setTextZoom(size); // WebSettings.TextSize.LARGER.ordinal()
    }

    /* 外部调用  返回键监听 */
    public boolean canFinish() {
        if (canGoBack()) {
            goBack();
            return false;
        } else {
            return true;
        }
    }
}
