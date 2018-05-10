package com.wang.social.home.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.common.CardLayoutManager;
import com.wang.social.home.common.ItemTouchCardCallback;
import com.wang.social.home.mvp.entities.Card;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterCard;
import com.wang.social.home.mvp.ui.dialog.CardPopupWindow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardActivity extends BasicAppActivity implements BaseAdapter.OnItemClickListener<Card>{

    @BindView(R2.id.recycler)
    RecyclerView recycler;
    @BindView(R2.id.tablayout)
    TabLayout tablayout;
    @BindView(R2.id.appbar)
    AppBarLayout appbar;
    @BindView(R2.id.shadow)
    View shadow;

    private RecycleAdapterCard adapter;
    private String[] titles = new String[]{"同类", "圈子"};

    private CardPopupWindow popupWindow;

    public static void start(Context context) {
        Intent intent = new Intent(context, CardActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.home_activity_card;
    }

    @Override
    @SuppressWarnings("all")
    public void initData(@NonNull Bundle savedInstanceState) {
        appbar.bringToFront();
        popupWindow = new CardPopupWindow(this);
        popupWindow.setShadowView(shadow);
        //初始化recycle卡片view
        adapter = new RecycleAdapterCard();
        adapter.setOnItemClickListener(this);
        recycler.setLayoutManager(new CardLayoutManager());
        recycler.setAdapter(adapter);
        //设置拖拽手势
        final ItemTouchCardCallback callback = new ItemTouchCardCallback(recycler, adapter.getData());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recycler);
        callback.setOnSwipedListener((ItemTouchCardCallback.OnSwipedListener<Card>) (bean, direction) -> {
        });

        //添加tab
        for (String title : titles) {
            TabLayout.Tab tab = tablayout.newTab();
            tab.setCustomView(R.layout.home_tab_text_blank_blue);
            CheckedTextView textName = tab.getCustomView().findViewById(R.id.custom_text);
            textName.setText(title);
            tablayout.addTab(tab);
            if (tab.getPosition() == 0) {
                popupWindow.setCheckable(textName);
                textName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.home_select_down_up, 0);
            }
        }

        //tab
        tablayout.getTabAt(0).getCustomView().setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                popupWindow.showPopupWindow(v);
            }
            return false;
        });

//        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    TextView textName = tab.getCustomView().findViewById(R.id.custom_text);
//                    textName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.home_select_down_up, 0);
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    TextView textName = tab.getCustomView().findViewById(R.id.custom_text);
//                    textName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.common_ic_down, 0);
//                }
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        adapter.refreshItem(new ArrayList<Card>() {{
            add(new Card(0));
            add(new Card(1));
            add(new Card(2));
            add(new Card(3));
            add(new Card(4));
            add(new Card(5));
            add(new Card(6));
        }});

    }

    @Override
    public void onItemClick(Card card, int position) {
        CardDetailActivity.start(this);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dislike) {
            ToastUtil.showToastShort("dislike");
        } else if (id == R.id.btn_like) {
            ToastUtil.showToastShort("like");
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }
}
