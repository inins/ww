package com.wang.social.personal.mvp.presonter;

import android.text.TextUtils;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.ToastUtil;
import com.frame.component.common.NetParam;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.entities.CommonEntity;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MeDetailPresonter extends BasePresenter<MeDetailContract.Model, MeDetailContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;
    @Inject
    QiNiuManager qiNiuManager;

    @Inject
    public MeDetailPresonter(MeDetailContract.Model model, MeDetailContract.View view) {
        super(model, view);
    }

    public void updateUserName(String nickname) {
        updateUserInfo(nickname, null, null, null, null, null, null);
    }

    public void updateUserAvatar(String path) {
//        mRootView.showLoading();
        qiNiuManager.uploadFile(mRootView, path, new QiNiuManager.OnSingleUploadListener() {
            @Override
            public void onSuccess(String url) {
                updateUserInfo(null, url, null, null, null, null, null);
            }

            @Override
            public void onFail() {
                ToastUtil.showToastLong("上传失败");
//                mRootView.hideLoading();
            }
        });
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
                        if (user == null) return;
                        if (!TextUtils.isEmpty(nickname)) user.setNickname(nickname);
                        if (!TextUtils.isEmpty(avatar)) {
                            user.setAvatar(avatar);
                            mRootView.setHeaderImg(avatar);
                        }
                        if (sex != null) user.setSex(sex);
                        if (!TextUtils.isEmpty(birthday)) user.setBirthday(birthday);
                        if (!TextUtils.isEmpty(province)) user.setProvince(province);
                        if (!TextUtils.isEmpty(city)) user.setCity(city);
                        if (TextUtils.isEmpty(autograph)) user.setAutograph(autograph);
                        AppDataHelper.saveUser(user);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        super.onError(e);
                        ToastUtil.showToastShort(e.getMessage());
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