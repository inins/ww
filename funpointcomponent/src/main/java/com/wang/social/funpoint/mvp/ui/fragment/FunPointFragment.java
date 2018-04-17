package com.wang.social.funpoint.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicFragment;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.wang.social.funpoint.R;
import com.wang.social.funpoint.R2;
import com.wang.social.funpoint.mvp.entities.Lable;
import com.wang.social.funpoint.mvp.ui.activity.RecycleAdapterLable;
import com.wang.social.funpoint.mvp.ui.adapter.RecycleAdapterHome;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 */

public class FunPointFragment extends BasicFragment implements BaseAdapter.OnItemClickListener<TestEntity>{

    @BindView(R2.id.recycler_lable)
    RecyclerView recyclerLable;
    @BindView(R2.id.recycler_content)
    RecyclerView recyclerContent;
    Unbinder unbinder;

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


        adapterLable.refreshData(new ArrayList<Lable>() {{
            add(new Lable("社交软件"));
            add(new Lable("APP"));
            add(new Lable("同城交友"));
        }});
        adapterHome.refreshData(new ArrayList<TestEntity>() {{
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
        }});
    }

    @Override
    public void onItemClick(TestEntity testEntity, int position) {
        WebActivity.start(getContext());
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
