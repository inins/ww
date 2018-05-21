package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FocusUtil;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.recharge.Recharge;
import com.wang.social.personal.mvp.model.api.UserService;
import com.wang.social.personal.mvp.ui.adapter.RecycleAdapterRecharge;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomPayway;
import com.wang.social.personal.net.helper.NetPayHelper;
import com.wang.social.personal.utils.PayUtil;

import java.util.List;

import butterknife.BindView;

@RouteNode(path = "/recharge", desc = "充值")
public class AccountRechargeActivity extends BasicAppNoDiActivity implements IView, BaseAdapter.OnItemClickListener<Recharge> {

    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.recycler)
    RecyclerView recycler;
    private RecycleAdapterRecharge adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AccountRechargeActivity.class);
        context.startActivity(intent);
    }


    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_account_recharge;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        FocusUtil.focusToTop(toolbar);

        adapter = new RecycleAdapterRecharge();
        adapter.setOnItemClickListener(this);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new GridSpacingItemDecoration(2, SizeUtils.dp2px(15), GridLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);

        springView.setHeader(new AliHeader(springView.getContext(), false));
        springView.setFooter(new AliFooter(springView.getContext(), false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                netGetPayList();
            }

            @Override
            public void onLoadmore() {
                springView.onFinishFreshAndLoadDelay(1000);
            }
        });
        springView.callFreshDelay();
    }

    public void onClick(View v) {

    }

    @Override
    public void onItemClick(Recharge recharge, int position) {
        DialogBottomPayway.showDialog(this, payway -> {
            if (payway == DialogBottomPayway.PAYWAY_ALI) {
                //请求支付参数并发起支付宝支付
                NetPayHelper.newInstance().netPayAli(this, recharge.getId(), recharge.getPrice());
            } else if (payway == DialogBottomPayway.PAYWAY_WX) {
                //请求支付参数并发起微信支付
                NetPayHelper.newInstance().netPayWX(this, recharge.getId(), recharge.getPrice());
            }
        });
    }

    public void netGetPayList() {
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(UserService.class).getPayList(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Recharge>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Recharge>> basejson) {
                        BaseListWrap<Recharge> wrap = basejson.getData();
                        List<Recharge> rechargeList = wrap.getList();
                        if (!StrUtil.isEmpty(rechargeList)) {
                            adapter.refreshData(rechargeList);
                        }
                        springView.onFinishFreshAndLoadDelay();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                        springView.onFinishFreshAndLoadDelay();
                    }
                });
    }
}
