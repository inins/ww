package com.wang.social.topic.app;

import android.support.v4.app.Fragment;

import com.frame.component.service.funpoint.FunpointService;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.service.personal.PersonalService;
import com.frame.component.service.topic.TopicService;
import com.wang.social.topic.ui.fragment.TopicFragment;

/**
 * ========================================
 * 向其他组件提供资源
 * ========================================
 */

public class TopicServiceImpl implements TopicService {

    @Override
    public Fragment getTopicFragment() {
        return TopicFragment.newInstance();
    }
}
