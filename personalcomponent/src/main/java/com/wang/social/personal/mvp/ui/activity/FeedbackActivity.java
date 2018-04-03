package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.wang.social.personal.R;
import com.wang.social.personal.di.component.DaggerFragmentComponent;
import com.wang.social.personal.mvp.ui.view.bundleimgview.BundleImgEntity;
import com.wang.social.personal.mvp.ui.view.bundleimgview.BundleImgView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackActivity extends BasicActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bundleview)
    BundleImgView bundleview;

    @Inject
    ImageLoader mImageLoader;

    public static void start(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_feedback;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        bundleview.setOnBundleLoadImgListener(new BundleImgView.OnBundleLoadImgListener() {
            @Override
            public void onloadImg(ImageView imageView, String imgurl, int defaultSrc) {
//                mImageLoader.loadImage(FeedbackActivity.this, ImageConfigImpl.
//                        builder()
//                        .imageView(imageView)
//                        .url(imgurl)
//                        .build());
            }
        });
        bundleview.setPhotos(new ArrayList<BundleImgEntity>() {{
            add(new BundleImgEntity("https://a.cdnsbn.com/images/products/l/20857459814.jpg"));
            add(new BundleImgEntity("https://a.cdnsbn.com/images/products/l/10005703602.jpg"));
            add(new BundleImgEntity("https://a.cdnsbn.com/images/products/l/12834780402.jpg"));
        }});
    }

    public void onClick(View v) {

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
//        DaggerFragmentComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
    }
}
