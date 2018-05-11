package com.wang.social.home.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.frame.component.router.Router;
import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.service.topic.TopicService;
import com.wang.social.home.mvp.ui.fragment.CardGroupFragment;
import com.wang.social.home.mvp.ui.fragment.CardUserFragment;


/**
 * Created by liaoinstan
 */

public class PagerAdapterCard extends FragmentPagerAdapter {

    private String[] titles;

    public PagerAdapterCard(FragmentManager fm, String[] titles) {
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
                return CardUserFragment.newInstance();
            case 1:
                return CardGroupFragment.newInstance();
            default:
                return null;
        }
    }
}
