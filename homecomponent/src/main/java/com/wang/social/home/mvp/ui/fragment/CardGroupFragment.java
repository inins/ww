package com.wang.social.home.mvp.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.frame.base.BasicFragment;
import com.frame.component.common.NetParam;
import com.frame.component.entities.BaseCardListWrap;
import com.frame.component.helper.NetGroupHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.common.CardLayoutManager;
import com.wang.social.home.common.ItemTouchCardCallback;
import com.wang.social.home.mvp.entities.card.CardGroup;
import com.wang.social.home.mvp.model.api.HomeService;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterCardGroup;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class CardGroupFragment extends BasicFragment implements RecycleAdapterCardGroup.OnCardClickListener, View.OnClickListener, IView {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_dislike)
    View btnDislike;
    @BindView(R2.id.btn_like)
    View btnLike;

    private RecycleAdapterCardGroup adapter;

    public static CardGroupFragment newInstance() {
        Bundle args = new Bundle();
        CardGroupFragment fragment = new CardGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.home_fragment_card_user;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnLike.setOnClickListener(this);
        btnDislike.setOnClickListener(this);

        //初始化recycle卡片view
        adapter = new RecycleAdapterCardGroup();
        adapter.setOnCardClickListener(this);
        recycler.setLayoutManager(new CardLayoutManager());
        recycler.setAdapter(adapter);
        //设置拖拽手势
        final ItemTouchCardCallback callback = new ItemTouchCardCallback(recycler, adapter.getData());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycler);
        callback.setOnSwipedListener((ItemTouchCardCallback.OnSwipedListener<CardGroup>) (bean, direction) -> {
            //如果是右滑，则提示好友请求
            if (ItemTouchHelper.RIGHT == direction) {
                NetGroupHelper.newInstance().addGroup(getContext(), CardGroupFragment.this, getChildFragmentManager(), bean.getGroupId(), isNeedValidation -> {
                    ToastUtil.showToastShort("加群成功");
                });
            }
            //如果剩余卡片小于等于5 张，则开始请求下一页数据
            if (adapter.getItemCount() == 5) {
                netGetCardGroups(false);
            } else if (adapter.getItemCount() == 0) {
                ToastUtil.showToastLong("没有更多数据了");
            }
        });

        netGetCardGroups(true);
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dislike) {
            ToastUtil.showToastShort("dislike");
        } else if (id == R.id.btn_like) {
            ToastUtil.showToastShort("like");
        }
    }

    @Override
    public void onItemClick(CardGroup bean, int position, RecycleAdapterCardGroup.Holder holder) {
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
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
    private int size = 6;
    private String orderByField;
    private String strategy;
    private String asc;

    private void netGetCardGroups(boolean needLoading) {
        Map<String, Object> map = NetParam.newInstance()
                .put("current", current + 1)
                .put("size", size)
                .put("orderByField", orderByField)
                .put("strategy", strategy)
                .put("asc", asc)
                .build();
        ApiHelperEx.execute(this, needLoading,
                ApiHelperEx.getService(HomeService.class).getCardGroups(map),
                new ErrorHandleSubscriber<BaseJson<BaseCardListWrap<CardGroup>>>() {
                    @Override
                    public void onNext(BaseJson<BaseCardListWrap<CardGroup>> basejson) {
                        BaseCardListWrap<CardGroup> warp = basejson.getData();
                        List<CardGroup> list = warp.getList();
                        current = warp.getCurrent();
                        orderByField = warp.getOrderByField();
                        strategy = warp.getStrategy();
                        asc = warp.getAsc();

                        if (!StrUtil.isEmpty(list)) {
                            Collections.reverse(list);
                            List<CardGroup> results = adapter.getData();
                            results.addAll(0, list);
                            adapter.notifyDataSetChanged();
                        } else {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
