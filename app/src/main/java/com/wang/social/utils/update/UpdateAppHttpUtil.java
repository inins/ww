package com.wang.social.utils.update;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.vector.update_app.HttpManager;

import java.io.File;
import java.util.Map;

/**
 * Created by Vector
 * on 2017/6/19 0019.
 */

public class UpdateAppHttpUtil implements HttpManager {
    Handler handler = new Handler(Looper.getMainLooper());

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
//        OkHttpUtils.get()
//                .url(url)
//                .params(params)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Response response, Exception e, int id) {
//                        callBack.onError(validateError(e, response));
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        callBack.onResponse(response);
//                    }
//                });
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
        callBack.onResponse("POSt");
//        OkHttpUtils.post()
//                .url(url)
//                .params(params)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Response response, Exception e, int id) {
//                        callBack.onError(validateError(e, response));
//                    }
//
//                    @Override
//                    public void onResponse(String response, int id) {
//                        callBack.onResponse(response);
//                    }
//                });
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
//        OkHttpUtils.get()
//                .url(url)
//                .build()
//                .execute(new FileCallBack(path, fileName) {
//                    @Override
//                    public void inProgress(float progress, long total, int id) {
//                        callback.onProgress(progress, total);
//                    }
//
//                    @Override
//                    public void onError(Call call, Response response, Exception e, int id) {
//                        callback.onError(validateError(e, response));
//                    }
//
//                    @Override
//                    public void onResponse(File response, int id) {
//                        callback.onResponse(response);
//
//                    }
//
//                    @Override
//                    public void onBefore(Request request, int id) {
//                        super.onBefore(request, id);
//                        callback.onBefore();
//                    }
//                });
        callback.onBefore();
        DownloadUtil.get().download(url, path, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess(File file) {
                callback.onResponse(file);
            }

            @Override
            public void onDownloading(float progress, long total) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onProgress(progress, total);
                    }
                });
            }

            @Override
            public void onDownloadFailed() {
                callback.onError("下载失败");
            }
        });
    }
}