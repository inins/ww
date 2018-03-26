package com.frame.http.api.error;

import android.content.Context;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:35
 * ========================================
 */

public class RxErrorHandler {

    public final String TAG = this.getClass().getSimpleName();
    private ErrorHandleFactory mHandleFactory;

    private RxErrorHandler(Builder builder) {
        this.mHandleFactory = builder.mErrorHandleFactory;
    }

    public static Builder builder(){
        return new Builder();
    }

    public ErrorHandleFactory getHandleFactory() {
        return mHandleFactory;
    }

    public static final class Builder{

        private Context mContext;
        private ResponseErrorListener mResponseErrorListener;
        private ErrorHandleFactory mErrorHandleFactory;

        private Builder(){
        }

        public Builder with(Context context){
            this.mContext = context;
            return this;
        }

        public Builder responseErrorListener(ResponseErrorListener responseErrorListener){
            this.mResponseErrorListener = responseErrorListener;
            return this;
        }

        public RxErrorHandler build(){
            if (mContext == null){
                throw new IllegalStateException("Context is required");
            }
            if (mResponseErrorListener == null){
                throw new IllegalStateException("ResponseErrorListener is required");
            }
            this.mErrorHandleFactory = new ErrorHandleFactory(mContext, mResponseErrorListener);
            return new RxErrorHandler(this);
        }
    }
}
