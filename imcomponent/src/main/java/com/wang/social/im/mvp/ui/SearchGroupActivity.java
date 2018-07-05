package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.search.SearchGroup;
import com.frame.component.enums.ConversationType;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchFriend;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchGroupActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.text_toolbar_title)
    TextView textToolbarTitle;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;

    private String key;
    private RecycleAdapterSearchGroup adapter;

    public static void start(Context context, String key) {
        Intent intent = new Intent(context, SearchGroupActivity.class);
        intent.putExtra("key", key);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_search_friend;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        key = getIntent().getStringExtra("key");
        textToolbarTitle.setText("搜索群组-" + key);

        adapter = new RecycleAdapterSearchGroup();
        adapter.setOnItemClickListener((bean, position) -> {
            if (bean.isMi()) {
                CommonHelper.ImHelper.gotoGroupConversation(this, bean.getGroupId() + "", ConversationType.TEAM, false);
            } else {
                CommonHelper.ImHelper.gotoGroupConversation(this, bean.getGroupId() + "", ConversationType.SOCIAL, false);
            }
        });
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));
        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetSearchList(true, false);
            }

            @Override
            public void onLoadmore() {
                netGetSearchList(false, false);
            }
        });

        netGetSearchList(true, true);
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetSearchList(boolean isFresh, boolean needLoading) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, needLoading,
                ApiHelperEx.getService(CommonService.class).searchGroupAll(key, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<SearchGroup>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<SearchGroup>> basejson) {
                        BaseListWrap<SearchGroup> warp = basejson.getData();
                        List<SearchGroup> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                    }
                });
    }
}
