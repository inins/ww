package com.wang.social.funshow.mvp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.NetLoginTestHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.view.barview.BarUser;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.di.component.DaggerFunShowListFragmentComponent;
import com.wang.social.funshow.di.module.FunshowListModule;
import com.wang.social.funshow.mvp.contract.FunshowListContract;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.wang.social.funshow.mvp.entities.user.TopUser;
import com.wang.social.funshow.mvp.presonter.FunshowListPresonter;
import com.wang.social.funshow.mvp.ui.activity.FunshowAddActivity;
import com.wang.social.funshow.mvp.ui.activity.FunshowDetailActivity;
import com.wang.social.funshow.mvp.ui.activity.HotUserListActivity;
import com.wang.social.funshow.mvp.ui.adapter.RecycleAdapterHome;
import com.wang.social.funshow.mvp.ui.dialog.DialogSureFunshowPay;
import com.wang.social.funshow.utils.FunShowUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 */
public class FunShowFragment extends BaseFragment<FunshowListPresonter> implements FunshowListContract.View, BaseAdapter.OnItemClickListener<Funshow> {

    @BindView(R2.id.barview)
    BarView barview;
    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler_content)
    RecyclerView recycler;

    private RecycleAdapterHome adapter;

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
            case EventBean.EVENT_FUNSHOW_UPDATE_ZAN: {
                int talkId = (int) event.get("talkId");
                boolean isZan = (boolean) event.get("isZan");
                int zanCount = (int) event.get("zanCount");
                adapter.refreshZanById(talkId, isZan, zanCount);
                break;
            }
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_EVA: {
                int talkId = (int) event.get("talkId");
                adapter.refreshCommentById(talkId);
                break;
            }
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.funshow_fragment;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterHome();
        adapter.setOnItemClickListener(this);
        adapter.setOnDislikeClickListener((v, funshow) -> {
            if (funshow.getUserId() == AppDataHelper.getUser().getUserId()) {
                ToastUtil.showToastShort("不能屏蔽自己");
            } else {
                mPresenter.shatDownUser(funshow.getUserId());
            }
        });
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

        mPresenter.netGetFunshowList(type, false);
        mPresenter.netGetFunshowTopUserList();

        //测试跳转代码
        getView().findViewById(R.id.btn_funshow_add).setOnClickListener(v -> FunshowAddActivity.start(getContext()));
        getView().findViewById(R.id.btn_funshow_login).setOnClickListener(v -> NetLoginTestHelper.newInstance().loginTest());
    }

    @Override
    public void onItemClick(Funshow funshow, int position) {
        if (funshow.isShopping()) {
            DialogSureFunshowPay.showDialog(getContext(), funshow.getPrice(), () -> {
                NetPayStoneHelper.newInstance().netPayFunshow(FunShowFragment.this, funshow.getTalkId(), funshow.getPrice(), () -> {
                    FunshowDetailActivity.start(getContext(), funshow.getTalkId());
                    adapter.refreshNeedPayById(funshow.getTalkId());
                });
            });
        } else {
            FunshowDetailActivity.start(getContext(), funshow.getTalkId());
        }
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
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void callRefresh() {
        springView.callFresh();
    }

    @Override
    public void reFreshList(List<Funshow> datas) {
        adapter.refreshData(datas);
    }

    @Override
    public void appendList(List<Funshow> datas) {
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
}
