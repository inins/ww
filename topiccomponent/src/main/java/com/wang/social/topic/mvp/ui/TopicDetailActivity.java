package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.BarUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.StringUtil;
import com.wang.social.topic.di.component.DaggerTopicDetailComponent;
import com.wang.social.topic.di.module.TopicDetailModule;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.TopicDetail;
import com.wang.social.topic.mvp.presenter.TopicDetailPresenter;
import com.wang.social.topic.mvp.ui.widget.AppBarStateChangeListener;
import com.wang.social.topic.mvp.ui.widget.GradualColorTextView;
import com.wang.social.topic.mvp.ui.widget.GradualImageView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
    // 顶部播放状态显示
    @BindView(R2.id.play_image_view)
    GradualImageView mGradualImageView;
    // 中间大一点的标题，展开头部时显示
    @BindView(R2.id.title_text_view)
    GradualColorTextView mTitleTV;
    // 标题栏小的标题，合拢时显示
    @BindView(R2.id.toolbar_title_text_view)
    GradualColorTextView mToolbarTitleTV;
    @BindView(R2.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;
    @BindView(R2.id.avatar_image_view)
    ImageView mAvatarIV;
    @BindView(R2.id.gender_image_view)
    ImageView mGenderIV;
    // 年龄
    @BindView(R2.id.age_text_view)
    TextView mAgeTV;
    @BindView(R2.id.constellation_text_view)
    TextView mConstellationTV;
    @BindView(R2.id.content_web_view)
    WebView mContentWV;
    @BindView(R2.id.background_image_view)
    ImageView mBackgroundIV;
    // 标签
    @BindView(R2.id.by_text_view)
    TextView mByTextView;
    @BindView(R2.id.tag_1_text_view)
    TextView mTag1TV;
    @BindView(R2.id.tag_2_text_view)
    TextView mTag2TV;
    @BindView(R2.id.tag_3_text_view)
    TextView mTag3TV;
    // 创建时间
    @BindView(R2.id.create_date_text_view)
    TextView mCreateDateTV;


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
        BarUtils.setTranslucent(this);

        mTopicId = getIntent().getIntExtra(NAME_TOPIC_ID, -1);

        mReportTV.setGradualColor(Color.WHITE, Color.parseColor("#ff333333"));
        mTitleTV.setGradualColor(Color.WHITE, Color.TRANSPARENT);
        mToolbarTitleTV.setGradualColor(Color.TRANSPARENT, Color.parseColor("#ff434343"));
        mGradualImageView.setDrawable(R.drawable.topic_icon_playing1, R.drawable.topic_icon_playing2);

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
//        mPresenter.test();
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
    public void onTopicDetailLoadSuccess(TopicDetail detail) {
        if (null == detail) return;

        // 背景图
        if (TextUtils.isEmpty(detail.getBackgroundImage())) {
            mByTextView.setTextColor(getResources().getColor(R.color.common_text_blank));
            mTag1TV.setTextColor(getResources().getColor(R.color.common_text_blank));
            mTag2TV.setTextColor(getResources().getColor(R.color.common_text_blank));
            mTag3TV.setTextColor(getResources().getColor(R.color.common_text_blank));
            mCreateDateTV.setTextColor(getResources().getColor(R.color.common_text_blank));
            mTitleTV.setGradualColor(getResources().getColor(R.color.common_text_blank), Color.TRANSPARENT);
        } else {
            mByTextView.setTextColor(Color.parseColor("#FFFFFF"));
            mTag1TV.setTextColor(Color.parseColor("#FFFFFF"));
            mTag2TV.setTextColor(Color.parseColor("#FFFFFF"));
            mTag3TV.setTextColor(Color.parseColor("#FFFFFF"));
            mCreateDateTV.setTextColor(Color.parseColor("#FFFFFF"));
            mTitleTV.setGradualColor(Color.parseColor("#FFFFFF"), Color.TRANSPARENT);

            FrameUtils.obtainAppComponentFromContext(this)
                    .imageLoader()
                    .loadImage(this,
                            ImageConfigImpl.builder()
                                    .imageView(mBackgroundIV)
                                    .url(detail.getBackgroundImage())
                                    .build());
        }

        // 标题
        mTitleTV.setText(detail.getTitle());
        mToolbarTitleTV.setText(detail.getTitle());

        // 标签
        for (int i = 0; i < Math.min(detail.getTags().size(), 3); i++) {
            String tagName = "#" + detail.getTags().get(i);
            if (TextUtils.isEmpty(tagName)) continue;

            switch (i) {
                case 0:
                    mTag1TV.setVisibility(View.VISIBLE);
                    mTag1TV.setText(tagName);
                    break;
                case 1:
                    mTag2TV.setVisibility(View.VISIBLE);
                    mTag2TV.setText(tagName);
                    break;
                case 2:
                    mTag3TV.setVisibility(View.VISIBLE);
                    mTag3TV.setText(tagName);
                    break;
            }
        }

        // 创建时间
        String year = getString(R.string.topic_year);
        String month = getString(R.string.topic_month);
        String day = getString(R.string.topic_day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy" + year + "MM" + month + "dd" + day, Locale.getDefault());
        mCreateDateTV.setText(format.format(TimeUtils.millis2Date(detail.getCreateTime())));

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
        mAgeTV.setText("" + StringUtil.getAgeFromBirthTime(TimeUtils.millis2Date(detail.getBirthday())) +
                getResources().getString(R.string.topic_age));

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
