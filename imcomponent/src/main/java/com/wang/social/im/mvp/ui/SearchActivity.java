package com.wang.social.im.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.component.view.ConerEditText;
import com.frame.di.component.AppComponent;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;

import com.wang.social.im.R;
import com.wang.social.im.R2;

public class SearchActivity extends BasicActivity {


    // 用户 趣聊 觅聊
    @BindView(R2.id.smart_tab_layout)
    SmartTabLayout mTabLayout;
    @BindView(R2.id.view_pager)
    ViewPager mViewPager;
    // 输入框
    @BindView(R2.id.edit_search)
    ConerEditText mSearchET;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {mSearchET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                mTags = "";
//                for (int i = 0; i < mSearchET.getTags().size(); i++) {
//                    mTags += (i == 0 ? "" : ",") + mSearchET.getTags().get(i);
//                }
//                mKeyword = mSearchET.getKey();
//
//                // 清空搜索框
////                    mSearchET.setText("");
//                mSpringView.callFreshDelay();
////                    mPresenter.searchTopic(mKeyword, mTags, true);
            }

            return false;
        }
    });

        initTabLayout();
    }


    private static String[] TAB_LAYOUT_TITLES = { "用户", "趣聊", "觅聊" };

    private void initTabLayout() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
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
        mTabLayout.setViewPager(mViewPager);
    }
}
