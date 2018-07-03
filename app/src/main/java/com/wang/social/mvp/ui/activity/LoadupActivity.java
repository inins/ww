package com.wang.social.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.frame.base.BasicActivity;
import com.frame.component.common.AppConstant;
import com.frame.component.entities.BillBoard;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetBillBoardHelper;
import com.frame.component.helper.NetShareHelper;
import com.frame.di.component.AppComponent;
import com.frame.integration.AppManager;
import com.frame.mvp.IView;
import com.frame.utils.AppUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.SPUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.Utils;

import java.util.Set;

import timber.log.Timber;

public class LoadupActivity extends BasicActivity implements IView {

    // 记录广告内容
    private BillBoard mBillBoard;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (showGuideView()) {
                SplashActivity.start(LoadupActivity.this);
            } else {
                if (CommonHelper.LoginHelper.isLogin()) {
                    if (null != mBillBoard) {
                        Timber.i("启动广告页");
                        // 启动广告页
                        BillBoardActivity.start(LoadupActivity.this, mBillBoard);
                    } else {
                        HomeActivity.start(LoadupActivity.this);
                    }
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
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return 0;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);

        /**
         * 外部链接打开
         */
        Intent intent = getIntent();
        if (CommonHelper.LoginHelper.isLogin() && intent != null && !TextUtils.isEmpty(intent.getScheme()) && intent.getScheme().equals("wang")) {
            Set<String> params = intent.getData().getQueryParameterNames();
            if (params.contains("target") && params.contains("targetId")) {
                String target = intent.getData().getQueryParameter("target");
                String targetId = intent.getData().getQueryParameter("targetId");
                String fromUserId = intent.getData().getQueryParameter("fromUserId");

                recordShare(target, targetId, fromUserId);

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

            finish();
        } else {
            mHandler.postDelayed(mRunnable, 1000);

//            // 加载广告
//            NetBillBoardHelper.newInstance().getBillboard(this,
//                    billBoard -> mBillBoard = billBoard);
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
        int versionCode = SPUtils.getInstance().getInt("versionCode", -111);
        if (AppUtils.getAppVersionCode() > versionCode) {
            SPUtils.getInstance().put("versionCode", AppUtils.getAppVersionCode());
            return true;
        }

        return false;
    }

    private void recordShare(String target, String objectId, String fromUserId) {
        int selfUserId = AppDataHelper.getUser().getUserId();
        int fromUid = 0;
        int objId = 0;
        try {
            fromUid = Integer.parseInt(fromUserId);
            objId = Integer.parseInt(objectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String type;
        switch (target) {
            case AppConstant.Share.SHARE_GROUP_OPEN_TARGET:
                type = NetShareHelper.SHARE_TYPE_GROUP;
                break;
            case AppConstant.Share.SHARE_TOPIC_OPEN_TARGET:
                type = NetShareHelper.SHARE_TYPE_TOPIC;
                break;
            case AppConstant.Share.SHARE_FUN_SHOW_OPEN_TARGET:
                type = NetShareHelper.SHARE_TYPE_FUN_SHOW;
                break;
            default:
                return;
        }
        NetShareHelper.newInstance().netShare(null, fromUid, selfUserId, objId, type, 1, new NetShareHelper.OnShareCallback() {

            @Override
            public void success() {

            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}

