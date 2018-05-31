package com.wang.social.im.mvp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.component.common.AppConstant;
import com.frame.component.entities.AutoPopupItemModel;
import com.frame.component.enums.ConversationType;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.AutoPopupWindow;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.ui.dialog.EditDialog;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.integration.AppManager;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.FileUtils;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerTeamHomeComponent;
import com.wang.social.im.di.modules.TeamHomeModule;
import com.wang.social.im.enums.MessageNotifyType;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.helper.ImageSelectHelper;
import com.wang.social.im.mvp.contract.TeamHomeContract;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.presenter.TeamHomePresenter;
import com.wang.social.im.mvp.ui.adapters.HomeMemberAdapter;
import com.wang.social.im.mvp.ui.adapters.HomeTagAdapter;
import com.wang.social.im.widget.ImageSelectDialog;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.socialize.SocializeUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.frame.entities.EventBean.EVENT_NOTIFY_BACKGROUND;

/**
 * 觅聊详情
 */
public class TeamHomeActivity extends BaseAppActivity<TeamHomePresenter> implements TeamHomeContract.View, PhotoHelper.OnPhotoCallback {

    private static final int PHOTO_TYPE_COVER = 1;
    private static final int PHOTO_TYPE_BACKGROUND = 2;

    private static final int REQUEST_CODE_CHARGE = 1000;
    private static final int REQUEST_CODE_OFFICIAL_PHOTO = 1001;

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
    @BindView(R2.id.th_sb_notify)
    SwitchButton thSbNotify;
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
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    private TeamInfo mTeam;
    private MemberInfo mSelfProfile;

    private ImageSelectHelper mImageSelectHelper;
    private int mPhotoType;

    //记录是否是通过用户点击修改状态
    private boolean applyFromCode = false;
    private boolean notifyFromCode = false;

