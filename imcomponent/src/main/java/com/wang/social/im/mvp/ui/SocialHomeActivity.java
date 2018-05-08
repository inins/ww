package com.wang.social.im.mvp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.EditDialog;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerSocialHomeComponent;
import com.wang.social.im.di.modules.SocialHomeModule;
import com.wang.social.im.enums.MessageNotifyType;
import com.wang.social.im.mvp.contract.SocialHomeContract;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.ShadowInfo;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.presenter.SocialHomePresenter;
import com.wang.social.im.mvp.ui.adapters.HomeMemberAdapter;
import com.wang.social.im.mvp.ui.adapters.HomeTagAdapter;
import com.wang.social.im.mvp.ui.adapters.SocialHomeTeamsAdapter;
import com.wang.social.pictureselector.helper.PhotoHelperEx;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 趣聊主页
 * <p>
 * Create by ChenJing on 4.28
 */
public class SocialHomeActivity extends BaseAppActivity<SocialHomePresenter> implements SocialHomeContract.View {

    private static final String EXTRA_SOCIAL_ID = "social_id";

    @BindView(R2.id.sc_toolbar)
    SocialToolbar scToolbar;
    @BindView(R2.id.sc_tv_title)
    TextView scTvTitle;
    @BindView(R2.id.sc_iv_qrcode)
    ImageView scIvQrcode;
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
    @BindView(R2.id.sc_tv_teams)
    TextView scTvTeams;
    @BindView(R2.id.sc_tv_team_size)
    TextView scTvTeamSize;
    @BindView(R2.id.sc_rlv_teams)
    RecyclerView scRlvTeams;
    @BindView(R2.id.sc_tv_member)
    TextView scTvMember;
    @BindView(R2.id.sc_tv_member_size)
    TextView scTvMemberSize;
    @BindView(R2.id.sc_rlv_members)
    RecyclerView scRlvMembers;
    @BindView(R2.id.sc_tv_member_dynamic)
    TextView scTvMemberDynamic;
    @BindView(R2.id.sc_tv_name_card)
    TextView scTvNameCard;
    @BindView(R2.id.sc_iv_portrait)
    ImageView scIvPortrait;
    @BindView(R2.id.sc_ll_portrait)
    LinearLayout scLlPortrait;
    @BindView(R2.id.sc_line_portrait)
    View scLinePortrait;
    @BindView(R2.id.sc_tv_nickname)
    TextView scTvNickname;
    @BindView(R2.id.sc_ll_nickname)
    LinearLayout scLlNickname;
    @BindView(R2.id.sc_line_name_card)
    View scLineNameCard;
    @BindView(R2.id.sc_tv_shadow)
    TextView scTvShadow;
    @BindView(R2.id.sc_tv_shadow_portrait)
    ImageView scTvShadowPortrait;
    @BindView(R2.id.sc_tv_shadow_nickname)
    TextView scTvShadowNickname;
    @BindView(R2.id.sc_ll_shadow)
    LinearLayout scLlShadow;
    @BindView(R2.id.sc_line_shadow)
    View scLineShadow;
    @BindView(R2.id.sc_tv_setting)
    TextView scTvSetting;
    @BindView(R2.id.sc_tv_social_name)
    TextView scTvSocialName;
    @BindView(R2.id.sc_ll_social_name)
    LinearLayout scLlSocialName;
    @BindView(R2.id.sc_line_social_name)
    View scLineSocialName;
    @BindView(R2.id.sc_sb_notify_type)
    SwitchButton scSbNotifyType;
    @BindView(R2.id.sc_ll_notify_type)
    LinearLayout scLlNotifyType;
    @BindView(R2.id.sc_line_notify_type)
    View scLineNotifyType;
    @BindView(R2.id.sc_tv_background_chat)
    TextView scTvBackgroundChat;
    @BindView(R2.id.sc_line_background_chat)
    View scLineBackgroundChat;
    @BindView(R2.id.sc_tv_clear_message)
    TextView scTvClearMessage;
    @BindView(R2.id.sc_line_clear_message)
    View scLineClearMessage;
    @BindView(R2.id.sc_tv_charge_setting)
    TextView scTvChargeSetting;
    @BindView(R2.id.sc_line_charge_setting)
    View scLineChargeSetting;
    @BindView(R2.id.sc_sb_public)
    SwitchButton scSbPublic;
    @BindView(R2.id.sc_ll_public)
    LinearLayout scLlPublic;
    @BindView(R2.id.sc_line_public)
    View scLinePublic;
    @BindView(R2.id.sc_tv_join_limit)
    TextView scTvJoinLimit;
    @BindView(R2.id.sc_line_join_limit)
    View scLineJoinLimit;
    @BindView(R2.id.sc_sb_create_team)
    SwitchButton scSbCreateTeam;
    @BindView(R2.id.sc_ll_create_team)
    LinearLayout scLlCreateTeam;
    @BindView(R2.id.sc_line_create_team)
    View scLineCreateTeam;
    @BindView(R2.id.sc_tvb_handle)
    TextView scTvbHandle;
    @BindView(R2.id.sc_cl_content)
    ConstraintLayout scClContent;

