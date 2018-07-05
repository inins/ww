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
import com.frame.component.helper.NetIsShoppingHelper;
import com.frame.component.helper.NetMsgHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.path.ImPath;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.view.DialogPay;
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
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.api.NotifyService;
import com.wang.social.im.mvp.model.entities.notify.CommonMsg;
import com.wang.social.im.mvp.model.entities.notify.EvaMsg;
import com.wang.social.im.mvp.model.entities.notify.ZanMsg;
import com.wang.social.im.mvp.ui.adapters.RecycleAdapterCommonMsg;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

@RouteNode(path = ImPath.NOTIFY_COMMENT_LIST, desc = "评论列表")
public class NotifyEvaListActivity extends BasicAppNoDiActivity implements IView, BaseAdapter.OnItemClickListener<CommonMsg> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.titleview)
    TitleView titleview;
    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;
    private RecycleAdapterCommonMsg adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, NotifyEvaListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_common_title_recycler;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);
        titleview.setTitle(getString(R.string.im_notify_eva_title));

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
            //检查是否需要支付，如果需要则先支付后进入详情
            NetIsShoppingHelper.newInstance().isTalkShopping(this, bean.getMainId(), rsp -> {
                if (!rsp.isFree() && !rsp.isPay()) {
                    DialogPay.showPayFunshow(NotifyEvaListActivity.this, getSupportFragmentManager(), rsp.getPrice(), -1, () -> {
                        NetPayStoneHelper.newInstance().netPayFunshow(NotifyEvaListActivity.this, bean.getMainId(), rsp.getPrice(), () -> {
                            CommonHelper.FunshowHelper.startDetailActivity(this, bean.getModePkId());
                            EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_PAYED).put("talkId", bean.getMainId()));
                        });
                    });
                } else {
                    CommonHelper.FunshowHelper.startDetailActivity(this, bean.getModePkId());
                }
            });
        } else if (bean.isTopic()) {
            //检查是否需要支付，如果需要则先支付后进入详情
            NetIsShoppingHelper.newInstance().isTopicShopping(this, bean.getMainId(), rsp -> {
                if (!rsp.isFree() && !rsp.isPay()) {
                    DialogPay.showPayTopic(NotifyEvaListActivity.this, getSupportFragmentManager(), rsp.getPrice(), -1, () -> {
                        NetPayStoneHelper.newInstance().netPayTopic(NotifyEvaListActivity.this, bean.getMainId(), rsp.getPrice(), () -> {
                            CommonHelper.TopicHelper.startTopicDetail(this, bean.getMainId());
                        });
                    });
                } else {
                    CommonHelper.TopicHelper.startTopicDetail(this, bean.getMainId());
                }
            });
        }
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetSysMsgList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(NotifyService.class).getEvaMsgList(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<EvaMsg>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<EvaMsg>> basejson) {
                        BaseListWrap<EvaMsg> warp = basejson.getData();
                        List<CommonMsg> list = EvaMsg.tans2CommonMsgList(warp.getList());
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                            loadingviewEx.showOut();
                        } else {
                            if (isFresh) loadingviewEx.showFailViewNoComment();
                        }
                        NetMsgHelper.newInstance().readEvaMsg();
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
