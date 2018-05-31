package com.frame.component.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.component.common.ItemDecorationDivider;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.NetCommonFunshowHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.adapter.RecycleAdapterCommonFunshow;
import com.frame.component.ui.base.BasicNoDiFragment;
import com.frame.component.view.LoadingLayoutEx;
import com.frame.entities.EventBean;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

import butterknife.BindView;

/**
 */

public class MeFunshowFragment extends BasicNoDiFragment {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.spring)
    SpringView springView;
    @BindView(R2.id.loadingview_ex)
    LoadingLayoutEx loadingviewEx;

    private RecycleAdapterCommonFunshow adapter;

    private int groupId;
    private boolean isGroup;

    public static MeFunshowFragment newInstance() {
        Bundle args = new Bundle();
        MeFunshowFragment fragment = new MeFunshowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MeFunshowFragment newInstanceForGroup(int groupId) {
        Bundle args = new Bundle();
        args.putInt("groupId", groupId);
        MeFunshowFragment fragment = new MeFunshowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupId = getArguments().getInt("groupId");
        isGroup = groupId != 0;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.fragment_funshow;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        adapter = new RecycleAdapterCommonFunshow();
        adapter.registEventBus();
        adapter.setShowMoreBtn(false);
        recycler.setNestedScrollingEnabled(false);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new ItemDecorationDivider(getContext()).setLineMargin(15));

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.unRegistEventBus();
    }

    @Override
    public void setData(@Nullable Object data) {
    }


    //////////////////////分页查询////////////////////
    private int current = 1;
    private int size = 20;

    private void netGetFunshowList(boolean isFresh) {
        if (isFresh) current = 0;
        NetCommonFunshowHelper.newInstance().netGetFunshowList(this, false, groupId, current + 1, size,
                isGroup ? NetCommonFunshowHelper.TYPE_GROUP : NetCommonFunshowHelper.TYPE_ME,
                new NetCommonFunshowHelper.OnFunshowBeanCallback() {
                    @Override
                    public void onSuccess(List<FunshowBean> list) {
                        if (!StrUtil.isEmpty(list)) {
                            current++;
                            if (isFresh) {
                                adapter.refreshData(list);
                            } else {
                                adapter.addItem(list);
                            }
                            loadingviewEx.showOut();
                        } else {
                            if (isFresh) loadingviewEx.showFailViewNoFunshow();
                        }
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
