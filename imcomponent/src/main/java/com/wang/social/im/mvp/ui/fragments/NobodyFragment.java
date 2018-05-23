package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.frame.base.BaseFragment;
import com.frame.component.helper.CommonHelper;
import com.frame.component.view.barview.BarUser;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerNobodyComponent;
import com.wang.social.im.di.modules.NobodyModule;
import com.wang.social.im.mvp.contract.NobodyContract;
import com.wang.social.im.mvp.presenter.NobodyPresenter;
import com.wang.social.im.mvp.ui.NewUsersActivity;
import com.wang.social.im.mvp.ui.PhoneBookActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 9:07
 * ============================================
 */
public class NobodyFragment extends BaseFragment<NobodyPresenter> implements NobodyContract.View {

    @BindView(R2.id.fn_knowledge)
    BarView fnKnowledge;
    @BindView(R2.id.fn_post)
    BarView fnPost;
    @BindView(R2.id.fn_new_users)
    BarView fnNewUsers;

    public static NobodyFragment newInstance() {
        Bundle args = new Bundle();
        NobodyFragment fragment = new NobodyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerNobodyComponent
                .builder()
                .appComponent(appComponent)
                .nobodyModule(new NobodyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_nobody;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getKnowledge();
        mPresenter.getFunShow();
        mPresenter.getNewUsers();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R2.id.fn_tvb_look_contacts, R2.id.fn_knowledge, R2.id.fn_post, R2.id.fn_new_users})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.fn_tvb_look_contacts) {
            PhoneBookActivity.start(getActivity());
        } else if (view.getId() == R.id.fn_knowledge) {
            CommonHelper.TopicHelper.startTopUser(getContext());
        } else if (view.getId() == R.id.fn_post) {
            
        } else if (view.getId() == R.id.fn_new_users) {
            NewUsersActivity.start(getContext());
        }
    }

    @Override
    public void showKnowledgeUsers(List<BarUser> users) {
        fnKnowledge.refreshData(users);
    }

    @Override
    public void showFunShowUsers(List<BarUser> users) {
        fnPost.refreshData(users);
    }

    @Override
    public void showNewUsers(List<BarUser> users) {
        fnNewUsers.refreshData(users);
    }
}
