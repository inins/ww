package com.wang.social.im.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.router.facade.annotation.Autowired;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.contract.AlertUserListContract;
import com.wang.social.im.mvp.model.entities.IndexMemberInfo;
import com.wang.social.im.mvp.presenter.AlertUserListPresenter;
import com.wang.social.im.mvp.ui.adapters.AlertUserAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * @ 用户列表
 */
public class AlertUserListActivity extends BaseAppActivity<AlertUserListPresenter> implements AlertUserListContract.View, IndexableAdapter.OnItemContentClickListener<IndexMemberInfo> {

    public static final String EXTRA_MEMBER = "memberInfo";

    @BindView(R2.id.aul_tvb_search)
    TextView aulTvbSearch;
    @BindView(R2.id.aul_toolbar)
    SocialToolbar aulToolbar;
    @BindView(R2.id.aul_users)
    IndexableLayout aulUsers;

    @Autowired
    private String groupId;

    public static void start(Activity activity, int requestCode, String groupId) {
        Intent intent = new Intent();
        intent.putExtra("groupId", groupId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_alert_user_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mPresenter.getMemberList(groupId);
    }

    private void init() {
        aulToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                onBackPressed();
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
    public void showMembers(List<IndexMemberInfo> members) {
        aulUsers.setLayoutManager(new LinearLayoutManager(this));
        AlertUserAdapter adapter = new AlertUserAdapter(this);
        adapter.setOnItemContentClickListener(this);
        aulUsers.setAdapter(adapter);
        adapter.setDatas(members);
        aulUsers.showAllLetter(false);
        aulUsers.setOverlayStyle_MaterialDesign(ContextCompat.getColor(this, R.color.common_colorAccent));
        aulUsers.setCompareMode(IndexableLayout.MODE_FAST);
    }

    @Override
    public void onItemClick(View v, int originalPosition, int currentPosition, IndexMemberInfo entity) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_MEMBER, entity);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R2.id.aul_tvb_search)
    public void onViewClicked() {
    }
}
