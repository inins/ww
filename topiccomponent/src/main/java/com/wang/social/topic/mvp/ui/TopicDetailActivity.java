package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.frame.component.common.AppConstant;
import com.frame.component.entities.IsShoppingRsp;
import com.frame.component.entities.User;
import com.frame.component.enums.ShareSource;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetIsShoppingHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetShareHelper;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.utils.EntitiesUtil;
import com.frame.component.utils.XMediaPlayer;
import com.frame.component.view.DialogPay;
import com.frame.component.view.MusicBoard;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiException;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.wang.social.socialize.SocializeUtil;
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
import com.wang.social.topic.utils.WebFontStyleUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import timber.log.Timber;

import static com.frame.utils.ToastUtil.showToastLong;

@RouteNode(path="/topic_detail", desc="话题详情")
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
    // 顶部返回按钮
    @BindView(R2.id.back_image_view)
    GradualImageView mBackGIV;
    // 顶部播放状态显示
    @BindView(R2.id.play_image_view)
    GradualImageView mGradualImageView;
    // 中间大一点的标题，展开头部时显示
    @BindView(R2.id.title_text_view)
    GradualColorTextView mTitleTV;
    // 标题栏小的标题，合拢时显示
    @BindView(R2.id.toolbar_title_text_view)
    GradualColorTextView mToolbarTitleTV;

    @BindView(R2.id.content_layout_wrapper)
    LinearLayout mContentLayoutWrapper;

    @BindView(R2.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;
    // 头像
    @BindView(R2.id.avatar_image_view)
    ImageView mAvatarIV;
    // 昵称
    @BindView(R2.id.nick_name_text_view)
    TextView mNickNameTV;
    @BindView(R2.id.no_bg_img_nick_name_text_view)
    TextView mNoBgImgNickNameTV;
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
    // 置顶标记
    @BindView(R2.id.set_top_image_view)
    ImageView mSetTopIV;

    // 音乐播放器，用于播放语音
    private XMediaPlayer mXMediaPlayer;
    // 话题ID
    private int mTopicId;
    private int mCreatorId;
    // 话题详情
    private TopicDetail mTopicDetail;

    private void initXMeidaPlayer() {
        mXMediaPlayer = new XMediaPlayer();
    }

    private void pauseXMediaPlayer() {
        if (null != mXMediaPlayer) {
            mXMediaPlayer.pause();
        }
    }

    private boolean mHasCoverImage = false;

    private void resetShareLayout(int count) {
        mShareTV.setText(String.format(getString(R.string.topic_share_format), count));
    }

    private void resetCommentLayout(int count) {
        mCommentTV.setText(String.format(getString(R.string.topic_comment_format), count));
    }

    /**
     * 重置底部点赞UI
     *
     * @param isSupport 是否已点赞
     * @param count     赞的数量
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

    @Override
    public void onDelMyTopicSuccess(int topicId) {
        Timber.i("删除话题成功");
        EventBean eventBean = new EventBean(EventBean.EVENTBUS_DEL_TOPIC_SUCCESS);
        eventBean.put("topicId", topicId);
        EventBus.getDefault().post(eventBean);
        finish();
    }

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
        StatusBarUtil.setTranslucent(this);

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
                mBackGIV.setRate(rate);
            }

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    if (mHasCoverImage) {
                        StatusBarUtil.setStatusBarColor(TopicDetailActivity.this,
                                android.R.color.transparent);
                    } else {
                        StatusBarUtil.setStatusBarColor(TopicDetailActivity.this,
                                R.color.common_text_dark_light);
                    }
                } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                    //折叠状态
                    StatusBarUtil.setStatusBarColor(TopicDetailActivity.this,
                            R.color.common_text_dark_light);
                } else {
                    //中间状态
                }
            }
        });

        mSocialToolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                finish();
            }

        });

        initXMeidaPlayer();

        // 隐藏UI
        resetView(false);

        // 加载详情
        mPresenter.getTopicDetails(mTopicId);
//        mPresenter.test();

//        NetAccountBalanceHelper.newInstance().accountBalance(this);
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
        mTopicDetail = detail;

        // 是否置顶
        mSetTopIV.setVisibility(detail.isTop() ? View.VISIBLE : View.GONE);

        // 背景图
        if (TextUtils.isEmpty(detail.getBackgroundImage())) {
            mHasCoverImage = false;
            StatusBarUtil.setStatusBarColor(TopicDetailActivity.this,
                    R.color.common_text_dark_light);

            // 没有背景图，返回按钮一直白色
            mBackGIV.setGradual(false);
            mBackGIV.setDrawable(R.drawable.common_ic_back, R.drawable.common_ic_back);

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
            mHasCoverImage = true;
            StatusBarUtil.setStatusBarColor(TopicDetailActivity.this,
                    android.R.color.transparent);

            // 有背景图，返回按钮白黑
            mBackGIV.setGradual(true);
            mBackGIV.setDrawable(R.drawable.common_ic_back_white, R.drawable.common_ic_back);
            // 标签颜色变化
            mTagTV.setGradual(true);
            mTagTV.setGradualColor(Color.parseColor("#FFFFFF"),
                    Color.TRANSPARENT);
            // 日期
            mCreateDateTV.setGradual(true);
            mCreateDateTV.setGradualColor(Color.parseColor("#FFFFFF"),
                    Color.TRANSPARENT);
            // 右上角举报文字颜色变化
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
            ImageLoaderHelper.loadImg(mBackgroundIV, detail.getBackgroundImage());
        }

        // 标题
        mTitleTV.setText(detail.getTitle());
        mToolbarTitleTV.setText(detail.getTitle());

        // 如果是自己发布的话题，显示 删除
        if (detail.getCreatorId() == AppDataHelper.getUser().getUserId()) {
            mReportTV.setText("删除");
        }

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

        // 模板背景
        if (!TextUtils.isEmpty(detail.getTemplateUrl())) {
            SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    BitmapDrawable bd = (BitmapDrawable) resource;
                    bd.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
//                    mNestedScrollView.setBackground(bd);
                    mContentLayoutWrapper.setBackground(bd);
                }
            };

            Glide.with(this).load(detail.getTemplateUrl()).into(simpleTarget);
        }

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
//        mNickNameTV.setText(detail.getNickname());
        showNickName(detail);
        // 性别
        if (detail.getSex() == 0) {
            mGenderIV.setImageResource(R.drawable.common_ic_man);
        } else {
            mGenderIV.setImageResource(R.drawable.common_ic_women);
        }

        // 年龄
//        mAgeTV.setText("" + StringUtil.getAgeFromBirthTime(TimeUtils.millis2Date(detail.getBirthday())) +
//                getResources().getString(R.string.topic_age));
        mAgeTV.setText("" + TimeUtils.getAgeByBirth(detail.getBirthday()) +
                getResources().getString(R.string.topic_age));

        // 星座
        mConstellationTV.setText(TimeUtils.getAstro(detail.getBirthday()));

        // 背景音乐
        if (!TextUtils.isEmpty(detail.getBackgroundMusicUrl())) {
            Music music = new Music();
            music.setMusicId(detail.getBackgroundMusicId());
            music.setMusicName(detail.getBackgroundMusicName());
            music.setUrl(detail.getBackgroundMusicUrl());
            mMusicBoard.resetMusic(music);
            mMusicBoard.setPlayStateListener(new MusicBoard.PlayStateListener() {
                @Override
                public void onPlaying() {
                    // 如果语音正在播放，先暂停语音
                    pauseXMediaPlayer();

                    mGradualImageView.startAnimation();
                }

                @Override
                public void onPause() {
                    mGradualImageView.stopAnimation();
                }

                @Override
                public void onStop() {
                    mGradualImageView.stopAnimation();
                }

                @Override
                public void onComplete() {
                    mGradualImageView.stopAnimation();
                }
            });
        } else {
            mMusicBoard.setVisibility(View.GONE);
            // 没有音乐，不显示顶部播放状态指示器
            mGradualImageView.setVisibility(View.GONE);
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
                    Timber.i("shouldOverrideUrlLoading : " + url);
                    return false;
                }

                public void onPageFinished(WebView view, String url) {
                    if (android.os.Build.VERSION.SDK_INT >= 19) {
                        mContentWV.loadUrl(
                                "javascript: function playAudio(url) {" +
                                        "window.App.playAudio(url);}"
                        );
                    }

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

    @Override
    public void onTopicDetailLoadFailed(ApiException e) {
        if (e.getErrorCode() == 102015) {
            Timber.i("话题未支付");
            showPayTopicDialog();
        }
    }

    /**
     * 显示支付对话框
     */
    private void showPayTopicDialog() {
        // 话题需要支付，先请求是否已支付数据
        NetIsShoppingHelper.newInstance().isTopicShopping(this,
                mTopicId,
                (IsShoppingRsp rsp) -> {
                    /**
                     "isFree": "是否收费（0免费 1收费）",
                     "price": "收费价格,宝石数",
                     "isShopping": "是否需要购买（0 无需购买 1 购买）"
                     */
                    if (rsp.getIsFree() >= 1 && rsp.getIsShopping() >= 1) {
                        // 可能之前的数据里面没有价格，所以这里要先设置价格
                        // 处理支付
                        // 弹出支付对话框
                        DialogPay.showPayTopic(this, getSupportFragmentManager(),
                                rsp.getPrice(),
                                -1,
                                () -> payTopic(mTopicId, rsp.getPrice()))
                                .setCancelCallback(() -> finish());
                    } else {
                        // 不需要支付，直接打开详情
                        mPresenter.getTopicDetails(mTopicId);
                    }
                });
    }

    /**
     * 话题支付
     *
     * @param topicId 话题id
     * @param price   价格
     */
    private void payTopic(int topicId, int price) {
        NetPayStoneHelper.newInstance()
                .netPayTopic(this,
                        topicId,
                        price,
                        () -> {
                            Timber.i("支付成功，打开详情");
                            mPresenter.getTopicDetails(mTopicId);
                        });
    }

    @JavascriptInterface
    public void playAudio(final String url) {
        Timber.i("播放语音 : " + url);

        runOnUiThread(() -> {
            if (null != mMusicBoard && mMusicBoard.isPlaying()) {
                Timber.i("暂停背景音乐播放");
                mMusicBoard.onPause();
            }

            if (null != mXMediaPlayer) {
                if (!mXMediaPlayer.reset(url, true)) {
                    Timber.i("语音已经准备好，直接播放");
                    // 音乐已经准备好了
                    if (mXMediaPlayer.getState() >= XMediaPlayer.STATE_PREPARED) {
                        mXMediaPlayer.play();
                    }
                }
            }

        });
    }

    /**
     * 显示昵称
     *
     * @param detail 话题详情
     */
    private void showNickName(TopicDetail detail) {
        if (TextUtils.isEmpty(detail.getBackgroundImage())) {
            // 没有背景图
            mNickNameTV.setVisibility(View.GONE);
            mNoBgImgNickNameTV.setVisibility(View.VISIBLE);
            mNoBgImgNickNameTV.setText(detail.getNickname());
        } else {
            mNickNameTV.setVisibility(View.VISIBLE);
            mNoBgImgNickNameTV.setVisibility(View.GONE);
            mNickNameTV.setText(detail.getNickname());
        }
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


    /**
     * 点击头像和昵称跳转到用户名片
     */
    @Optional
    @OnClick({R2.id.avatar_image_view, R2.id.nick_name_text_view, R2.id.no_bg_img_nick_name_text_view})
    public void openPersonalCard() {
        CommonHelper.ImHelper.startPersonalCardForBrowse(this, mTopicDetail.getCreatorId());
    }

    /**
     * 删除或举报
     */
    @OnClick(R2.id.report_text_view)
    public void report() {
        if (mReportTV.getText().equals("删除")) {
            DialogSure.showDialog(this,
                    "确定删除?",
                    () -> {
                        Timber.i("删除话题");
                        mPresenter.delMyTopic(mTopicId);
                    });
        } else {
            DialogSure.showDialog(this,
                    "确认举报该话题？",
                    () -> mPresenter.report());
        }
    }

    /**
     * 点赞
     */
    @OnClick(R2.id.support_layout)
    public void support() {
        mPresenter.topicSupport();
    }

    /**
     * 评论
     */
    @OnClick(R2.id.comment_layout)
    public void comment() {
        CommentActivity.startFirstLevel(this, mTopicId, mCreatorId);
    }

    /**
     * 分享
     */
    @OnClick(R2.id.share_layout)
    public void share() {
        if (null == mTopicDetail) return;

        User loginUser = AppDataHelper.getUser();
        String shareUrl = String.format(AppConstant.Share.SHARE_TOPIC_URL, mTopicDetail.getTopicId().toString(), loginUser.getUserId());
        String shareContent = String.format(AppConstant.Share.SHARE_TOPIC_CONTENT, AppDataHelper.getUser().getNickname());
        SocializeUtil.shareWithWW(getSupportFragmentManager(),
                new SocializeUtil.ShareListener() {
                    @Override
                    public void onStart(int platform) {
                    }

                    @Override
                    public void onResult(int platform) {
                        ToastUtil.showToastShort("分享成功");

                        NetShareHelper.newInstance().netShareTopic(
                                TopicDetailActivity.this,
                                null,
                                mTopicId,
                                1,
                                () -> {
                                    // 发送通知分享增加
                                    EventBean bean = new EventBean(EventBean.EVENTBUS_ADD_TOPIC_SHARE);
                                    bean.put("topicId", mTopicId);
                                    EventBus.getDefault().post(bean);
                                });
                    }

                    @Override
                    public void onError(int platform, Throwable t) {
                        ToastUtil.showToastShort(t.getMessage());
                    }

                    @Override
                    public void onCancel(int platform) {
                        ToastUtil.showToastShort("分享取消");
                    }
                },
                shareUrl,
                mTopicDetail.getTitle(),
                shareContent,
                TextUtils.isEmpty(EntitiesUtil.assertNotNull(mTopicDetail.getBackgroundImage())) ?
                        AppConstant.Share.SHARE_DEFAULT_IMAGE :
                        EntitiesUtil.assertNotNull(mTopicDetail.getBackgroundImage()),
                (String url, String title, String content, String imageUrl) -> {
                    CommonHelper.ImHelper.startWangWangShare(this,
                            AppConstant.Share.SHARE_TOPIC_TITLE,
                            content,
                            imageUrl,
                            ShareSource.SOURCE_TOPIC,
                            Integer.toString(mTopicId));
                });
    }

    @OnClick(R2.id.back_image_view)
    public void quit() {
        finish();
    }

    @Override
    public void showToast(String msg) {
        showToastLong(msg);
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

        // 一定要调用，否则后台时任会播放
        if (null != mMusicBoard) {
            mMusicBoard.onPause();
        }

        if (null != mXMediaPlayer) {
            mXMediaPlayer.pause();
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

        if (null != mMusicBoard) {
            mMusicBoard.onStop();
        }

        if (null != mXMediaPlayer) {
            mXMediaPlayer.stop();
        }

        super.onDestroy();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENTBUS_ADD_TOPIC_COMMENT:
                int topicId = (int) event.get("topicId");
                int topicCommentId = (int) event.get("topicCommentId");

                Timber.i("话题详情 评论增加 : " + topicId + " " + topicCommentId);

                if (mTopicId == topicId) {
                    mTopicDetail.setCommentTotal(mTopicDetail.getCommentTotal() + 1);
                    resetCommentLayout(mTopicDetail.getCommentTotal());
                }

                break;
            case EventBean.EVENTBUS_ADD_TOPIC_SHARE:
                // 转发成功，转发量加1
                int shareTopicID = Integer.valueOf(event.get("topicId").toString());
                if (shareTopicID == mTopicId) {
                    Timber.i("话题详情-转发成功 : " + shareTopicID);
                    mTopicDetail.setShareTotal(mTopicDetail.getShareTotal() + 1);
                    resetShareLayout(mTopicDetail.getShareTotal());
                }
                break;
            case EventBean.EVENTBUS_TOPIC_SUPPORT:
                // 点赞
                int supportTopicId = (int) event.get("topicId");
                boolean isSupport = (boolean) event.get("isSupport");
                if (supportTopicId == mTopicId) {
                    Timber.i("话题详情-点赞成功 : " + supportTopicId + " " + Boolean.toString(isSupport));
                    mTopicDetail.setIsSupport(isSupport ? 1 : 0);
                    mTopicDetail.setSupportTotal(Math.max(0, mTopicDetail.getSupportTotal() + (isSupport ? 1 : -1)));
                    resetSupportLayout(mTopicDetail.getIsSupport(), mTopicDetail.getSupportTotal());
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
