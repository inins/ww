package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.component.utils.UIUtil;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.router.facade.annotation.Autowired;
import com.frame.utils.ToastUtil;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerMemberListComponent;
import com.wang.social.im.di.modules.MemberListModule;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.mvp.model.entities.TryToExit;
import com.wang.social.im.mvp.presenter.MemberListPresenter;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;
import com.wang.social.im.mvp.ui.adapters.members.MembersAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成员列表
 */
public class MemberListActivity extends BaseAppActivity<MemberListPresenter> implements MemberListContract.View, MembersAdapter.OnHandleListener {

    @BindView(R2.id.ml_title)
    TitleView mlTitle;
    @BindView(R2.id.if_iv_portrait)
    ImageView ifIvPortrait;
    @BindView(R2.id.if_tv_nickname)
    TextView ifTvNickname;
    @BindView(R2.id.if_tv_age)
    TextView ifTvAge;
    @BindView(R2.id.if_tv_constellation)
    TextView ifTvConstellation;
    @BindView(R2.id.if_tv_tags)
    TextView ifTvTags;
    @BindView(R2.id.ml_members)
    RecyclerView mlMembers;
    @BindView(R2.id.ml_ll_title)
    LinearLayout mlLlTitle;

    @Autowired
    String groupId;
    @Autowired
    boolean isSocial;

    @Inject
    ImageLoader mImageLoader;

    private MemberInfo mMasterInfo;
    private MembersAdapter mAdapter;

    public static void start(Context context, String groupId, boolean isSocial) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra("groupId", groupId);
        intent.putExtra("isSocial", isSocial);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageLoader = null;
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMemberListComponent
                .builder()
                .appComponent(appComponent)
                .memberListModule(new MemberListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_member_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getMembers(groupId);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        hideLoadingDialog();
    }

    @Override
    public void showMembers(int memberCount, int friendCount, MemberInfo master, List<MembersLevelOne> members) {
        mlLlTitle.setVisibility(View.VISIBLE);

        mlTitle.setNote(UIUtil.getString(isSocial ? R.string.im_social_member_list_title_tip : R.string.im_team_member_list_title_tip, memberCount, friendCount));

        showMasterInfo(master);

        mlMembers.setLayoutManager(new LinearLayoutManager(this));
        List list = new ArrayList(members);
        mAdapter = new MembersAdapter(list, this, master.getMemberId().equals(TIMManager.getInstance().getLoginUser()));
        mlMembers.setAdapter(mAdapter);
    }

    @Override
    public void onKickOutComplete(MemberInfo memberInfo) {
        List<MembersLevelOne> groups = (List<MembersLevelOne>) mAdapter.getDataList();
        if (groups != null) {
            for (MembersLevelOne levelOne : groups) {
                for (MemberInfo member : levelOne.getMembers()) {
                    if (member.getMemberId().equals(memberInfo.getMemberId())) {
                        levelOne.getMembers().remove(member);
                        mAdapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onTryToExitSuccess(MemberInfo memberInfo, TryToExit result) {
        if (result.isHasTeam()) {
            // 有创建觅聊
            DialogSure.showDialog(this,
                    "该用户已在趣聊内创建觅聊，退出后觅聊将由最早加入的成员接管",
                    () -> mPresenter.kickOutMember(groupId, memberInfo));
        } else {
            // 没有创建觅聊
            DialogSure.showDialog(this,
                    UIUtil.getString(R.string.im_take_out_sure, memberInfo.getNickname()),
                    () -> mPresenter.kickOutMember(groupId, memberInfo));
        }
    }

    @OnClick(R2.id.ml_cl_master)
    public void onViewClicked() {
        PersonalCardActivity.start(this, Integer.parseInt(mMasterInfo.getMemberId()));
    }

    private void showMasterInfo(MemberInfo master) {
        mMasterInfo = master;
        mImageLoader.loadImage(this, ImageConfigImpl.builder()
                .placeholder(R.drawable.common_default_circle_placeholder)
                .errorPic(R.drawable.common_default_circle_placeholder)
                .isCircle(true)
                .imageView(ifIvPortrait)
                .url(master.getPortrait())
                .build());
        ifTvNickname.setText(master.getNickname());
        ifTvAge.setText(master.getAgeRange());
        Drawable genderDrawable;
        if (master.getGender() == Gender.MALE) {
            ifTvAge.setBackgroundResource(R.drawable.im_bg_male);
            genderDrawable = ContextCompat.getDrawable(this, R.drawable.common_ic_man);
        } else {
            ifTvAge.setBackgroundResource(R.drawable.im_bg_female);
            genderDrawable = ContextCompat.getDrawable(this, R.drawable.common_ic_women);
        }
        genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
        ifTvAge.setCompoundDrawables(genderDrawable, null, null, null);
        ifTvConstellation.setText(master.getConstellation());
        String tags = "";
        for (Tag tag : master.getTags()) {
            tags = tags + "#" + tag.getTagName() + " ";
        }
        ifTvTags.setText(tags);
    }

    @Override
    public void onFriendly(MemberInfo memberInfo) {
        DialogValiRequest.showDialog(this, content -> {
            if (TextUtils.isEmpty(content)) {
                ToastUtil.showToastShort("请输入申请理由");
                return;
            }
            mPresenter.friendRequest(memberInfo.getMemberId(), content);
        });
    }

    @Override
    public void onTakeOut(MemberInfo memberInfo, int position) {
        // 踢人，先查询是否有觅聊
        mPresenter.tryToExit(memberInfo, false);
    }

//    /**
//     * 弹出对话框，提示踢人
//     */
//    private void takeOut(MemberInfo memberInfo, int position) {
//        DialogSure.showDialog(this,
//                UIUtil.getString(R.string.im_take_out_sure, memberInfo.getNickname()),
//                () -> mPresenter.kickOutMember(groupId, memberInfo));
//    }

    @Override
    public void onItemClick(MemberInfo memberInfo, int position) {
        PersonalCardActivity.start(this, Integer.parseInt(memberInfo.getMemberId()));
    }
}
