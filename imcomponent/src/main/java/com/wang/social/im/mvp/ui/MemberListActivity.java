package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.modules.MemberListModule;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.mvp.presenter.MemberListPresenter;
import com.wang.social.im.mvp.ui.adapters.members.MembersAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 成员列表
 */
public class MemberListActivity extends BaseAppActivity<MemberListPresenter> implements MemberListContract.View {

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

    @Autowired
    String groupId;

    public static void start(Context context, String groupId) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra("groupId", groupId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void showMembers(List<MembersLevelOne> members) {
        mlMembers.setLayoutManager(new LinearLayoutManager(this));
        List list = new ArrayList(members);
        mlMembers.setAdapter(new MembersAdapter(list));
    }

    @OnClick(R2.id.ml_iv_search)
    public void onViewClicked() {
    }
}
