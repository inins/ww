package com.wang.social.personal.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.ShatDownUser;
import com.frame.component.entities.TestEntity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.BaseListJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.di.component.DaggerFragmentComponent;
import com.wang.social.personal.di.module.UserModule;
import com.wang.social.personal.mvp.entities.income.DiamondStoneIncome;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterDepositDetail;

import java.util.ArrayList;
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

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    private RecycleAdapterDepositDetail adapter;

    public static AccountDepositDetailFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt("position", position);
        AccountDepositDetailFragment fragment = new AccountDepositDetailFragment();
        fragment.setArguments(args);
        return fragment;
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
                netDiamondIncomeList(true);
            }

            @Override
            public void onLoadmore() {
                netDiamondIncomeList(false);
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
    }

    @Override
    public void hideLoading() {
    }

    //////////////////////分页查询////////////////////
    private int current = 0;
    private int size = 20;

    public void netDiamondIncomeList(boolean isReFresh) {
        Observable<BaseJson<BaseListWrap<DiamondStoneIncome>>> call;
        if (adapter.isDiamond()) {
            call = ApiHelperEx.getService(UserService.class).diamondIncomeList(0, ++current, size);
        } else {
            call = ApiHelperEx.getService(UserService.class).toneIncomeList(0, ++current, size);
        }

        if (isReFresh) current = 0;
        ApiHelperEx.execute(this, false, call,
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<DiamondStoneIncome>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<DiamondStoneIncome>> basejson) {
                        BaseListWrap<DiamondStoneIncome> wrap = basejson.getData();
                        List<DiamondStoneIncome> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            if (isReFresh) adapter.refreshData(list);
                            else adapter.addItem(list);
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, null, () -> springView.onFinishFreshAndLoadDelay());
    }
}
