package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.api.CommonService;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.ShatDownUser;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.User;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.frame.component.common.ItemDecorationDivider;
import com.wang.social.personal.di.component.DaggerSingleActivityComponent;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterBlacklist;
import com.frame.component.view.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlackListActivity extends BasicAppActivity implements IView, RecycleAdapterBlacklist.OnBlankListUserClickListener {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.spring)
    SpringView springView;
    private RecycleAdapterBlacklist adapter;

    private boolean isBlankList;

    public static void startBlankList(Context context) {
        start(context, true);
    }

    public static void startShutdownList(Context context) {
        start(context, false);
    }

    private static void start(Context context, boolean isBlankList) {
        Intent intent = new Intent(context, BlackListActivity.class);
        intent.putExtra("isBlankList", isBlankList);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_blacklist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        isBlankList = getIntent().getBooleanExtra("isBlankList", false);
        titleview.setTitle(getResources().getString(isBlankList ? R.string.personal_blacklist_title : R.string.personal_shutdown_title));
        titleview.setNote(getResources().getString(isBlankList ? R.string.personal_blacklist_title_note : R.string.personal_shutdown_title_note));

        adapter = new RecycleAdapterBlacklist();
        adapter.setOnBlankListUserClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));
        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetPhotoList();
            }

            @Override
            public void onLoadmore() {
                springView.onFinishFreshAndLoadDelay(1000);
            }
        });
        springView.callFreshDelay();
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
        }
    }

    @Override
    public void onUserClick(ShatDownUser user, int position) {

    }

    @Override
    public void onFreeBtnClick(ShatDownUser user, int position) {
        DialogSure.showDialog(this, "确定要释放该囚犯？", () -> {
            netFreeUsers(String.valueOf(user.getUserId()));
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerSingleActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }


    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    ///////////////////////////////

    private void netGetPhotoList() {
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(UserService.class).shatDownList(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<ShatDownUser>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<ShatDownUser>> basejson) {
                        BaseListWrap<ShatDownUser> wrap = basejson.getData();
                        List<ShatDownUser> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            adapter.refreshData(list);
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private void netFreeUsers(String userIds) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(CommonService.class).shatDownUser(userIds, 2),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        springView.callFresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
