package com.frame.component.helper;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.frame.component.api.Api;
import com.frame.component.api.CommonService;
import com.frame.component.entities.QiNiu;
import com.frame.component.entities.dto.QiNiuDTO;
import com.frame.component.utils.UrlUtil;
import com.frame.http.api.BaseJson;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.Utils;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UploadManager;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * 七牛文件上传工具类
 * <p>
 * Author: ChenJing
 * Date: 2017-06-07 上午 11:09
 * Version: 1.0
 */
public class QiNiuManager {

    private IRepositoryManager mRepositoryManager;

    @Inject
    public QiNiuManager(IRepositoryManager repositoryManager) {
        this.mRepositoryManager = repositoryManager;
    }

    private QiNiuManager() {
        this.mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();
    }

    public static QiNiuManager newInstance() {
        return new QiNiuManager();
    }

    /**
     * 上传文件列表
     *
     * @param view           要绑定生命周期的组件
     * @param files
     * @param uploadListener
     */
    public void uploadFiles(final IView view, final ArrayList<String> files, final OnBatchUploadListener uploadListener) {
        //如果图片路径集合是空的，那么直接返回
        if (StrUtil.isEmpty(files)) {
            if (uploadListener != null) uploadListener.onSuccess(files);
            return;
        }
        if (view != null) view.showLoading();
        getToken(view, new OnTokenListener() {
            @Override
            public void success(final String token) {
                Observable observable = Observable.just(files)
                        .map(new Function<ArrayList<String>, ArrayList<String>>() {
                            @Override
                            public ArrayList<String> apply(@NonNull ArrayList<String> strings) throws Exception {
                                ArrayList<String> urls = new ArrayList<String>();
                                for (String string : files) {
                                    urls.add(upload(token, string));
                                }
                                return urls;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                if (view != null) {
                    observable.compose(RxLifecycleUtils.<ArrayList<String>>bindToLifecycle(view));
                }
                observable.subscribe(new Observer<ArrayList<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<String> strings) {
                        if (view != null) view.hideLoading();
                        if (uploadListener != null) {
                            uploadListener.onSuccess(strings);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) view.hideLoading();
                        if (uploadListener != null) {
                            uploadListener.onFail();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void fail() {
                uploadListener.onFail();
            }
        });
    }

    /**
     * 上传单个文件
     *
     * @param path
     * @param onSingleUploadListener
     */
    public void uploadFile(final IView view, final String path, final OnSingleUploadListener onSingleUploadListener) {
        //如果资源是空，那么直接返回
        if (TextUtils.isEmpty(path)) {
            if (onSingleUploadListener != null) onSingleUploadListener.onSuccess(path);
            return;
        }
        if (view != null) view.showLoading();
        getToken(view, new OnTokenListener() {
            @Override
            public void success(final String token) {
                Observable observable = Observable.just(path)
                        .map(new Function<String, String>() {
                            @Override
                            public String apply(@NonNull String string) {
                                return upload(token, string);
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
                if (view != null) {
                    observable.compose(RxLifecycleUtils.<String>bindToLifecycle(view));
                }
                observable.subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String strings) {
                        if (view != null) view.hideLoading();
                        if (onSingleUploadListener != null) {
                            onSingleUploadListener.onSuccess(strings);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) view.hideLoading();
                        if (onSingleUploadListener != null) {
                            onSingleUploadListener.onFail();
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }

            @Override
            public void fail() {
                if (onSingleUploadListener != null) {
                    onSingleUploadListener.onFail();
                }
            }
        });
    }

    /**
     * 获取上传Token
     *
     * @param onTokenListener
     * @return
     */
    private String getToken(IView view, final OnTokenListener onTokenListener) {
        Observable<BaseJson<QiNiuDTO>> observable = mRepositoryManager.obtainRetrofitService(CommonService.class).getQiNiuToken();
        if (view != null) {
            observable.compose(RxLifecycleUtils.bindToLifecycle(view));
        }
        observable.map(new Function<BaseJson<QiNiuDTO>, QiNiu>() {
            @Override
            public QiNiu apply(BaseJson<QiNiuDTO> t) {
                return t.getData().transform();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QiNiu>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(QiNiu qiNiu) {
                        onTokenListener.success(qiNiu.getToken());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        onTokenListener.fail();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return null;
    }

    /**
     * 调用七牛上传文件Api
     *
     * @param path
     * @return
     */
    private String upload(String token, String path) {
        //特殊处理：如果传入的path本身就是网络图片地址，则直接返回
        if (StrUtil.isUrl(path)) {
            return path;
        }
//        path = Uri.encode(path);
        final File file = new File(path);
        if (!file.exists()) {
            Timber.e(path + " 不存在");
            return "";
        }

        UploadManager uploadManager = new UploadManager();
        String fileName = file.getName();
        String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
        String uploadName = TimeUtils.millis2String(System.currentTimeMillis(), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())) + "_" + UUID.randomUUID() + suffix;
        ResponseInfo responseInfo = uploadManager.syncPut(file, uploadName, token, null);
        if (responseInfo.isOK()) {
            return Api.QINIU_PREFIX + uploadName;
        } else {
            return "";
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mRepositoryManager = null;
    }

    /**
     * 上传Token获取监听
     */
    private interface OnTokenListener {

        void success(String token);

        void fail();
    }

    /**
     * 单文件上传监听
     */
    public interface OnSingleUploadListener {

        void onSuccess(String url);

        void onFail();
    }

    /**
     * 批量上传监听
     */
    public interface OnBatchUploadListener {

        void onSuccess(ArrayList<String> urls);

        void onFail();
    }
}