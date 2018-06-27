package com.wang.social.im.mvp.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BaseController;
import com.frame.mvp.IView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.SearchFriendActivity;
import com.wang.social.im.mvp.ui.SearchGroupActivity;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchFriend;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchGroup;

import java.util.ArrayList;

import butterknife.BindView;

public class SearchGroupController extends BaseController {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_more)
    TextView btnMore;
    @BindView(R2.id.text_title)
    TextView textTitle;
    private RecycleAdapterSearchGroup adapter;

    public SearchGroupController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.im_lay_search_fram;
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        textTitle.setText("群组/觅聊");
        adapter = new RecycleAdapterSearchGroup();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        btnMore.setOnClickListener(view -> {
            SearchGroupActivity.start(getContext(),"啊");
        });
    }

    @Override
    protected void onInitData() {
        adapter.refreshData(new ArrayList<TestEntity>(){{
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
}
