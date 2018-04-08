package com.wang.social.personal.helper;

import com.frame.http.api.BaseJson;
import com.frame.http.api.Mapper;
import com.frame.mvp.IView;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.ToastUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MyApiHelper {

    /**
     * 基于Rxjava 调用API 帮助类方法，bindView,doOnSubscribe,doFinally均可以为空
     * bindView 为null 则不绑定生命周期
     * doOnSubscribe,doFinally 为null 则不会执行
     */
    public static <T> void execute(IView bindView, Observable<T> observable, Observer<T> observer, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        observable = observable.subscribeOn(Schedulers.io());
        if (bindView != null) {
            observable = observable.compose(RxLifecycleUtils.bindToLifecycle(bindView));
        }
        observable = observable.observeOn(AndroidSchedulers.mainThread());
        if (doOnSubscribe != null) {
            observable = observable.doOnSubscribe(doOnSubscribe);
        }
        if (doFinally != null) {
            observable = observable.doFinally(doFinally);
        }
        observable.subscribe(observer);
    }

    /**
     * 上面方法的重载
     * 将doOnSubscribe，doFinally 简化由 Runnable实现
     * 同时将showLoading和hideLoading独立出来由needLoading参数进行控制
     */
    public static <T> void execute(IView bindView, boolean needLoading, Observable<T> observable, Observer<T> observer, Runnable doStart, Runnable doFinally) {
        execute(bindView, observable, observer, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                if (needLoading && bindView != null) bindView.showLoading();
                if (doStart != null) doStart.run();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                if (needLoading && bindView != null) bindView.hideLoading();
                if (doFinally != null) doFinally.run();
            }
        });
    }

    //上面方法的重载，默认Runnable doStart, Runnable doFinally为Null
    public static <T> void execute(IView bindView, boolean needLoading, Observable<T> observable, Observer<T> observer) {
        execute(bindView, needLoading, observable, observer, null, null);
    }
}
