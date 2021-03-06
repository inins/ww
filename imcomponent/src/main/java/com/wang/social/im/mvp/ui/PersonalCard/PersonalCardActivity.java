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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.acticity.tags.TagUtils;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogInput;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.component.view.GradualImageView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.RegexUtils;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.im.helper.GroupPersonReportHelper;
import com.wang.social.im.mvp.ui.PersonalCard.contract.PersonalCardContract;
import com.wang.social.im.mvp.ui.PersonalCard.di.DaggerPersonalCardComponent;
import com.wang.social.im.mvp.ui.PersonalCard.di.PersonalCardModule;
import com.frame.component.entities.PersonalInfo;
import com.wang.social.im.mvp.ui.PersonalCard.model.entities.UserStatistics;
import com.wang.social.im.mvp.ui.PersonalCard.presenter.PersonalCardPresenter;
import com.frame.component.ui.fragment.FriendListFragment;
import com.frame.component.ui.fragment.GroupListFragment;
import com.frame.component.ui.fragment.TalkListFragment;
import com.frame.component.ui.fragment.TopicListFragment;
import com.wang.social.im.mvp.ui.PersonalCard.ui.widget.AppBarStateChangeListener;
import com.wang.social.im.mvp.ui.PersonalCard.ui.widget.PWFriendMoreMenu;
import com.wang.social.pictureselector.ActivityPicturePreview;
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

