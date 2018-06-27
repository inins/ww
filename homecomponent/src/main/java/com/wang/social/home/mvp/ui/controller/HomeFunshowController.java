package com.wang.social.home.mvp.ui.controller;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.ui.adapter.RecycleAdapterCommonFunshow;
import com.frame.component.ui.base.BaseController;
import com.frame.component.utils.ListUtil;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.helper.CatchHelper;
import com.wang.social.home.mvp.entities.funshow.FunshowHome;
import com.wang.social.home.mvp.model.api.HomeService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class HomeFunshowController extends BaseController {
    @BindView(R2.id.recycler_funshow)
    RecyclerView recycler;
    @BindView(R2.id.btn_funshow_more)
    TextView btnFunshowMore;

    private RecycleAdapterCommonFunshow adapter;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            //新增一条趣晒，收到通知刷新列表
            case EventBean.EVENT_FUNSHOW_ADD:
                //在详情页被删除了，收到通知刷新列表
            case EventBean.EVENT_FUNSHOW_DEL:
                //在详情页不喜欢，收到通知刷新列表
            case EventBean.EVENT_FUNSHOW_DISSLIKE:
                refreshData();
                break;
        }
    }

    public HomeFunshowController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.home_lay_funshow;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        adapter = new RecycleAdapterCommonFunshow();
        adapter.registEventBus();
        adapter.setOnDislikeClickListener((v, funshow) -> netShatDownUser(funshow.getUserId()));
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

        btnFunshowMore.setOnClickListener(view -> {
            //切换到广场-趣晒
            EventBus.getDefault().post(new EventBean(EventBean.EVENT_CHANGE_TAB_PLAZA));
            EventBus.getDefault().post(new EventBean(EventBean.EVENT_CHANGE_TAB_PLAZA_FUNSHOW));
        });
    }

    @Override
    protected void onInitData() {
        //加载缓存
        adapter.refreshData(CatchHelper.getFunshowHome());
        netGetNewFunshow(false);
    }

    @Override
    public void onDestory() {
        super.onDestory();
        adapter.unRegistEventBus();
    }

    public void refreshData() {
        netGetNewFunshow(false);
    }

    public void netShatDownUser(int userId) {
        if (userId == AppDataHelper.getUser().getUserId()) {
            ToastUtil.showToastShort("不能屏蔽自己");
            return;
        }
        ApiHelperEx.execute(getIView(), true,
                ApiHelperEx.getService(CommonService.class).shatDownUser(userId + "", 1),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        netGetNewFunshow(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public void netGetNewFunshow(boolean needLoading) {
        ApiHelperEx.execute(getIView(), needLoading,
                ApiHelperEx.getService(HomeService.class).getNewFunshow(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowHome>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowHome>> basejson) {
                        BaseListWrap<FunshowHome> wrap = basejson.getData();
                        List<FunshowHome> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            List<FunshowBean> funshowBeans = FunshowHome.tans2FunshowBeanList(list);
                            //只加载前5条
                            funshowBeans = ListUtil.getFirst(funshowBeans, 5);
                            //加入缓存
                            CatchHelper.saveFunshowHome(funshowBeans);
                            adapter.refreshData(funshowBeans);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, () -> {
                    if (needLoading) getLoadingLayout().showLoadingView();
                }, () -> {
                    if (needLoading) getLoadingLayout().showOut();
                });
    }
}
