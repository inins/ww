package com.wang.social.im.mvp.presenter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.frame.component.enums.ConversationType;
import com.frame.component.utils.FileUtil;
import com.frame.di.scope.ActivityScope;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.PageList;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ImageUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.contract.BackgroundSettingContract;
import com.wang.social.im.mvp.model.entities.OfficialImage;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.frame.entities.EventBean.EVENT_NOTIFY_BACKGROUND;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-24 9:19
 * ============================================
 */
@ActivityScope
public class BackgroundSettingPresenter extends BasePresenter<BackgroundSettingContract.Model, BackgroundSettingContract.View> {

    private int currentPage;

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public BackgroundSettingPresenter(BackgroundSettingContract.Model model, BackgroundSettingContract.View view) {
        super(model, view);
    }

    public void getImages(boolean showLoading) {
        currentPage++;
        mApiHelper.execute(mRootView, mModel.getOfficialImages(currentPage),
                new ErrorHandleSubscriber<PageList<OfficialImage>>() {
                    @Override
                    public void onNext(PageList<OfficialImage> listData) {
                        mRootView.showImages(listData.getList(), listData.getCurrent() >= listData.getMaxPage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        currentPage--;
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (showLoading) {
                            mRootView.showLoading();
                        } else {
                            mRootView.hideListLoading();
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        if (showLoading) {
                            mRootView.hideLoading();
                        } else {
                            mRootView.hideListLoading();
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void downloadAndSetting(String url, ConversationType conversationType, String targetId) {
        Observable.just(url)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String s) throws Exception {
                        Bitmap bitmap = Glide
                                .with(mRootView.getActivity())
                                .asBitmap()
                                .load(s)
                                .into(720, 1080)
                                .get();
                        if (bitmap != null) {
                            File file = new File(ImHelper.getBackgroundCachePath(), ImHelper.getBackgroundFileName(conversationType, targetId));
                            file.deleteOnExit();
                            ImageUtils.save(bitmap, file, Bitmap.CompressFormat.JPEG);
                            return file.getPath();
                        }
                        return "";
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (!TextUtils.isEmpty(s) && new File(s).exists()) {
                            EventBean event = new EventBean(EVENT_NOTIFY_BACKGROUND);
                            event.put("typeOrdinal", conversationType.ordinal());
                            event.put("targetId", targetId);
                            EventBus.getDefault().post(event);

                            mRootView.getActivity().finish();
                        } else {
                            ToastUtil.showToastShort("背景图片设置失败");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtil.showToastShort("背景图片设置失败");
                    }
                });
    }
}