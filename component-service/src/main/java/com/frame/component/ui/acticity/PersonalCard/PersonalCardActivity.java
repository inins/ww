package com.frame.component.ui.acticity.PersonalCard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.acticity.PersonalCard.contract.PersonalCardContract;
import com.frame.component.ui.acticity.PersonalCard.di.DaggerPersonalCardComponent;
import com.frame.component.ui.acticity.PersonalCard.di.PersonalCardModule;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserInfo;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserStatistics;
import com.frame.component.ui.acticity.PersonalCard.presenter.PersonalCardPresenter;
import com.frame.component.ui.acticity.PersonalCard.ui.fragment.FriendListFragment;
import com.frame.component.ui.acticity.PersonalCard.ui.fragment.TalkListFragment;
import com.frame.component.ui.acticity.PersonalCard.ui.fragment.TestFragment;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.acticity.tags.TagUtils;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.TimeUtils;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.Calendar;

import butterknife.BindView;

public class PersonalCardActivity extends BaseAppActivity<PersonalCardPresenter> implements
        PersonalCardContract.View {

    // 顶部栏
    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
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

    // 用户id
    private int mUserId;


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
//        StatusBarUtil.setTranslucent(this);

        mUserId = 10001;
        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            }
        });

        initTabLayout();

        mPresenter.loadUserStatistics(mUserId);
        mPresenter.loadUserInfoAndPhotos(mUserId);
    }

    private static String[] TAB_LAYOUT_TITLES = { "好友", "趣聊", "趣晒", "话题" };
    private void initTabLayout() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return FriendListFragment.newInstance(mUserId);
                    case 1:
                        return TalkListFragment.newInstance(mUserId);
                }

                return new TestFragment();
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

    /**
     * 加载用户信息成功呢
     * @param userInfo 用户信息
     */
    @Override
    public void onLoadUserInfoSuccess(UserInfo userInfo) {
        if (null == userInfo) return;

        // 性别 0 男 1女
        if (userInfo.getSex() == 0) {
            mGenderBGIV.setBackgroundResource(R.drawable.personal_card_bg_men);
            mInfoLayout.setBackgroundResource(R.drawable.personal_card_rect_men);
            mGenderIV.setImageResource(R.drawable.common_ic_man);
        } else if (userInfo.getSex() == 1) {
            mGenderBGIV.setBackgroundResource(R.drawable.personal_card_bg_women);
            mInfoLayout.setBackgroundResource(R.drawable.personal_card_rect_women);
            mGenderIV.setImageResource(R.drawable.common_ic_women);
        } else {

            mGenderBGIV.setBackgroundResource(R.drawable.personal_card_bg_unknown);
            mInfoLayout.setBackgroundResource(R.drawable.personal_card_rect_unknown);
            mGenderIV.setVisibility(View.GONE);
        }

        // 头像
        ImageLoaderHelper.loadCircleImg(mAvatarIV, userInfo.getAvatar());
        //
        mNameTV.setText(userInfo.getNickname());
        // 年纪标签
        mPropertyTV.setText(getBirthYears(userInfo.getBirthday()) + "后");
        // 星座
        mXingZuoTV.setText(TimeUtils.getZodiac(userInfo.getBirthday()));
        // 标签
        mTagsTV.setText(TagUtils.formatTagNames(userInfo.getTags()));
        // 签名
        mSignTV.setText(userInfo.getAutograph());
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
    }
}
