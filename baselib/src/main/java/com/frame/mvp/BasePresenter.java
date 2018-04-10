package com.frame.mvp;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

import com.frame.utils.Preconditions;
import com.trello.rxlifecycle2.RxLifecycle;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;

/**
 * ======================================
 * Presenter 基类
 * <p>
 * Create by ChenJing on 2018-03-12 13:32
 * ======================================
 */

public class BasePresenter<M extends IModel, V extends IView> implements IPresenter, LifecycleObserver {

    protected final String TAG = this.getClass().getSimpleName();

    protected CompositeDisposable mDisposable;

    protected M mModel;
    protected V mRootView;

    /**
     * 若当前页面同时需要 Model 层和 View 层，则使用此构造函数(默认)
     *
     * @param model
     * @param view
     */
    public BasePresenter(M model, V view) {
        Preconditions.checkNotNull(model, "%s cannot be null!", IModel.class.getName());
        Preconditions.checkNotNull(view, "%s cannot be null!", IView.class.getName());
        this.mModel = model;
        this.mRootView = view;
        onStart();
    }

    /**
     * 若当前页面不需要操作数据，只需要 View 层，则使用此构造函数
     * @param rootView
     */
    public BasePresenter(V rootView){
        Preconditions.checkNotNull(rootView, "%s cannot be null!", IView.class.getName());
        this.mRootView = rootView;
        onStart();
    }

    public BasePresenter(){
        onStart();
    }

    @Override
    public void onStart() {
        //必须将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (mRootView != null && mRootView instanceof LifecycleOwner){
            ((LifecycleOwner) mRootView).getLifecycle().addObserver(this);
            if (mModel != null && mModel instanceof LifecycleObserver){
                ((LifecycleOwner) mRootView).getLifecycle().addObserver((LifecycleObserver) mModel);
            }
        }
        if (useEventBus()){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (useEventBus()){
            //如果使用了 EventBus 则解除注册
            EventBus.getDefault().unregister(this);
        }
        clearDisposable();
        if (mModel != null){
            mModel.onDestroy();
        }
        this.mModel = null;
        this.mRootView = null;
        this.mDisposable = null;
    }

    /**
     * 只有当 {@code mRootView} 不为null, 并且{@code mRootView} 实现了 {@link LifecycleOwner} 时，此方法才会被调用
     * 所以当想在 {@link android.app.Service} 以及 自定义 {@link android.view.View} 或 自定义类中使用 {@code Presenter} 时
     * 则不能继续使用 {@link OnLifecycleEvent} 绑定生命周期
     *
     * @param owner
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(LifecycleOwner owner){
        /**
         * 注意, 如果在这里调用了 {@link #onDestroy()} 方法, 会出现某些地方引用 {@code mModel} 或 {@code mRootView} 为 null 的情况
         * 比如在 {@link RxLifecycle} 终止 {@link Observable} 时, 在 {@link io.reactivex.Observable#doFinally(Action)} 中却引用了 {@code mRootView} 做一些释放资源的操作, 此时会空指针
         * 或者如果你声明了多个 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) 时在其他 @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
         * 中引用了 {@code mModel} 或 {@code mRootView} 也可能会出现此情况
         */
        owner.getLifecycle().removeObserver(this);
    }

    /**
     * 若使用{@link org.greenrobot.eventbus.EventBus}则返回 true, 默认为使用
     *
     * @return
     */
    public boolean useEventBus(){
        return false;
    }

    /**
     * 将{@link io.reactivex.disposables.Disposable} 添加到{@link CompositeDisposable} 中同意管理
     * 可在{@link Activity#onDestroy()}中使用{@link #clearDisposable()}停止正在执行的 RxJava 任务，避免内存泄漏
     * 框架使用{@link com.trello.rxlifecycle2.RxLifecycle} 避免内存泄露,此方法作为备选方案
     */
    public void addDisposable(){
        if (mDisposable == null){
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(mDisposable);
    }

    /**
     * 停止集合中正在执行的 RxJava 任务
     */
    public void clearDisposable(){
        if (mDisposable != null){
            mDisposable.clear();  //确保 Activity 结束时取消所有正在执行的订阅
        }
    }
}
