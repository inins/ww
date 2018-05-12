package com.wang.social.personal.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wang.social.personal.mvp.ui.fragment.MeFunshowFragment;
import com.wang.social.personal.mvp.ui.fragment.MeTopicFragment;


/**
 * Created by liaoinstan
 */

public class PagerAdapterFunshowTopic extends FragmentPagerAdapter {

    private String[] titles;

    public PagerAdapterFunshowTopic(FragmentManager fm, String[] titles) {
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
                return MeFunshowFragment.newInstance();
            case 1:
                return MeTopicFragment.newInstance();
            default:
                return null;
        }
    }
}
