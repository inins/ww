package com.frame.mvp;

/**
 * ======================================
 * 框架要求每个Model都需实现此接口，以满足规范
 *
 * Create by ChenJing on 2018-03-12 13:26
 * ======================================
 */
public interface IModel {

    /**
     * 框架中{@link BasePresenter {@link #onDestroy()}} 会自动调用此方法
     */
    void onDestroy();
}