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
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.view.TitleView;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.api.NotifyService;
import com.wang.social.im.mvp.model.entities.notify.CommonMsg;
import com.wang.social.im.mvp.model.entities.notify.ZanMsg;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterCommonMsg;

import java.util.List;

import butterknife.BindView;

public class NotifyZanListActivity extends BasicAppNoDiActivity implements IView, BaseAdapter.OnItemClickListener<CommonMsg> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.titleview)
    TitleView titleview;
    private RecycleAdapterCommonMsg adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NotifyZanListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_common_title_recycler;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        titleview.setTitle(getString(R.string.im_notify_zan_title));

        adapter = new RecycleAdapterCommonMsg();
        adapter.setOnItemClickListener(this);
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
    public void onItemClick(CommonMsg bean, int position) {
        if (bean.isFunshow()) {
            CommonHelper.FunshowHelper.startDetailActivity(this, bean.getModeId());
        } else if (bean.isTopic()) {
            CommonHelper.TopicHelper.startTopicDetail(this, bean.getModeId());
        }
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetSysMsgList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(NotifyService.class).getZanMsgList(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<ZanMsg>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<ZanMsg>> basejson) {
                        BaseListWrap<ZanMsg> warp = basejson.getData();
                        List<CommonMsg> list = ZanMsg.tans2CommonMsgList(warp.getList());
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
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
