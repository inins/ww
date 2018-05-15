package com.wang.social.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.frame.component.router.Router;
import com.frame.component.service.home.HomeService;
import com.frame.component.service.personal.PersonalService;
import com.frame.component.ui.fragment.BuildFragment;
import com.wang.social.mvp.ui.fragment.PlazaFragment;


/**
 * Created by Administrator on 2017/7/7.
 */

public class PagerAdapterHome extends FragmentPagerAdapter {

    private Fragment currentFragment;
    private Fragment personalFragment;

    public PagerAdapterHome(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                HomeService homeService = (HomeService) Router.getInstance().getService(HomeService.class.getName());
                return homeService.getHomeFragment();
            case 1:
                return BuildFragment.newInstance();
            case 2:
                return PlazaFragment.newInstance();
            case 3: {
                PersonalService personalService = (PersonalService) Router.getInstance().getService(PersonalService.class.getName());
                personalFragment = personalService.getPersonalFragment();
                return personalFragment;
            }
            default:
                return null;
        }
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public Fragment getPersonalFragment() {
        return personalFragment;
    }
}
