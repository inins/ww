package com.frame.component.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.frame.component.ui.fragment.BuildFragment;
import com.frame.component.ui.fragment.NewGuideRecommendFriendFragment;


/**
 * Created by Administrator on 2017/7/7.
 */

public class PagerAdapterNewguideRecommend extends FragmentPagerAdapter {

    private String[] titles;

    public PagerAdapterNewguideRecommend(FragmentManager fm, String[] titles) {
        super(fm);
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NewGuideRecommendFriendFragment.newInstance();
            case 1:
                return NewGuideRecommendFriendFragment.newInstance();
            default:
                return null;
        }
    }

}