@RouteNode(path = "/personal_card", desc = "个人名片")
public class PersonalCardActivity extends BaseAppActivity<PersonalCardPresenter> implements
        PersonalCardContract.View {

    public final static int REQUEST_AVATAR_IMAGE = 10033;
    public final static int REQUEST_REPORT_IMAGE = 10034;

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
    private @PersonCardType
    int mType;
    // 消息id
    private int mMsgId;
    // 图片选择封装
    private PhotoHelperEx mPhotoHelperEx;
    // 选择图片类型
    private int mRequestImageType;
    // 记录举报信息
    private String mReportComment;
    // 右上角弹出窗口
    private PWFriendMoreMenu mPWFriendMoreMenu;
    // 备注输入对话框
    private DialogInput mSetRemarkDialog;


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

        mUserId = getIntent().getIntExtra("userid", -1);
//        mUserId = 10012;
        // 默认为浏览用户信息模式
        mType = getIntent().getIntExtra("type", TYPE_BROWS);
        // 消息id
        mMsgId = getIntent().getIntExtra("msgId", -1);

        mBackGIV.setDrawable(R.drawable.common_ic_back_white, R.drawable.common_ic_back);
        mBackGIV.setOnClickListener(v -> finish());

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
                    StatusBarUtil.setStatusBarColor(PersonalCardActivity.this,
                            android.R.color.transparent);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    showReportTV(false);
                    showMoreLayout(false);
                    StatusBarUtil.setStatusBarColor(PersonalCardActivity.this,
                            R.color.common_text_dark_light);
                } else {
                    //中间状态
                    showReportTV(true);
                    showMoreLayout(true);
                }
            }
        });

        // 图片选择的回调
        mPhotoHelperEx = PhotoHelperEx.newInstance(this,
                path -> {
                    Timber.i("图片返回 : " + path);
                    if (mRequestImageType == REQUEST_AVATAR_IMAGE) {
                        String[] pathArray = PhotoHelper.format2Array(path);

                        if (pathArray.length <= 0) return;

                        mPresenter.setFriendAvatar(mUserId, pathArray[0]);
                    } else if (mRequestImageType == REQUEST_REPORT_IMAGE) {
                        // 弹出确认举报对话框
                        GroupPersonReportHelper.confirmReportPerson(this, this,
                                "确定要举报该用户？",
                                mUserId,
                                mReportComment,
                                PhotoHelper.format2Array(path));
                    }
                });

        mPresenter.loadUserInfoAndPhotos(mUserId);
    }

    private void resetShowMoreLayout(boolean visible) {
        mMoreLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void showMoreLayout(boolean visible) {
        if (null != mPersonalInfo && mPersonalInfo.getIsFriend() > 0) {
            resetShowMoreLayout(visible);
        }
    }

    private void resetShowReportTV(boolean visible) {
        mReportTV.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void showReportTV(boolean visible) {
        if (null != mPersonalInfo && mPersonalInfo.getIsFriend() < 1) {
            resetShowReportTV(visible);
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
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_blue_gray_conerfull);
            mGenderIV.setImageResource(R.drawable.common_ic_man);
            mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.common_blue_gray));
            mTabLayout.setCustomTabView(R.layout.im_personal_card_tab_layout_male, R.id.name_text_view);
            setBottomButtonBg(R.drawable.im_personal_card_btn_men);
        } else if (personalInfo.getSex() == 1) {
            mGenderBGIV.setBackgroundResource(R.drawable.im_personal_card_bg_women);
            mInfoLayout.setBackgroundResource(R.drawable.im_personal_card_rect_women);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_redgray_conerfull);
            mGenderIV.setImageResource(R.drawable.common_ic_women);
            mTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.common_red_gray));
            mTabLayout.setCustomTabView(R.layout.im_personal_card_tab_layout_female, R.id.name_text_view);
            setBottomButtonBg(R.drawable.im_personal_card_btn_women);
        } else {
            mGenderBGIV.setBackgroundResource(R.drawable.im_personal_card_bg_unknown);
            mInfoLayout.setBackgroundResource(R.drawable.im_personal_card_rect_unknown);
            mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_dark_conerfull);
            mGenderIV.setVisibility(View.GONE);
            mTabLayout.setSelectedIndicatorColors(0xFF88A1FF);
            mTabLayout.setCustomTabView(R.layout.im_personal_card_tab_layout_unknown, R.id.name_text_view);
            setBottomButtonBg(R.drawable.im_personal_card_btn_unknown);
        }

        // 头像(如果有备注头像，则使用备注头像)
        ImageLoaderHelper.loadCircleImg(mAvatarIV,
                TextUtils.isEmpty(personalInfo.getRemarkHeadImg()) ?
                        personalInfo.getAvatar() : personalInfo.getRemarkHeadImg());
        // 昵称（优先使用备注昵称）
        mNameTV.setText(TextUtils.isEmpty(personalInfo.getRemarkName()) ?
                personalInfo.getNickname() : personalInfo.getRemarkName());
        // 年纪标签
        mPropertyTV.setText(getBirthYears(personalInfo.getBirthday()) + "后");
        // 星座
        mXingZuoTV.setText(TimeUtils.getAstro(personalInfo.getBirthday()));
        // 标签
        mTagsTV.setText(TagUtils.formatTagNames(personalInfo.getTags()));
        // 签名
        mSignTV.setText(personalInfo.getAutograph());

        resetViewWithRelationship(personalInfo);

        // 显示
        mGenderLayout.setVisibility(View.VISIBLE);
        mXingZuoTV.setVisibility(View.VISIBLE);
        mTabLayout.setVisibility(View.VISIBLE);

        initTabLayout();

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
        int localUserId = 0;
        if (null != AppDataHelper.getUser()) {
            localUserId = AppDataHelper.getUser().getUserId();
        }
        if (personalInfo.getUserId() == localUserId) {
            Timber.i("自己的名片");
            return;
        }

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
            // 跳转到聊天页面
            mBottomMiddleTV.setOnClickListener(
                    v -> CommonHelper.ImHelper.gotoPrivateConversation(this, String.valueOf(mUserId)));
        } else {
            // 还不是好友，可能是 好友申请 或者 添加好友
            if (mType == TYPE_REQUEST_FRIEND) {
                // 好友申请，显示 拒绝 同意
                mBottomLeftTV.setVisibility(View.VISIBLE);
                mBottomRightTV.setVisibility(View.VISIBLE);
                mBottomLeftTV.setText("拒绝");
                mBottomLeftTV.setOnClickListener(v -> mPresenter.rejectApply(mUserId, mMsgId));
                mBottomRightTV.setText("同意");
                mBottomRightTV.setOnClickListener(v -> mPresenter.agreeApply(mUserId, mMsgId));
            } else {
                // 浏览模式，不是好友显示 添加好友
                mBottomMiddleTV.setVisibility(View.VISIBLE);
                mBottomMiddleTV.setText("添加好友");
                mBottomMiddleTV.setOnClickListener(
                        v -> DialogValiRequest.showDialog(PersonalCardActivity.this,
                                content -> mPresenter.addFriendApply(mUserId, content)));
            }
        }

        // 右上角显示
        resetShowMoreLayout(personalInfo.getIsFriend() > 0);
        resetShowReportTV(personalInfo.getIsFriend() <= 0);
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
    }

    @Override
    public void onDeleteFriendSuccess() {
        toastShort("删除成功");

        // 设置已经不是好友了
        mPersonalInfo.setIsFriend(0);

        // 重置界面
        resetViewWithRelationship(mPersonalInfo);

        /**删除好友事件发送转移至IM关系链监听中
         EventBean eventBean = new EventBean(EventBean.EVENTBUS_FRIEND_DELETE);
         eventBean.put("userid", mUserId);
         EventBus.getDefault().post(eventBean);
         */
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
     *
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
        toastShort(isBlack ? "已拉黑" : "已取消黑名单");

        // 修改当前黑名单信息
        mPersonalInfo.setIsBlack(isBlack ? 1 : 0);

        EventBean eventBean = new EventBean(EventBean.EVENTBUS_FRIEND_ADD_BLACK_LIST);
        eventBean.put("userid", mUserId);
        eventBean.put("isBlack", isBlack);
        EventBus.getDefault().post(eventBean);
    }

    @OnClick(R2.id.avatar_image_view)
    public void showAvatar() {
        if (null != mPersonalInfo && !TextUtils.isEmpty(mPersonalInfo.getAvatar())) {
            ActivityPicturePreview.startBrowse(this, 0, false, mPersonalInfo.getAvatar());
        }
    }

    @OnClick(R2.id.report_text_view)
    public void report() {
        doReport();
    }

    private void doReport() {
        GroupPersonReportHelper.doReport(getSupportFragmentManager(),
                (position, text) -> {
                    // 记录文字信息
                    mReportComment = text;
                    // 弹出图片选择
                    mRequestImageType = REQUEST_REPORT_IMAGE;
                    mPhotoHelperEx.setMaxSelectCount(1);
                    mPhotoHelperEx.setClip(false);
//                    mPhotoHelperEx.showDefaultDialog();
                    mPhotoHelperEx.startPhoto();
                });
    }

    private void showSetRemarkDialog() {
        if (null == mSetRemarkDialog) {
            mSetRemarkDialog = DialogInput.newInstance(PersonalCardActivity.this,
                    "设置备注",
                    "最多输入12个字",
                    "",
                    "取消",
                    "设置",
                    12);
            mSetRemarkDialog.setOnInputListener(
                    text -> {
                        boolean dismiss = true;
                        // 设置备注
                        if (!TextUtils.isEmpty(text)) {
                            // 检测输入格式
                            if (RegexUtils.isUsernameMe(text)) {
                                mPresenter.setFriendRemard(mUserId, text);
                            } else {
                                ToastUtil.showToastShort("仅允许输入下划线符号");
                                dismiss = false;
                            }
                        }

                        if (dismiss) {
                            mSetRemarkDialog.dismiss();
                        }
                    }
            );
        }

        mSetRemarkDialog.setText(mPersonalInfo.getNickname());
        mSetRemarkDialog.show();
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
                    // 设置图片选择类型
                    mRequestImageType = REQUEST_AVATAR_IMAGE;
                    // 设置图片选择参数
                    mPhotoHelperEx.setMaxSelectCount(1);
                    mPhotoHelperEx.setClip(true);
                    // 显示图片选择对话框
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
                            () -> mPresenter.deleteFriend(mUserId));
                }

                @Override
                public void onAddBlackList() {
                    // 是否黑名单，默认未拉黑 0：未拉黑，大于0：黑名单
                    boolean isBlack = null == mPersonalInfo ? false : mPersonalInfo.getIsBlack() > 0;
                    DialogSure.showDialog(PersonalCardActivity.this,
                            isBlack ? "确认取消黑名单？" : "确认加入黑名单？",
                            () -> mPresenter.changeMyBlack(mUserId, !isBlack));
                }
            });
        }

        // 设置是否黑名单(默认未拉黑）
        mPWFriendMoreMenu.setBlack(null == mPersonalInfo ? 0 : mPersonalInfo.getIsBlack());
        mPWFriendMoreMenu.showPopupWindow(mMoreIV);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPhotoHelperEx.onActivityResult(requestCode, resultCode, data);
    }
}
