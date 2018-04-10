package com.wang.social.socialize.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.socialize.R;
import com.wang.social.socialize.SocializeUtil;

import java.util.Map;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by King on 2018/4/2.
 */

public class MainActivity extends BasicActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.socialize_activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
//        UMShareConfig config = new UMShareConfig();
//        config.isNeedAuthOnGetUserInfo(true);
//        UMShareAPI.get(this).setShareConfig(config);

        Button wxLoginBtn = findViewById(R.id.wx_login_btn);
        wxLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wxLogin();
            }
        });
        Button qqLoginBtn = findViewById(R.id.qq_login_btn);
        qqLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qqLogin();
            }
        });
        Button sinaLoginBtn = findViewById(R.id.weibo_login_btn);
        sinaLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinaLogin();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private SocializeUtil.ThirdPartyLoginListener thirdPartyLoginListener = new SocializeUtil.ThirdPartyLoginListener() {
        @Override
        public void onStart(int type) {
            Timber.i("onStart");
        }

        @Override
        public void onComplete(int type, Map<String, String> data) {
            Timber.i("onComplete");
        }

        @Override
        public void onComplete(int type, String uid, String name, int gender, String iconUrl) {
            Timber.i("onComplete");
            Timber.i(String.format("%s \n %s \n %s \n %d \n %s", getLoginTypeName(type), uid, name, gender, iconUrl));
        }

        @Override
        public void onError(int type, Throwable t) {
            Timber.i("onError : " + t.getMessage());
        }

        @Override
        public void onCancel(int type) {
            Timber.i("onCancel");
        }
    };

    private String getLoginTypeName(@SocializeUtil.LoginPlatform int type) {
        switch (type) {
            case SocializeUtil.LOGIN_PLATFORM_WEIXIN:
                return "微信登录";
            case SocializeUtil.LOGIN_PLATFORM_QQ:
                return "QQ登录";
            case SocializeUtil.LOGIN_PLATFORM_SINA_WEIBO:
                return "新浪微博登录";
            default:
                return "未知";
        }
    }

    private void wxLogin() {
        SocializeUtil.wxLogin(this, thirdPartyLoginListener);
    }

    private void qqLogin() {
        SocializeUtil.qqLogin(this, thirdPartyLoginListener);
    }

    private void sinaLogin() {
        SocializeUtil.sinaLogin(this, thirdPartyLoginListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != compositeDisposable) {
            compositeDisposable.dispose();
        }
    }
}
