package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.component.enums.ConversationType;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetGroupHelper;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.helper.GroupPersonReportHelper;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;
import com.wang.social.im.mvp.ui.adapters.HomeTagAdapter;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.pictureselector.helper.PhotoHelperEx;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import timber.log.Timber;

@RouteNode(path = "/team_invite_card", desc = "觅聊名片")
public class GroupMiInviteDetailActivity extends BaseAppActivity implements IView {

    // 浏览模式（底部显示 立即加入）
    public final static int TYPE_BROWSE = 0;
    // 申请模式（群主收到的加入觅聊申请页面，底部显示 拒绝 同意）
    public final static int TYPE_INVITE = 1;

    @IntDef({
            TYPE_BROWSE,
            TYPE_INVITE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface GroupMiInviteType {
    }

    public static void startFromMsg(Context context, int groupId, int msgId) {
        start(context, TYPE_INVITE, groupId, msgId);
    }

    /**
     * 觅聊名片页面，底部显示 立即加入
     * @param context context
     * @param groupId 觅聊群id
     */
    public static void startBrowse(Context context, int groupId) {
        start(context, TYPE_BROWSE, groupId, -1);
    }

    private static void start(Context context, @GroupMiInviteType int type, int groupId, int msgId) {
        Intent intent = new Intent(context, GroupMiInviteDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("groupid", groupId);
        intent.putExtra("msgid", msgId);
        context.startActivity(intent);
    }

    // 顶部栏
    @BindView(R2.id.sc_toolbar)
    SocialToolbar scToolbar;
    // 群名称
    @BindView(R2.id.sc_tv_title)
    TextView scTvTitle;
    // 成员数量
    @BindView(R2.id.member_count_text_view)
    TextView mMemberCountTV;
    // 封面图片
    @BindView(R2.id.sc_iv_cover)
    ImageView scIvCover;
    // 支付标志
    @BindView(R2.id.sc_iv_pay_logo)
    ImageView scIvPayLogo;
    // 标签列表
    @BindView(R2.id.sc_rlv_tags)
    RecyclerView scRlvTags;
    // 群简介
    @BindView(R2.id.sc_tv_intro)
    TextView scTvIntro;
    // layout
    @BindView(R2.id.content_layout)
    ConstraintLayout mContentLayout;
    // 底部栏
    @BindView(R2.id.bottom_layout)
    View mBottomLayout;
    // 拒绝按钮
    @BindView(R2.id.refuse_text_view)
    TextView mRefuseTV;
    // 同意按钮
    @BindView(R2.id.agree_text_view)
    TextView mAgreeTV;
    // 申请入群按钮
    @BindView(R2.id.apply_layout)
    View mApplyLayout;
    @BindView(R2.id.apply_text_view)
    TextView mApplyTV;
    // 申请入群提示
    @BindView(R2.id.apply_hint_text_view)
    TextView mApplyHintTV;


    private ApiHelper mApiHelper = new ApiHelper();
    private IRepositoryManager mRepositoryManager;
    private TeamInfo mTeamInfo;
    private int mGroupId;
    private int mMsgId;
    // 图片选择
    private PhotoHelperEx mPhotoHelperEx;
    // 选择的举报内容
    private String mReportComment;
    // UI类型
    private @GroupMiInviteType int mType;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_mi_invite_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        // UI类型，默认是群主收到的申请加入页面
        mType = getIntent().getIntExtra("type", TYPE_INVITE);
        mGroupId = getIntent().getIntExtra("groupid", -1);
        mMsgId = getIntent().getIntExtra("msgid", -1);

        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();

        scToolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                finish();
            } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                // 举报
                doReport();
            }
        });

        // 图片选择的回调
        mPhotoHelperEx = PhotoHelperEx.newInstance(this,
                path -> {
                    Timber.i("图片返回 : " + path);
                    // 弹出确认举报对话框
                    GroupPersonReportHelper.confirmReportGroup(this, this,
                            "确定要举报该觅聊？",
                            mGroupId,
                            mReportComment,
                            PhotoHelper.format2Array(path));
                });

        if (null != scToolbar.getTvRight()) {
            scToolbar.getTvRight().setVisibility(View.GONE);
        }

        getTeamInfo(mGroupId);
    }

    private void doReport() {
        GroupPersonReportHelper.doReport(getSupportFragmentManager(),
                (position, text) -> {
                    // 记录文字信息
                    mReportComment = text;
                    // 弹出图片选择
                    mPhotoHelperEx.setMaxSelectCount(1);
//                    mPhotoHelperEx.showDefaultDialog();
                    mPhotoHelperEx.startPhoto();
                });
    }

    private void resetMemberCount(int count) {
        mMemberCountTV.setText(
                String.format(getString(R.string.im_format_member_count),
                        count));
    }

    /**
     * 加载信息成功后重置ui显示
     */
    public void showTeamInfo(TeamInfo teamInfo) {
        mTeamInfo = teamInfo;

        mContentLayout.setVisibility(View.VISIBLE);

        if (null != scToolbar.getTvRight()) {
            scToolbar.getTvRight().setVisibility(View.VISIBLE);
        }

        // 群名称
        scTvTitle.setText(mTeamInfo.getName());
        // 群成员数量
        resetMemberCount(mTeamInfo.getMemberSize());

        // 封面图片
        ImageLoaderHelper.getImageLoader()
                .loadImage(this, ImageConfigImpl.builder()
                        .placeholder(R.drawable.im_round_image_placeholder)
                        .errorPic(R.drawable.im_round_image_placeholder)
                        .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                        .imageView(scIvCover)
                        .url(mTeamInfo.getCover())
                        .build());

        // 是否付费
        if (!mTeamInfo.isFree()) {
            scIvPayLogo.setVisibility(View.VISIBLE);
        }

        // 标签
        showTags(mTeamInfo.getTags());

        // 备注说明

        // 底部栏
        resetBottomLayout(teamInfo);
    }

    /**
     * 更具UI类型显示底部的按钮
     */
    private void resetBottomLayout(TeamInfo teamInfo) {
        mBottomLayout.setVisibility(View.VISIBLE);

        if (mType == TYPE_BROWSE) {
            mRefuseTV.setVisibility(View.GONE);
            mAgreeTV.setVisibility(View.GONE);
            mApplyLayout.setVisibility(View.VISIBLE);
            mApplyTV.setVisibility(View.VISIBLE);
            if (teamInfo.isFree()) {
                mApplyHintTV.setVisibility(View.GONE);
            } else {
                mApplyHintTV.setVisibility(View.VISIBLE);
                mApplyHintTV.setText(String.format(getString(R.string.im_group_mi_join_hint),
                        teamInfo.getJoinCost()));
            }

        } else if (mType == TYPE_INVITE) {
            mRefuseTV.setVisibility(View.VISIBLE);
            mAgreeTV.setVisibility(View.VISIBLE);
            mApplyLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 显示标签
     */
    private void showTags(List<Tag> tags) {
        scRlvTags.setNestedScrollingEnabled(false);
        scRlvTags.setLayoutManager(
                ChipsLayoutManager.newBuilder(this)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .build());
        scRlvTags.addItemDecoration(
                new SpacingItemDecoration(SizeUtils.dp2px(5), SizeUtils.dp2px(5)));
        scRlvTags.setAdapter(new HomeTagAdapter(tags));
    }

    /**
     * 获取觅聊详情
     *
     * @param groupId 群id
     */
    public void getTeamInfo(int groupId) {
        mApiHelper.execute(this,
                netGetTeamInfo(Integer.toString(groupId)),
                new ErrorHandleSubscriber<TeamInfo>() {
                    @Override
                    public void onNext(TeamInfo teamInfo) {
                        showTeamInfo(teamInfo);
                    }
                }, disposable -> showLoading()
                , () -> hideLoading());
    }

    /**
     * 获取觅聊详情
     */
    private Observable<BaseJson<TeamInfoDTO>> netGetTeamInfo(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getTeamInfo("2.0.0", socialId);
    }

    /**
     * 同意入群
     */
    private void agreeAdd(int groupId, int msgId) {
        mApiHelper.executeForData(this,
                agreeOrRejectAddCommit(groupId, msgId, 0),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        //  入群后隐藏底部
                        mBottomLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                }, disposable -> showLoading()
                , () -> hideLoading());
    }

    /**
     * 拒绝入群
     */
    private void refuseAdd(int groupId, int msgId) {
        mApiHelper.executeForData(this,
                agreeOrRejectAddCommit(groupId, msgId, 1),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        //  拒绝入群后隐藏底部
                        mBottomLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                }, disposable -> showLoading()
                , () -> hideLoading());
    }

    /**
     * 同意、拒绝邀请加入趣聊、觅聊（别人邀请我的）
     * @param groupId 趣聊/觅聊群id
     * @param msgId 消息id
     * @param type 类型（0：同意，1：拒绝）
     */
    private Observable<BaseJson> agreeOrRejectAddCommit(int groupId, int msgId, int type) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .agreeOrRejectAdd("2.0.0", groupId, msgId, type);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @OnClick(R2.id.refuse_text_view)
    public void refuse() {
        refuseAdd(mGroupId, mMsgId);
    }

    @OnClick(R2.id.agree_text_view)
    public void agree() {
        agreeAdd(mGroupId, mMsgId);
    }

    /**
     * 立即加入
     */
    @OnClick(R2.id.apply_text_view)
    public void apply() {
        // 加入成功后点击可直接进入觅聊
        if (mApplyTV.getText().equals("进入觅聊")) {
            CommonHelper.ImHelper.gotoGroupConversation(
                    this,
                    Integer.toString(mGroupId),
                    ConversationType.TEAM,
                    false);
            finish();
        } else {
            NetGroupHelper.newInstance().addGroup(
                    this,
                    this,
                    getSupportFragmentManager(),
                    mGroupId,
                    isNeedValidation -> {
                        // 隐藏底部栏
//                    mBottomLayout.setVisibility(View.GONE);

                        // 人数+1
                        mTeamInfo.setMemberSize(mTeamInfo.getMemberSize() + 1);
                        resetMemberCount(mTeamInfo.getMemberSize());

                        // 加群成功，发出通知
                        EventBus.getDefault().post(new EventBean(EventBean.EVENTBUS_ADD_TEAM_SUCCESS));

                        if (!isNeedValidation) {
                            // 修改状态为进入趣聊
                            setApplyTextToGoChat();
                        }
                    });
        }
    }


    private void setApplyTextToGoChat() {
        mApplyTV.setText("进入觅聊");

        mApplyHintTV.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPhotoHelperEx.onActivityResult(requestCode, resultCode, data);
    }
}
