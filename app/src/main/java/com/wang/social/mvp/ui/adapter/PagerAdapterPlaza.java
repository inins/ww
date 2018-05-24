package com.wang.social.mvp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.frame.component.router.Router;
import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.service.topic.TopicService;


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
                FunshowService funshowService = (FunshowService) Router.getInstance().getService(FunshowService.class.getName());
                return funshowService.getFunshowFragment();
            case 1:
                TopicService topicService = (TopicService) Router.getInstance().getService(TopicService.class.getName());
                return topicService.getTopicFragment();
            case 2:
                FunpointService funpointService = (FunpointService) Router.getInstance().getService(FunpointService.class.getName());
                return funpointService.getFunpointFragment();
            default:
                return null;
        }
    }
}