    public static void start(Context context, String teamId) {
        Intent intent = new Intent(context, TeamHomeActivity.class);
        intent.putExtra("teamId", teamId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
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
        mPresenter.getSelfProfile(teamId);
        mPresenter.getMemberList(teamId);
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
        thClContent.setVisibility(View.VISIBLE);

        mTeam = teamInfo;
        if (mSelfProfile != null) {
            mTeam.setSelfProfile(mSelfProfile);
        }

        thTvTitle.setText(teamInfo.getName());
        thTvTeamName.setText(teamInfo.getName());
        mImageLoader.loadImage(this, ImageConfigImpl.builder()
                .placeholder(R.drawable.im_round_image_placeholder)
                .errorPic(R.drawable.im_round_image_placeholder)
                .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .imageView(thIvCover)
                .url(teamInfo.getCover())
                .build());
        if (!teamInfo.isFree()) {
            thIvPayLogo.setVisibility(View.VISIBLE);
        }
        showTags(teamInfo.getTags());
        if (teamInfo.isValidation()) {
            applyFromCode = true;
        }
        thSbApply.setChecked(teamInfo.isValidation());
    }

    @Override
    public void showProfile(MemberInfo memberInfo) {
        if (mTeam != null) {
            mTeam.setSelfProfile(memberInfo);
        } else {
            mSelfProfile = memberInfo;
        }
        if (memberInfo.getNotifyType() == MessageNotifyType.ALL) {
            notifyFromCode = true;
        }
        thSbNotify.setChecked(memberInfo.getNotifyType() == MessageNotifyType.ALL);
        if (memberInfo.getRole() == MemberInfo.ROLE_MASTER) {
            thTvbHandle.setText(R.string.im_dissolve_team);

            thLlTeamName.setVisibility(View.VISIBLE);
            thLineTeamName.setVisibility(View.VISIBLE);
            thTvChargeSetting.setVisibility(View.VISIBLE);
            thLineChargeSetting.setVisibility(View.VISIBLE);
            thLlApply.setVisibility(View.VISIBLE);
            thLineApply.setVisibility(View.VISIBLE);
        } else {
            thTvbHandle.setText(R.string.im_exit_team);
        }
    }

    private void init() {
        thSbNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!notifyFromCode) {
                    MemberInfo selfProfile = mTeam.getSelfProfile();
                    if (selfProfile != null) {
                        mPresenter.updateNameCard(mTeam.getTeamId(), ConversationType.TEAM, selfProfile.getNickname(), selfProfile.getPortrait(), isChecked ? MessageNotifyType.ALL : MessageNotifyType.NONE);
                    }
                }
                notifyFromCode = false;
            }
        });

        thSbApply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!applyFromCode) {
                    mTeam.setValidation(isChecked);
                    mPresenter.updateTeamInfo(mTeam);
                }
                applyFromCode = false;
            }
        });

        thToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                switch (clickType) {
                    case LEFT_ICON:
                        onBackPressed();
                        break;
                }
            }
        });

        mImageSelectHelper = ImageSelectHelper.newInstance(this, this, new ImageSelectDialog.OnItemSelectedListener() {
            @Override
            public void onGallerySelected() {
                switch (mPhotoType) {
                    case PHOTO_TYPE_COVER:
                        CommonHelper.PersonalHelper.startOfficialPhotoActivity(TeamHomeActivity.this, REQUEST_CODE_OFFICIAL_PHOTO);
                        break;
                    case PHOTO_TYPE_BACKGROUND:
                        BackgroundSettingActivity.start(TeamHomeActivity.this, ConversationType.TEAM, ImHelper.wangId2ImId(teamId, ConversationType.TEAM));
                        break;
                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onShootSelected() {
                new RxPermissions(TeamHomeActivity.this)
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    mImageSelectHelper.startCamera();
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    ToastUtil.showToastShort("请在设置中打开相机权限");
                                }
                            }
                        });
            }

            @Override
            public void onAlbumSelected() {
                mImageSelectHelper.startPhoto();
            }
        });
    }

    @Override
    public void showMembers(List<MemberInfo> members) {
        thTvMemberSize.setText(UIUtil.getString(R.string.im_member_size_format, members.size()));
        if (members.size() > 5) {
            members = members.subList(0, 5);
        }
        members.add(new MemberInfo());

        int space = (ScreenUtils.getScreenWidth() - UIUtil.getDimen(R.dimen.common_border_margin) * 2 - UIUtil.getDimen(R.dimen.im_group_home_member_size) * 6) / 5;
        thRlvMembers.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = space;
            }
        });
        thRlvMembers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        thRlvMembers.setAdapter(new HomeMemberAdapter(members, teamId, false));
    }

    @Override
    public void onExitOrDissolve() {
        mAppManager.killActivity(GroupConversationActivity.class);
        finish();
    }

    @Override
    public void onPortraitUploaded(String url) {

    }

    @OnClick({R2.id.th_ll_team_name, R2.id.th_iv_cover, R2.id.th_tv_clear_message, R2.id.th_tv_background_chat, R2.id.th_tv_charge_setting, R2.id.th_tvb_handle})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.th_ll_team_name) { //觅聊名称
            EditDialog editDialog = new EditDialog(this, mTeam.getName(), UIUtil.getString(R.string.im_group_name_setting), 8, new EditDialog.OnInputCompleteListener() {
                @Override
                public void onComplete(Dialog dialog, String content) {
                    if (TextUtils.isEmpty(content)) {
                        ToastUtil.showToastShort(UIUtil.getString(R.string.im_toast_social_name));
                        return;
                    }
                    dialog.dismiss();
                    mTeam.setName(content);
                    thTvTitle.setText(content);
                    thTvTeamName.setText(content);
                    mPresenter.updateTeamInfo(mTeam);
                }
            });
            editDialog.show();
        } else if (view.getId() == R.id.th_iv_cover) {
            mPhotoType = PHOTO_TYPE_COVER;
            mImageSelectHelper.setShowShoot(true);
            mImageSelectHelper.showDialog();
        } else if (view.getId() == R.id.th_tv_clear_message) {//清除聊天内容
            DialogSure.showDialog(this, "确认清空消息？", new DialogSure.OnSureCallback() {
                @Override
                public void onOkClick() {
                    mPresenter.clearMessages(ImHelper.wangId2ImId(teamId, ConversationType.TEAM));
                }
            });

        } else if (view.getId() == R.id.th_tv_background_chat) {//背景图片
            mPhotoType = PHOTO_TYPE_BACKGROUND;
            mImageSelectHelper.setShowShoot(false);
            mImageSelectHelper.showDialog();
        } else if (view.getId() == R.id.th_tv_charge_setting) {//收费设置
            TeamChargeSettingActivity.start(this, mTeam, REQUEST_CODE_CHARGE);
        } else if (view.getId() == R.id.th_tvb_handle) {//退出/解散觅聊
            if (mTeam.getSelfProfile() != null && mTeam.getSelfProfile().getRole() == MemberInfo.ROLE_MASTER) {
                DialogSure.showDialog(this, "确认解散此觅聊？", new DialogSure.OnSureCallback() {
                    @Override
                    public void onOkClick() {
                        mPresenter.dissolveGroup(teamId);
                    }
                });
            } else {
                DialogSure.showDialog(this, "确认退出此觅聊？", new DialogSure.OnSureCallback() {
                    @Override
                    public void onOkClick() {
                        mPresenter.exitGroup(teamId);
                    }
                });
            }
        }
    }

    private void showTags(List<Tag> tags) {
        thRlvTags.setNestedScrollingEnabled(false);
        thRlvTags.setLayoutManager(
                ChipsLayoutManager.newBuilder(this)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .build());
        thRlvTags.addItemDecoration(
                new SpacingItemDecoration(SizeUtils.dp2px(5), SizeUtils.dp2px(5)));
        thRlvTags.setAdapter(new HomeTagAdapter(tags));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mImageSelectHelper != null) {
            mImageSelectHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHARGE:
                    mTeam = data.getParcelableExtra(TeamChargeSettingActivity.EXTRA_TEAM);
                    if (mTeam.isFree()) {
                        thIvPayLogo.setVisibility(View.GONE);
                    } else {
                        thIvPayLogo.setVisibility(View.VISIBLE);
                    }
                    break;
                case REQUEST_CODE_OFFICIAL_PHOTO:
                    String coverUrl = data.getStringExtra("url");
                    mImageLoader.loadImage(this, ImageConfigImpl.builder()
                            .placeholder(R.drawable.im_round_image_placeholder)
                            .errorPic(R.drawable.im_round_image_placeholder)
                            .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                            .imageView(thIvCover)
                            .url(coverUrl)
                            .build());
                    mPresenter.updateCover(coverUrl, mTeam);
                    break;
            }
        }
    }

    @Override
    public void onResult(String path) {
        switch (mPhotoType) {
            case PHOTO_TYPE_COVER:
                mImageLoader.loadImage(this, ImageConfigImpl.builder()
                        .placeholder(R.drawable.im_round_image_placeholder)
                        .errorPic(R.drawable.im_round_image_placeholder)
                        .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                        .imageView(thIvCover)
                        .url(path)
                        .build());
                mPresenter.updateCover(path, mTeam);
                break;
            case PHOTO_TYPE_BACKGROUND:
                Observable.just(path)
                        .subscribeOn(Schedulers.io())
                        .map(new Function<String, Boolean>() {
                            @Override
                            public Boolean apply(String s) throws Exception {
                                File file = new File(ImHelper.getBackgroundCachePath(), ImHelper.getBackgroundFileName(ConversationType.TEAM, ImHelper.wangId2ImId(teamId, ConversationType.TEAM)));
                                if (file.exists()) {
                                    file.delete();
                                }
                                File selectFile = new File(path);
                                return FileUtils.copyFile(selectFile, file);
                            }
                        })
                        .unsubscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    ToastUtil.showToastShort("设置成功");
                                    EventBean event = new EventBean(EVENT_NOTIFY_BACKGROUND);
                                    event.put("typeOrdinal", ConversationType.TEAM.ordinal());
                                    event.put("targetId", ImHelper.wangId2ImId(teamId, ConversationType.TEAM));
                                    EventBus.getDefault().post(event);
                                } else {
                                    ToastUtil.showToastShort("设置失败");
                                }
                            }
                        });
                break;
        }
    }
}