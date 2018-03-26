package com.frame.http.api.error;

import android.content.Context;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:36
 * ========================================
 */

public interface ResponseErrorListener {

    void handleResponseError(Context context, Throwable t);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handleResponseError(Context context, Throwable t) {

        }
    };
}
