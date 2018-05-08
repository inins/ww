package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.MusicBoard;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.BarUtils;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.utils.StringUtil;
import com.wang.social.topic.di.component.DaggerTopicDetailComponent;
import com.wang.social.topic.di.module.TopicDetailModule;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.entities.TopicDetail;
import com.wang.social.topic.mvp.presenter.TopicDetailPresenter;
import com.wang.social.topic.mvp.ui.widget.AppBarStateChangeListener;
import com.wang.social.topic.mvp.ui.widget.GradualColorTextView;
import com.wang.social.topic.mvp.ui.widget.GradualImageView;
import com.wang.social.topic.utils.WebFontStyleUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class TopicDetailActivity extends BaseAppActivity<TopicDetailPresenter> implements TopicDetailContract.View {
    private static final String SETUP_HTML = "file:///android_asset/editor.html";
    public final static String NAME_TOPIC_ID = "NAME_TOPIC_ID";
    public final static String NAME_CREATOR_ID = "NAME_CREATOR_ID";

    public static void start(Context context, int topicId, int creatorId) {
        if (null == context) {
            return;
        }

        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(NAME_TOPIC_ID, topicId);
        intent.putExtra(NAME_CREATOR_ID, creatorId);
        context.startActivity(intent);
    }

    // 加载详情成功前只显示左上返回按钮，所以添加了一个cover层
    @BindView(R2.id.social_tool_bar)
    SocialToolbar mSocialToolbar;

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
    // 头像
    @BindView(R2.id.avatar_image_view)
    ImageView mAvatarIV;
    // 昵称
    @BindView(R2.id.nick_name_text_view)
    TextView mNickNameTV;
    // 性别
    @BindView(R2.id.gender_image_view)
    ImageView mGenderIV;
    // 年龄
    @BindView(R2.id.age_text_view)
    TextView mAgeTV;
    // 星座
    @BindView(R2.id.constellation_text_view)
    TextView mConstellationTV;
    // 背景音乐
    @BindView(R2.id.music_board_layout)
    MusicBoard mMusicBoard;
    // 内容 WebView
    @BindView(R2.id.content_layout)
    FrameLayout mContentLayout;
    WebView mContentWV;
    @BindView(R2.id.background_image_view)
    ImageView mBackgroundIV;
    // 标签
    @BindView(R2.id.tag_text_view)
    GradualColorTextView mTagTV;
    // 创建时间
    @BindView(R2.id.create_date_text_view)
    GradualColorTextView mCreateDateTV;
    // 底部栏
    @BindView(R2.id.bottom_layout)
    View mBottomLayout;
    // 底部点赞
    @BindView(R2.id.support_image_view)
    ImageView mSupportIV;
    @BindView(R2.id.support_text_view)
    TextView mSupportTV;
    // 底部评论
    @BindView(R2.id.comment_image_view)
    ImageView mCommentIV;
    @BindView(R2.id.comment_text_view)
    TextView mCommentTV;
    // 底部分享
    @BindView(R2.id.share_image_view)
    ImageView mShareIV;
    @BindView(R2.id.share_text_view)
    TextView mShareTV;

    private void resetShareLayout(int count) {
        mShareTV.setText(String.format(getString(R.string.topic_share_format), count));
    }

    private void resetCommentLayout(int count) {
        mCommentTV.setText(String.format(getString(R.string.topic_comment_format), count));
    }

    /**
     * 重置底部点赞UI
     * @param isSupport 是否已点赞
     * @param count 赞的数量
     */
    @Override
    public void resetSupportLayout(int isSupport, int count) {
        if (isSupport == 0) {
            mSupportIV.setImageResource(R.drawable.common_ic_zan);
            mSupportTV.setText(String.format(getString(R.string.topic_support_format), count));
            mSupportTV.setTextColor(Color.parseColor("#818181"));
        } else {
            mSupportIV.setImageResource(R.drawable.common_ic_zan_hot);
            mSupportTV.setText(String.format(getString(R.string.topic_supported_format), count));
            mSupportTV.setTextColor(getResources().getColor(R.color.common_blue_deep));
        }
    }

    // 话题ID
    private int mTopicId;
    private int mCreatorId;

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
        mCreatorId = getIntent().getIntExtra(NAME_CREATOR_ID, -1);
//        mTopicId = 30;

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            // CollapsingToolbarLayout收起的进度
            @Override
            public void onRateChanged(float rate) {
                mReportTV.setRate(rate);
                mTitleTV.setRate(rate);
                mToolbarTitleTV.setRate(rate);
                mTagTV.setRate(rate);
                mCreateDateTV.setRate(rate);
                mGradualImageView.setRate(rate);
            }

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
//                    mGradualImageView.startAnimation();
                } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                    //折叠状态
