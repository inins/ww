package com.wang.social.home.mvp.ui.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetReadHelper;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.component.view.DialogPay;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.contract.HomeContract;
import com.wang.social.home.mvp.entities.FunpointAndTopic;
import com.wang.social.home.mvp.entities.topic.TopicHome;
import com.wang.social.home.mvp.model.api.HomeService;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterHome;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeContentController extends BaseController implements RecycleAdapterHome.OnFunpointClickListener, RecycleAdapterHome.OnTopicClickListener {

    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.recycler)
    RecyclerView recycler;

    private RecycleAdapterHome adapter;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case 123: {
                //TODO:目前没有这个消息，后续添加
                //在详情页点赞，收到通知刷新点赞状态及其点赞数量
                int topicId = (int) event.get("topicId");
                boolean isZan = (boolean) event.get("isZan");
                adapter.reFreshZanCountById(topicId, isZan);
                break;
            }
            case 1234: {
                //TODO:目前没有这个消息，后续添加
                //在详情页评论，收到通知刷新评论数量
                int topicId = (int) event.get("topicId");
                adapter.reFreshEvaCountById(topicId);
                break;
            }
        }
    }

    public HomeContentController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.home_lay_content;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        FontUtils.boldText(textTitle);

        adapter = new RecycleAdapterHome();
        adapter.setOnFunpointClickListener(this);
        adapter.setOnTopicClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new ItemDecorationDivider(getContext(), LinearLayoutManager.VERTICAL));
        recycler.setAdapter(adapter);
    }

    @Override
    protected void onInitData() {
        netGetFunpointAndTopicList(true);
    }

    @Override
    public void onFunpointClick(int position, Funpoint funpoint) {
        WebActivity.start(getContext(), funpoint.getUrl());
        //增加阅读数量
        NetReadHelper.newInstance().netReadFunpoint(funpoint.getNewsId(), () -> {
            funpoint.setReadTotal(funpoint.getReadTotal() + 1);
            adapter.notifyItemChanged(position);
        });
    }

    @Override
    public void onTopicClick(int position, TopicHome topic) {
        // 是否需要支付
        if (!topic.isFree() && !topic.isPay()) {
            // 处理支付
            DialogPay.showPayTopic(getIView(), getFragmentManager(), topic.getPrice(), -1, () -> {
                NetPayStoneHelper.newInstance().netPayTopic(getIView(), topic.getTopicId(), topic.getPrice(), () -> {
                    topic.setIsPay(true);
                    CommonHelper.TopicHelper.startTopicDetail(getContext(), topic.getTopicId());
                });
            });
        } else {
            CommonHelper.TopicHelper.startTopicDetail(getContext(), topic.getTopicId());
        }
    }

    ////////////////////////////////////////////////

    public void refreshData() {
        netGetFunpointAndTopicList(true);
    }

    public void loadmoreData() {
        netGetFunpointAndTopicList(false);
    }

    ////////////////////////////////////////////////

    private int current = 1;
    private int size = 10;

    public void netGetFunpointAndTopicList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(HomeService.class).getFunpointAndTopic(1, current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<JsonObject>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<JsonObject>> basejson) {
                        BaseListWrap<JsonObject> warp = basejson.getData();
                        List<JsonObject> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            //封装实体
                            List<FunpointAndTopic> ftList = new ArrayList<>();
                            for (JsonObject jsonObject : list) {
                                if (jsonObject.has("newsId")) {
                                    ftList.add(new FunpointAndTopic(new Gson().fromJson(jsonObject, Funpoint.class)));
                                } else if (jsonObject.has("topicId")) {
                                    ftList.add(new FunpointAndTopic(new Gson().fromJson(jsonObject, TopicHome.class)));
                                }
                            }
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshData(ftList);
                            } else {
                                adapter.addItem(ftList);
                            }
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                        if (getIView() != null && getIView() instanceof HomeContract.View) {
                            ((HomeContract.View) getIView()).finishSpringView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        if (getIView() != null && getIView() instanceof HomeContract.View) {
                            ((HomeContract.View) getIView()).finishSpringView();
                        }
                    }
                });
    }
}
