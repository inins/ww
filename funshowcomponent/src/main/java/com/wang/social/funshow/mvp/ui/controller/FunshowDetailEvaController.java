package com.wang.social.funshow.mvp.ui.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.TestEntity;
import com.frame.component.ui.base.BaseController;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterEva;

import java.util.ArrayList;

import butterknife.BindView;

public class FunshowDetailEvaController extends BaseController implements BaseAdapter.OnItemClickListener<TestEntity>{

    @BindView(R2.id.recycler_eva)
    RecyclerView recyclerEva;

    private RecycleAdapterEva adapterEva;

    public FunshowDetailEvaController(View root) {
        super(root);
//        int layout = R.layout.funshow_lay_detail_eva;
    }

    @Override
    protected void onInitCtrl() {
        adapterEva = new RecycleAdapterEva();
        adapterEva.setOnItemClickListener(this);
        recyclerEva.setNestedScrollingEnabled(false);
        recyclerEva.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerEva.setAdapter(adapterEva);
        recyclerEva.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));
    }

    @Override
    protected void onInitData() {
        adapterEva.refreshData(new ArrayList() {{
            add(new TestEntity());
            add(new TestEntity());
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

    }
}
