package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.view.LoadingLayout;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.FocusUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.deposit.DepositRecord;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterDepositRecord;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountDepositRecordActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.text_amount_total)
    TextView textAmountTotal;
    @BindView(R2.id.loadingLayout)
    LoadingLayout loadingLayout;
    private RecycleAdapterDepositRecord adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AccountDepositRecordActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_account_deposit_record;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterDepositRecord();
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(this).setLineMargin(15));

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetFunshowList(true);
            }

            @Override
            public void onLoadmore() {
                netGetFunshowList(false);
            }
        });
        springView.callFreshDelay();
    }

    private void setData(float amountTotal) {
        textAmountTotal.setText(String.valueOf(amountTotal));
    }

    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetFunshowList(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(UserService.class).depositRecode(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<DepositRecord>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<DepositRecord>> basejson) {
                        BaseListWrap<DepositRecord> warp = basejson.getData();
                        setData(warp.getTotalMoney());
                        List<DepositRecord> list = warp.getList();
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
                        if (adapter.getItemCount() != 0) {
                            loadingLayout.showOut();
                        } else {
                            loadingLayout.showLackView();
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                        loadingLayout.showFailView();
                    }
                }, () -> {
                    if (isFresh) loadingLayout.showLoadingView();
                }, null);
    }
}
