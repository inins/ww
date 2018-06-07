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
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetFriendHelper;
import com.frame.component.helper.NetGroupHelper;
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
import com.wang.social.im.mvp.model.entities.notify.GroupRequest;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterGroupRequest;

import java.util.List;

import butterknife.BindView;

@RouteNode(path = "/notify_group_join", desc = "趣聊邀请")
public class NotifyGroupRequestListActivity extends BasicAppNoDiActivity implements IView, BaseAdapter.OnItemClickListener<GroupRequest> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;
    private RecycleAdapterGroupRequest adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NotifyGroupRequestListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENTBUS_AGREE_ADD_GROUP_SUCCESS:
            case EventBean.EVENTBUS_REFUSE_ADD_GROUP_SUCCESS:
                netGetSysMsgList(true);
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
        titleview.setTitle(getString(R.string.im_notify_funchat_request_title));

        adapter = new RecycleAdapterGroupRequest();
        adapter.setOnItemClickListener(this);
        adapter.setOnJoinClickListener((bean, position) -> {
//            NetFriendHelper.newInstance().netAgreeGroupApply(NotifyGroupRequestListActivity.this, bean.getGroupId(), bean.getMsgId(), true, () -> {
//                netGetSysMsgList(true);
//            });
            NetGroupHelper.newInstance().applyAddGroup(NotifyGroupRequestListActivity.this, NotifyGroupRequestListActivity.this, getSupportFragmentManager(), bean.getGroupId(), bean.getMsgId(), true, isNeedValidation -> {
                netGetSysMsgList(true);
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
                netGetSysMsgList(true);
            }

            @Override
            public void onLoadmore() {
                netGetSysMsgList(false);
            }
        });
        springView.callFreshDelay();
    }

    @Override
    public void onItemClick(GroupRequest bean, int position) {
        CommonHelper.ImHelper.startGroupInviteFromMsg(this,
                bean.getGroupId(),
                bean.getMsgId(),
                bean.getPass() == 3);
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetSysMsgList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(NotifyService.class).getGroupJoinRequstList(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<GroupRequest>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<GroupRequest>> basejson) {
                        BaseListWrap<GroupRequest> warp = basejson.getData();
                        List<GroupRequest> list = warp != null ? warp.getList() : null;
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                            loadingviewEx.showOut();
                        } else {
                            if (isFresh) loadingviewEx.showFailViewNoGroup();
                        }
                        NetMsgHelper.newInstance().readGroupJoinMsg();
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
