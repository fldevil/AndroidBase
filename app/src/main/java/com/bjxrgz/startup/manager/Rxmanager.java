package com.bjxrgz.startup.manager;

import android.os.Handler;
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
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by JiangZhiGuo on 2016-11-24.
 * describe RxBus管理类
 */

public class RxManager {

    // object是订阅的类型 ,List<Subject>里时候所有订阅此类型的订阅者
    private HashMap<Object, List<Subject>> maps = new HashMap<>();
    private static RxManager instance;

    private RxManager() {
    }

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

    /* 注册 */
    public <T> Observable<T> register(RxEvent<T> rxEvent) {
        return createObservable(rxEvent.getId()); // 获取观察者(外部绑定订阅者或者做事件处理)
    }

    public <T> Observable<T> registerMain(RxEvent<T> rxEvent) {
        Observable<T> observable = register(rxEvent); // 获取观察者
        observable.observeOn(AndroidSchedulers.mainThread()); // 主线程
        return observable;
    }

    public <T> Observable<T> registerBack(RxEvent<T> rxEvent) {
        Observable<T> observable = register(rxEvent); // 获取观察者
        observable.observeOn(AndroidSchedulers.from(new Handler().getLooper())); // 子线程
        return observable;
    }

    public <T> Observable<T> registerMain(RxEvent<T> rxEvent, Action1<? super T> onNext) {
        Observable<T> register = registerMain(rxEvent); // 获取观察者
        register.subscribe(onNext); // 事件处理
        return register;
    }

    public <T> Observable<T> registerBack(RxEvent<T> rxEvent, Action1<? super T> onNext) {
        Observable<T> register = registerBack(rxEvent); // 获取观察者
        register.subscribe(onNext); // 事件处理
        return register;
    }

    public <T> Observable<T> registerMain(RxEvent<T> rxEvent, Action1<? super T> onNext,
                                          final Action1<Throwable> onError,
                                          final Action0 onCompleted) {
        Observable<T> register = registerMain(rxEvent); // 获取观察者
        register.subscribe(onNext, onError, onCompleted); // 事件,异常,最后处理(不用绑定订阅者)
        return register;
    }

    public <T> Observable<T> registerBack(RxEvent<T> rxEvent, Action1<? super T> onNext,
                                          final Action1<Throwable> onError,
                                          final Action0 onCompleted) {
        Observable<T> register = registerBack(rxEvent); // 获取观察者
        register.subscribe(onNext, onError, onCompleted);  // 事件,异常,最后处理(不用绑定订阅者)
        return register;
    }

    /* 注销 */
    public <T> void unregister(RxEvent<T> rxEvent, @NonNull Observable observable) {
        removeObservable(rxEvent.getId(), observable); // 移除此event绑定的观察者
    }

    /* 发送消息(只有已注册的才会收到) */
    public <T> void post(@NonNull RxEvent<T> rxEvent) {
        RxEvent.ID id = rxEvent.getId();
        T object = rxEvent.getObject();
        next(id, object);
    }

    /* 注意:这个是即时发送消息的，没有注册这么一说 */
    public <T> Observable<T> postMain(final RxEvent<T> rxEvent, Subscriber<? super T> subscriber) {
        Observable<T> observable = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(rxEvent.getObject()); // 发送事件
                subscriber.onCompleted(); // 完成事件
            }
        });
        observable.subscribe(subscriber); // 观察者和订阅者绑定(订阅者直接new出来就好)
        return observable;
    }

    public <T> Observable<T> postBack(final RxEvent<T> rxEvent, Subscriber<? super T> subscriber) {
        Observable<T> observable = Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext(rxEvent.getObject()); // 发送事件
                subscriber.onCompleted(); // 完成事件
            }
        });
        observable.subscribe(subscriber); // 观察者和订阅者绑定(订阅者直接new出来就好)
        return observable;
    }

    /* 获取观察者 */
    @SuppressWarnings("unchecked")
    private <T> Observable<T> createObservable(@NonNull Object tag) {
        List<Subject> subjects = maps.get(tag);
        if (subjects == null) { // 这个tag没有订阅者
            subjects = new ArrayList<>();
            maps.put(tag, subjects); // 创建这个tag的订阅者集合
        }
        Subject<T, T> subject = PublishSubject.create();
        subjects.add(subject); // 向这个tag下的订阅者集合里添加订阅者
        return subject;
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

}
