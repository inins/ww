package com.wang.social.home.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.frame.base.BasicFragment;
import com.frame.component.common.NetParam;
import com.frame.component.entities.BaseCardListWrap;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetGroupHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.base.BasicNoDiFragment;
import com.frame.component.utils.viewutils.MotionEventHelper;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;
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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class CardGroupFragment extends BasicNoDiFragment implements RecycleAdapterCardGroup.OnCardGestureListener, View.OnClickListener, IView {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_dislike)
    View btnDislike;
    @BindView(R2.id.btn_like)
    View btnLike;
    @BindView(R2.id.lay_loading)
    View layLoading;

    private RecycleAdapterCardGroup adapter;
    private ItemTouchHelper itemTouchHelper;

    private boolean hasLoad;

    public static CardGroupFragment newInstance() {
        Bundle args = new Bundle();
        CardGroupFragment fragment = new CardGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            //TODO:在详情页加入了该群后需要发出一条通知通知该页面刷新列表到下一条，这里暂时填123
            case 123:
                //在详情页加入了该群
                adapter.nextCard();
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
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
        adapter.setOnCardGestureListener(this);
        recycler.setLayoutManager(new CardLayoutManager());
        recycler.setAdapter(adapter);
        //设置拖拽手势
        final ItemTouchCardCallback callback = new ItemTouchCardCallback(recycler, adapter.getData());
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycler);
        callback.setOnSwipedListener((ItemTouchCardCallback.OnSwipedListener<CardGroup>) (bean, direction) -> {
            //如果是右滑，则提示好友请求
            if (ItemTouchHelper.RIGHT == direction) {
                NetGroupHelper.newInstance().addGroup(getContext(), CardGroupFragment.this, getChildFragmentManager(), bean.getGroupId(), isNeedValidation -> {
                    //ToastUtil.showToastShort("加群成功");
                });
            }
            //如果剩余卡片小于小于10 张，则开始请求下一页数据
            if (adapter.getItemCount() < 10 && !hasLoad) {
                netGetCardGroups(false);
            } else if (adapter.getItemCount() == 0) {
                ToastUtil.showToastLong("没有更多数据了");
            }
        });

        netGetCardGroups(true);
        layoutMeasure();
    }

    @Override
    public void setData(@Nullable Object data) {
    }

    @Override
    public void onClick(View v) {
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();
        int id = v.getId();
        if (id == R.id.btn_dislike) {
            //模拟左滑触摸轨迹
            Point start = new Point(screenWidth * 3 / 4, screenHeight / 2);
            Point end = new Point(200, screenHeight / 2 - 300);
            MotionEventHelper.createTrack(start, end, 300, recycler);
        } else if (id == R.id.btn_like) {
            //模拟右滑触摸轨迹
            Point start = new Point(screenWidth * 1 / 4, screenHeight / 2);
            Point end = new Point(screenWidth - 200, screenHeight / 2 - 300);
            MotionEventHelper.createTrack(start, end, 300, recycler);
        }
    }

    @Override
    public void onItemClick(CardGroup bean, RecycleAdapterCardGroup.Holder holder) {
        CommonHelper.ImHelper.startGroupInviteBrowse(getContext(), bean.getGroupId());
    }

    @Override
    public void onItemScroll(CardGroup bean, RecycleAdapterCardGroup.Holder holder) {
        itemTouchHelper.startSwipe(holder);
    }

    //////////////////////分页查询////////////////////
    private int current = 0;
    private int size = 20;
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
        ApiHelperEx.execute(this, false,
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
                }, () -> {
                    hasLoad = true;
                    layLoading.setVisibility(View.VISIBLE);
                }, () -> {
                    hasLoad = false;
                    layLoading.setVisibility(View.GONE);
                });
    }

    /**
     * 这个页面考虑到card的遮挡和绘制区域问题，recycler是铺满全屏并且位于布局最底部的（card拖拽的时候需要在屏幕任何位置可以绘制）
     * 由于布局特殊，无法通过传统方法通过布局依赖关系进行多屏幕适配，这里不得不自行根据屏幕尺寸计算必要的view的边距
     */
    private void layoutMeasure() {
        getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int screenHeight = ScreenUtils.getScreenHeight();
                int screenWidth = ScreenUtils.getScreenWidth();
                int cardHight = screenWidth - recycler.getPaddingLeft() * 2 + SizeUtils.dp2px(70);
                int wishTopSpace = (screenHeight - cardHight) / 3;
                int topSpace = wishTopSpace > SizeUtils.dp2px(76) ? wishTopSpace : SizeUtils.dp2px(76); //顶部距离不得小于80（会遮挡toolbar）
                int bottomSpace = screenHeight - cardHight - topSpace;
                int btnHight = btnLike.getMeasuredHeight();
                //计算floatingButton底外边距
                int btnBottomMargin = (bottomSpace - btnHight) / 2;
                //设置必要的边距数据
                ((FrameLayout.LayoutParams) btnLike.getLayoutParams()).bottomMargin = btnBottomMargin;
                ((FrameLayout.LayoutParams) btnDislike.getLayoutParams()).bottomMargin = btnBottomMargin;
                recycler.setPadding(recycler.getPaddingLeft(), topSpace, recycler.getPaddingRight(), recycler.getPaddingBottom());
                //移除observer
                getView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }
}
