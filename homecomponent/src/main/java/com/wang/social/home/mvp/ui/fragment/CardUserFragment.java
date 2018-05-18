package com.wang.social.home.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.frame.base.BasicFragment;
import com.frame.component.common.NetParam;
import com.frame.component.entities.BaseCardListWrap;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.NetFriendHelper;
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

public class CardUserFragment extends BasicFragment implements RecycleAdapterCardUser.OnCardClickListener, View.OnClickListener, IView {

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.btn_dislike)
    View btnDislike;
    @BindView(R2.id.btn_like)
    View btnLike;

    private RecycleAdapterCardUser adapter;

    private Integer gender = -1;
    private String age = "all";

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
                netGetCardUsers(true);
                break;
            case EventBean.EVENT_HOME_CARD_AGE_SELECT:
                age = (String) event.get("age");
                netGetCardUsers(true);
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
        adapter.setOnCardClickListener(this);
        recycler.setLayoutManager(new CardLayoutManager());
        recycler.setAdapter(adapter);
        //设置拖拽手势
        final ItemTouchCardCallback callback = new ItemTouchCardCallback(recycler, adapter.getData());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
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
            //如果剩余卡片小于等于5 张，则开始请求下一页数据
            if (adapter.getItemCount() == 5) {
                netGetCardUsers(false);
            } else if (adapter.getItemCount() == 0) {
                ToastUtil.showToastLong("没有更多数据了");
            }
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
    public void onItemClick(CardUser bean, int position, RecycleAdapterCardUser.Holder holder) {
        Intent intent = new Intent(getContext(), CardDetailActivity.class);
        intent.putExtra("userId", bean.getUserId());
        intent.putExtra("cardUser", bean);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                Pair.create(holder.imgPic, "share_img"),
                Pair.create(holder.textName, "share_name"),
                Pair.create(holder.textLableGender, "share_gender"),
                Pair.create(holder.textLableAstro, "share_astro")
//                Pair.create(holder.layBoard, "share_board")
//                Pair.create(holder.itemView, "share_root")
//                Pair.create(holder.textPosition, "share_position"),
//                Pair.create(holder.textTag, "share_lable")
        );
        startActivity(intent, options.toBundle());
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

    private void netGetCardUsers(boolean needLoading) {
        Map<String, Object> map = NetParam.newInstance()
                .put("sex", gender)
                .put("ageRange", age)
                .put("current", current + 1)
                .put("size", size)
                .put("orderByField", orderByField)
                .put("strategy", strategy)
                .put("asc", asc)
                .build();
        ApiHelperEx.execute(this, needLoading,
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
