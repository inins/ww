package com.wang.social.funpoint.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.LoadingLayout;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.di.component.DaggerSingleActivityComponent;
import com.wang.social.funpoint.di.component.DaggerSingleFragmentComponent;
import com.wang.social.funpoint.mvp.model.api.FunpointService;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class SearchFunpointFragment extends BasicFragment implements IView, BaseAdapter.OnItemClickListener<Funpoint> {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.loadingview)
    LoadingLayout loadingview;

    private RecycleAdapterSearch adapter;

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_APP_SEARCH:
                String tags = (String) event.get("tags");
                String key = (String) event.get("key");
                adapter.setTags(tags);
                adapter.setKey(key);
                search(true);
                break;
        }
    }

    public static SearchFunpointFragment newInstance() {
        Bundle args = new Bundle();
        SearchFunpointFragment fragment = new SearchFunpointFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funpoint_fragment_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterSearch();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                search(true);
            }

            @Override
            public void onLoadmore() {
                search(false);
            }
        });
        loadingview.showLackView();
    }

    @Override
    public void onItemClick(Funpoint bean, int position) {
        netReadFunpoint(bean.getNewsId());
        WebActivity.start(getContext(), bean.getUrl());
    }

    private void search(boolean isFresh) {
        if (!TextUtils.isEmpty(adapter.getTags()) || !TextUtils.isEmpty(adapter.getKey())) {
            netGetSearchList(adapter.getTags(), adapter.getKey(), isFresh);
        } else {
            ToastUtil.showToastShort("请输入搜索关键字");
        }
    }

    @Override
    public void showLoading() {
        if (getActivity() instanceof BasicAppActivity) {
            ((BasicAppActivity) getActivity()).showLoadingDialog();
        }
    }

    @Override
    public void hideLoading() {
        if (getActivity() instanceof BasicAppActivity) {
            ((BasicAppActivity) getActivity()).dismissLoadingDialog();
        }
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSingleFragmentComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetSearchList(String tags, String key, boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(FunpointService.class).getSearchFunpointList(tags, key, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funpoint>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funpoint>> basejson) {
                        BaseListWrap<Funpoint> warp = basejson.getData();
                        List<Funpoint> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            loadingview.showOut();
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                        } else {
                            if (isFresh) {
                                loadingview.showLackView();
                            }
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                    }
                });
    }

    public void netReadFunpoint(int newsId) {
        ApiHelperEx.execute(null, false,
                ApiHelperEx.getService(FunpointService.class).readFunpoint(newsId),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        adapter.reFreshReadCountById(newsId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
