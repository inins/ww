package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.frame.base.BasicFragment;
import com.frame.component.entities.User;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.DataHelper;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.wang.social.personal.di.component.DaggerFragmentComponent;
import com.wang.social.personal.di.module.UserModule;
import com.wang.social.personal.mvp.entities.UserWrap;
import com.wang.social.personal.mvp.model.UserModel;
import com.wang.social.personal.mvp.ui.activity.AboutActivity;
import com.wang.social.personal.mvp.ui.activity.AccountActivity;
import com.wang.social.personal.mvp.ui.activity.FeedbackActivity;
import com.wang.social.personal.mvp.ui.activity.LableActivity;
import com.wang.social.personal.mvp.ui.activity.MeDetailActivity;
import com.wang.social.personal.mvp.ui.activity.SettingActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * ========================================
 * 个人中心
 * <p>
 * Create by ChenJing on 2018-03-23 16:22
 * ========================================
 */

public class PersonalFragment extends BasicFragment {

    @BindView(R2.id.header)
    ImageView header;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;

    @Inject
    ImageLoader mImageLoader;

    public static PersonalFragment newInstance() {
        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
                .userModule(new UserModule())
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_personal_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        toolbar.bringToFront();
        mImageLoader.loadImage(mActivity, ImageConfigImpl.
                builder()
                .imageView(header)
                .url("http://resouce.dongdongwedding.com/2017-08-08_rtUbDxhH.png")
                .build());
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick({R2.id.header, R2.id.btn_right, R2.id.btn_me_account, R2.id.btn_me_lable, R2.id.btn_me_feedback, R2.id.btn_me_share, R2.id.btn_me_about, R2.id.btn_me_eva})
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.header:
                MeDetailActivity.start(getContext());
                break;
            case R.id.btn_right:
                SettingActivity.start(getContext());
                break;
            case R.id.btn_me_account:
                AccountActivity.start(getContext());
                break;
            case R.id.btn_me_lable:
                LableActivity.start(getContext());
                break;
            case R.id.btn_me_feedback:
                FeedbackActivity.start(getContext());
                break;
            case R.id.btn_me_share:
                break;
            case R.id.btn_me_about:
                AboutActivity.start(getContext());
                break;
            case R.id.btn_me_eva:

                userModel.login("18002247238", "111111")
                        .subscribeOn(Schedulers.newThread())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                Log.e("tag", "start");
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("tag", "end");
                            }
                        })
                        .subscribe(new Observer<BaseJson<UserWrap>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Log.e("tag", "onSubscribe");
                            }

                            @Override
                            public void onNext(BaseJson<UserWrap> userWrap) {
                                Log.e("tag", "onNext");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("tag", "onError");
                                e.printStackTrace();
                            }

                            @Override
                            public void onComplete() {
                                Log.e("tag", "onComplete");
                            }
                        });
                break;
        }
    }

    @Inject
    @Nullable
    UserModel userModel;
}
