package com.frame.mvp;

/**
 * ======================================
 * 框架要求每个Presenter都需实现此接口， 以满足规范
 * <p>
 * <p>
 * Create by ChenJing on 2018-03-12 13:30
 * ======================================
 */

public interface IPresenter {

    /**
     * 做一些初始化操作
     */
    void onStart();

    /**
     * 在框架中{@link android.app.Activity #onDestroy()} 时会默认调用此方法
     */
    void onDestroy();
}
