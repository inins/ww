package com.wang.social.login.mvp.presenter;

import android.text.TextUtils;

import com.frame.component.common.AppConstant;
import com.frame.component.helper.AppDataHelper;
import com.wang.social.login.R;
import com.wang.social.login.mvp.contract.LoginContract;
import com.wang.social.login.mvp.model.entities.LoginInfo;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.component.ui.acticity.tags.Tags;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.wang.social.socialize.SocializeUtil;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * ========================================
 * <p>
 * Create by ChenJing on 2018-03-21 13:13
 * ========================================
 */
@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View view) {
        super(model, view);
    }

    public void clearToken() {
        AppDataHelper.saveToken("");
        AppDataHelper.saveImSign("");
    }

    /**
     * 密码登录
     * @param mobile
     * @param password
     */
    public void passwordLogin(String mobile, String password) {
        mApiHelper.execute(mRootView,
                mModel.passwordLogin(mobile, password),
                new ErrorHandleSubscriber<LoginInfo>(mErrorHandler) {

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        doLoginSuccess(loginInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }

                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }


    /**
     * 已选推荐标签列表(兴趣标签)
     */
    public void myRecommendTag() {
        mApiHelper.execute(mRootView,
                // 这里需要一次获取所有的标签，所以给一个很大的数字
                mModel.myRecommendTag(Integer.MAX_VALUE, 0),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        if (tags != null && tags.getList() != null) {
                            doLoginComplete(tags.getList().size() > 0);
                        } else {
                            doLoginComplete(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        doLoginComplete(false);
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 登录成功后的操作，保存用户信息，尝试获取用户兴趣标签信息
     *
     * @param loginInfo 返回的用户信息
     */
    private void doLoginSuccess(LoginInfo loginInfo) {
        Timber.i("登录成功");
        // 保存数据
        AppDataHelper.saveToken(loginInfo.getToken());
        AppDataHelper.saveImSign(loginInfo.getImSign());
        AppDataHelper.saveUser(loginInfo.getUserInfo());

        // 获取标签信息
        myRecommendTag();
    }

    /**
     * 登录成功后需要判断是否选择了兴趣标签
     * @param hasTags
     */
    private void doLoginComplete(boolean hasTags) {
        if (!hasTags) {
            // 跳转到标签选择页面
            mRootView.gotoTagSelection();
        } else {
            // 跳转到首页
            mRootView.gotoMainPage();
        }

    }

    /**
     * 短信登录
     * @param mobile
     * @param code
     */
    public void messageLogin(String mobile, String code) {
        mApiHelper.execute(mRootView,
                mModel.verifyCodeLogin(mobile, code, ""),
                new ErrorHandleSubscriber<LoginInfo>(mErrorHandler) {

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        doLoginSuccess(loginInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }

                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 第三方登录
     */
    private void platformLogin(int platform, String uid, String nickname, String headUrl, int sex) {

        mApiHelper.execute(mRootView,
                mModel.platformLogin(platform,
                        uid,
                        nickname,
                        headUrl,
                        sex, ""),
                new ErrorHandleSubscriber<LoginInfo>(mErrorHandler) {
                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        doLoginSuccess(loginInfo);
                    }


                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 注册
     * @param mobile 手机号码
     * @param code 短信验证码
     * @param password 设置的密码
     * @param inviteCode 邀请码
     */
    public void register(String mobile, String code, String password, String inviteCode) {
        mApiHelper.execute(mRootView,
                mModel.userRegister(mobile, code, password, "", inviteCode),
                new ErrorHandleSubscriber<LoginInfo>(mErrorHandler) {

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        // 保存数据
                        AppDataHelper.saveToken(loginInfo.getToken());
                        AppDataHelper.saveToken(loginInfo.getImSign());
                        AppDataHelper.saveUser(loginInfo.getUserInfo());
                        // 提示注册成功
                        // 自动登录
                        passwordLogin(mobile, password);
//                        // 跳转到标签选择页面
//                        TagSelectionActivity.startSelectionFromLogin(mRootView.getActivity());
//                        mRootView.getActivity().finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }

                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 手机号码加短信验证码登录
     * @param mobile 手机号码
     * @param type 用途类型
     *
    用途类型
    （注册 type=1;
    找回密码 type=2;
    三方账号绑定手机 type=4;
    更换手机号 type=5;
    短信登录 type=6）
     * @return
     */
    public void sendVerifyCode(String mobile, int type) {
        mApiHelper.executeForData(mRootView,
                mModel.sendVerifyCode(mobile, type),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {
//                        mRootView.showToast(o.toString());

                        mRootView.onSendVerifyCodeSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }

                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    // 第三方登录回调
    private SocializeUtil.LoginListener loginListener = new SocializeUtil.LoginListener() {
        @Override
        public void onStart(int platform) {
            mRootView.showLoading();
        }

        @Override
        public void onComplete(int platform, Map<String, String> data, String uid, String nickname, int sex, String headUrl) {
            Timber.i(
                    String.format(
                            "授权成功 : %d %s %s %s %d \n %s",
                            platform, platform, uid, nickname, sex, headUrl));

            // 授权登录成功，调用往往第三方登录接口
            platformLogin(platform,
                    uid,
                    TextUtils.isEmpty(nickname) ? "W-NewType" : nickname,
                    TextUtils.isEmpty(headUrl) ? "http://static.wangsocial.com/userDefault.png" : headUrl,
                    sex);
        }

        @Override
        public void onError(int platform, Throwable t) {
            mRootView.hideLoading();
            mRootView.showToast(t.getMessage());
        }

        @Override
        public void onCancel(int platform) {
            mRootView.hideLoading();
            mRootView.showToast(mRootView.getActivity().getString(R.string.login_user_cancel));
        }
    };

    /**
     * 微信登录
     */
    public void wxLogin() {
        SocializeUtil.wxLogin(mRootView.getActivity(), loginListener);
    }

    /**
     * QQ登录
     */
    public void qqLogin() {
        SocializeUtil.qqLogin(mRootView.getActivity(), loginListener);
    }

    /**
     * 新浪微博登录
     */
    public void sinaLogin() {
        SocializeUtil.sinaLogin(mRootView.getActivity(), loginListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}