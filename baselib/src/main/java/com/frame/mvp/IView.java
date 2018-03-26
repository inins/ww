package com.frame.mvp;

/**
 * ======================================
 * 框架要求每个View都需实现此接口，以满足规范
 *
 * Create by ChenJing on 2018-03-12 13:26
 * ======================================
 */
public interface IView {

    /**
     * 显示加载框
     */
    void showLoading();

    /**
     * 隐藏加载框
     */
    void hideLoading();
}
