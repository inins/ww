package com.wang.social.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.AppConstant;
import com.frame.component.entities.BillBoard;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetBillBoardHelper;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.integration.AppManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class BillBoardActivity extends BaseAppActivity implements IView {

    private static final int INTERVAL = 3;
    @BindView(R.id.skip_text_view)
    TextView mSkipTV;
    @BindView(R.id.bill_board_image_view)
    ImageView mBillBoardIV;

    Disposable mDisposable;

    private AppManager mAppManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext())
            .appManager();

    // 广告内容
    private BillBoard mBillBoard;

    public static void start(Context context, BillBoard billBoard) {
        Intent intent = new Intent(context, BillBoardActivity.class);
        intent.putExtra("BillBoard", billBoard);
        context.startActivity(intent);
    }

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

        mBillBoard = getIntent().getParcelableExtra("BillBoard");

        if (null != mBillBoard) {
            mSkipTV.setText(String.format("跳过(%1$dS)", INTERVAL));

//            ImageLoaderHelper.loadImg(mBillBoardIV, mBillBoard.getPicUrl());
            FrameUtils.obtainAppComponentFromContext(Utils.getContext())
                    .imageLoader()
                    .loadImage(Utils.getContext(),
                            ImageConfigImpl.
                                    builder()
                                    .imageView(mBillBoardIV)
                                    .isCircle(false)
                                    .url(mBillBoard.getPicUrl())
                                    .build());

            mBillBoardIV.setOnClickListener(v -> billBoardClick());

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
                            mSkipTV.setText(String.format("跳过(%1$dS)", Math.max(0, INTERVAL - aLong - 1)));
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                            mSkipTV.setText(String.format("跳过(%1$dS)", 0));
                            skip();
                        }
                    });
        } else {
            skip();
        }

    }

    @OnClick(R.id.skip_text_view)
    public void skip() {
        // 启动首页
        HomeActivity.start(this);
        finish();
    }

    //    @OnClick(R.id.bill_board_image_view)
    public void billBoardClick() {
//        ToastUtil.showToastShort("点击广告");
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


    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mDisposable) {
            mDisposable.dispose();
        }
    }
}
