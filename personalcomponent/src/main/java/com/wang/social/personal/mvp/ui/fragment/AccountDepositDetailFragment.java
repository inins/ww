package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerFragmentComponent;
import com.wang.social.personal.mvp.entities.income.DiamondStoneIncome;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterDepositDetail;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * ========================================
 * 钻石 宝石 收支明细
 * ========================================
 */

public class AccountDepositDetailFragment extends BasicFragment implements IView {

    private int position;
    private int type;

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;
    private RecycleAdapterDepositDetail adapter;

    public static AccountDepositDetailFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        AccountDepositDetailFragment fragment = new AccountDepositDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_DEPOSITDETAIL_SORT:
                int position = (int) event.get("position");
                int type = (int) event.get("type");
                if (position == this.position) {
                    this.type = type;
                    netDiamondIncomeList(true, true);
                }
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.personal_fragment_account_deposit_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        position = getArguments().getInt("position");
        adapter = new RecycleAdapterDepositDetail();
        adapter.setDiamond(position == 0);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));
        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netDiamondIncomeList(true, false);
            }

            @Override
            public void onLoadmore() {
                netDiamondIncomeList(false, false);
            }
        });
        springView.callFreshDelay();
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerFragmentComponent.builder()
                .appComponent(appComponent)
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

    //////////////////////分页查询////////////////////
    private int current = 0;
    private int size = 20;

    public void netDiamondIncomeList(boolean isReFresh, boolean needloading) {
        if (isReFresh) current = 0;
        Observable<BaseJson<BaseListWrap<DiamondStoneIncome>>> call;
        if (adapter.isDiamond()) {
            call = ApiHelperEx.getService(UserService.class).diamondIncomeList(type, current + 1, size);
        } else {
            call = ApiHelperEx.getService(UserService.class).toneIncomeList(type, current + 1, size);
        }
        ApiHelperEx.execute(this, needloading, call,
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<DiamondStoneIncome>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<DiamondStoneIncome>> basejson) {
                        BaseListWrap<DiamondStoneIncome> wrap = basejson.getData();
                        List<DiamondStoneIncome> list = wrap != null ? wrap.getList() : null;
                        if (!StrUtil.isEmpty(list)) {
                            current = wrap.getCurrent();
                            if (isReFresh) adapter.refreshData(list);
                            else adapter.addItem(list);
                            loadingviewEx.showOut();
                        } else {
                            if (isReFresh) loadingviewEx.showFailViewNoData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        if (isReFresh) loadingviewEx.showFailViewNoNet();
                    }
                }, null, () -> springView.onFinishFreshAndLoadDelay());
    }
}
