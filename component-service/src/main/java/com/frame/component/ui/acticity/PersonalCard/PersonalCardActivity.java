package com.frame.component.ui.acticity.PersonalCard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetReadHelper;
import com.frame.component.helper.NetReportHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.acticity.PersonalCard.contract.PersonalCardContract;
import com.frame.component.ui.acticity.PersonalCard.di.DaggerPersonalCardComponent;
import com.frame.component.ui.acticity.PersonalCard.di.PersonalCardModule;
import com.frame.component.ui.acticity.PersonalCard.model.entities.PersonalInfo;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserStatistics;
import com.frame.component.ui.acticity.PersonalCard.presenter.PersonalCardPresenter;
import com.frame.component.ui.acticity.PersonalCard.ui.fragment.FriendListFragment;
import com.frame.component.ui.acticity.PersonalCard.ui.fragment.GroupListFragment;
import com.frame.component.ui.acticity.PersonalCard.ui.fragment.TalkListFragment;
import com.frame.component.ui.acticity.PersonalCard.ui.fragment.TopicListFragment;
import com.frame.component.ui.acticity.PersonalCard.ui.widget.AppBarStateChangeListener;
import com.frame.component.ui.acticity.PersonalCard.ui.widget.DialogActionSheet;
import com.frame.component.ui.acticity.PersonalCard.ui.widget.PWFriendMoreMenu;
import com.frame.component.ui.acticity.tags.TagUtils;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.GradualImageView;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.TimeUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalCardActivity extends BaseAppActivity<PersonalCardPresenter> implements
        PersonalCardContract.View {
    public final static int TYPE_UNKNOWN = -1;
    // 好友申请，底部显示 拒绝 同意
    public final static int TYPE_REQUEST_FRIEND = 1;
    // 浏览，底部显示 添加好友
    public final static int TYPE_BROWS = 2;

    @IntDef({
            TYPE_UNKNOWN,
            TYPE_REQUEST_FRIEND,
            TYPE_BROWS
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface PersonCardType {}

    public static void start(Context context, int userId) {
        start(context, userId, TYPE_UNKNOWN);
    }

    public static void start(Context context, int userId, @PersonCardType int type) {
        Intent intent = new Intent(context, PersonalCardActivity.class);
        intent.putExtra("userid", userId);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    // 顶部栏
    @BindView(R2.id.back_icon_image_view)
    GradualImageView mBackGIV;
    // 举报
    @BindView(R2.id.report_text_view)
    TextView mReportTV;
    // 更多
    @BindView(R2.id.more_layout)
    View mMoreLayout;
    @BindView(R2.id.more_image_view)
    ImageView mMoreIV;


    @BindView(R2.id.app_bar_layout)
    AppBarLayout mAppBarLayout;
    // 好友 趣聊 趣晒 话题
    @BindView(R2.id.smart_tab_layout)
    SmartTabLayout mTabLayout;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager;
    // 性别不同显示不同背景图片
    @BindView(R2.id.bg_image_view)
    ImageView mGenderBGIV;
    // 性别不同，信息背景图片不同
    @BindView(R2.id.info_layout)
    View mInfoLayout;
    // 头像
    @BindView(R2.id.avatar_image_view)
    ImageView mAvatarIV;
    // 名字
    @BindView(R2.id.name_text_view)
    TextView mNameTV;
    // 性别不同不同背景
    @BindView(R2.id.gender_layout)
    View mGenderLayout;
    // 性别
    @BindView(R2.id.gender_image_view)
    ImageView mGenderIV;
    // 年纪标签
    @BindView(R2.id.property_text_view)
    TextView mPropertyTV;
    // 星座
    @BindView(R2.id.xingzuo_text_view)
    TextView mXingZuoTV;
    // 标签
    @BindView(R2.id.tags_text_view)
    TextView mTagsTV;
    // 签名
    @BindView(R2.id.sign_text_view)
    TextView mSignTV;
    @BindView(R2.id.bottom_layout)
    View mBottomLayout;
    @BindView(R2.id.bottom_left_text_view)
    TextView mBottomLeftTV;
    @BindView(R2.id.bottom_middle_text_view)
    TextView mBottomMiddleTV;
    @BindView(R2.id.bottom_right_text_view)
    TextView mBottomRightTV;

    // 用户id
    private int mUserId;
    private PersonalInfo mPersonalInfo;
    private @PersonCardType int mType;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerPersonalCardComponent.builder()
                .appComponent(appComponent)
                .personalCardModule(new PersonalCardModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_card_activity;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);

        mUserId = getIntent().getIntExtra("userid", -1);
//        mUserId = 10001;
        mType = getIntent().getIntExtra("type", TYPE_UNKNOWN);

        mBackGIV.setDrawable(R.drawable.common_ic_back_white, R.drawable.common_ic_back);
        mBackGIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        initTabLayout();

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onRateChanged(float rate) {
                mBackGIV.setRate(rate);
            }

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if (state == State.EXPANDED) {
                    //展开状态
                    StatusBarUtil.setTextLight(PersonalCardActivity.this);
                } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                    //折叠状态
                    showReportTV(false);
                    showMoreLayout(false);
                    StatusBarUtil.setTextDark(PersonalCardActivity.this);
                } else {
                    //中间状态
                    showReportTV(true);
                    showMoreLayout(true);
                }
            }
        });

        mPresenter.loadUserInfoAndPhotos(mUserId);
