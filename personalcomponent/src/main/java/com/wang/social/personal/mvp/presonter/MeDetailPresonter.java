package com.wang.social.personal.mvp.presonter;

import android.text.TextUtils;

import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.frame.component.common.NetParam;
import com.frame.http.api.ApiHelperEx;
import com.wang.social.personal.mvp.contract.MeDetailContract;
import com.wang.social.personal.mvp.entities.CommonEntity;
import com.frame.component.entities.photo.Photo;
import com.wang.social.personal.net.helper.NetPhotoHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

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
    NetPhotoHelper netPhotoHelper;

    @Inject
    public MeDetailPresonter(MeDetailContract.Model model, MeDetailContract.View view) {
        super(model, view);
    }

    public void updateUserName(String nickname) {
        updateUserInfo(nickname, null, null, null, null, null, null);
    }

    public void updateUserAvatar(String path) {
        qiNiuManager.uploadFile(mRootView, path, new QiNiuManager.OnSingleUploadListener() {
            @Override
            public void onSuccess(String url) {
                updateUserInfo(null, url, null, null, null, null, null);
            }

            @Override
            public void onFail() {
                ToastUtil.showToastLong("上传失败");
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
        ApiHelperEx.execute(mRootView, true,
                mModel.updateUserInfo(map),
                new ErrorHandleSubscriber<BaseJson<CommonEntity>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson<CommonEntity> basejson) {
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
                        if (!TextUtils.isEmpty(autograph)) user.setAutograph(autograph);
                        AppDataHelper.saveUser(user);
                        EventBus.getDefault().post(new EventBean(EventBean.EVENT_USERINFO_CHANGE));
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    public void getPhotoList() {
        netPhotoHelper.netGetPhotoList(mRootView, false, new NetPhotoHelper.OnPhotoListApiCallBack() {
            @Override
            public void onSuccess(List<Photo> photoList) {
                mRootView.setPhotoCount(photoList != null ? photoList.size() : 0);
            }

            @Override
            public void onError(Throwable e) {
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