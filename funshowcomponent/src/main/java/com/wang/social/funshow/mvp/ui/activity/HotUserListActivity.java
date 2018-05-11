package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.TitleView;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleActivityComponent;
import com.wang.social.funshow.mvp.entities.user.TopUser;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterHotUserList;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class HotUserListActivity extends BasicAppActivity implements IView, BaseAdapter.OnItemClickListener<TopUser> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    private RecycleAdapterHotUserList adapter;

    @Inject
    IRepositoryManager mRepositoryManager;
    @Inject
    RxErrorHandler mErrorHandler;

    public static void start(Context context) {
        Intent intent = new Intent(context, HotUserListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_hotuserlist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterHotUserList();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetTopUserList(false);
            }

            @Override
            public void onLoadmore() {
                //趣晒魔接口没有分页
                //netLoadmore();
                springView.onFinishFreshAndLoadDelay(1000);
            }
        });
        springView.callFreshDelay();
    }

    @Override
    public void onItemClick(TopUser testEntity, int position) {

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

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 1000;

    public void netGetTopUserList(boolean needloading) {
        current = 1;
        ApiHelperEx.execute(this, needloading,
                ApiHelperEx.getService(FunshowService.class).getFunshowTopUserList("square", current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<TopUser>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<TopUser>> basejson) {
                        BaseListWrap<TopUser> warp = basejson.getData();
                        List<TopUser> list = warp.getList();
                        adapter.refreshData(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, null, () -> {
                    springView.onFinishFreshAndLoadDelay();
                });
    }


//    public void netLoadmore() {
//        ApiHelperEx.execute(this, false,
//                ApiHelperEx.getService(FunshowService.class).getFunshowTopUserList("square", current + 1, size),
//                new ErrorHandleSubscriber<BaseJson<BaseListWrap<TopUser>>>() {
//                    @Override
//                    public void onNext(BaseJson<BaseListWrap<TopUser>> basejson) {
//                        BaseListWrap<TopUser> warp = basejson.getData();
//                        List<TopUser> list = warp.getList();
//                        if (!StrUtil.isEmpty(list)) {
//                            current++;
//                            adapter.addItem(list);
//                        } else {
//                            ToastUtil.showToastLong("没有更多数据了");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtil.showToastLong(e.getMessage());
//                    }
//                }, null, () -> {
//                    springView.onFinishFreshAndLoadDelay();
//                });
//    }
}
