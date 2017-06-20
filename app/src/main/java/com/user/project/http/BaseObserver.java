package com.user.project.http;

import android.app.ProgressDialog;

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
    private ProgressDialog loading;

    public interface CallBack<T> {
        void onSuccess(T result);

        void onError(int httpCode, String errorMessage);
    }

    public BaseObserver(CallBack<T> callBack) {
        this.callBack = callBack;
    }

    public BaseObserver(ProgressDialog loading,CallBack<T> callBack) {
        this.loading = loading;
        this.callBack = callBack;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        this.disposable = d;
        if (loading != null){
            loading.show();
        }
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
        if (loading != null) {
            loading.dismiss();
        }
        if (e instanceof HttpException){

        }
        callBack.onError(0, "");
    }

    @Override
    public void onComplete() {
        if (disposable.isDisposed()) {
            disposable.dispose();
        }
        if (loading != null) {
            loading.dismiss();
        }
        LogUtil.d("onComplete");
    }
}
