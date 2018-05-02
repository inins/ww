package com.wang.social.im.mvp.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.im.R;
import com.wang.social.im.R2;
//import com.wang.social.im.di.component.DaggerSocialHomeComponent;
//import com.wang.social.im.di.modules.SocialHomeModule;
import com.wang.social.im.mvp.contract.SocialHomeContract;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.SocialInfo;
import com.wang.social.im.mvp.presenter.SocialHomePresenter;
//import com.wang.social.im.mvp.presenter.SocialHomePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
//
///**
// * 趣聊主页
// * <p>
// * Create by ChenJing on 4.28
// */
public class SocialHomeActivity extends BaseAppActivity<SocialHomePresenter> implements SocialHomeContract.View {
//
////    @BindView(R2.id.sc_toolbar)
////    SocialToolbar scToolbar;
////    @BindView(R2.id.sc_tv_title)
////    TextView scTvTitle;
////    @BindView(R2.id.sc_iv_qrcode)
////    ImageView scIvQrcode;
////    @BindView(R2.id.sc_iv_cover)
////    ImageView scIvCover;
////    @BindView(R2.id.sc_iv_pay_logo)
////    ImageView scIvPayLogo;
////    @BindView(R2.id.sc_tv_tag)
////    TextView scTvTag;
////    @BindView(R2.id.sc_rlv_tags)
////    RecyclerView scRlvTags;
////    @BindView(R2.id.sc_tv_social_intro)
////    TextView scTvSocialIntro;
////    @BindView(R2.id.sc_iv_intro_edit)
////    ImageView scIvIntroEdit;
////    @BindView(R2.id.sc_tv_intro)
////    TextView scTvIntro;
////    @BindView(R2.id.sc_tv_teams)
////    TextView scTvTeams;
////    @BindView(R2.id.sc_tv_team_size)
////    TextView scTvTeamSize;
////    @BindView(R2.id.sc_rlv_teams)
////    RecyclerView scRlvTeams;
////    @BindView(R2.id.sc_tv_member)
////    TextView scTvMember;
////    @BindView(R2.id.sc_tv_member_size)
////    TextView scTvMemberSize;
////    @BindView(R2.id.sc_rlv_members)
////    RecyclerView scRlvMembers;
////    @BindView(R2.id.sc_tv_member_dynamic)
////    TextView scTvMemberDynamic;
////    @BindView(R2.id.sc_tv_name_card)
////    TextView scTvNameCard;
////    @BindView(R2.id.sc_iv_portrait)
////    ImageView scIvPortrait;
////    @BindView(R2.id.sc_ll_portrait)
////    LinearLayout scLlPortrait;
////    @BindView(R2.id.sc_line_portrait)
////    View scLinePortrait;
////    @BindView(R2.id.sc_tv_nickname)
////    TextView scTvNickname;
////    @BindView(R2.id.sc_ll_nickname)
////    LinearLayout scLlNickname;
////    @BindView(R2.id.sc_line_name_card)
////    View scLineNameCard;
////    @BindView(R2.id.sc_tv_shadow)
////    TextView scTvShadow;
////    @BindView(R2.id.sc_tv_shadow_portrait)
////    ImageView scTvShadowPortrait;
////    @BindView(R2.id.sc_tv_shadow_nickname)
////    TextView scTvShadowNickname;
////    @BindView(R2.id.sc_ll_shadow)
////    LinearLayout scLlShadow;
////    @BindView(R2.id.sc_line_shadow)
////    View scLineShadow;
////    @BindView(R2.id.sc_tv_setting)
////    TextView scTvSetting;
////    @BindView(R2.id.sc_tv_social_name)
////    TextView scTvSocialName;
////    @BindView(R2.id.sc_ll_social_name)
////    LinearLayout scLlSocialName;
////    @BindView(R2.id.sc_line_social_name)
////    View scLineSocialName;
////    @BindView(R2.id.sc_tv_notify_type)
////    TextView scTvNotifyType;
////    @BindView(R2.id.sc_ll_notify_type)
////    LinearLayout scLlNotifyType;
////    @BindView(R2.id.sc_line_notify_type)
////    View scLineNotifyType;
////    @BindView(R2.id.sc_tv_background_chat)
////    TextView scTvBackgroundChat;
////    @BindView(R2.id.sc_line_background_chat)
////    View scLineBackgroundChat;
////    @BindView(R2.id.sc_tv_clear_message)
////    TextView scTvClearMessage;
////    @BindView(R2.id.sc_line_clear_message)
////    View scLineClearMessage;
////    @BindView(R2.id.sc_tv_charge_setting)
////    TextView scTvChargeSetting;
////    @BindView(R2.id.sc_line_charge_setting)
////    View scLineChargeSetting;
////    @BindView(R2.id.sc_sb_public)
////    SwitchButton scSbPublic;
////    @BindView(R2.id.sc_ll_public)
////    LinearLayout scLlPublic;
////    @BindView(R2.id.sc_line_public)
////    View scLinePublic;
////    @BindView(R2.id.sc_tv_join_limit)
////    TextView scTvJoinLimit;
////    @BindView(R2.id.sc_line_join_limit)
////    View scLineJoinLimit;
////    @BindView(R2.id.sc_sb_create_team)
////    SwitchButton scSbCreateTeam;
////    @BindView(R2.id.sc_ll_create_team)
////    LinearLayout scLlCreateTeam;
////    @BindView(R2.id.sc_line_create_team)
////    View scLineCreateTeam;
////    @BindView(R2.id.sc_tvb_handle)
////    TextView scTvbHandle;
////    @BindView(R2.id.sc_cl_content)
////    ConstraintLayout scClContent;
//
//    @Autowired
//    private String socialId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
////        DaggerSocialHomeComponent
////                .builder()
////                .appComponent(appComponent)
////                .socialHomeModule(new SocialHomeModule(this))
////                .build()
////                .inject(this);
    }
