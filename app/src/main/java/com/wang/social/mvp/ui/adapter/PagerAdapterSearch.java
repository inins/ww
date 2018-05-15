package com.wang.social.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.frame.component.router.Router;
import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.ui.fragment.BuildFragment;


/**
 * Created by liaoinstan
 */

public class PagerAdapterSearch extends FragmentPagerAdapter {

    private String[] titles;

    public PagerAdapterSearch(FragmentManager fm, String[] titles) {
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
                FunpointService funpointService = (FunpointService) Router.getInstance().getService(FunpointService.class.getName());
                return funpointService.getSearchFunpointFragment();
            case 1:
                FunshowService funshowService = (FunshowService) Router.getInstance().getService(FunshowService.class.getName());
                return funshowService.getSearchFunshowFragment();
            case 2:
                return BuildFragment.newInstance();
            case 3:
                return BuildFragment.newInstance();
            case 4:
                return BuildFragment.newInstance();
            default:
                return null;
        }
    }
}
