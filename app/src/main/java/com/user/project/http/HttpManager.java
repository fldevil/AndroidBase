package com.user.project.http;

import okhttp3.ResponseBody;

/**
 * Created by Fan on 2017/5/18.
 * http 帮助类
 */

public class HttpManager {

    private static HttpManager instance;
    private static AppAPI appAPI;

    private HttpManager() {
        appAPI = RetrofitFactory.getInstance().getService(RetrofitFactory.Factory.gson, AppAPI.BASE_URL, AppAPI.class);
    }

    public static HttpManager getInstance() {
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) {
                    instance = new HttpManager();
                }
            }
        }
        return instance;
    }

    /**
     * 检查是否有新版本
     */
    public void checkUpdate(int version, BaseObserver.CallBack<ResponseBody> callBack) {
        appAPI.checkUpdate(version)
                .compose(RxSchedulers.<ResponseBody>compose())
                .subscribe(new BaseObserver<>(callBack));
    }

    /**
     * 下载文件
     */
    public void downloadLargeFile(String url, BaseObserver.CallBack<ResponseBody> callBack) {
        appAPI.downloadLargeFile(url)
                .compose(RxSchedulers.<ResponseBody>compose())
                .subscribe(new BaseObserver<>(callBack));
    }
}
