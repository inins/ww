package com.wang.social.mvp.ui.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.frame.component.common.AppConstant;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.viewutils.AppUtil;
import com.frame.di.component.AppComponent;
import com.frame.integration.AppManager;
import com.frame.utils.AppUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.SPUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.Utils;
import com.wang.social.R;

import java.util.Set;

public class LoadupActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (showGuideView()) {
                SplashActivity.start(LoadupActivity.this);
            } else {
                if (CommonHelper.LoginHelper.isLogin()) {
                    HomeActivity.start(LoadupActivity.this);
                } else {
                    CommonHelper.LoginHelper.startLoginActivity(LoadupActivity.this);
                }
            }

            finish();
        }
    };

    private AppManager mAppManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext())
            .appManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setTranslucent(this);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        if (CommonHelper.LoginHelper.isLogin() && intent != null && !TextUtils.isEmpty(intent.getScheme()) && intent.getScheme().equals("wang")) {
            Set<String> params = intent.getData().getQueryParameterNames();
            if (params.contains("target") && params.contains("targetId")) {
                String target = intent.getData().getQueryParameter("target");
                String targetId = intent.getData().getQueryParameter("targetId");
//                String fromUserId = intent.getData().getQueryParameter("fromUserId");

                // 判断HomeActivity是否在后台
                if (mAppManager.activityClassIsLive(HomeActivity.class)) {
                    HomeActivity activity = (HomeActivity) mAppManager.findActivity(HomeActivity.class);
                    if (null != activity) {
                        HomeActivity.start(this);
                        activity.performRemoteCall(target, targetId);
                    }
                } else {
                    HomeActivity.start(this, target, targetId);
                }
            }
        } else {
            mHandler.postDelayed(mRunnable, 1500);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mHandler) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    private boolean showGuideView() {
//        int bootCount = SPUtils.getInstance().getInt("bootCount", 0);
//        SPUtils.getInstance().put("bootCount", bootCount + 1);
//        if (bootCount <= 0) {
//            return true;
//        }

        int versionCode = SPUtils.getInstance().getInt("versionCode", -111);
        if (AppUtils.getAppVersionCode() > versionCode) {
            SPUtils.getInstance().put("versionCode", AppUtils.getAppVersionCode());
            return true;
        }

        return false;
    }
}

