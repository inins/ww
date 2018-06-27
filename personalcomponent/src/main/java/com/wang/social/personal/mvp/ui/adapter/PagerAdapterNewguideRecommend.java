package com.wang.social.personal.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wang.social.personal.mvp.ui.fragment.NewGuideRecommendFriendFragment;
import com.wang.social.personal.mvp.ui.fragment.NewGuideRecommendGroupFragment;


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
                return NewGuideRecommendGroupFragment.newInstance();
            default:
                return null;
        }
    }

}
