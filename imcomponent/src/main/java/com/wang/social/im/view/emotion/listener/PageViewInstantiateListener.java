package com.wang.social.im.view.emotion.listener;

import android.view.View;
import android.view.ViewGroup;

import com.wang.social.im.view.emotion.data.PageEntity;


public interface PageViewInstantiateListener<T extends PageEntity> {

    View instantiateItem(ViewGroup container, int position, T pageEntity);
}
