package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import com.frame.base.BasicActivity;
import com.frame.component.view.ConerEditText;
import com.frame.component.view.LoadingLayout;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.OnClick;

import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.frame.component.ui.fragment.FriendListFragment;
import com.frame.component.ui.fragment.GroupListFragment;

import org.greenrobot.eventbus.EventBus;

public class SearchActivity extends BasicActivity {

    @BindView(R2.id.loadingview)
    LoadingLayout loadingview;
    @BindView(R2.id.edit_search)
    ConerEditText editSearch;
    @BindView(R2.id.pager)
    ViewPager pager;
    @BindView(R2.id.tablayout)
    SmartTabLayout tablayout;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        initTabLayout();
        editSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search();
            }
            return false;
        });
        //loadingview.showLackView();
        //延迟0.1秒后弹出软键盘
        new Handler().postDelayed(() -> KeyboardUtils.showSoftInput(editSearch), 100);
    }

    private void search() {
        String tags = editSearch.getTagsStr();
        String key = editSearch.getKey();
        if (!TextUtils.isEmpty(tags) || !TextUtils.isEmpty(key)) {
            EventBean eventBean = new EventBean(EventBean.EVENT_IM_SEARCH);
            eventBean.put("tags", tags);
            eventBean.put("key", key);
            EventBus.getDefault().post(eventBean);
            //loadingview.showOut();
        } else {
            ToastUtil.showToastShort("请输入搜索关键字");
        }
    }

    @Override
    public void finish() {
        KeyboardUtils.hideSoftInput(this);
        super.finish();
    }

    private static String[] TAB_LAYOUT_TITLES = {"用户", "趣聊", "觅聊"};

    private void initTabLayout() {
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return FriendListFragment.newGroupSearch();
                    case 1:
                        return GroupListFragment.newSearchGroup();
                    case 2:
                        return GroupListFragment.newSearchMi();
                }
                return null;
            }

            @Override
            public int getCount() {
                return TAB_LAYOUT_TITLES.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return TAB_LAYOUT_TITLES[position];
            }
        });
        tablayout.setViewPager(pager);
    }

    @OnClick(R2.id.btn_right)
    public void cancel() {
        finish();
    }
}
