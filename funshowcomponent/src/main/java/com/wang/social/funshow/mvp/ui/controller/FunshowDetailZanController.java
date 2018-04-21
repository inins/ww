package com.wang.social.funshow.mvp.ui.controller;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.User;
import com.frame.component.ui.base.BaseController;
import com.frame.utils.SizeUtils;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.activity.ZanUserListActivity;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterZan;

import java.util.ArrayList;

import butterknife.BindView;

public class FunshowDetailZanController extends BaseController {

    @BindView(R2.id.recycler_zan)
    RecyclerView recyclerZan;

    private RecycleAdapterZan adapterZan;

    public FunshowDetailZanController(View root) {
        super(root);
//        int layout = R.layout.funshow_lay_detail_zan;
    }

    @Override
    protected void onInitCtrl() {
        adapterZan = new RecycleAdapterZan();
        adapterZan.setOnMoreClickListener(v -> {
            ZanUserListActivity.start(getContext());
        });
        recyclerZan.setNestedScrollingEnabled(false);
        recyclerZan.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerZan.setAdapter(adapterZan);
        recyclerZan.addItemDecoration(new GridSpacingItemDecoration(1, SizeUtils.dp2px(5), GridLayoutManager.HORIZONTAL, false));
    }

    @Override
    protected void onInitData() {
        adapterZan.refreshData(new ArrayList<User>() {{
            add(new User());
            add(new User());
            add(new User());
            add(new User());
            add(new User());
            add(new User());
        }});
    }
}
