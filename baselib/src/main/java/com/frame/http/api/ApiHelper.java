package com.frame.http.api;

import com.frame.mvp.IView;
import com.frame.utils.RxLifecycleUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ========================================
 * 用于将DTO对象转换成用于界面展示的VO实体
 * <p>
 * Create by ChenJing on 2018-03-20 15:03
 * ========================================
 */
public class ApiHelper {

    private Integer successCode;

    public ApiHelper() {
        this.successCode = successCode;
    }

    /**
     * 适用于执行不需要获取响应结果的情况
     *
     * @param bindView
     * @param observable
     * @return
     */
    public <T extends BaseJson> void executeNone(IView bindView, Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .subscribe(observer);
    }

    /**
     * 适用于执行不需要获取响应结果的情况
     *
     * @param bindView
     * @param observable
     * @param doOnSubscribe 在订阅成功的时候回掉（可用于显示加载框等操作）
     * @param doFinally 线程执行完毕之后回掉（可用于关闭加载框等操作）
     * @return
     */
    public <T extends BaseJson> void executeNone(IView bindView, Observable<T> observable, Observer<T> observer, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(doOnSubscribe)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(doFinally)
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .subscribe(observer);
    }

    /**
     * 适用于获取的返回结果是一个实体的情况
     *
     * @param bindView
     * @param observable
     * @param <T>
     * @param <K>
     * @param <R>
     * @return
     */
    public <T extends BaseJson<K>, K extends Mapper<R>, R> void execute(IView bindView, Observable<T> observable, Observer<R> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .map(new Function<T, R>() {
                    @Override
                    public R apply(T t) throws Exception {
                        return t.getData().transform();
                    }
                })
                .subscribe(observer);
    }

    /**
     * 适用于获取的返回结果是一个实体的情况
     *
     * @param bindView
     * @param observable
     * @param doOnSubscribe 在订阅成功的时候回掉（可用于显示加载框等操作）
     * @param doFinally 线程执行完毕之后回掉（可用于关闭加载框等操作）
     * @param <T>
     * @param <K>
     * @param <R>
     * @return
     */
    public <T extends BaseJson<K>, K extends Mapper<R>, R> void execute(IView bindView, Observable<T> observable, Observer<R> observer, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(doOnSubscribe)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(doFinally)
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .map(new Function<T, R>() {
                    @Override
                    public R apply(T t) throws Exception {
                        return t.getData().transform();
                    }
                })
                .subscribe(observer);
    }

    /**
     * 适用于获取的返回结果是一个List集合
     *
     * @param bindView
     * @param observable
     * @param <T>
     * @param <K>
     * @param <R>
     * @return
     */
    public <T extends BaseListJson<K>, K extends Mapper<R>, R> void executeForList(IView bindView, Observable<T> observable, Observer<List<R>> observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .map(new Function<T, List<R>>() {
                    @Override
                    public List<R> apply(T t) throws Exception {
                        List<R> result = new ArrayList<>();
                        if (t.getData() != null) {
                            List<K> dtos = t.getData();
                            for (K k : dtos) {
                                result.add(k.transform());
                            }
                        }
                        return result;
                    }
                })
                .subscribe(observer);
    }

    /**
     * 适用于获取的返回结果是一个List集合
     *
     * @param bindView
     * @param observable
     * @param doOnSubscribe 在订阅成功的时候回掉（可用于显示加载框等操作）
     * @param doFinally 线程执行完毕之后回掉（可用于关闭加载框等操作）
     * @param <T>
     * @param <K>
     * @param <R>
     * @return
     */
    public <T extends BaseListJson<K>, K extends Mapper<R>, R> void executeForList(IView bindView, Observable<T> observable, Observer<List<R>> observer, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(doOnSubscribe)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(doFinally)
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .map(new Function<T, List<R>>() {
                    @Override
                    public List<R> apply(T t) throws Exception {
                        List<R> result = new ArrayList<>();
                        if (t.getData() != null) {
                            List<K> dtos = t.getData();
                            for (K k : dtos) {
                                result.add(k.transform());
                            }
                        }
                        return result;
                    }
                })
                .subscribe(observer);
    }

    /**
     * 适用于返回结果不是一个Json数据（如：data的内容是int、boolean、String）
     *
     * @param bindView
     * @param observable
     * @param <T>
     * @return
     */
    public <T extends BaseJson> void executeForData(IView bindView, Observable<T> observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .map(new Function<T, Object>() {
                    @Override
                    public Object apply(T t) throws Exception {
                        if (t.getData() != null) {
                            return t.getData();
                        }
                        return "";
                    }
                })
                .subscribe(observer);
    }

    /**
     * 适用于返回结果不是一个Json数据（如：data的内容是int、boolean、String）
     *
     * @param bindView
     * @param observable
     * @param doOnSubscribe 在订阅成功的时候回掉（可用于显示加载框等操作）
     * @param doFinally 线程执行完毕之后回掉（可用于关闭加载框等操作）
     * @param <T>
     * @return
     */
    public <T extends BaseJson> void executeForData(IView bindView, Observable<T> observable, Observer observer, Consumer<Disposable> doOnSubscribe, Action doFinally) {
        observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(doOnSubscribe)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(doFinally)
                .compose(RxLifecycleUtils.bindToLifecycle(bindView))
                .map(new Function<T, Object>() {
                    @Override
                    public Object apply(T t) throws Exception {
                        if (t.getData() != null) {
                            return t.getData();
                        }
                        return "";
                    }
                })
                .subscribe(observer);
    }
}