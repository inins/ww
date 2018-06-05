package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.NetFriendHelper;
import com.frame.component.helper.NetMsgHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.component.view.TitleView;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.api.NotifyService;
import com.wang.social.im.mvp.model.entities.notify.FindChatRequest;
import com.wang.social.im.mvp.model.entities.notify.RequestBean;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterFriendRequest;

import java.util.List;

import butterknife.BindView;

@RouteNode(path = "/notify_group_request", desc = "加入觅聊申请页面")
public class NotifyFindChatRequestListActivity extends BasicAppNoDiActivity implements IView, BaseAdapter.OnItemClickListener<RequestBean> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;
    private RecycleAdapterFriendRequest adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NotifyFindChatRequestListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_NOTIFY_DETAIL_DEAL:
                netGetMsgList(true);
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_common_title_recycler;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        titleview.setTitle(getString(R.string.im_notify_findchat_request_title));

        adapter = new RecycleAdapterFriendRequest();
        adapter.setGroup(true);
        adapter.setOnItemClickListener(this);
        adapter.setOnAgreeClickListener((bean, position) -> {
            NetFriendHelper.newInstance().netAgreeFindChatApply(NotifyFindChatRequestListActivity.this, bean.getGroupId(), bean.getUserId(), bean.getMsgId(), true, () -> {
                netGetMsgList(true);
            });
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
                netGetMsgList(true);
            }

            @Override
            public void onLoadmore() {
                netGetMsgList(false);
            }
        });
        springView.callFreshDelay();
    }

    @Override
    public void onItemClick(RequestBean bean, int position) {
        NotifyFriendRequestDetailActivity.start(this, bean);
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetMsgList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(NotifyService.class).getGroupRequstList(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FindChatRequest>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FindChatRequest>> basejson) {
                        BaseListWrap<FindChatRequest> warp = basejson.getData();
                        List<RequestBean> list = FindChatRequest.tans2RequestBeanList(warp.getList());
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                            loadingviewEx.showOut();
                        } else {
                            if (isFresh) loadingviewEx.showFailViewNoFindChat();
                        }
                        NetMsgHelper.newInstance().readGroupMsg();
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                        if (isFresh) loadingviewEx.showFailViewNoNet();
                    }
                });
    }

}
