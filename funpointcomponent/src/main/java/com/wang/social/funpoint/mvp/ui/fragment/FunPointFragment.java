package com.wang.social.funpoint.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseFragment;
import com.frame.base.BasicFragment;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetLoginTestHelper;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.view.barview.BarUser;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.di.component.DaggerFunPointListFragmentComponent;
import com.wang.social.funpoint.di.component.DaggerSingleFragmentComponent;
import com.wang.social.funpoint.di.module.FunpointListModule;
import com.wang.social.funpoint.mvp.contract.FunpointListContract;
import com.wang.social.funpoint.mvp.entities.Funpoint;
import com.wang.social.funpoint.mvp.entities.Lable;
import com.wang.social.funpoint.mvp.presonter.FunpointListPresonter;
import com.wang.social.funpoint.mvp.ui.activity.SearchActivity;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterHome;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterLable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 */

public class FunPointFragment extends BaseFragment<FunpointListPresonter> implements FunpointListContract.View, BaseAdapter.OnItemClickListener<Funpoint> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler_lable)
    RecyclerView recyclerLable;
    @BindView(R2.id.recycler_content)
    RecyclerView recyclerContent;
    Unbinder unbinder;
    @BindView(R2.id.btn_search)
    View btnSearch;

    private RecycleAdapterLable adapterLable;
    private RecycleAdapterHome adapterHome;

    public static FunPointFragment newInstance() {
        Bundle args = new Bundle();
        FunPointFragment fragment = new FunPointFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funpoint_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapterLable = new RecycleAdapterLable();
        adapterLable.setOnMoreClickListener(v -> SearchActivity.start(getContext()));
        recyclerLable.setNestedScrollingEnabled(false);
        recyclerLable.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerLable.setAdapter(adapterLable);
        recyclerLable.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));

        adapterHome = new RecycleAdapterHome();
        adapterHome.setOnItemClickListener(this);
        recyclerContent.setNestedScrollingEnabled(false);
        recyclerContent.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerContent.setAdapter(adapterHome);
        recyclerContent.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(7), GridLayoutManager.VERTICAL, false));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.netGetFunpointList(false);
            }

            @Override
            public void onLoadmore() {
            }
        });

        mPresenter.netGetFunpointList(false);

        adapterLable.refreshData(new ArrayList<Lable>() {{
            add(new Lable("社交软件"));
            add(new Lable("APP"));
            add(new Lable("同城交友"));
        }});

        getView().findViewById(R.id.btn_funshow_login).setOnClickListener(v -> NetLoginTestHelper.newInstance().loginTest());
    }

    @Override
    public void onItemClick(Funpoint bean, int position) {
        WebActivity.start(getContext(), bean.getUrl());
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
    }

    @Override
    public void hideLoading() {
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
    public void finishSpringView() {
        springView.onFinishFreshAndLoadDelay();
    }


}
