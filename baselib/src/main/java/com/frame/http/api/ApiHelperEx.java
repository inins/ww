package com.frame.http.api;

import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.Utils;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by liaointan
 * <p>
 * 对{@link ApiHelper}的拓展
 * 提供自动的loading接口调用
 * 允许IView为null，允许不进行lifecycle绑定
 * 不对接口entity进行DTO转换，如需要自行在回调中处理
 */
public class ApiHelperEx {

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
        execute(bindView, observable, observer, disposable -> {
                    if (needLoading && bindView != null) bindView.showLoading();
                    if (doStart != null) doStart.run();
                },
                () -> {
                    if (needLoading && bindView != null) bindView.hideLoading();
                    if (doFinally != null) doFinally.run();
                });
    }

    //上面方法的重载，默认Runnable doStart, Runnable doFinally为Null
    public static <T> void execute(IView bindView, boolean needLoading, Observable<T> observable, Observer<T> observer) {
        execute(bindView, needLoading, observable, observer, null, null);
    }

    public static <T> T getService(Class<T> service) {
        return FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager().obtainRetrofitService(service);
    }

}
