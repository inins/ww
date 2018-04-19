package com.wang.social.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.im.R;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.ui.ConversationFragment;

public class ConversationActivity extends BasicActivity {

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
        int type = getIntent().getExtras().getInt("conversationType");
        ConversationType conversationType = ConversationType.values()[type];
        ConversationFragment conversationFragment = ConversationFragment.newInstance(conversationType, targetId);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.conversation, conversationFragment, ConversationFragment.class.getName());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(ConversationFragment.class.getName());
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
