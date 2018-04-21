package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageConfig;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTopicDetailComponent;
import com.wang.social.topic.di.module.TopicDetailModule;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.entities.TopicDetail;
import com.wang.social.topic.mvp.presenter.TopicDetailPresenter;
import com.wang.social.topic.mvp.ui.widget.AppBarStateChangeListener;
import com.wang.social.topic.mvp.ui.widget.GradualColorTextView;
import com.wang.social.topic.mvp.ui.widget.GradualImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class TopicDetailActivity extends BaseAppActivity<TopicDetailPresenter> implements TopicDetailContract.View {

    public final static String NAME_TOPIC_ID = "NAME_TOPIC_ID";

    public static void start(Context context, int topicId) {
        if (null == context) {
            return;
        }

        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(NAME_TOPIC_ID, topicId);
        context.startActivity(intent);
    }

    @BindView(R2.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    @BindView(R2.id.report_text_view)
    GradualColorTextView mReportTV;
    @BindView(R2.id.play_image_view)
    GradualImageView mGradualImageView;
    @BindView(R2.id.title_text_view)
    GradualColorTextView mTitleTV;  // 中间大一点的标题，展开头部时显示
    @BindView(R2.id.toolbar_title_text_view)
    GradualColorTextView mToolbarTitleTV; // 标题栏小的标题，合拢时显示
    @BindView(R2.id.image_layout)
    View mIVLayout;

    // 话题ID
    private int mTopicId;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTopicDetailComponent.builder()
                .appComponent(appComponent)
                .topicDetailModule(new TopicDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_topic_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mTopicId = getIntent().getIntExtra(NAME_TOPIC_ID, -1);

        mReportTV.setGradualColor(Color.WHITE, Color.parseColor("#ff333333"));
        mTitleTV.setGradualColor(Color.WHITE, Color.TRANSPARENT);
        mToolbarTitleTV.setGradualColor(Color.TRANSPARENT, Color.parseColor("#ff434343"));

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            // CollapsingToolbarLayout收起的进度
            @Override
            public void onRateChanged(float rate) {
                mReportTV.setRate(rate);
                mTitleTV.setRate(rate);
                mToolbarTitleTV.setRate(rate);
                mGradualImageView.setRate(rate);
            }

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    mGradualImageView.startAnimation();
                } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                    //折叠状态
                    mGradualImageView.stopAnimation();
                } else {
                    //中间状态
                }
            }
        });

        // 加载详情
        mPresenter.getTopicDetails(mTopicId);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @BindView(R2.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;
    @BindView(R2.id.avatar_image_view)
    ImageView mAvatarIV;
    @BindView(R2.id.gender_image_view)
    ImageView mGenderIV;
    @BindView(R2.id.age_text_view)
    TextView mAgeTV;
    @BindView(R2.id.constellation_text_view)
    TextView mConstellationTV;
    @BindView(R2.id.content_web_view)
    WebView mContentWV;

    @Override
    public void onTopicDetailLoadSuccess(TopicDetail detail) {
        if (null == detail) return;

        // 标题
        mTitleTV.setText(detail.getTitle());
        mToolbarTitleTV.setText(detail.getTitle());

        // 头像
        FrameUtils.obtainAppComponentFromContext(this)
                .imageLoader()
                .loadImage(this,
                        ImageConfigImpl.builder()
                                .imageView(mAvatarIV)
                                .url(detail.getAvatar())
                                .isCircle(true)
                                .build());

        // 性别
        if (detail.getSex() == 0) {
            mGenderIV.setImageResource(R.drawable.common_ic_man);
        } else {
            mGenderIV.setImageResource(R.drawable.common_ic_women);
        }

        // 年龄
//        mAgeTV.setText(detail.get)

        // 星座
        mConstellationTV.setText(TimeUtils.getZodiac(detail.getBirthday()));

        // 页面内容
        mContentWV.loadData(detail.getContent(), "text/html; charset=UTF-8", null);
    }

    @OnClick(R2.id.back_image_view)
    public void back() {
        finish();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }
}
