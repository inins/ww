package com.wang.social.mvp.ui.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.frame.base.BasicActivity;
import com.frame.component.common.AppConstant;
import com.frame.component.entities.BillBoard;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetBillBoardHelper;
import com.frame.component.helper.NetShareHelper;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.integration.AppManager;
import com.frame.mvp.IView;
import com.frame.utils.AppUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.SPUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.Utils;
import com.wang.social.R;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class LoadupActivity extends BasicActivity implements IView {
    private final static int LIFT_TIME = 1500;

    // 记录广告内容
    private BillBoard mBillBoard;
    private long mBillBoardImageDownMills;
    private boolean mBillBoardPicReady = false;

    private Handler mHandler = new Handler();

    private static final int INTERVAL = 10;
    @BindView(R.id.skip_text_view)
    TextView mSkipTV;
    @BindView(R.id.bill_board_image_view)
    ImageView mBillBoardIV;
    // 广告是否已经显示
    private boolean mIsBillBoardVisible = false;

    Disposable mDisposable;

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doTimeOut();
        }
    };

    /**
     * 启动页驻留时间结束
     */
    private void doTimeOut() {
        if (showGuideView()) {
            SplashActivity.start(LoadupActivity.this);
        } else {
            if (CommonHelper.LoginHelper.isLogin()) {
                if (null != mBillBoard && mBillBoardPicReady) {
                    Timber.i("启动广告页");

                    if (!mIsBillBoardVisible) {
                        showBillBoard();
                    }

                    return;
                } else {
                    HomeActivity.start(LoadupActivity.this);
                }
            } else {
                CommonHelper.LoginHelper.startLoginActivity(LoadupActivity.this);
            }
        }

        finish();
    }

    private void showBillBoard() {
        mIsBillBoardVisible = true;
        mSkipTV.setVisibility(View.VISIBLE);
        // 显示广告图片
        mBillBoardIV.setVisibility(View.VISIBLE);
        FrameUtils.obtainAppComponentFromContext(Utils.getContext())
                .imageLoader()
                .loadImage(Utils.getContext(),
                        ImageConfigImpl.
                                builder()
                                .imageView(mBillBoardIV)
                                .isCircle(false)
                                .url(mBillBoard.getPicUrl())
                                .build());

        // 设置图片点击
        mBillBoardIV.setOnClickListener(v -> billBoardClick());

        mSkipTV.setText(String.format("跳过(%1$dS)", INTERVAL));
        mSkipTV.setOnClickListener(v -> {
            skip();
        });

        // 倒计时
        Observable.intervalRange(0, INTERVAL, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (null != mSkipTV) {
                            mSkipTV.setText(String.format("跳过(%1$dS)", Math.max(0, INTERVAL - aLong - 1)));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        if (null != mSkipTV) {
                            mSkipTV.setText(String.format("跳过(%1$dS)", 0));
                        }
                        skip();
                    }
                });
    }

    public void skip() {
        // 启动首页
        HomeActivity.start(this);

        finish();
    }

    public void billBoardClick() {
        Timber.i("打开广告");

        NetBillBoardHelper.newInstance().clickBillBoard(this, mBillBoard.getBillboardId(),
                success -> {});

        String target = "";
        /**
         * linkType-1：内部浏览器2：话题3：趣晒4：外部浏览器
         */
        switch (mBillBoard.getLinkType()) {
            case 1:
                target = AppConstant.AD.AD_INSIDE_WEB;
                break;
            case 2:
                target = AppConstant.AD.AD_TOPIC;
                break;
            case 3:
                target = AppConstant.AD.AD_FUNSHOW;
                break;
            case 4:
                target = AppConstant.AD.AD_OUTSIDE_WEB;
                break;

        }

        recordShare(target, mBillBoard.getLinkUrl(), "");

        // 判断HomeActivity是否在后台
        if (mAppManager.activityClassIsLive(HomeActivity.class)) {
            HomeActivity activity = (HomeActivity) mAppManager.findActivity(HomeActivity.class);
            if (null != activity) {
                HomeActivity.start(this);
                activity.performRemoteCall(target, mBillBoard.getLinkUrl());
            }
        } else {
            HomeActivity.start(this, target, mBillBoard.getLinkUrl());
        }

        finish();
    }

    private AppManager mAppManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext())
            .appManager();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_bill_board;
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
            mHandler.postDelayed(mRunnable, LIFT_TIME);

            // 加载广告,获取内容后开始加载图片
            NetBillBoardHelper.newInstance().getBillboard(this,
                    billBoard -> {
                        mBillBoard = billBoard;
                        if (!TextUtils.isEmpty(billBoard.getPicUrl())) {
                            // 下载广告图片
                            Timber.i("下载广告图片");
                            mBillBoardImageDownMills = System.currentTimeMillis();
                            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                    long duration = System.currentTimeMillis() - mBillBoardImageDownMills;
                                    Timber.i("下载广告图片耗时 : " + Long.toString(duration));
                                    mBillBoardPicReady = true;

                                    // 显示广告
//                                    showBillBoard();
                                }
                            };

                            Glide.with(getApplicationContext())
                                    .load(billBoard.getPicUrl())
                                    .into(simpleTarget);
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mHandler) {
            mHandler.removeCallbacks(mRunnable);
        }
        if (null != mDisposable) {
            mDisposable.dispose();
        }
        super.onDestroy();
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