//
    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_social_home;
    }
//
    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getSocialInfo("");
    }
//
//    @Override
//    public void showLoadingDialog() {
//        super.showLoadingDialog();
//        dialogLoading.get().setOnCancelListener(this);
//    }
//
    @Override
    public void showLoading() {
        showLoadingDialog();
    }
//
    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
//
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        finish();
//    }
//
////    @OnClick({R2.id.sc_iv_qrcode, R2.id.sc_iv_intro_edit, R2.id.sc_tv_member_dynamic, R2.id.sc_ll_portrait, R2.id.sc_ll_nickname, R2.id.sc_ll_shadow, R2.id.sc_ll_social_name, R2.id.sc_ll_notify_type, R2.id.sc_tv_background_chat, R2.id.sc_tv_clear_message, R2.id.sc_tv_charge_setting, R2.id.sc_tv_join_limit, R2.id.sc_tvb_handle})
////    public void onViewClicked(View view) {
////        if (view.getId() == R.id.sc_iv_qrcode) {//二维码
////
////        } else if (view.getId() == R.id.sc_iv_intro_edit) { //简介编辑
////
////        } else if (view.getId() == R.id.sc_tv_member_dynamic) {//成员动态
////
////        } else if (view.getId() == R.id.sc_ll_portrait) {//我的群头像
////
////        } else if (view.getId() == R.id.sc_ll_nickname) {//我的群昵称
////
////        } else if (view.getId() == R.id.sc_ll_shadow) {//分身
////
////        } else if (view.getId() == R.id.sc_ll_social_name) {//趣聊名称
////
////        } else if (view.getId() == R.id.sc_ll_notify_type) {//消息通知
////
////        } else if (view.getId() == R.id.sc_tv_background_chat) {//聊天背景
////
////        } else if (view.getId() == R.id.sc_tv_clear_message) {//清空消息
////
////        } else if (view.getId() == R.id.sc_tv_charge_setting) {//收费设置
////
////        } else if (view.getId() == R.id.sc_tv_join_limit) {//加入限制
////
////        } else if (view.getId() == R.id.sc_tvb_handle) {//退出/解散
////
////        }
////    }
//
    @Override
    public void showSocialInfo(SocialInfo socialInfo) {

    }
//
////    @Override
////    public void showMembers(List<MemberInfo> members) {
////
////    }
}