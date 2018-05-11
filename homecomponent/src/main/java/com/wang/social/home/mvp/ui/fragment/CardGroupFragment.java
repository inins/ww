package com.wang.social.home.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.frame.base.BasicFragment;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.dialog.DialogValiRequest;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
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
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.model.api.HomeService;
import com.wang.social.home.mvp.ui.activity.CardDetailActivity;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterCardGroup;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterCardUser;

import java.util.List;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class CardGroupFragment extends BasicFragment implements RecycleAdapterCardGroup.OnCardClickListener, View.OnClickListener, IView {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.btn_dislike)
    View btnDislike;
    @BindView(R.id.btn_like)
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
            if (ItemTouchHelper.RIGHT == direction)
                DialogValiRequest.showDialog(getContext(), content -> {
                    ToastUtil.showToastShort(content);
                });
        });

        netGetCardUsers(true);
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
    private int current = 1;

    private int size = 20;

    private void netGetCardUsers(boolean isFresh) {
        if (isFresh) current = 0;
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(HomeService.class).getCardGroups(current + 1, size),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<CardGroup>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<CardGroup>> basejson) {
                        BaseListWrap<CardGroup> warp = basejson.getData();
                        List<CardGroup> list = warp.getList();
                        if (!StrUtil.isEmpty(list)) {
                            current = warp.getCurrent();
                            if (isFresh) {
                                adapter.refreshItem(list);
                            } else {
                                adapter.addItem(list);
                            }
                        } else {
                            ToastUtil.showToastLong("没有更多数据了");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
