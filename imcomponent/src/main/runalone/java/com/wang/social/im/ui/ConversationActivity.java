package com.wang.social.im.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.ui.ConversationFragment;

public class ConversationActivity extends BasicActivity{

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_conversation;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        String targetId = getIntent().getExtras().getString("target");
        ConversationFragment conversationFragment = ConversationFragment.newInstance(ConversationType.PRIVATE, targetId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.conversation, conversationFragment, ConversationFragment.class.getName());
        transaction.commitAllowingStateLoss();
    }
}
