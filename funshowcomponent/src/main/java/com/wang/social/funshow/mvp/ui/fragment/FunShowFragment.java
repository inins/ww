package com.wang.social.funshow.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.model.GuidePage;
import com.frame.base.BaseFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.GuidePageHelper;
import com.frame.component.ui.adapter.RecycleAdapterCommonFunshow;
import com.frame.component.ui.base.BaseLazyFragment;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerFunShowListFragmentComponent;
import com.wang.social.funshow.di.module.FunshowListModule;
import com.wang.social.funshow.mvp.contract.FunshowListContract;
import com.wang.social.funshow.mvp.entities.user.TopUser;
import com.wang.social.funshow.mvp.presonter.FunshowListPresonter;
import com.wang.social.funshow.mvp.ui.activity.HotUserListActivity;
import com.wang.social.location.mvp.helper.LocationHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.hubert.guide.model.HighLight.Shape.CIRCLE;
import static com.app.hubert.guide.model.HighLight.Shape.ROUND_RECTANGLE;

/**
 */
public class FunShowFragment extends BaseLazyFragment<FunshowListPresonter> implements FunshowListContract.View {

    @BindView(R2.id.barview)
    BarView barview;
    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler_content)
    RecyclerView recycler;

    private RecycleAdapterCommonFunshow adapter;

    private int type = 0;

    public static FunShowFragment newInstance() {
        Bundle args = new Bundle();
        FunShowFragment fragment = new FunShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            //新增一条趣晒，收到通知刷新列表
            case EventBean.EVENT_FUNSHOW_ADD:
                //在详情页被删除了，收到通知刷新列表
            case EventBean.EVENT_FUNSHOW_DEL:
                //在详情页不喜欢，收到通知刷新列表
            case EventBean.EVENT_FUNSHOW_DISSLIKE: {
                springView.callFreshDelay();
                break;
            }
            case EventBean.EVENT_FUNSHOW_LIST_TYPE_CHANGE: {
                //切换佬友筛选条件
                type = (int) event.get("type");
                mPresenter.netGetFunshowList(type, true);
                break;
            }
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funshow_fragment;
    }

    @Override
    public void initDataLazy() {
        adapter = new RecycleAdapterCommonFunshow();
        adapter.registEventBus();
        adapter.setOnDislikeClickListener((v, funshow) -> mPresenter.shatDownUser(funshow.getUserId()));
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(SizeUtils.dp2px(15)));
        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.netGetFunshowList(type, false);
            }

            @Override
            public void onLoadmore() {
                mPresenter.netLoadmore(type);
            }
        });

        mPresenter.netGetFunshowList(type, true);
        mPresenter.netGetFunshowTopUserList();

        NewbieGuide.with(this)
                .setLabel("guide_funshow")
                .addGuidePage(GuidePage.newInstance()
                        .addHighLight(barview, ROUND_RECTANGLE, SizeUtils.dp2px(25), 0)
                        .addHighLight(getActivity().findViewById(R.id.img_add), CIRCLE)
                        .setLayoutRes(R.layout.lay_guide_funshow, R.id.btn_go)
                        .setEverywhereCancelable(false)
                        .setEnterAnimation(GuidePageHelper.getEnterAnimation())
                        .setExitAnimation(GuidePageHelper.getExitAnimation()))
                .show();
        FocusUtil.focusToTop(springView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.unRegistEventBus();
    }

    @OnClick({R2.id.barview})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.barview) {
            HotUserListActivity.start(getContext());
        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFunShowListFragmentComponent.builder()
                .appComponent(appComponent)
                .funshowListModule(new FunshowListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void showLoading() {
        if (getActivity() instanceof BasicAppActivity) {
            ((BasicAppActivity) getActivity()).showLoadingDialog();
        }
    }

    @Override
    public void hideLoading() {
        if (getActivity() instanceof BasicAppActivity) {
            ((BasicAppActivity) getActivity()).dismissLoadingDialog();
        }
    }

    @Override
    public void callRefresh() {
        springView.callFresh();
    }

    @Override
    public void reFreshList(List<FunshowBean> datas) {
        adapter.refreshData(datas);
    }

    @Override
    public void appendList(List<FunshowBean> datas) {
        adapter.addItem(datas);

    }

    @Override
    public void finishSpringView() {
        springView.onFinishFreshAndLoadDelay();
    }

    @Override
    public void reFreshTopUsers(List<TopUser> topUsers) {
        barview.refreshData(TopUser.topUsers2BarUsers(topUsers));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
