package com.frame.http.api.error;

import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:41
 * ========================================
 */

public abstract class ErrorHandleSubscriber<T> implements Observer<T> {

    private ErrorHandleFactory mErrorHandleFactory;

    public ErrorHandleSubscriber() {
        this.mErrorHandleFactory = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).rxErrorHandler().getHandleFactory();
    }

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler) {
        this.mErrorHandleFactory = rxErrorHandler.getHandleFactory();
    }

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();

        mErrorHandleFactory.handleError(e);
    }
}
