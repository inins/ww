package com.wang.social.im.mvp.ui.PersonalCard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetReportHelper;
import com.frame.component.ui.acticity.tags.TagUtils;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogInput;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.component.view.GradualImageView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.im.mvp.ui.PersonalCard.contract.PersonalCardContract;
import com.wang.social.im.mvp.ui.PersonalCard.di.DaggerPersonalCardComponent;
import com.wang.social.im.mvp.ui.PersonalCard.di.PersonalCardModule;
import com.frame.component.entities.PersonalInfo;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.UserStatistics;
import com.wang.social.im.mvp.ui.PersonalCard.presenter.PersonalCardPresenter;
import com.wang.social.im.mvp.ui.PersonalCard.ui.fragment.FriendListFragment;
import com.wang.social.im.mvp.ui.PersonalCard.ui.fragment.GroupListFragment;
import com.wang.social.im.mvp.ui.PersonalCard.ui.fragment.TalkListFragment;
import com.wang.social.im.mvp.ui.PersonalCard.ui.fragment.TopicListFragment;
import com.wang.social.im.mvp.ui.PersonalCard.ui.widget.AppBarStateChangeListener;
import com.frame.component.ui.dialog.DialogActionSheet;
import com.wang.social.im.mvp.ui.PersonalCard.ui.widget.PWFriendMoreMenu;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.pictureselector.helper.PhotoHelperEx;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;
import com.wang.social.im.R;
import com.wang.social.im.R2;

