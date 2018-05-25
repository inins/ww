package com.wang.social.funshow.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.base.BasicAppNoDiActivity;
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
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.user.ZanUser;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterZanUserList;

import java.util.List;

import butterknife.BindView;

public class ZanUserListActivity extends BasicAppNoDiActivity implements IView,BaseAdapter.OnItemClickListener<ZanUser> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    private RecycleAdapterZanUserList adapter;

    private int talkId;

    public static void start(Context context, int talkId) {
        Intent intent = new Intent(context, ZanUserListActivity.class);
        intent.putExtra("talkId", talkId);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.funshow_activity_zanuserlist;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        talkId = getIntent().getIntExtra("talkId", 0);

        adapter = new RecycleAdapterZanUserList();
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
                netGetZanUserList(false);
            }

            @Override
            public void onLoadmore() {
                netLoadmore();
            }
        });
        springView.callFreshDelay();
    }

    @Override
    public void onItemClick(ZanUser bean, int position) {
        CommonHelper.ImHelper.startPersonalCardForBrowse(this, bean.getUserId());
    }

    //////////////////////分页查询////////////////////
    private int current = 1;

    private int size = 20;

    public void netGetZanUserList(boolean needloading) {
        current = 1;
        ApiHelperEx.execute(this, needloading,
                ApiHelperEx.getService(FunshowService.class).funshowZanList(talkId, current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<ZanUser>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<ZanUser>> basejson) {
                        BaseListWrap<ZanUser> warp = basejson.getData();
                        List<ZanUser> list = warp.getList();
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


    public void netLoadmore() {
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(FunshowService.class).funshowZanList(talkId, ++current, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<ZanUser>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<ZanUser>> basejson) {
                        BaseListWrap<ZanUser> warp = basejson.getData();
                        List<ZanUser> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            adapter.addItem(list);
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, null, () -> {
                    springView.onFinishFreshAndLoadDelay();
                });
    }
}
