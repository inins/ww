package com.wang.social.personal.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.entities.User;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.personal.PersonalFragmentInterface;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicLazyFragment;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.ImageLoader;
import com.frame.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerFragmentComponent;
import com.wang.social.personal.di.module.UserModule;
import com.wang.social.personal.mvp.entities.user.QrcodeInfo;
import com.wang.social.personal.mvp.entities.user.UserStatistic;
import com.wang.social.personal.mvp.ui.activity.AboutActivity;
import com.wang.social.personal.mvp.ui.activity.AccountActivity;
import com.wang.social.personal.mvp.ui.activity.FeedbackActivity;
import com.frame.component.ui.acticity.FunshowTopicActivity;
import com.wang.social.personal.mvp.ui.activity.LableActivity;
import com.wang.social.personal.mvp.ui.activity.MeDetailActivity;
import com.wang.social.personal.mvp.ui.activity.QrcodeActivity;
import com.wang.social.personal.mvp.ui.activity.RepresentActivity;
import com.wang.social.personal.mvp.ui.activity.SettingActivity;
import com.wang.social.personal.net.helper.NetUserHelper;
import com.wang.social.socialize.SocializeUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ========================================
 * 个人中心
 * <p>
 * Create by ChenJing on 2018-03-23 16:22
 * ========================================
 */

public class PersonalFragment extends BasicLazyFragment implements PersonalFragmentInterface {

    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.btn_left)
    ImageView btnLeft;
    @BindView(R2.id.btn_right)
    ImageView btnRight;
    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_count_sq)
    TextView textCountSq;
    @BindView(R2.id.text_count_ht)
    TextView textCountHt;

    @Inject
    ImageLoader mImageLoader;
    @Inject
    NetUserHelper netUserHelper;

    public static PersonalFragment newInstance() {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_USERINFO_CHANGE:
                setUserData();
                break;
            case EventBean.EVENT_TAB_USER:
                netGetUserStatistics();
                break;
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_fragment_home;
    }

    @Override
    public void initDataLazy() {
        toolbar.bringToFront();
        setUserData();
        netGetUserInfo();
        netGetUserStatistics();
    }

    private void setUserData() {
        User user = AppDataHelper.getUser();
        if (user != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader, user.getAvatar());
            textName.setText(user.getNickname());
        }
    }

    private void setUserData(QrcodeInfo qrcodeInfo) {
        if (qrcodeInfo != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader, qrcodeInfo.getUserAvatar());
            textName.setText(qrcodeInfo.getNickName());
        }
    }

    private void setStatistic(UserStatistic userStatistic) {
        if (userStatistic != null) {
            if (null != textCountSq) {
                textCountSq.setText(String.valueOf(userStatistic.getTalkCount()));
            }
            if (null != textCountHt) {
                textCountHt.setText(String.valueOf(userStatistic.getTopicCount()));
            }
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick({R2.id.lay_nameboard, R2.id.btn_right, R2.id.btn_left, R2.id.btn_me_account,
            R2.id.btn_me_lable, R2.id.btn_me_feedback, R2.id.btn_me_share,
            R2.id.btn_me_about, R2.id.btn_me_eva, R2.id.btn_me_represent,
            R2.id.lay_funshow, R2.id.lay_topic})
    public void onViewClicked(View v) {
        if (v.getId() == R.id.lay_nameboard) {
            MeDetailActivity.start(getContext());
        } else if (v.getId() == R.id.btn_left) {
            User user = AppDataHelper.getUser();
            if (CommonHelper.LoginHelper.isLogin()) {
                QrcodeActivity.start(getContext(), user.getUserId());
            } else {
                CommonHelper.LoginHelper.startLoginActivity(getContext());
            }
        } else if (v.getId() == R.id.btn_right) {
            SettingActivity.start(getContext());
        } else if (v.getId() == R.id.btn_me_account) {
            AccountActivity.start(getContext());
        } else if (v.getId() == R.id.btn_me_lable) {
            LableActivity.start(getContext());
        } else if (v.getId() == R.id.btn_me_feedback) {
            FeedbackActivity.start(getContext());
        } else if (v.getId() == R.id.btn_me_share) {
            User loginUser = AppDataHelper.getUser();
            String shareUrl = String.format(AppConstant.Share.SHARE_APP_URL, String.valueOf(loginUser.getUserId()));
            SocializeUtil.shareWeb(getChildFragmentManager(),
                    null,
                    shareUrl,
                    AppConstant.Share.SHARE_APP_TITLE,
                    String.format(AppConstant.Share.SHARE_APP_CONTENT, loginUser.getNickname()),
                    AppConstant.Share.SHARE_APP_IMAGE);
        } else if (v.getId() == R.id.btn_me_about) {
            AboutActivity.start(getContext(), AppConstant.Url.wwAbout);
        } else if (v.getId() == R.id.btn_me_eva) {
            WebActivity.start(getContext(), AppConstant.Url.eva);
        } else if (v.getId() == R.id.btn_me_represent) {
            RepresentActivity.start(getContext());
        } else if (v.getId() == R.id.lay_funshow) {
            FunshowTopicActivity.startFunshow(getContext());
        } else if (v.getId() == R.id.lay_topic) {
            FunshowTopicActivity.startTopic(getContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMShareAPI.get(getContext()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .userModule(new UserModule())
                .build()
                .inject(this);
    }

    ////////////////////////////////

    /////////////////////////////////

    private void netGetUserInfo() {
        User user = AppDataHelper.getUser();
        if (user == null) return;
        netUserHelper.getUserInfoByUserId(null, user.getUserId(), new NetUserHelper.OnUserApiCallBack() {
            @Override
            public void onSuccess(QrcodeInfo qrcodeInfo) {
                setUserData(qrcodeInfo);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToastLong(e.getMessage());
            }
        });
    }

    private void netGetUserStatistics() {
        User user = AppDataHelper.getUser();
        if (user == null) return;
        netUserHelper.getUserStatistic(null, user.getUserId(), new NetUserHelper.OnUserStatisticApiCallBack() {
            @Override
            public void onSuccess(UserStatistic userStatistic) {
                setStatistic(userStatistic);
            }
        });
    }
}
