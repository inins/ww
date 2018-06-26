package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.HVItemDecoration;
import com.frame.component.entities.UserInfo;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerActivityComponent;
import com.wang.social.im.mvp.model.api.ChainService;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;
import com.wang.social.im.mvp.ui.adapters.NewUsersAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 新用户
 */
@RouteNode(path = "/newUser", desc = "新用户")
public class NewUsersActivity extends BasicAppActivity implements IView, BaseAdapter.OnItemClickListener<UserInfo> {

    @BindView(R2.id.sl_rlv_users)
    RecyclerView slRlvUsers;

    @Inject
    ApiHelper mApiHelper;
    @Inject
    IRepositoryManager mRepositoryManager;

    public static void start(Context context) {
        Intent intent = new Intent(context, NewUsersActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_new_users;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mApiHelper.execute(this, mRepositoryManager.obtainRetrofitService(ChainService.class)
                .getNewUsers("2.0.0"), new ErrorHandleSubscriber<ListData<UserInfo>>() {
            @Override
            public void onNext(ListData<UserInfo> userInfoListData) {
                showUsers(userInfoListData.getList());
            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                showLoadingDialog();
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                dismissLoadingDialog();
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    private void showUsers(List<UserInfo> users) {
        slRlvUsers.setLayoutManager(new LinearLayoutManager(this));
        HVItemDecoration itemDecoration = new HVItemDecoration(this, HVItemDecoration.LINEAR_DIVIDER_VERTICAL);
        itemDecoration.setLeftMargin(UIUtil.getDimen(R.dimen.common_border_margin));
        slRlvUsers.addItemDecoration(itemDecoration);
        NewUsersAdapter adapter = new NewUsersAdapter(users);
        adapter.setOnItemClickListener(this);
        slRlvUsers.setAdapter(adapter);
    }

    @Override
    public void onItemClick(UserInfo userInfo, int position) {
        PersonalCardActivity.start(this, Integer.valueOf(userInfo.getUserId()));
    }
}