public class PersonalCardActivity extends BaseAppActivity<PersonalCardPresenter> implements
        PersonalCardContract.View {

    public final static int REQUEST_CODE_AVATAR_IMAGE = 10033;

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
    @interface PersonCardType {
    }

    public static void start(Context context, int userId) {
        start(context, userId, TYPE_UNKNOWN);
    }

    public static void start(Context context, int userId, @PersonCardType int type) {
        startFromMsg(context, userId, -1, type);
    }

    public static void startFromMsg(Context context, int userId, int msgId, @PersonCardType int type) {
        Intent intent = new Intent(context, PersonalCardActivity.class);
        intent.putExtra("userid", userId);
        intent.putExtra("msgId", msgId);
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
    // 存储用户信息
    private PersonalInfo mPersonalInfo;
    // ui类型
    private @PersonCardType int mType;
    // 消息id
    private int mMsgId;
    private PhotoHelperEx mPhotoHelperEx;


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
        return R.layout.im_personal_card_activity;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextDark(this);

        mUserId = getIntent().getIntExtra("userid", -1);
//        mUserId = 10001;
        // 默认为浏览用户信息模式
        mType = getIntent().getIntExtra("type", TYPE_BROWS);
        // 消息id
        mMsgId = getIntent().getIntExtra("msgId", -1);

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
                } else if (state == State.COLLAPSED) {
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

        mPhotoHelperEx = PhotoHelperEx.newInstance(this, new PhotoHelper.OnPhotoCallback() {
            @Override
            public void onResult(String path) {

                Timber.i("图片返回 : " + path);
                String[] pathArray = PhotoHelper.format2Array(path);

                if (null == pathArray) return;
                if (pathArray.length <= 0) return;

                mPresenter.setFriendAvatar(mUserId, pathArray[0]);
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

    @Override
    public void toastShort(String msg) {
        ToastUtil.showToastShort(msg);
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
        mBottomLeftTV.setBackgroundResource(R.drawable.im_personal_card_btn_refuse);

        // 性别 0 男 1女
        if (personalInfo.getSex() == 0) {
            mGenderBGIV.setBackgroundResource(R.drawable.im_personal_card_bg_men);
            mInfoLayout.setBackgroundResource(R.drawable.im_personal_card_rect_men);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_blue_conerfull);
            mGenderIV.setImageResource(R.drawable.common_ic_man);
            mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.common_blue_deep));
            setBottomButtonBg(R.drawable.im_personal_card_btn_men);
        } else if (personalInfo.getSex() == 1) {
            mGenderBGIV.setBackgroundResource(R.drawable.im_personal_card_bg_women);
            mInfoLayout.setBackgroundResource(R.drawable.im_personal_card_rect_women);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_redgray_conerfull);
            mGenderIV.setImageResource(R.drawable.common_ic_women);
            mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.common_red_gray));
            setBottomButtonBg(R.drawable.im_personal_card_btn_women);
        } else {
            mGenderBGIV.setBackgroundResource(R.drawable.im_personal_card_bg_unknown);
            mInfoLayout.setBackgroundResource(R.drawable.im_personal_card_rect_unknown);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_dark_conerfull);
            mGenderIV.setVisibility(View.GONE);
            mTabLayout.setSelectedIndicatorColors(0xFF88A1FF);
            setBottomButtonBg(R.drawable.im_personal_card_btn_unknown);
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

        resetViewWithRelationship(personalInfo);

        // 显示
        mGenderLayout.setVisibility(View.VISIBLE);
        mXingZuoTV.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);

        // 加载用户数据统计
        mPresenter.loadUserStatistics(mUserId);
    }

    /**
     * 设置底部按钮背景
     *
     * @param resid 背景资源
     */
    private void setBottomButtonBg(int resid) {
        mBottomMiddleTV.setBackgroundResource(resid);
        mBottomRightTV.setBackgroundResource(resid);
    }

    /**
     * 根据好友关系来重置相关UI
     */
    private void resetViewWithRelationship(PersonalInfo personalInfo) {
        // 底部栏可见
        mBottomLayout.setVisibility(View.VISIBLE);

        // 先隐藏全部按钮
        mBottomLeftTV.setVisibility(View.GONE);
        mBottomMiddleTV.setVisibility(View.GONE);
        mBottomRightTV.setVisibility(View.GONE);

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
                mBottomLeftTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.rejectApply(mUserId, mMsgId);
                    }
                });
                mBottomRightTV.setText("同意");
                mBottomRightTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.agreeApply(mUserId, mMsgId);
                    }
                });
            } else {
                // 浏览模式，显示添加好友
                mBottomMiddleTV.setVisibility(View.VISIBLE);
                mBottomMiddleTV.setText("添加好友");
                mBottomMiddleTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        mPresenter.addFriendApply();
                        DialogValiRequest.showDialog(PersonalCardActivity.this,
                                content -> {
                                    mPresenter.addFriendApply(mUserId, content);
                                });
                    }
                });
            }
        }

        // 右上角显示
        showMoreLayout(personalInfo.getIsFriend() > 0);
        showReportTV(personalInfo.getIsFriend() <= 0);
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

    @Override
    public void onDeleteFriendSuccess() {
        toastShort("删除成功");

        // 设置已经不是好友了
        mPersonalInfo.setIsFriend(0);

        // 重置界面
        resetViewWithRelationship(mPersonalInfo);

        EventBean eventBean = new EventBean(EventBean.EVENTBUS_FRIEND_DELETE);
        eventBean.put("userid", mUserId);
        EventBus.getDefault().post(eventBean);
    }

    @Override
    public void onSetFriendRemarkSuccess(String remark) {
        // 修改显示昵称
        mNameTV.setText(remark);
        mPersonalInfo.setNickname(remark);

//        toastShort("设置成功");
    }

    /**
     * 同意或拒绝添加好友请求成功
     * @param type 类型（0：同意，1：拒绝）
     */
    @Override
    public void onAgreeOrRejectApllySuccess(int type) {
        if (type == 0) {
            // 同意了，已经是好友
            mPersonalInfo.setIsFriend(1);
        } else {
            // 拒绝，不是好友
            mPersonalInfo.setIsFriend(0);
        }

        // 模式改为浏览模式
        mType = TYPE_BROWS;

        resetViewWithRelationship(mPersonalInfo);
    }

    @Override
    public void onAddFriendApplySuccess() {
        toastShort("申请已发送");
    }

    @Override
    public void onSetFriendAvatarSuccess(String url) {
        mPersonalInfo.setAvatar(url);
        ImageLoaderHelper.loadCircleImg(mAvatarIV, url);
    }

    @Override
    public void onChangeMyBlackSuccess(boolean isBlack) {
        toastShort("已拉黑");

        EventBean eventBean = new EventBean(EventBean.EVENTBUS_FRIEND_ADD_BLACK_LIST);
        eventBean.put("userid", mUserId);
        EventBus.getDefault().post(eventBean);
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
                        // 提示确认是否删除
                        DialogSure.showDialog(PersonalCardActivity.this,
                                "确定要举报该用户？",
                                new DialogSure.OnSureCallback() {
                                    @Override
                                    public void onOkClick() {
                                        NetReportHelper.newInstance().netReportPerson(PersonalCardActivity.this,
                                                mUserId, text, new NetReportHelper.OnReportCallback() {
                                                    @Override
                                                    public void success() {
                                                        ToastUtil.showToastShort("举报成功");
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    private PWFriendMoreMenu mPWFriendMoreMenu;

    private DialogInput mSetRemarkDialot;

    private void showSetRemarkDialog() {
        if (null == mSetRemarkDialot) {
            mSetRemarkDialot = DialogInput.newInstance(PersonalCardActivity.this,
                    "设置备注",
                    "最多输入12个字",
                    "",
                    "取消",
                    "设置",
                    12);
            mSetRemarkDialot.setOnInputListener(new DialogInput.OnInputListener() {
                @Override
                public void onInputText(String text) {
                    // 设置备注
                    mPresenter.setFriendRemard(mUserId, text);

                    mSetRemarkDialot.dismiss();
                }
            });
        }

        mSetRemarkDialot.setText(mPersonalInfo.getNickname());
        mSetRemarkDialot.show();
    }

    @OnClick(R2.id.more_layout)
    public void more() {
        if (null == mPWFriendMoreMenu) {
            mPWFriendMoreMenu = new PWFriendMoreMenu(this);
            mPWFriendMoreMenu.setCallback(new PWFriendMoreMenu.FriendMoreMenuCallback() {
                @Override
                public void onSetRemark() {
                    showSetRemarkDialog();
                }

                @Override
                public void onSetAvatar() {
                    mPhotoHelperEx.setMaxSelectCount(1);
                    mPhotoHelperEx.setClip(true);
                    mPhotoHelperEx.showDefaultDialog();
                }

                @Override
                public void onReport() {
                    doReport();
                }

                @Override
                public void onDeleteFriend() {
                    DialogSure.showDialog(PersonalCardActivity.this,
                            "确认删除好友？",
                            new DialogSure.OnSureCallback() {
                                @Override
                                public void onOkClick() {
                                    mPresenter.deleteFriend(mUserId);
                                }
                            });
                }

                @Override
                public void onAddBlackList() {
                    DialogSure.showDialog(PersonalCardActivity.this,
                            "确认加入黑名单？",
                            new DialogSure.OnSureCallback() {
                                @Override
                                public void onOkClick() {
                                    mPresenter.changeMyBlack(mUserId, true);
                                }
                            });
                }
            });
        }

        mPWFriendMoreMenu.showPopupWindow(mMoreIV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPhotoHelperEx.onActivityResult(requestCode, resultCode, data);
    }
}
