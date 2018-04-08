package com.wang.social.im.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tencent.imsdk.TIMConversationType;
import com.wang.social.im.R;
import com.wang.social.im.enums.ConversationType;
import com.wang.social.im.view.IMInputView;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_main);

        IMInputView inputView = findViewById(R.id.inputView);
        inputView.setConversationType(ConversationType.PRIVATE);

        Timber.tag("test").d("adasasdasdasdasdsa++++++++++++++");
    }
}
