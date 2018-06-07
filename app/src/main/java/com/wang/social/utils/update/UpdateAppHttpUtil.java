package com.wang.social.utils.update;

import android.support.annotation.NonNull;

import com.vector.update_app.HttpManager;

import java.io.File;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by Vector
 * on 2017/6/19 0019.
 */

public class UpdateAppHttpUtil implements HttpManager {
    /**
     * 异步get
     *
     * @param url      get请求地址
     * @param params   get参数
     * @param callBack 回调
     */
    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        callBack.onResponse("GET");
    }

    /**
     * 异步post
     *
     * @param url      post请求地址
     * @param params   post请求参数
     * @param callBack 回调
     */
    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        callBack.onResponse("POST");
    }

    /**
     * 下载
     *
     * @param url      下载地址
     * @param path     文件保存路径
     * @param fileName 文件名称
     * @param callback 回调
     */
    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
        callback.onBefore();
        DownloadUtil.getInstance().download(url, path, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                callback.onResponse(file);
            }

            @Override
            public void onDownloading(int progress, long total) {
                Timber.tag("onDownloading").d("progress:" + progress + ",total:" + total);
                callback.onProgress(progress * 0.01f, total);
            }

            @Override
            public void onDownloadFailed() {
                callback.onError("下载失败");
            }
        });
    }
}