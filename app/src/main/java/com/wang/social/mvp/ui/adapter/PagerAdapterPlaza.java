package com.wang.social.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.frame.component.router.Router;
import com.frame.component.service.personal.PersonalService;
import com.wang.social.mvp.ui.fragment.BuildFragment;
import com.wang.social.mvp.ui.fragment.PlazaFragment;


/**
 * Created by liaoinstan
 */

public class PagerAdapterPlaza extends FragmentPagerAdapter {

    private String[] titles;

    public PagerAdapterPlaza(FragmentManager fm, String[] titles) {
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
                return BuildFragment.newInstance();
            case 1:
                return BuildFragment.newInstance();
            case 2:
                return BuildFragment.newInstance();
            default:
                return null;
        }
    }
}
