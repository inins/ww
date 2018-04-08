package com.wang.social.login.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.login.R;
import com.wang.social.login.mvp.ui.ForgotPasswordActivity;
import com.wang.social.login.mvp.ui.ResetPasswordActivity;
import com.wang.social.login.mvp.ui.VerifyPhoneActivity;
import com.wang.social.login.mvp.ui.widget.DialogFragmentLoading;

import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
        return R.layout.login_activity_main;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (null != compositeDisposable) {
            compositeDisposable.dispose();
        }
    }

    @OnClick(R.id.reset_password_btn)
    public void resetPassword() {
//        ResetPasswordActivity.start(this);
    }

    @OnClick(R.id.forgot_password_btn)
    public void forgotPassword() {
        ForgotPasswordActivity.start(this);
    }

    @OnClick(R.id.verify_phone_btn)
    public void verifyPhone() {
//        VerifyPhoneActivity.start(this);
    }

    DialogFragmentLoading loadingDialog;

    @OnClick(R.id.loading_btn)
    public void loading() {
//        Observable.timer(6, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        compositeDisposable.add(d);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        MainActivity.this.finish();
//                    }
//                });

        Observable.timer(10, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);


                        loadingDialog = DialogFragmentLoading.showDialog(getSupportFragmentManager(), TAG);
                    }

                    @Override
                    public void onNext(Long aLong) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }
                });
    }
}
