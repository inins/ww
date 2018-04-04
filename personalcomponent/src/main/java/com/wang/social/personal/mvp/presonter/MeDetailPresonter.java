package com.wang.social.personal.mvp.presonter;

import android.text.TextUtils;
import android.util.Log;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.RxLifecycleUtils;
import com.wang.social.personal.common.NetParam;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.wang.social.personal.mvp.entities.QiniuTokenWrap;
import com.wang.social.personal.mvp.entities.UserWrap;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MeDetailPresonter extends BasePresenter<MeDetailContract.Model, MeDetailContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    @Inject
    public MeDetailPresonter(MeDetailContract.Model model, MeDetailContract.View view) {
        super(model, view);
    }

    public void uploadImg(String path) {
        mModel.getQiniuToken()
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<QiniuTokenWrap>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<QiniuTokenWrap> baseJson) {
                        QiniuTokenWrap wrap = baseJson.getData();
                        Timber.e(wrap.getToken());
                    }
                });
    }

    public void updateUserName(String nickname) {
        updateUserInfo(nickname, null, null, null, null, null, null);
    }

    public void updateUserAvatar(String avatar) {
        updateUserInfo(null, avatar, null, null, null, null, null);
    }

    public void updateUserGender(String gender) {
        updateUserInfo(null, null, gender.equals("男") ? 0 : 1, null, null, null, null);
    }

    public void updateUserBirthday(String birthday) {
        updateUserInfo(null, null, null, birthday, null, null, null);
    }

    public void updateUserSign(String sign) {
        updateUserInfo(null, null, null, null, sign, null, null);
    }

    public void updateUserAddress(String province, String city) {
        updateUserInfo(null, null, null, null, null, province, city);
    }

    private void updateUserInfo(String nickname, String avatar, Integer sex, String birthday, String autograph, String province, String city) {
        Map<String, Object> map = new NetParam()
                .put("nickname", nickname)
                .put("avatar", avatar)
                .put("sex", sex)
                .put("birthday", birthday)
                .put("autograph", autograph)
                .put("province", province)
                .put("city", city)
                .build();
        mModel.updateUserInfo(map)
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<CommonEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<CommonEntity> baseJson) {
                        User user = AppDataHelper.getUser();
                        if (user == null) user = new User();
                        if (TextUtils.isEmpty(nickname)) user.setNickname(nickname);
                        if (TextUtils.isEmpty(avatar)) user.setAvatar(avatar);
                        if (sex != null) user.setSex(sex);
                        if (TextUtils.isEmpty(birthday)) user.setBirthday(birthday);
//                        if (TextUtils.isEmpty(province)) user.se(province);
//                        if (TextUtils.isEmpty(city)) user.setc(city);
//                        if (TextUtils.isEmpty(autograph)) user.set(autograph);
                        mRootView.finishActivity();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }

}