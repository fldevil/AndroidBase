package com.user.project.http;

/**
 * Created by Fan on 2017/9/25.
 * 封装回调基类
 */

public interface Callback {

    public interface CommonCallback<T> {
        void onSuccess(T result);

        void onError(int httpCode, String errorMessage);
    }

}
