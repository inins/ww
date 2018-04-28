package com.frame.component.app;

import android.content.Context;

import com.frame.component.service.R;
import com.frame.component.utils.UIUtil;
import com.frame.http.api.error.ResponseErrorListener;
import com.frame.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * ========================================
 * 处理网络请求返回的公共错误码
 * <p>
 * Create by ChenJing on 2018-03-21 16:27
 * ========================================
 */

public class ResponseErrorListenerImp implements ResponseErrorListener {

    @Override
    public void handleResponseError(Context context, Throwable t) {
        if (t instanceof ConnectException) {
            ToastUtil.showToastShort(UIUtil.getString(R.string.com_toast_network_error));
        } else if (t instanceof SocketTimeoutException) {
            ToastUtil.showToastShort(UIUtil.getString(R.string.com_toast_timeout));
        }
    }
}