package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetGroupHelper;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.TeamInfoDTO;
import com.wang.social.im.mvp.ui.adapters.HomeTagAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class GroupMiInviteDetailActivity extends BaseAppActivity implements IView {

    public static void start(Context context, int groupId) {
        start(context, groupId, -1);
    }

    public static void start(Context context, int groupId, int msgId) {
        Intent intent = new Intent(context, GroupMiInviteDetailActivity.class);
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


    private ApiHelper mApiHelper = new ApiHelper();
    private IRepositoryManager mRepositoryManager;
    private TeamInfo mTeamInfo;
    private int mGroupId;
    private int mMsgId;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_mi_invite_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mGroupId = getIntent().getIntExtra("groupid", 2);
        mMsgId = getIntent().getIntExtra("msgid", -1);

        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();

        scToolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                finish();
            } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                // 举报
                DialogSure.showDialog(GroupMiInviteDetailActivity.this,
                        "", new DialogSure.OnSureCallback() {
                            @Override
                            public void onOkClick() {

                            }
                        });
            }
        });

        if (null != scToolbar.getTvRight()) {
            scToolbar.getTvRight().setVisibility(View.GONE);
        }

        getTeamInfo(mGroupId);
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
        mBottomLayout.setVisibility(View.VISIBLE);
        mRefuseTV.setVisibility(View.VISIBLE);
        mAgreeTV.setVisibility(View.VISIBLE);
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

}
