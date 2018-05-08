package com.wang.social.im.mvp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BasicFragment;
import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.ui.ConversationListFragment;
import com.wang.social.im.mvp.ui.adapters.FragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ============================================
 * 往来
 * <p>
 * Create by ChenJing on 2018-05-07 17:42
 * ============================================
 */
public class ContactsFragment extends BasicFragment {

    @BindView(R2.id.fc_iv_more)
    ImageView fcIvMore;
    @BindView(R2.id.fc_iv_search)
    ImageView fcIvSearch;
    @BindView(R2.id.fc_tab_layout)
    SmartTabLayout fcTabLayout;
    @BindView(R2.id.fc_viewpager)
    ViewPager fcViewpager;

    public static ContactsFragment newInstance() {

        Bundle args = new Bundle();

        ContactsFragment fragment = new ContactsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_contacts;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        List<String> titles = new ArrayList<>();
        titles.add(UIUtil.getString(R.string.im_chat));
        titles.add(UIUtil.getString(R.string.im_friend));
        titles.add(UIUtil.getString(R.string.im_notify));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ConversationListFragment.newInstance());
        fragments.add(FriendsFragment.newInstance());
        fragments.add(NotifyFragment.newInstance());

        fcViewpager.setAdapter(new FragmentAdapter(getFragmentManager(), fragments, titles));

        fcTabLayout.setViewPager(fcViewpager);

        ((TextView) fcTabLayout.getTabAt(0)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);

        fcTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < 3; i++) {
                    if (i == position) {
                        ((TextView) fcTabLayout.getTabAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
                    } else {
                        ((TextView) fcTabLayout.getTabAt(i)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @OnClick({R2.id.fc_iv_more, R2.id.fc_iv_search})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.fc_iv_more) {

        } else if (view.getId() == R.id.fc_iv_search) {

        }
    }
}
