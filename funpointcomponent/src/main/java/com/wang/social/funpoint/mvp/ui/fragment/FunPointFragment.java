package com.wang.social.funpoint.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseFragment;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.Tag;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.frame.component.ui.adapter.RecycleAdapterFunpoint;
import com.frame.component.ui.base.BaseLazyFragment;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.di.component.DaggerFunPointListFragmentComponent;
import com.wang.social.funpoint.di.module.FunpointListModule;
import com.wang.social.funpoint.mvp.contract.FunpointListContract;
import com.frame.component.entities.funpoint.Funpoint;
import com.wang.social.funpoint.mvp.presonter.FunpointListPresonter;
import com.wang.social.funpoint.mvp.ui.activity.SearchActivity;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterLable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 */

public class FunPointFragment extends BaseLazyFragment<FunpointListPresonter> implements FunpointListContract.View {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler_lable)
    RecyclerView recyclerLable;
    @BindView(R2.id.recycler_content)
    RecyclerView recyclerContent;
    @BindView(R2.id.btn_search)
    View btnSearch;

    private RecycleAdapterLable adapterLable;
    private RecycleAdapterFunpoint adapterHome;
    private boolean isBigKnow;

    public static FunPointFragment newInstance() {
        Bundle args = new Bundle();
        FunPointFragment fragment = new FunPointFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENTBUS_TAG_UPDATED:
                mPresenter.netGetRecommendTag();
                mPresenter.netGetFunpointList(true, true, isBigKnow);
                break;
            case EventBean.EVENTBUS_TAG_ALL:
                //大量知识，有待处理
                isBigKnow = true;
                mPresenter.netGetFunpointList(true, false, isBigKnow);
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funpoint_fragment;
    }

    @Override
    public void initDataLazy() {
        adapterLable = new RecycleAdapterLable();
        adapterLable.setOnMoreClickListener(v -> TagSelectionActivity.startSelection(getActivity(), TagSelectionActivity.TAG_TYPE_INTEREST));
        recyclerLable.setNestedScrollingEnabled(false);
        recyclerLable.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerLable.setAdapter(adapterLable);
        recyclerLable.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));

        adapterHome = new RecycleAdapterFunpoint();
        recyclerContent.setNestedScrollingEnabled(false);
        recyclerContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerContent.setAdapter(adapterHome);
        recyclerContent.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(7), GridLayoutManager.VERTICAL, false));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                isBigKnow = false;
                mPresenter.netGetFunpointList(true, false, isBigKnow);
            }

            @Override
            public void onLoadmore() {
                mPresenter.netGetFunpointList(false, false, isBigKnow);
            }
        });

        mPresenter.netGetFunpointList(true, true, isBigKnow);
        mPresenter.netGetRecommendTag();
    }

    @OnClick(R2.id.btn_search)
    public void onViewClicked() {
        SearchActivity.start(getContext());
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFunPointListFragmentComponent.builder()
                .appComponent(appComponent)
                .funpointListModule(new FunpointListModule(this))
                .build()
                .inject(this);
    }

    //////////////////////////////////////////

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
    public void reFreshList(List<Funpoint> datas) {
        adapterHome.refreshData(datas);
    }

    @Override
    public void appendList(List<Funpoint> datas) {
        adapterHome.addItem(datas);
    }

    @Override
    public void reFreshReadCountById(int newsId) {
        adapterHome.reFreshReadCountById(newsId);
    }

    @Override
    public void reFreshTags(List<Tag> tags) {
        if (null != adapterLable) {
            adapterLable.refreshData(tags);
        }
    }

    @Override
    public void finishSpringView() {
        springView.onFinishFreshAndLoadDelay();
    }


}
