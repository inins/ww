package com.wang.social.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.service.R2;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.view.ConerEditText;
import com.frame.component.view.LoadingLayout;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.R;
import com.wang.social.mvp.ui.adapter.PagerAdapterSearch;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

@RouteNode(path = "/search", desc = "搜索")
public class SearchActivity extends BasicAppActivity implements IView, BaseAdapter.OnItemClickListener<Funpoint> {

    @BindView(R.id.loadingview)
    LoadingLayout loadingview;
    @BindView(R.id.edit_search)
    ConerEditText editSearch;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tablayout)
    SmartTabLayout tablayout;

    private String[] titles = new String[]{"趣点", "趣晒", "话题", "趣聊", "用户"};
    private PagerAdapterSearch pagerAdapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.activity_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        pagerAdapter = new PagerAdapterSearch(getSupportFragmentManager(), titles);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(5);
        tablayout.setViewPager(pager);

        editSearch.setOnKeyListener((view, keyCode, event) -> {
            if (keyCode == event.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                search();
                return true;
            }
            return false;
        });
        loadingview.showLackView();
        //延迟0.1秒后弹出软键盘
        new Handler().postDelayed(() -> KeyboardUtils.showSoftInput(editSearch), 100);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_right) {
            finish();
        }
    }

    @Override
    public void onItemClick(Funpoint bean, int position) {
    }

    private void search() {
        String tags = editSearch.getTagsStr();
        String key = editSearch.getKey();
        if (!TextUtils.isEmpty(tags) || !TextUtils.isEmpty(key)) {
            EventBean eventBean = new EventBean(EventBean.EVENT_APP_SEARCH);
            eventBean.put("tags",tags);
            eventBean.put("key",key);
            EventBus.getDefault().post(eventBean);
            loadingview.showOut();
        } else {
            ToastUtil.showToastShort("请输入搜索关键字");
        }
    }

    @Override
    public void finish() {
        KeyboardUtils.hideSoftInput(this);
        super.finish();
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

}
