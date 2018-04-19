package com.wang.social.funshow.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BasicFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.view.barview.BarUser;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerSingleFragmentComponent;
import com.wang.social.funshow.mvp.ui.activity.FunshowDetailActivity;
import com.wang.social.funshow.mvp.ui.activity.HotUserListActivity;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterHome;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 */

public class FunShowFragment extends BasicFragment implements BaseAdapter.OnItemClickListener<TestEntity> {

    @BindView(R2.id.barview)
    BarView barview;
    @BindView(R2.id.recycler_content)
    RecyclerView recycler;
    Unbinder unbinder;

    private RecycleAdapterHome adapter;

    public static FunShowFragment newInstance() {
        Bundle args = new Bundle();
        FunShowFragment fragment = new FunShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funshow_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterHome();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(SizeUtils.dp2px(15)));

        barview.refreshData(new ArrayList<BarUser>() {{
            add(new BarUser("http://i-7.vcimg.com/trim/48b866104e7efc1ffd7367e7423296c11060910/trim.jpg"));
            add(new BarUser("https://tse3-mm.cn.bing.net/th?id=OIP.XzZcrXAIrxTtUH97rMlNGQHaEo&p=0&o=5&pid=1.1"));
            add(new BarUser("http://photos.tuchong.com/23552/f/624083.jpg"));
        }});

        adapter.refreshData(new ArrayList<TestEntity>() {{
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
        FunshowDetailActivity.start(getContext());
    }

    @OnClick({R2.id.barview})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.barview) {
            HotUserListActivity.startBlankList(getContext());

        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerSingleFragmentComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
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
