package com.wang.social.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BasicActivity;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetStatisticsHelper;
import com.frame.di.component.AppComponent;
import com.frame.utils.StatusBarUtil;
import com.wang.social.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SplashActivity extends BasicActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    private final static int[] pics = {
            R.drawable.img_guide_shadow,
            R.drawable.img_guide_fun_point,
            R.drawable.img_guide_fun_show
    };

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private List<View> mImageViewList = new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_splash;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);

        initImageViews();

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pics.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(mImageViewList.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(mImageViewList.get(position));
                return mImageViewList.get(position);
            }
        });

        //app安装后首次启动埋点
        NetStatisticsHelper.newInstance().netAppInstall();
    }

    private void initImageViews() {
        mImageViewList.clear();

        for (int i = 0; i < pics.length; i++) {
            View view = LayoutInflater.from(this)
                    .inflate(R.layout.lay_splash, null);

            ImageView iv = view.findViewById(R.id.image_view);
            // 为ImageView添加图片资源
            iv.setImageResource(pics[i]);

            if (i == pics.length - 1) {
                ImageView startIV = view.findViewById(R.id.start_image_view);
                startIV.setVisibility(View.VISIBLE);
                startIV.setOnClickListener(v -> {
                    if (CommonHelper.LoginHelper.isLogin()) {
                        HomeActivity.start(SplashActivity.this);
                    } else {
                        CommonHelper.LoginHelper.startLoginActivity(SplashActivity.this);
                    }
                    finish();
                });
            }

            mImageViewList.add(view);
        }
    }
}
