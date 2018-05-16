package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.component.app.Constant;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetGroupHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.ui.dialog.PayDialog;
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
import com.frame.component.entities.AddGroupApplyRsp;
import com.frame.component.entities.AddGroupRsp;
import com.wang.social.im.mvp.model.entities.DistributionAge;
import com.wang.social.im.mvp.model.entities.DistributionGroup;
import com.wang.social.im.mvp.model.entities.DistributionSex;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.frame.component.entities.dto.AddGroupApplyRspDTO;
import com.frame.component.entities.dto.AddGroupRspDTO;
import com.wang.social.im.mvp.model.entities.dto.DistributionGroupDTO;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;
import com.wang.social.im.mvp.ui.adapters.DistributionAgeAdapter;
import com.wang.social.im.mvp.ui.adapters.DistributionSexAdapter;
import com.wang.social.im.mvp.ui.adapters.HomeTagAdapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

public class GroupInviteDetailActivity extends BaseAppActivity implements IView {

    public final static int TYPE_BROWSE = 0;
    public final static int TYPE_INVITE = 1;

    @IntDef({
            TYPE_BROWSE,
            TYPE_INVITE
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface GroupInviteType {
    }

    /**
     * 邀请入群
     * 底部显示 拒绝 同意
     */
    public static void startForInvite(Context context, int groupId, int msgId) {
        start(context, TYPE_INVITE, groupId, msgId);
    }

    /**
     * 浏览信息
     * 底部显示 立即加入
     */
    public static void startForBrowse(Context context, int groupId) {
        start(context, TYPE_BROWSE, groupId, -1);
    }

    private static void start(Context context, int type, int groupId, int msgId) {
        Intent intent = new Intent(context, GroupInviteDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("groupid", groupId);
        intent.putExtra("msgid", msgId);
        context.startActivity(intent);
    }

    @BindView(R2.id.sc_toolbar)
    SocialToolbar scToolbar;
    @BindView(R2.id.sc_tv_title)
    TextView scTvTitle;
    @BindView(R2.id.sc_iv_cover)
    ImageView scIvCover;
    @BindView(R2.id.sc_iv_pay_logo)
    ImageView scIvPayLogo;
    @BindView(R2.id.sc_tv_tag)
    TextView scTvTag;
    @BindView(R2.id.sc_rlv_tags)
    RecyclerView scRlvTags;
    @BindView(R2.id.sc_tv_social_intro)
    TextView scTvSocialIntro;
    @BindView(R2.id.sc_iv_intro_edit)
    ImageView scIvIntroEdit;
    @BindView(R2.id.sc_tv_intro)
    TextView scTvIntro;
    @BindView(R2.id.content_layout)
    ConstraintLayout mContentLayout;
    @BindView(R2.id.bottom_layout)
    View mBottomLayout;
    @BindView(R2.id.refuse_text_view)
    TextView mRefuseTV;
    @BindView(R2.id.agree_text_view)
    TextView mAgreeTV;
    @BindView(R2.id.apply_layout)
    View mApplyLayout;
    @BindView(R2.id.apply_hint_text_view)
    TextView mApplyHintTV;
    // 年龄分布
    @BindView(R2.id.age_recycler_view)
    RecyclerView mAgeRV;
    DistributionAgeAdapter mAgeAdapter;
    // 性别分布
    @BindView(R2.id.sex_recycler_view)
    RecyclerView mSexRV;
    DistributionSexAdapter mSexAdapter;

    private ApiHelper mApiHelper = new ApiHelper();
    private IRepositoryManager mRepositoryManager;
    private SocialInfo mSocial;
    private @GroupInviteType
    int mType;
    private int mGroupId;
    private int mMsgId;

    // 年龄分布
    private List<DistributionAge> mAgeList = new ArrayList<>();
    // 性别分布
    private List<DistributionSex> mSexList = new ArrayList<>();

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_invite_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mType = getIntent().getIntExtra("type", TYPE_BROWSE);
        mGroupId = getIntent().getIntExtra("groupid", -1);
        mMsgId = getIntent().getIntExtra("msgid", -1);

        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(Utils.getContext()).repoitoryManager();

        scToolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                finish();
            } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                // 举报
                DialogSure.showDialog(GroupInviteDetailActivity.this,
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

        initDistributionAge();
        initDistributionSex();

        loadSocialInfo(mGroupId);
    }

    /**
     * 年龄分布UI
     */
    private void initDistributionAge() {
        mAgeAdapter = new DistributionAgeAdapter(this, mAgeList);
        mAgeRV.setLayoutManager(new GridLayoutManager(this, 4));
        mAgeRV.setAdapter(mAgeAdapter);
    }

    /**
     * 性别分布UI
     */
    private void initDistributionSex() {
        mSexAdapter = new DistributionSexAdapter(this, mSexList);
        mSexRV.setLayoutManager(new GridLayoutManager(this, 4));
        mSexRV.setAdapter(mSexAdapter);
    }

    /**
     * 加载信息成功后重置ui显示
     */
    public void showSocialInfo(SocialInfo socialInfo) {
        mSocial = socialInfo;

        mContentLayout.setVisibility(View.VISIBLE);

        if (null != scToolbar.getTvRight()) {
            scToolbar.getTvRight().setVisibility(View.VISIBLE);
        }

        scTvTitle.setText(socialInfo.getName());

        ImageLoaderHelper.getImageLoader()
                .loadImage(this, ImageConfigImpl.builder()
                        .placeholder(R.drawable.im_round_image_placeholder)
                        .errorPic(R.drawable.im_round_image_placeholder)
                        .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                        .imageView(scIvCover)
                        .url(socialInfo.getCover())
                        .build());

        if (socialInfo.getAttr().isCharge()) {
            scIvPayLogo.setVisibility(View.VISIBLE);
        }

        showTags(socialInfo.getTags());

        scTvIntro.setText(socialInfo.getDesc());

        mBottomLayout.setVisibility(View.VISIBLE);
        if (mType == TYPE_INVITE) {
            mRefuseTV.setVisibility(View.VISIBLE);
            mAgreeTV.setVisibility(View.VISIBLE);
            mApplyLayout.setVisibility(View.GONE);
        } else {
            // 显示申请加入
            mApplyLayout.setVisibility(View.VISIBLE);
            mRefuseTV.setVisibility(View.GONE);
            mAgreeTV.setVisibility(View.GONE);
            if (socialInfo.getAttr().getGem() > 0) {
                mApplyHintTV.setText(
                        String.format(getString(R.string.im_personal_card_join_hint),
                                socialInfo.getAttr().getGem()));
            } else {
                mApplyHintTV.setVisibility(View.GONE);
            }
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
     * 获取趣聊信息
     *
     * @param socialId 群id
     */
    public void loadSocialInfo(int socialId) {
        mApiHelper.execute(this,
                getSocialInfo(Integer.toString(socialId)),
                new ErrorHandleSubscriber<SocialInfo>() {
                    @Override
                    public void onNext(SocialInfo socialInfo) {
                        showSocialInfo(socialInfo);

                        loadDistribution(socialId);
                    }
                }, disposable -> showLoading()
                , () -> hideLoading());
    }

    /**
     * 加载群信息
     */
    private Observable<BaseJson<SocialDTO>> getSocialInfo(String socialId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getSocialInfo("2.0.0", socialId);
    }

    /**
     * 获取年龄和性别分布
     *
     * @param groupId 群id
     */
    public void loadDistribution(int groupId) {
        mApiHelper.execute(this,
                getDistribution(Integer.toString(groupId)),
                new ErrorHandleSubscriber<DistributionGroup>() {
                    @Override
                    public void onNext(DistributionGroup group) {
                        if (null != group.getAge()) {
                            mAgeList.addAll(group.getAge());
                            if (null != mAgeAdapter) {
                                mAgeAdapter.resetCount(group.getAge());
                                mAgeAdapter.notifyDataSetChanged();
                            }
                        }
                        if (null != group.getSex()) {
                            if (null != group.getSex()) {
                                mSexList.addAll(group.getSex());

                                if (null != mSexAdapter) {
                                    mSexAdapter.resetCount(group.getSex());
                                    mSexAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                });
    }

    private Observable<BaseJson<DistributionGroupDTO>> getDistribution(String groupId) {
        return mRepositoryManager
                .obtainRetrofitService(GroupService.class)
                .getDistribution("2.0.0", groupId);
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
                        // 重新加载统计信息
                        loadDistribution(mGroupId);
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

    @OnClick(R2.id.apply_text_view)
    public void apply() {
        NetGroupHelper.newInstance().addGroup(
                this,
                this,
                mGroupId,
                isNeedValidation -> {
                    // 隐藏底部栏
                    mBottomLayout.setVisibility(View.GONE);

                    if (!isNeedValidation) {
                        // 重新加载群统计
                        loadDistribution(mGroupId);
                    }
                });
    }
}
