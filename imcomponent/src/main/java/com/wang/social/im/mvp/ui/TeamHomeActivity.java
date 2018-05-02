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
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerTeamHomeComponent;
import com.wang.social.im.di.modules.TeamHomeModule;
import com.wang.social.im.mvp.contract.TeamHomeContract;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.presenter.TeamHomePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 觅聊详情
 */
public class TeamHomeActivity extends BaseAppActivity<TeamHomePresenter> implements TeamHomeContract.View {

    @BindView(R2.id.th_toolbar)
    SocialToolbar thToolbar;
    @BindView(R2.id.th_tv_title)
    TextView thTvTitle;
    @BindView(R2.id.th_iv_cover)
    ImageView thIvCover;
    @BindView(R2.id.th_iv_pay_logo)
    ImageView thIvPayLogo;
    @BindView(R2.id.th_tv_tag)
    TextView thTvTag;
    @BindView(R2.id.th_rlv_tags)
    RecyclerView thRlvTags;
    @BindView(R2.id.th_tv_member)
    TextView thTvMember;
    @BindView(R2.id.th_tv_member_size)
    TextView thTvMemberSize;
    @BindView(R2.id.th_rlv_members)
    RecyclerView thRlvMembers;
    @BindView(R2.id.th_tv_setting)
    TextView thTvSetting;
    @BindView(R2.id.th_tv_team_name)
    TextView thTvTeamName;
    @BindView(R2.id.th_ll_team_name)
    LinearLayout thLlTeamName;
    @BindView(R2.id.th_line_team_name)
    View thLineTeamName;
    @BindView(R2.id.th_tv_notify_type)
    TextView thTvNotifyType;
    @BindView(R2.id.th_ll_notify_type)
    LinearLayout thLlNotifyType;
    @BindView(R2.id.th_line_notify_type)
    View thLineNotifyType;
    @BindView(R2.id.th_tv_clear_message)
    TextView thTvClearMessage;
    @BindView(R2.id.th_line_clear_message)
    View thLineClearMessage;
    @BindView(R2.id.th_tv_background_chat)
    TextView thTvBackgroundChat;
    @BindView(R2.id.th_line_background_chat)
    View thLineBackgroundChat;
    @BindView(R2.id.th_tv_charge_setting)
    TextView thTvChargeSetting;
    @BindView(R2.id.th_line_charge_setting)
    View thLineChargeSetting;
    @BindView(R2.id.th_sb_apply)
    SwitchButton thSbApply;
    @BindView(R2.id.th_ll_apply)
    LinearLayout thLlApply;
    @BindView(R2.id.th_line_apply)
    View thLineApply;
    @BindView(R2.id.th_tvb_handle)
    TextView thTvbHandle;
    @BindView(R2.id.th_cl_content)
    ConstraintLayout thClContent;

    @Autowired
    String teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTeamHomeComponent
                .builder()
                .appComponent(appComponent)
                .teamHomeModule(new TeamHomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_team_home;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getTeamInfo(teamId);
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

    @Override
    public void showTeamInfo(TeamInfo teamInfo) {

    }

    @Override
    public void showMembers(List<MemberInfo> members) {

    }

    @OnClick({R2.id.th_ll_team_name, R2.id.th_ll_notify_type, R2.id.th_tv_clear_message, R2.id.th_tv_background_chat, R2.id.th_tv_charge_setting, R2.id.th_tvb_handle})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.th_ll_team_name){ //觅聊名称

        }else if (view.getId() == R.id.th_ll_notify_type){ //通知类型

        }else if (view.getId() == R.id.th_tv_clear_message){//清除聊天内容

        }else if (view.getId() == R.id.th_tv_background_chat){//背景图片

        }else if (view.getId() == R.id.th_tv_charge_setting){//收费设置

        }else if (view.getId() == R.id.th_tvb_handle){//退出/解散觅聊

        }
    }
}
