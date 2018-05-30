package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BasicActivity;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.di.component.AppComponent;
import com.frame.integration.AppManager;
import com.frame.utils.ScreenUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.di.component.DaggerActivityComponent;

import javax.inject.Inject;

import butterknife.OnClick;

/**
 * 被提下线提醒
 */
public class RemoteLoginDialogActivity extends BasicActivity {

    @Inject
    AppManager mAppManager;

    public static void start(Context context) {
        Intent intent = new Intent(context, RemoteLoginDialogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerActivityComponent
                .builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_ac_remote_login_dialog;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        getWindow().setLayout((int) (ScreenUtils.getScreenWidth() * 0.85f), ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @OnClick({R2.id.rld_tvb_exit, R2.id.rld_tvb_login})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.rld_tvb_exit) {
            mAppManager.exitApp();
        } else if (view.getId() == R.id.rld_tvb_login) {
            CommonHelper.LoginHelper.startLoginActivity(this);
            mAppManager.removeActivity(this);
            finish();
            mAppManager.killAll();
        }
    }

    @Override
    public void onBackPressed() {
        mAppManager.exitApp();
    }
}
