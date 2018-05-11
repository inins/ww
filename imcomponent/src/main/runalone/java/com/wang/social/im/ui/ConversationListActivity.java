package com.wang.social.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;

import com.frame.base.BasicActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.R;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.mvp.ui.ConversationFragment;
import com.wang.social.im.mvp.ui.ConversationListFragment;
import com.wang.social.im.mvp.ui.fragments.ContactsFragment;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-17 15:49
 * ============================================
 */
public class ConversationListActivity extends BasicActivity {

//    @BindView(R.id.toolbar)
//    SocialToolbar toolbar;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_conversation_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
//        setSupportActionBar(toolbar);

        setListener();

        ContactsFragment fragment = ContactsFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.cv_list, fragment, ContactsFragment.class.getName());
        transaction.commitAllowingStateLoss();
    }

    private void setListener() {
//        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
//            @Override
//            public void onButtonClick(SocialToolbar.ClickType clickType) {
//                if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
//                    if (TIMManager.getInstance().getLoginUser().equals(IMConstants.USRE_IDENTIFIER_1)) {
//                        Intent intent = new Intent(ConversationListActivity.this, ConversationActivity.class);
//                        intent.putExtra("target", IMConstants.USRE_IDENTIFIER_2);
//                        startActivity(intent);
//                    } else {
//                        Intent intent = new Intent(ConversationListActivity.this, ConversationActivity.class);
//                        intent.putExtra("target", IMConstants.USRE_IDENTIFIER_1);
//                        startActivity(intent);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
