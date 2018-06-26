package com.frame.component.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.NetCommonFunshowHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.adapter.RecycleAdapterCommonFunshow;
import com.frame.component.ui.adapter.RecycleAdapterNewguideRecommendFriend;
import com.frame.component.ui.base.BasicNoDiFragment;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 */

public class NewGuideRecommendFriendFragment extends BasicNoDiFragment {

    @BindView(R2.id.recycler)
    RecyclerView recycler;

    private RecycleAdapterNewguideRecommendFriend adapter;

    public static NewGuideRecommendFriendFragment newInstance() {
        Bundle args = new Bundle();
        NewGuideRecommendFriendFragment fragment = new NewGuideRecommendFriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.fragment_newguide_recommend_friend;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterNewguideRecommendFriend();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new GridSpacingItemDecoration(3, SizeUtils.dp2px(20), GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        adapter.refreshData(new ArrayList<TestEntity>(){{
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
            add(new TestEntity());
        }});
    }

    @Override
    public void setData(@Nullable Object data) {
    }


}