//                    mGradualImageView.stopAnimation();
                } else {
                    //中间状态
                }
            }
        });

        mSocialToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            }
        });

        // 隐藏UI
        resetView(false);

        // 加载详情
        mPresenter.getTopicDetails(mTopicId);
//        mPresenter.test();

//        NetFindMyWalletHelper.newInstance().findMyWallet(this);
    }

    private void resetView(boolean loaded) {
        if (loaded) {
            mSocialToolbar.setVisibility(View.GONE);

            mAppBarLayout.setVisibility(View.VISIBLE);
            mNestedScrollView.setVisibility(View.VISIBLE);
            mBottomLayout.setVisibility(View.VISIBLE);
        } else {
            // 隐藏UI
            mAppBarLayout.setVisibility(View.GONE);
            mNestedScrollView.setVisibility(View.GONE);
            mBottomLayout.setVisibility(View.GONE);

            mSocialToolbar.setVisibility(View.VISIBLE);
        }
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
        mTopicId = detail.getTopicId();
        mCreatorId = detail.getCreatorId();

        // 背景图
        if (TextUtils.isEmpty(detail.getBackgroundImage())) {
            // 没有背景图时候的配色方案
            mBackgroundIV.setVisibility(View.GONE);
            mGradualImageView.setVisibility(View.GONE);
            // 标签
            mTagTV.setGradual(true);
            mTagTV.setGradualColor(getResources().getColor(R.color.common_text_blank),
                    Color.TRANSPARENT);
            // 日期
            mCreateDateTV.setGradual(true);
            mCreateDateTV.setGradualColor(getResources().getColor(R.color.common_text_blank),
                    Color.TRANSPARENT);
            // 大标题颜色变化
            mTitleTV.setGradual(true);
            mTitleTV.setGradualColor(getResources().getColor(R.color.common_text_blank),
                    Color.TRANSPARENT);
            // 小标题颜色变化
            mToolbarTitleTV.setGradual(true);
            mToolbarTitleTV.setGradualColor(Color.TRANSPARENT,
                    Color.parseColor("#ff434343"));
            // 右上角举报文字颜色不变化
            mReportTV.setTextColor(getResources().getColor(R.color.common_text_blank));
            mReportTV.setGradual(false);
            // 音乐播放的状态图标不变化
            mGradualImageView.setDrawable(R.drawable.common_ic_playing2, R.drawable.common_ic_playing2);
            mGradualImageView.setGradual(false);
        } else {
            mTagTV.setGradual(true);
            mTagTV.setGradualColor(Color.parseColor("#FFFFFF"),
                    Color.TRANSPARENT);
            // 日期
            mCreateDateTV.setGradual(true);
            mCreateDateTV.setGradualColor(Color.parseColor("#FFFFFF"),
                    Color.TRANSPARENT);

            mReportTV.setGradual(true);
            mReportTV.setGradualColor(Color.WHITE, getResources().getColor(R.color.common_text_blank));
            // 大标题颜色变化
            mTitleTV.setGradual(true);
            mTitleTV.setGradualColor(Color.parseColor("#FFFFFF"),
                    Color.TRANSPARENT);
            // 小标题颜色变化
            mToolbarTitleTV.setGradual(true);
            mToolbarTitleTV.setGradualColor(Color.TRANSPARENT,
                    Color.parseColor("#ff434343"));
            mToolbarTitleTV.setGradualColor(Color.TRANSPARENT, Color.parseColor("#ff434343"));
            // 音乐播放的状态图标变化
            mGradualImageView.setGradual(true);
            mGradualImageView.setDrawable(R.drawable.common_ic_playing1, R.drawable.common_ic_playing2);

            mBackgroundIV.setVisibility(View.VISIBLE);
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
        String tag = "by";
        for (int i = 0; i < Math.min(detail.getTags().size(), 3); i++) {
            String tagName = detail.getTags().get(i).getTagName();
            if (TextUtils.isEmpty(tagName)) continue;

            tag = tag + " #" + tagName;
        }
        mTagTV.setText(tag);

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
        // 昵称
        mNickNameTV.setText(detail.getNickname());
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

        // 背景音乐
        if (!TextUtils.isEmpty(detail.getBackgroundMusicUrl())) {
            Music music = new Music();
            music.setMusicId(detail.getBackgroundMusicId());
            music.setMusicName(detail.getBackgroundMusicName());
            music.setUrl(detail.getBackgroundMusicUrl());
            mMusicBoard.resetMusic(music);
        } else {
            mMusicBoard.setVisibility(View.GONE);
        }

        // 页面内容
        if (null == mContentWV) {
            mContentWV = new WebView(getApplicationContext());
            mContentWV.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            WebSettings setting = mContentWV.getSettings();


            setting.setDefaultTextEncodingName("UTF-8");
//            setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            setting.setDefaultTextEncodingName("utf-8");
            setting.setLoadsImagesAutomatically(true);//设置自动加载图片
            setting.setJavaScriptEnabled(true);
            mContentWV.setWebChromeClient(new WebChromeClient());
            mContentWV.setVerticalScrollBarEnabled(false);
            mContentWV.setHorizontalScrollBarEnabled(false);
            mContentWV.setBackgroundColor(Color.TRANSPARENT);

            mContentWV.addJavascriptInterface(this, "App");

            mContentWV.setWebViewClient(new WebViewClient() {

                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                public void onPageFinished(WebView view, String url) {
                    if(android.os.Build.VERSION.SDK_INT >= 19) {
                        mContentWV.loadUrl("javascript:(function(){"
                                + "var objs = document.getElementsByTagName('img'); "
                                + "for(var i=0;i<objs.length;i++) {"
                                + // //webview图片自适应，android4.4之前都有用，4.4之后google优化后，无法支持，需要自己手动缩放
                                " objs[i].style.width = '100%';" +
                                "objs[i].style.height = 'auto';"
                                + "}"
                                + "})()"
                        );
                    } else{
                        view.loadUrl("javascript:var imgs = document.getElementsByTagName('img');for(var i = 0; i<imgs.length; i++){imgs[i].style.width = '100%';imgs[i].style.height= 'auto';}");

                    }
                    //LogUtils.showTagE(wv_content.getContentHeight() + "");
                    //wv_content.loadUrl("javascript:window.jo.run(document.documentElement.scrollHeight+'');");
                    mContentWV.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                }
            });

            mContentLayout.addView(mContentWV);
        }
//        mContentWV.loadData(detail.getContent(), "text/html; charset=UTF-8", null);
        mContentWV.loadDataWithBaseURL(SETUP_HTML,
                WebFontStyleUtil.getFontStyle() + detail.getContent(),
                "text/html", "utf-8", null);

        // 底部
        resetSupportLayout(detail.getIsSupport(), detail.getSupportTotal());
        resetCommentLayout(detail.getCommentTotal());
        resetShareLayout(detail.getShareTotal());

        // 显示UI
        resetView(true);
    }


    @JavascriptInterface
    public void resize(final float height) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //wv_content.getLayoutParams().height = (int) (height * getResources().getDisplayMetrics().density);
                Timber.w("resize : " + height);
                mContentWV.setLayoutParams(
                        new FrameLayout.LayoutParams(
//                                getResources().getDisplayMetrics().widthPixels,
                                mContentLayout.getWidth(),
                                (int) (height * getResources().getDisplayMetrics().density)
                                        + (SizeUtils.dp2px(14) * 2) + mBottomLayout.getHeight()));
            }
        });
    }

    private class HeightGetter {
        @JavascriptInterface
        public void run(final String height) {
            runOnUiThread(new Runnable() {
                public void run() {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                            mContentWV.getLayoutParams();
                    params.height = Integer.parseInt(height);
                    Timber.w("HeightGetter : " + params.height);
                    mContentLayout.setLayoutParams(params);
                    //Toast.makeText(getApplicationContext(), height, 0).show();
                }
            });
        }
    }

    @OnClick(R2.id.report_text_view)
    public void report() {
        mPresenter.report();
    }

    @OnClick(R2.id.support_layout)
    public void support() {
        mPresenter.topicSupport();
    }

    @OnClick(R2.id.comment_layout)
    public void comment() {
        CommentActivity.startFirstLevel(this, mTopicId, mCreatorId);
    }

    @OnClick(R2.id.share_layout)
    public void share() {

    }

    @OnClick(R2.id.back_image_view)
    public void quit() {
        finish();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (null != mContentWV) {
            mContentWV.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != mContentWV) {
            mContentWV.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDestroy() {
        if (null != mContentWV) {
            mContentWV.setVisibility(View.GONE);
            mContentLayout.removeAllViews();
            mContentWV.destroy();
            mContentWV = null;
        }

        super.onDestroy();
    }
}
