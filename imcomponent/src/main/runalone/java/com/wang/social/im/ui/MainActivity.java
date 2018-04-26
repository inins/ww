package com.wang.social.im.ui;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.frame.component.utils.UIUtil;
import com.frame.utils.ToastUtil;
import com.squareup.haha.perflib.Main;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.R;
import com.wang.social.im.app.IMConstants;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.mvp.ui.ConversationFragment;
import com.wang.social.im.view.IMInputView;
import com.wang.social.location.mvp.ui.LocationActivity;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_main);
        findViewById(R.id.login1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RxPermissions(MainActivity.this)
                        .requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted) {
                                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                                    startActivityForResult(intent, 111);
                                } else if (permission.shouldShowRequestPermissionRationale) {
                                    ToastUtil.showToastShort(UIUtil.getString(com.wang.social.location.R.string.loc_toast_open_location_permission));
                                }
                            }
                        });
//                TIMManager.getInstance().login(IMConstants.USRE_IDENTIFIER_1, IMConstants.USER_SIGN_1, new TIMCallBack() {
//                    @Override
//                    public void onError(int i, String s) {
//                        ToastUtil.showToastShort("Login Error.");
//                    }
//
//                    @Override
//                    public void onSuccess() {
//                        Intent intent = new Intent(MainActivity.this, ConversationListActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
            }
        });

        findViewById(R.id.login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMManager.getInstance().login(IMConstants.USRE_IDENTIFIER_2, IMConstants.USER_SIGN_2, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        ToastUtil.showToastShort("Login Error.");
                    }

                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(MainActivity.this, ConversationListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TIMManager.getInstance().logout(null);
    }
}
