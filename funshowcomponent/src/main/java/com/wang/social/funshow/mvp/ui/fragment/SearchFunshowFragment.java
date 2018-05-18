package com.wang.social.funshow.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.base.BasicNoDiFragment;
import com.frame.component.view.LoadingLayout;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleFragmentComponent;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.funshow.FunshowSearch;
import com.wang.social.funshow.mvp.model.api.FunshowService;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class SearchFunshowFragment extends BasicNoDiFragment implements IView, BaseAdapter.OnItemClickListener<FunshowBean> {

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

    public static SearchFunshowFragment newInstance() {
        Bundle args = new Bundle();
        SearchFunshowFragment fragment = new SearchFunshowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funshow_fragment_search;
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
    public void onItemClick(FunshowBean bean, int position) {
    }

    private void search(boolean isFresh) {
        if (!TextUtils.isEmpty(adapter.getTags()) || !TextUtils.isEmpty(adapter.getKey())) {
            netGetSearchList(adapter.getKey(), isFresh);
        } else {
            ToastUtil.showToastShort("请输入搜索关键字");
        }
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetSearchList(String key, boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(FunshowService.class).searchFunshow(key, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowSearch>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowSearch>> basejson) {
                        BaseListWrap<FunshowSearch> warp = basejson.getData();
                        //List<FunshowSearch> list = warp.getList();
                        List<FunshowBean> list = FunshowSearch.tans2FunshowBeanList(warp.getList());
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
                            ToastUtil.showToastShort("没有更多数据了");
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                    }
                });
    }
}
