package com.wang.social.home.mvp.ui.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterHome;

import java.util.ArrayList;

import butterknife.BindView;

public class HomeContentController extends BaseController implements BaseAdapter.OnItemClickListener<Funpoint> {

    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.recycler)
    RecyclerView recycler;

    private RecycleAdapterHome adapter;

    public HomeContentController(View root) {
        super(root);
        int layout = R.layout.home_lay_content;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        FontUtils.boldText(textTitle);

        adapter = new RecycleAdapterHome();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new ItemDecorationDivider(getContext(), LinearLayoutManager.VERTICAL));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onInitData() {
        adapter.refreshData(new ArrayList<Funpoint>(){{
            add(new Funpoint());
            add(new Funpoint());
            add(new Funpoint());
            add(new Funpoint());
            add(new Funpoint());
            add(new Funpoint());
            add(new Funpoint());
            add(new Funpoint());
        }});
    }

    @Override
    public void onItemClick(Funpoint funpoint, int position) {

    }
}
