package com.wang.social.funshow.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.helper.NetLoginTestHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetShareHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.barview.BarView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
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
import com.wang.social.socialize.SocializeUtil;

import org.greenrobot.eventbus.EventBus;

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
                //在详情页点赞，收到通知刷新点赞状态及其点赞数量
                int talkId = (int) event.get("talkId");
                boolean isZan = (boolean) event.get("isZan");
                int zanCount = (int) event.get("zanCount");
                adapter.refreshZanById(talkId, isZan, zanCount);
                break;
            }
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_EVA: {
                //在详情页评论，收到通知刷新评论数量
                int talkId = (int) event.get("talkId");
                adapter.refreshCommentById(talkId);
                break;
            }
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_SHARE: {
                //在详情页分享，收到通知刷新分享数量
                int talkId = (int) event.get("talkId");
                adapter.refreshShareById(talkId);
                break;
            }
            case EventBean.EVENT_FUNSHOW_DISSLIKE: {
                //在详情页不喜欢，收到通知刷新列表
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
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterHome();
        adapter.setOnItemClickListener(this);
        adapter.setOnDislikeClickListener((v, funshow) -> mPresenter.shatDownUser(funshow.getUserId()));
        adapter.setOnShareClickListener((v, funshow) -> {
            SocializeUtil.shareWeb(getChildFragmentManager(),
                    new SocializeUtil.SimpleShareListener() {
                        @Override
                        public void onResult(int platform) {
                            adapter.refreshShareById(funshow.getTalkId());
                            NetShareHelper.newInstance().netShareFunshow(null, null, funshow.getTalkId(), null);
                        }
                    },
                    "http://www.wangsocial.com/",
                    "往往",
                    "有点2的社交软件",
                    "http://resouce.dongdongwedding.com/activity_cashcow_moneyTree.png");
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
        getView().findViewById(R.id.btn_funshow_type).setOnClickListener(v -> {
            int typein = type == 0 ? 1 : 0;
            EventBean eventBean = new EventBean(EventBean.EVENT_FUNSHOW_LIST_TYPE_CHANGE);
            eventBean.put("type", typein);
            EventBus.getDefault().post(eventBean);
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
