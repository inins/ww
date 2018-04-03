package com.wang.social.im.view.emotion.listener;

import android.view.ViewGroup;

import com.wang.social.im.view.emotion.adapter.EmoticonsAdapter;


public interface EmoticonDisplayListener<T> {

    void onBindView(int position, ViewGroup parent, EmoticonsAdapter.ViewHolder viewHolder, T t, boolean isDelBtn);
}