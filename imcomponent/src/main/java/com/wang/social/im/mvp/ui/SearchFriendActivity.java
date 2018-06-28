package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.common.NetParam;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.PersonalInfo;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.dto.SearchUserInfoDTO;
import com.frame.component.helper.CommonHelper;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.utils.StringUtils;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageList;
import com.frame.http.api.PageListDTO;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.api.NotifyService;
import com.wang.social.im.mvp.model.entities.notify.AiteMsg;
import com.wang.social.im.mvp.model.entities.notify.CommonMsg;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterSearchFriend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFriendActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.text_toolbar_title)
    TextView textToolbarTitle;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;

    private String key;
    private RecycleAdapterSearchFriend adapter;

    public static void start(Context context, String key) {
        Intent intent = new Intent(context, SearchFriendActivity.class);
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
        textToolbarTitle.setText("搜索好友-" + key);

        adapter = new RecycleAdapterSearchFriend();
        adapter.setOnItemClickListener((personalInfo, position) -> {
            CommonHelper.ImHelper.startPersonalCardForBrowse(this, personalInfo.getUserId());
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
        boolean isMobile = StringUtils.isMobileNO(key);
        Map<String, Object> param = new NetParam()
                .put("key", isMobile ? "" : key)
                .put("phone", isMobile ? key : "")
                .put("current", current + 1)
                .put("size", size)
                .put("v", "2.0.0")
                .build();
        ApiHelperEx.execute(this, needLoading,
                ApiHelperEx.getService(CommonService.class).chatListSearchUser(param),
                new ErrorHandleSubscriber<BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>>>() {
                    @Override
                    public void onNext(BaseJson<PageListDTO<SearchUserInfoDTO, PersonalInfo>> basejson) {
                        PageListDTO<SearchUserInfoDTO, PersonalInfo> warp = basejson.getData();
                        PageList<PersonalInfo> transform = warp != null ? warp.transform() : null;
                        List<PersonalInfo> list = transform.getList();
                        if (!StrUtil.isEmpty(list)) {
                            current = transform.getCurrent();
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
