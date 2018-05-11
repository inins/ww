package com.wang.social.personal.net.helper;

import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.http.api.ApiHelperEx;
import com.frame.component.entities.photo.Photo;
import com.wang.social.personal.mvp.entities.photo.PhotoListWrap;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/4/4.
 */

public class NetPhotoHelper {

    IRepositoryManager mRepositoryManager;
    RxErrorHandler mErrorHandler;

    @Inject
    public NetPhotoHelper(IRepositoryManager mRepositoryManager, RxErrorHandler mErrorHandler) {
        this.mRepositoryManager = mRepositoryManager;
        this.mErrorHandler = mErrorHandler;
    }

    public void netGetPhotoList(IView view, OnPhotoListApiCallBack callBack) {
        netGetPhotoList(view, true, callBack);
    }

    public void netGetPhotoList(IView view, boolean needLoading, OnPhotoListApiCallBack callBack) {
        ApiHelperEx.execute(view, needLoading,
                mRepositoryManager.obtainRetrofitService(UserService.class).getPhotoList(),
                new ErrorHandleSubscriber<BaseJson<PhotoListWrap>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<PhotoListWrap> baseJson) {
                        PhotoListWrap wrap = baseJson.getData();
                        List<Photo> photoList = wrap.getList();
                        if (callBack != null) callBack.onSuccess(photoList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null) callBack.onError(e);
                    }
                });
    }

    public void netAddPhoto(IView view, String url, OnPhotoAddApiCallBack callBack) {
        ApiHelperEx.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).addPhoto(url),
                new ErrorHandleSubscriber<BaseJson<Photo>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Photo> basejson) {
                        if (callBack != null) {
                            Photo photo = basejson.getData();
                            photo.setPhotoUrl(url);
                            callBack.onSuccess(photo);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null) callBack.onError(e);
                    }
                });
    }

    public void netDelPhoto(IView view, int userPhotoId, OnPhotoModifyApiCallBack callBack) {
        ApiHelperEx.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).delPhoto(userPhotoId),
                new ErrorHandleSubscriber<BaseJson<Boolean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Boolean> basejson) {
                        if (callBack != null) callBack.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null) callBack.onError(e);
                    }
                });
    }

    public void netModifyPhoto(IView view, int userPhotoId, String url, OnPhotoModifyApiCallBack callBack) {
        ApiHelperEx.execute(view, true,
                mRepositoryManager.obtainRetrofitService(UserService.class).editPhoto(userPhotoId, url),
                new ErrorHandleSubscriber<BaseJson<Boolean>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<Boolean> basejson) {
                        if (callBack != null) callBack.onSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callBack != null) callBack.onError(e);
                    }
                });
    }

    public interface OnPhotoListApiCallBack {
        void onSuccess(List<Photo> photoList);

        void onError(Throwable e);
    }

    public interface OnPhotoAddApiCallBack {
        void onSuccess(Photo photo);

        void onError(Throwable e);
    }

    public interface OnPhotoModifyApiCallBack {
        void onSuccess();

        void onError(Throwable e);
    }
}
