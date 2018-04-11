package com.wang.social.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.frame.component.router.Router;
import com.frame.component.service.personal.PersonalService;
import com.wang.social.mvp.ui.fragment.BuildFragment;


/**
 * Created by Administrator on 2017/7/7.
 */

public class PagerAdapterHome extends FragmentPagerAdapter {

    private Fragment currentFragment;

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
                return BuildFragment.newInstance();
            case 1:
                return BuildFragment.newInstance();
            case 2:
                return BuildFragment.newInstance();
            case 3: {
                PersonalService personalService = (PersonalService) Router.getInstance().getService(PersonalService.class.getName());
                return personalService.getPersonalFragment();
            }
            default:
                return null;
        }
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
