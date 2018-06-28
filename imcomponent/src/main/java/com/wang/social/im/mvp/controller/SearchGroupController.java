package com.wang.social.im.mvp.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.AppConstant;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.entities.search.SearchGroup;
import com.frame.component.enums.ConversationType;
import com.frame.component.helper.CommonHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.SearchFriendActivity;
import com.wang.social.im.mvp.ui.SearchGroupActivity;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchFriend;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchGroupController extends BaseController {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_more)
    TextView btnMore;
    @BindView(R2.id.text_title)
    TextView textTitle;
    private RecycleAdapterSearchGroup adapter;
    private String key;

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
        adapter.setOnItemClickListener((bean, position) -> {
            if (bean.isMi()) {
                CommonHelper.ImHelper.gotoGroupConversation(getContext(), bean.getGroupId() + "", ConversationType.TEAM, false);
            } else {
                CommonHelper.ImHelper.gotoGroupConversation(getContext(), bean.getGroupId() + "", ConversationType.SOCIAL, false);
            }
        });
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        btnMore.setOnClickListener(view -> {
            SearchGroupActivity.start(getContext(), key);
        });
        getRoot().setVisibility(View.GONE);
    }

    @Override
    protected void onInitData() {
    }

    public void search(String key) {
        this.key = key;
        netGetSearchList(key);
    }

    //////////////////////分页查询////////////////////

    private void netGetSearchList(String key) {
        ApiHelperEx.execute(getIView(), true,
                ApiHelperEx.getService(CommonService.class).searchGroupAll(key, 1, 20),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<SearchGroup>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<SearchGroup>> basejson) {
                        BaseListWrap<SearchGroup> warp = basejson.getData();
                        List<SearchGroup> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            adapter.refreshData(list);
                            getRoot().setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
