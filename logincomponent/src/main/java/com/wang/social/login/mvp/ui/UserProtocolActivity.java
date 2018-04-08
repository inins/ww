package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.base.BasicActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.login.R;

import butterknife.BindView;

public class UserProtocolActivity extends BasicActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, UserProtocolActivity.class);
        context.startActivity(intent);
    }

    @BindView(R.id.toolbar)
    SocialToolbar toolbar;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_user_protocol;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    UserProtocolActivity.this.finish();
                }
            }
        });
    }
}
