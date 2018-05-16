package com.wang.social.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.NetLoginTestHelper;
import com.frame.utils.ToastUtil;
import com.squareup.haha.perflib.Main;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.R;
import com.wang.social.im.helper.FriendShipHelper;
import com.wang.social.im.helper.GroupHelper;
import com.wang.social.im.mvp.ui.GroupInviteDetailActivity;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;
import com.wang.social.im.mvp.ui.SearchActivity;

public class MainActivity extends AppCompatActivity {

    private EditText mUserIdET;
    private int mUserId = 10001;
    private int mGroupId;
    private int mMsgId;
    private EditText mGroupIdET;
    private EditText mMsgIdET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_main);
        findViewById(R.id.login0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetLoginTestHelper.newInstance().loginTest("15882370797", "qqqqqq");
            }
        });
        findViewById(R.id.login1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetLoginTestHelper.newInstance().loginTest("15882370798", "qqqqqq");
            }
        });

        findViewById(R.id.login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMManager.getInstance().login(String.valueOf(AppDataHelper.getUser().getUserId()), AppDataHelper.getSign(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        ToastUtil.showToastShort("Login Error.");
                    }

                    @Override
                    public void onSuccess() {
                        GroupHelper.getInstance();
                        FriendShipHelper.getInstance();

                        Intent intent = new Intent(MainActivity.this, ConversationListActivity.class);
                        startActivity(intent);
                        finish();
//                        Intent intent = new Intent(MainActivity.this, GameConversationActivity.class);
//                        startActivity(intent);
//                        finish();
                    }
                });
            }
        });
        findViewById(R.id.search).setOnClickListener(v -> SearchActivity.start(this));

        mUserIdET = findViewById(R.id.user_id_edit_text);
        mUserIdET.setText("10001");
        try {
            mUserId = Integer.parseInt(mUserIdET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.personal_card)
                .setOnClickListener(v -> PersonalCardActivity.start(this, mUserId));

        mGroupIdET = findViewById(R.id.group_id_edit_text);
        mMsgIdET = findViewById(R.id.msg_id_edit_text);
        mGroupIdET.setText("26");
        mGroupId = 26;
        try {
            mGroupId = Integer.parseInt(mGroupIdET.getText().toString());
            mMsgId = Integer.parseInt(mMsgIdET.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

            findViewById(R.id.group_invite)
                    .setOnClickListener(v -> {
                        if (mMsgId > 0) {
                            GroupInviteDetailActivity.startForInvite(MainActivity.this,
                                    mGroupId, mMsgId);
                        } else {
                            GroupInviteDetailActivity.startForBrowse(MainActivity.this,
                                    mGroupId);
                        }
                    });

    }

    @Override
    protected void onResume() {
        super.onResume();
        TIMManager.getInstance().logout(null);
    }
}