//        mPresenter.loadUserStatistics(mUserId);
    }

    private void showMoreLayout(boolean visible) {
        if (null != mPersonalInfo && mPersonalInfo.getIsFriend() > 0) {
            mMoreLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void showReportTV(boolean visible) {
        if (null != mPersonalInfo && mPersonalInfo.getIsFriend() < 1) {
            mReportTV.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private static String[] TAB_LAYOUT_TITLES = {"好友", "趣聊", "趣晒", "话题"};

    private void initTabLayout() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return FriendListFragment.newInstance(mUserId);
                    case 1:
                        return GroupListFragment.newInstance(mUserId);
                    case 2:
                        return TalkListFragment.newInstance(mUserId);
                    case 3:
                        return TopicListFragment.newInstance(mUserId);
                }

                return FriendListFragment.newInstance(mUserId);
            }

            @Override
            public int getCount() {
                return TAB_LAYOUT_TITLES.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return TAB_LAYOUT_TITLES[position];
            }
        });
        mTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    private void setBottomButtonBg(int resid) {
        mBottomMiddleTV.setBackgroundResource(resid);
        mBottomRightTV.setBackgroundResource(resid);
    }

    /**
     * 加载用户信息成功呢
     *
     * @param personalInfo 用户信息
     */
    @Override
    public void onLoadUserInfoSuccess(PersonalInfo personalInfo) {
        if (null == personalInfo) return;

        mPersonalInfo = personalInfo;
//        personalInfo.setIsFriend(2);

        // 底部按钮背景
        mBottomLeftTV.setBackgroundResource(R.drawable.personal_card_btn_refuse);

        // 性别 0 男 1女
        if (personalInfo.getSex() == 0) {
            mGenderBGIV.setBackgroundResource(R.drawable.personal_card_bg_men);
            mInfoLayout.setBackgroundResource(R.drawable.personal_card_rect_men);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_blue_conerfull);
            mGenderIV.setImageResource(R.drawable.common_ic_man);
            mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.common_blue_deep));
            setBottomButtonBg(R.drawable.personal_card_btn_men);
        } else if (personalInfo.getSex() == 1) {
            mGenderBGIV.setBackgroundResource(R.drawable.personal_card_bg_women);
            mInfoLayout.setBackgroundResource(R.drawable.personal_card_rect_women);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_redgray_conerfull);
            mGenderIV.setImageResource(R.drawable.common_ic_women);
            mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.common_red_gray));
            setBottomButtonBg(R.drawable.personal_card_btn_women);
        } else {
            mGenderBGIV.setBackgroundResource(R.drawable.personal_card_bg_unknown);
            mInfoLayout.setBackgroundResource(R.drawable.personal_card_rect_unknown);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_dark_conerfull);
            mGenderIV.setVisibility(View.GONE);
            mTabLayout.setSelectedIndicatorColors(0xFF88A1FF);
            setBottomButtonBg(R.drawable.personal_card_btn_unknown);
        }

        // 头像
        ImageLoaderHelper.loadCircleImg(mAvatarIV, personalInfo.getAvatar());
        //
        mNameTV.setText(personalInfo.getNickname());
        // 年纪标签
        mPropertyTV.setText(getBirthYears(personalInfo.getBirthday()) + "后");
        // 星座
        mXingZuoTV.setText(TimeUtils.getZodiac(personalInfo.getBirthday()));
        // 标签
        mTagsTV.setText(TagUtils.formatTagNames(personalInfo.getTags()));
        // 签名
        mSignTV.setText(personalInfo.getAutograph());


        mBottomLayout.setVisibility(View.VISIBLE);
        // 是否是好友
        if (personalInfo.getIsFriend() > 0) {
            // 已经是好友了，底部显示 开始聊天
            mBottomMiddleTV.setVisibility(View.VISIBLE);
            mBottomMiddleTV.setText("开始聊天");
        } else {
            // 还不是好友，可能是 好友申请 或者 添加好友
            if (mType == TYPE_REQUEST_FRIEND) {
                // 好友申请，显示 拒绝 同意
                mBottomLeftTV.setVisibility(View.VISIBLE);
                mBottomRightTV.setVisibility(View.VISIBLE);
                mBottomLeftTV.setText("拒绝");
                mBottomRightTV.setText("同意");
            } else {
                // 浏览模式，显示添加好友
                mBottomMiddleTV.setVisibility(View.VISIBLE);
                mBottomMiddleTV.setText("添加好友");
            }
        }

        // 右上角显示
        showMoreLayout(personalInfo.getIsFriend() > 0);
        showReportTV(personalInfo.getIsFriend() <= 0);

        // 显示
        mGenderLayout.setVisibility(View.VISIBLE);
        mXingZuoTV.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);

        // 加载用户数据统计
        mPresenter.loadUserStatistics(mUserId);
    }


    private String getBirthYears(long mills) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mills);
        int year = c.get(Calendar.YEAR);
        year = year - (year % 10);
        year = year % 100;
        if (year == 0) return "00";
        return Integer.toString(year);
    }

    /**
     * 加载用户统计数据成功
     *
     * @param statistics 用户统计数据
     */
    @Override
    public void onLoadUserStatisticsSuccess(UserStatistics statistics) {
        if (null == statistics) return;

        for (int i = 0; i < 4; i++) {
            View view = mTabLayout.getTabAt(i);
            if (null == view) continue;
            TextView numTV = view.findViewById(R.id.number_text_view);

            if (null == numTV) continue;

            switch (i) {
                case 0:
                    // 好友总数
                    numTV.setText("" + statistics.getFriendCount());
                    break;
                case 1:
                    // 趣聊总数
                    numTV.setText("" + statistics.getGroupCount());
                    break;
                case 2:
                    // 趣晒总数
                    numTV.setText("" + statistics.getTalkCount());
                    break;
                case 3:
                    // 话题总数
                    numTV.setText("" + statistics.getTopicCount());
                    break;
            }
        }

        initTabLayout();
    }

    @OnClick(R2.id.report_text_view)
    public void report() {
        doReport();
    }

    private void doReport() {
        final String[] reports = {
                "语言谩骂/骚扰信息",
                "存在欺诈骗钱行为",
                "发布不适当内容"
        };

        DialogActionSheet.show(getSupportFragmentManager(), reports)
                .setActionSheetListener(new DialogActionSheet.ActionSheetListener() {
                    @Override
                    public void onItemSelected(int position, String text) {
                        NetReportHelper.newInstance().netReportPerson(PersonalCardActivity.this,
                                mUserId, text, new NetReportHelper.OnReportCallback() {
                            @Override
                            public void success() {

                            }
                        });
                    }
                });
    }

    private PWFriendMoreMenu mPWFriendMoreMenu;

    @OnClick(R2.id.more_layout)
    public void more() {
        if (null == mPWFriendMoreMenu) {
            mPWFriendMoreMenu = new PWFriendMoreMenu(this);
            mPWFriendMoreMenu.setCallback(new PWFriendMoreMenu.FriendMoreMenuCallback() {
                @Override
                public void onSetRemark() {

                }

                @Override
                public void onSetAvatar() {

                }

                @Override
                public void onReport() {
                    doReport();
                }

                @Override
                public void onDeleteFriend() {

                }

                @Override
                public void onAddBlackList() {

                }
            });
        }

        mPWFriendMoreMenu.showPopupWindow(mMoreIV);
    }
}
