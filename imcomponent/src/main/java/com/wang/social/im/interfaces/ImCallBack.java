package com.wang.social.im.interfaces;

/**
 * ============================================
 * 通用回调
 * <p>
 * Create by ChenJing on 2018-05-09 18:00
 * ============================================
 */
public abstract class ImCallBack {

    /**
     * 执行成功
     *
     * @param object
     */
    public abstract void onSuccess(Object object);

    /**
     * 执行失败
     *
     * @param e
     * @return 返回 false 则不拦截错误处理， 返回 true 则拦截不进行公共错误处理
     */
    public boolean onFail(Throwable e) {
        return false;
    }
}