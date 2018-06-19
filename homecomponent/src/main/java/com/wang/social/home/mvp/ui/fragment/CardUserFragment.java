package com.wang.social.home.mvp.ui.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.frame.base.BasicFragment;
import com.frame.component.common.NetParam;
import com.frame.component.entities.BaseCardListWrap;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.NetFriendHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.base.BasicNoDiFragment;
import com.frame.component.ui.dialog.DialogValiRequest;
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
import com.wang.social.home.helper.CatchHelper;
import com.wang.social.home.mvp.entities.card.CardUser;
import com.wang.social.home.mvp.model.api.HomeService;
import com.wang.social.home.mvp.ui.activity.CardDetailActivity;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterCardUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 建设中 fragment 占位
 */

public class CardUserFragment extends BasicNoDiFragment implements RecycleAdapterCardUser.OnCardGestureListener, View.OnClickListener, IView {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_dislike)
    View btnDislike;
    @BindView(R2.id.btn_like)
    View btnLike;
    @BindView(R2.id.lay_loading)
    View layLoading;


    private RecycleAdapterCardUser adapter;
    private ItemTouchHelper itemTouchHelper;

    private Integer gender = -1;
    private String age = "all";
    private boolean hasLoad;

    public static CardUserFragment newInstance() {
        Bundle args = new Bundle();
        CardUserFragment fragment = new CardUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_HOME_CARD_GENDER_SELECT:
                gender = (Integer) event.get("gender");
                netGetCardUsers(true, true);
                break;
            case EventBean.EVENT_HOME_CARD_AGE_SELECT:
                age = (String) event.get("age");
                netGetCardUsers(true, true);
                break;
            case EventBean.EVENT_HOME_CARD_DETAIL_ADDFIREND:
                //在详情页进行了添加好友操作通知卡牌页面展示下一个用户
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
        adapter = new RecycleAdapterCardUser();
        adapter.setOnCardGestureListener(this);
        recycler.setLayoutManager(new CardLayoutManager());
        recycler.setAdapter(adapter);
        //设置拖拽手势
        final ItemTouchCardCallback callback = new ItemTouchCardCallback(recycler, adapter.getData());
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycler);
        callback.setOnSwipedListener((ItemTouchCardCallback.OnSwipedListener<CardUser>) (bean, direction) -> {
            //如果是右滑，则提示好友请求
            if (ItemTouchHelper.RIGHT == direction) {
                DialogValiRequest.showDialog(getContext(), content -> {
                    NetFriendHelper.newInstance().netSendFriendlyApply(CardUserFragment.this, bean.getUserId(), content, () -> {
                        ToastUtil.showToastShort("请求已发送");
                    });
                });
            }
            //如果剩余卡片小于小于10 张，则开始请求下一页数据
            if (adapter.getItemCount() < 10 && !hasLoad) {
                netGetCardUsers(false, false);
            } else if (adapter.getItemCount() == 0) {
                ToastUtil.showToastLong("没有更多数据了");
            }
        });

        //先加载本地缓存
        adapter.refreshData(CatchHelper.getCardUser());
        //如果缓存数据<20条则加载网络数据
        if (adapter.getItemCount() < 20) {
            netGetCardUsers(false, false);
        }
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
            Point start = new Point(screenWidth * 4 / 5, screenHeight / 2);
            Point end = new Point(screenWidth * 1 / 5, screenHeight / 2 - 300);
            MotionEventHelper.createTrack(start, end, 300, recycler);
        } else if (id == R.id.btn_like) {
            //模拟右滑触摸轨迹
            Point start = new Point(screenWidth * 1 / 5, screenHeight / 2);
            Point end = new Point(screenWidth * 4 / 5, screenHeight / 2 - 300);
            MotionEventHelper.createTrack(start, end, 300, recycler);
        }
    }

    @Override
    public void onItemClick(CardUser bean, RecycleAdapterCardUser.Holder holder) {
        //FIXME:这里本身有个转场动画（共享元素，类似探探），但是由于需求决定列表大图显示头像而详情大图显示相册第一张（？无法理解），ImageView内容不同导致动画生硬。注释掉动画相关代码直接启动activity
        CardDetailActivity.start(getContext(), bean);
        /**
         Intent intent = new Intent(getContext(), CardDetailActivity.class);
         intent.putExtra("userId", bean.getUserId());
         intent.putExtra("cardUser", bean);
         ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
         Pair.create(holder.imgPic, "share_img"),
         Pair.create(holder.textName, "share_name"),
         Pair.create(holder.textLableGender, "share_gender"),
         Pair.create(holder.textLableAstro, "share_astro")
         //Pair.create(holder.layBoard, "share_board")
         //Pair.create(holder.itemView, "share_root")
         //Pair.create(holder.textPosition, "share_position"),
         //Pair.create(holder.textTag, "share_lable")
         );
         startActivity(intent, options.toBundle());
         */
    }

    @Override
    public void onItemScroll(CardUser bean, RecycleAdapterCardUser.Holder holder) {
        itemTouchHelper.startSwipe(holder);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //加入本地缓存
        CatchHelper.saveCardUser(adapter.getData());
    }

    //////////////////////分页查询////////////////////
    private int current = 0;
    private int size = 20;
    private String orderByField;
    private String strategy;
    private String asc;

    private void netGetCardUsers(boolean needLoading, boolean isFresh) {
        if (isFresh) {
            //刷新的时候，重置搜索条件
            current = 0;
            orderByField = null;
            strategy = null;
            asc = null;
        }
        Map<String, Object> map = NetParam.newInstance()
                .put("sex", gender)
                .put("ageRange", age)
                .put("current", current + 1)
                .put("size", size)
                .put("orderByField", orderByField)
                .put("strategy", strategy)
                .put("asc", asc)
                .build();
        ApiHelperEx.execute(this, false,
                ApiHelperEx.getService(HomeService.class).getCardUsers(map),
                new ErrorHandleSubscriber<BaseJson<BaseCardListWrap<CardUser>>>() {
                    @Override
                    public void onNext(BaseJson<BaseCardListWrap<CardUser>> basejson) {
                        BaseCardListWrap<CardUser> warp = basejson.getData();
                        List<CardUser> list = warp.getList();
                        current = warp.getCurrent();
                        orderByField = warp.getOrderByField();
                        strategy = warp.getStrategy();
                        asc = warp.getAsc();

                        if (!StrUtil.isEmpty(list)) {
                            Collections.reverse(list);
                            List<CardUser> results = adapter.getData();
                            if (isFresh) adapter.getData().clear();
                            results.addAll(0, list);
                            adapter.notifyDataSetChanged();
                        } else {
                            if (isFresh) {
                                adapter.getData().clear();
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, () -> {
                    hasLoad = true;
                    if (needLoading && layLoading != null) layLoading.setVisibility(View.VISIBLE);
                }, () -> {
                    hasLoad = false;
                    if (needLoading && layLoading != null) layLoading.setVisibility(View.GONE);
                });
    }

    ///////////////////

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
