package com.frame.http.api.error;

import android.content.Context;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:38
 * ========================================
 */

public class ErrorHandleFactory {

    public final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private ResponseErrorListener mResponseErrorListener;

    public ErrorHandleFactory(Context mContext, ResponseErrorListener mResponseErrorListener) {
        this.mContext = mContext;
        this.mResponseErrorListener = mResponseErrorListener;
    }

    public void handleError(Throwable throwable){
        if (mResponseErrorListener != null){
            mResponseErrorListener.handleResponseError(mContext, throwable);
        }
    }
}