    @Autowired
    String socialId = "26";
    @Inject
    ImageLoader mImageLoader;
    PhotoHelperEx mPhotoHelper;

    private SocialInfo mSocial;

    public static void start(Context context, String socialId) {
        Intent intent = new Intent(context, SocialHomeActivity.class);
        intent.putExtra(EXTRA_SOCIAL_ID, socialId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSocialHomeComponent
                .builder()
                .appComponent(appComponent)
                .socialHomeModule(new SocialHomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_social_home;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getSocialInfo(socialId);
    }

    private void init() {
        scSbCreateTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSocial.setCreateTeam(isChecked);
                mPresenter.updateSocialInfo(mSocial);
            }
        });
        scSbNotifyType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSocial.getMemberInfo().setNotifyType(isChecked ? MessageNotifyType.ALL : MessageNotifyType.NONE);
                mPresenter.updateNameCard(socialId, mSocial.getMemberInfo().getNickname(),
                        mSocial.getMemberInfo().getPortrait(), mSocial.getMemberInfo().getNotifyType());
            }
        });
        scSbPublic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSocial.getAttr().setOpen(isChecked);
                mPresenter.updateSocialInfo(mSocial);
            }
        });
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
        dialogLoading.get().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @OnClick({R2.id.sc_iv_qrcode, R2.id.sc_iv_intro_edit, R2.id.sc_tv_member_dynamic, R2.id.sc_ll_portrait, R2.id.sc_ll_nickname, R2.id.sc_ll_shadow, R2.id.sc_ll_social_name, R2.id.sc_tv_background_chat, R2.id.sc_tv_clear_message, R2.id.sc_tv_charge_setting, R2.id.sc_tv_join_limit, R2.id.sc_tvb_handle})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.sc_iv_qrcode) {//二维码

        } else if (view.getId() == R.id.sc_iv_intro_edit) { //简介编辑
            EditDialog editDialog = new EditDialog(this, mSocial.getDesc(), UIUtil.getString(R.string.im_group_desc_setting), 15, new EditDialog.OnInputCompleteListener() {
                @Override
                public void onComplete(Dialog dialog, String content) {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_group_desc));
                        return;
                    }
                    dialog.dismiss();
                    mSocial.setDesc(content);
                    scTvIntro.setText(content);
                    mPresenter.updateSocialInfo(mSocial);
                }
            });
            editDialog.show();
        } else if (view.getId() == R.id.sc_tv_member_dynamic) {//成员动态

        } else if (view.getId() == R.id.sc_ll_portrait) {//我的群头像
            mPhotoHelper = PhotoHelperEx.newInstance(this, new PortraitCallBack());
            mPhotoHelper.showDefaultDialog();
        } else if (view.getId() == R.id.sc_ll_nickname) {//我的群昵称
            String oldNickname = mSocial.getMemberInfo() == null ? "" : mSocial.getMemberInfo().getNickname();
            EditDialog editDialog = new EditDialog(this, oldNickname, UIUtil.getString(R.string.im_self_group_name), 8, new EditDialog.OnInputCompleteListener() {
                @Override
                public void onComplete(Dialog dialog, String content) {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_nickname));
                        return;
                    }
                    dialog.dismiss();
                    mSocial.getMemberInfo().setNickname(content);
                    scTvNickname.setText(content);
                    mPresenter.updateNameCard(socialId, content, mSocial.getMemberInfo().getPortrait(), mSocial.getMemberInfo().getNotifyType());
                }
            });
            editDialog.show();
        } else if (view.getId() == R.id.sc_ll_shadow) {//分身
            ShadowSettingActivity.start(this, mSocial.getShadowInfo());
        } else if (view.getId() == R.id.sc_ll_social_name) {//趣聊名称
            EditDialog editDialog = new EditDialog(this, mSocial.getDesc(), UIUtil.getString(R.string.im_group_name_setting), 8, new EditDialog.OnInputCompleteListener() {
                @Override
                public void onComplete(Dialog dialog, String content) {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_social_name));
                        return;
                    }
                    dialog.dismiss();
                    mSocial.setName(content);
                    scTvTitle.setText(content);
                    scTvSocialName.setText(content);
                    mPresenter.updateSocialInfo(mSocial);
                }
            });
            editDialog.show();
        } else if (view.getId() == R.id.sc_tv_background_chat) {//聊天背景

        } else if (view.getId() == R.id.sc_tv_clear_message) {//清空消息
            mPresenter.clearMessages(socialId);
        } else if (view.getId() == R.id.sc_tv_charge_setting) {//收费设置

        } else if (view.getId() == R.id.sc_tv_join_limit) {//加入限制

        } else if (view.getId() == R.id.sc_tvb_handle) {//退出/解散
            if (mSocial.getMemberInfo() != null && mSocial.getMemberInfo().getRole() == MemberInfo.ROLE_MASTER) {
                mPresenter.dissolveGroup(socialId);
            } else {
                mPresenter.exitGroup(socialId);
            }
        }
    }

    @Override
    public void showSocialInfo(SocialInfo socialInfo) {
        mSocial = socialInfo;
        scClContent.setVisibility(View.VISIBLE);

        scTvTitle.setText(socialInfo.getName());

        mImageLoader.loadImage(this, ImageConfigImpl.builder()
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

        //我的名片
        MemberInfo selfInfo = socialInfo.getMemberInfo();
        if (selfInfo != null) {
            mImageLoader.loadImage(this, ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .imageView(scIvPortrait)
                    .url(selfInfo.getPortrait())
                    .isCircle(true)
                    .build());
            scTvNickname.setText(selfInfo.getNickname());

            //消息提醒
            scSbNotifyType.setChecked(selfInfo.getNotifyType() == MessageNotifyType.ALL);

            //设置按钮
            switch (selfInfo.getRole()) {
                case MemberInfo.ROLE_MASTER:
                    scTvbHandle.setText(R.string.im_dissolve_social);
                    scIvIntroEdit.setVisibility(View.VISIBLE);
                    scLlSocialName.setVisibility(View.VISIBLE);
                    scLineSocialName.setVisibility(View.VISIBLE);
                    scTvChargeSetting.setVisibility(View.VISIBLE);
                    scLineChargeSetting.setVisibility(View.VISIBLE);
                    scLlPublic.setVisibility(View.VISIBLE);
                    scLinePublic.setVisibility(View.VISIBLE);
                    scTvJoinLimit.setVisibility(View.VISIBLE);
                    scLineJoinLimit.setVisibility(View.VISIBLE);
                    scLlCreateTeam.setVisibility(View.VISIBLE);
                    scLineCreateTeam.setVisibility(View.VISIBLE);
                    break;
                default:
                    scTvbHandle.setText(R.string.im_exit_social);
                    break;
            }
        }
        //分身信息
        ShadowInfo shadowInfo = socialInfo.getShadowInfo();
        if (shadowInfo != null) {
            mImageLoader.loadImage(this, ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .imageView(scTvShadowPortrait)
                    .url(selfInfo.getPortrait())
                    .isCircle(true)
                    .build());
            scTvShadowNickname.setText(selfInfo.getNickname());
        }

        scTvSocialName.setText(socialInfo.getName());

        scSbPublic.setChecked(socialInfo.getAttr().isOpen());
        scSbCreateTeam.setChecked(socialInfo.isCreateTeam());
    }

    @Override
    public void showTeams(List<TeamInfo> teams) {
        scTvTeamSize.setText(UIUtil.getString(R.string.im_team_size_format, teams.size()));

        int space = (ScreenUtils.getScreenWidth() - UIUtil.getDimen(R.dimen.common_border_margin) * 2 - SizeUtils.dp2px(58) * 5) / 4;
        scRlvTeams.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = space;
            }
        });
        scRlvTeams.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        scRlvTeams.setAdapter(new SocialHomeTeamsAdapter(teams));
    }

    @Override
    public void showMembers(List<MemberInfo> members) {
        scTvMemberSize.setText(UIUtil.getString(R.string.im_member_size_format, members.size()));
        if (members.size() > 5) {
            members = members.subList(0, 5);
        }
        members.add(new MemberInfo());

        int space = (ScreenUtils.getScreenWidth() - UIUtil.getDimen(R.dimen.common_border_margin) * 2 - UIUtil.getDimen(R.dimen.im_group_home_member_size) * 6) / 5;
        scRlvMembers.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = space;
            }
        });
        scRlvMembers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        scRlvMembers.setAdapter(new HomeMemberAdapter(members));
    }

    @Override
    public void onExitOrDissolve() {
        finish();
    }

    @Override
    public void onPortraitUploaded(String url) {
        mSocial.getMemberInfo().setPortrait(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPhotoHelper != null) {
            mPhotoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

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
     * 我的群头像选择回调
     */
    private class PortraitCallBack implements PhotoHelperEx.OnPhotoCallback {

        @Override
        public void onResult(String path) {
            mPresenter.updateNameCard(socialId, mSocial.getMemberInfo().getNickname(), path, mSocial.getMemberInfo().getNotifyType());
        }
    }
}