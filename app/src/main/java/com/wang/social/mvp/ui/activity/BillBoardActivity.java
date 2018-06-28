package com.wang.social.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.BillBoard;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BillBoardActivity extends BaseAppActivity implements IView {

    private static final int INTERVAL = 5;
    @BindView(R.id.skip_text_view)
    TextView mSkipTV;
    @BindView(R.id.bill_board_image_view)
    ImageView mBillBoardIV;

    Disposable mDisposable;

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
        mBillBoard = getIntent().getParcelableExtra("BillBoard");

        // 倒计时
        Observable.intervalRange(0, INTERVAL + 1, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mSkipTV.setText(String.format("跳过(%1$dS)", Math.max(0, INTERVAL - aLong)));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.skip_text_view)
    public void skip() {
        finish();
    }

    @OnClick(R.id.bill_board_image_view)
    public void billBoardClick() {
        ToastUtil.showToastShort("点击广告");
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
