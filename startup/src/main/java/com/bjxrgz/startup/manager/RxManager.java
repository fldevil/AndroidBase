package com.bjxrgz.startup.manager;

import android.support.annotation.NonNull;

import com.bjxrgz.startup.domain.RxEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by JiangZhiGuo on 2016-11-24.
 * describe RxBus管理类
 */
public class RxManager {

    // object是订阅的类型 ,List<Subject>里时候所有订阅此频道的订阅者
    private HashMap<Object, List<Subject>> maps = new HashMap<>();
    private static RxManager instance;

    public static RxManager get() {
        if (instance == null) {
            synchronized (RxManager.class) {
                if (instance == null) {
                    instance = new RxManager();
                }
            }
        }
        return instance;
    }

    /* 发送频道消息(已注册的频道里的观察者才会收到) */
    public <T> void post(@NonNull RxEvent<T> rxEvent) {
        RxEvent.ID id = rxEvent.getId();
        T object = rxEvent.getObject();
        if (id != null && object != null) {
            next(id, object);
        }
    }

    /* 注意:这个是即时发送消息的，没有注册这么一说 (可用于线程间的操作)*/
    public <T> Observable<T> post(final T send, Subscriber<? super T> subscriber) {
        Observable<T> observable = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(send); // 发送事件
                subscriber.onCompleted(); // 完成事件
            }
        });
        observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        return observable;
    }

    /* 注册频道 */
    public <T> Observable<T> register(RxEvent.ID eventId, Action1<? super T> onNext) {
        Observable<T> observable = createObservable(eventId); // 获取观察者
        // Rx最好连着点出来,不连着点，下面全是Bug
        observable.subscribeOn(Schedulers.immediate()) // 当前线程
                // 解决连续发送数据过快时的异常，toList也行
                .onBackpressureBuffer()
                // 接受线程和事件处理必须连起来,否则回调线程会不正确
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
        return observable;
    }

    public <T> Observable<T> register(RxEvent.ID eventId, Action1<? super T> onNext,
                                      final Action1<Throwable> onError,
                                      final Action0 onCompleted) {
        Observable<T> observable = createObservable(eventId); // 获取观察者
        // Rx最好连着点出来,不连着点，下面全是Bug
        observable.subscribeOn(Schedulers.immediate()) // 当前线程
                // 解决连续发送数据过快时的异常，toList也行
                .onBackpressureBuffer()
                // 接受线程和事件处理必须连起来,否则回调线程会不正确
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onCompleted);
        return observable;
    }

    /* 注销频道里的单个观察者 */
    public void unregister(RxEvent.ID eventId, Observable observable) {
        if (observable != null) {
            removeObservable(eventId, observable); // 移除此event绑定的观察者
        }
    }

    /* 获取观察者 */
    @SuppressWarnings("unchecked")
    private <T> Observable<T> createObservable(@NonNull Object tag) {
        List<Subject> subjects = maps.get(tag);
        if (subjects == null) { // 这个tag没有订阅者
            subjects = new ArrayList<>();
            maps.put(tag, subjects); // 创建这个tag的订阅者集合
        }
        Subject<T, T> subject = PublishSubject.create(); // 获取观察者
        subjects.add(subject); // 向这个tag下的订阅者集合里添加订阅者
        return subject;
    }

    /* 观察者发送消息给订阅者 */
    @SuppressWarnings("unchecked")
    private <T> void next(@NonNull Object tag, @NonNull T t) {
        List<Subject> subjects = maps.get(tag);
        if (subjects != null && !subjects.isEmpty()) {
            for (Subject s : subjects) { // 向所有订阅tag的对象发送消息
                s.onNext(t);
            }
        }
    }

    /* 移除观察者 */
    @SuppressWarnings("unchecked")
    private void removeObservable(@NonNull Object tag, @NonNull Observable observable) {
        List<Subject> subjects = maps.get(tag);
        if (subjects != null) { // 这个tag的订阅者集合不为空
            subjects.remove((Subject) observable);
            if (subjects.isEmpty()) { // 这个tag的订阅者没有时，去掉tag
                maps.remove(tag);
            }
        }
    }

}
