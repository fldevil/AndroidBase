package com.user.project.http;

import com.bjxrgz.base.utils.LogUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * Created by Fan on 2017/5/20.
 * 观察者封装
 */

public class BaseObserver<T> implements Observer<T> {

    private Disposable disposable;
    private CallBack<T> callBack;

    public interface CallBack<T> {
        void onSuccess(T result);

        void onError(int httpCode, String errorMessage);
    }

    public BaseObserver(CallBack<T> callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposable = d;
    }

    @Override
    public void onNext(@NonNull T t) {
        callBack.onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
        if (e instanceof HttpException) {

        }
        callBack.onError(0, "");
    }

    @Override
    public void onComplete() {
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
        LogUtil.d("onComplete");
    }
}
