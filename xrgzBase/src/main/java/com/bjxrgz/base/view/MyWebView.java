package com.bjxrgz.base.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bjxrgz.base.utils.AppUtils;
import com.bjxrgz.base.utils.FileUtils;

import java.util.Map;

/**
 * Created by JiangZhiGuo on 2016-11-7.
 * 通用WebView
 * 4.4以后WebView的操作尽量放在ui线程中去执行
 */
public class MyWebView extends WebView {
    public boolean supportJS = true; // 是否支持js
    public boolean supportZoom = false; // 是否支持缩放
    public boolean supportCache = false; // 是否支持缓存

    private Context mContext;
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

    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context) {
        mContext = context;
        // WebViewClient可以辅助WebView处理各种通知,请求等事件
        this.setWebViewClient(webViewClient);
        // WebChromeClient专门用来辅助WebView处理js的对话框,网站title,网站图标,加载进度条等
        this.setWebChromeClient(webChromeClient);
        // 使用isInEditMode解决可视化编辑器无法识别自定义控件的问题
        if (isInEditMode()) return;
        requestFocusFromTouch(); // 支持获取手势焦点

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
        settings.setDomStorageEnabled(supportCache); // 开启DOM 缓存
        settings.setDatabaseEnabled(supportCache); // 开启database 缓存
        if (supportCache) { // 优先使用缓存
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            String resDir = AppUtils.get().getFilesDir();
            cacheDir = resDir + "web_cache";
            FileUtils.createOrExistsFile(cacheDir);
            settings.setAppCachePath(cacheDir);
        } else { // 不用缓存
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        // 图片
        settings.setUseWideViewPort(true); // 将图片调整到适合webView的大小
        settings.setLoadsImagesAutomatically(true); // 支持自动加载图片
        settings.setBlockNetworkImage(false); // 图片加载放在最后来加载渲染 4.4需要为false
        // 其他
        settings.setDefaultTextEncodingName("UTF-8");
        settings.supportMultipleWindows();  // 多窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 支持内容重新布局
        settings.setAllowFileAccess(true);  // 设置可以访问文件
        settings.setNeedInitialFocus(true); // 当webView调用requestFocus时为webView设置节点
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CookieManager instance = CookieManager.getInstance();
            cookie = instance.getCookie(url);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
    };

    /* 外部调用 */
    public void load(String url) {
        loadUrl(url);
    }

    /* 外部调用 */
    public void load(String url, Map<String, String> headers) {
        loadUrl(url, headers);
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

    public void clear() {
        clearCache(true);
        clearHistory();
        clearFormData();
        FileUtils.deleteDir(cacheDir);
    }

    /* 外部调用  返回键监听 */
    public boolean goFinish() {
        if (canGoBack()) {
            goBack();
            return true;
        } else {
            return false;
        }
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

}
