package com.wang.social.topic.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wang.social.topic.R;
import com.wang.social.topic.mvp.ui.fragment.TopicFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_activity_main);

        Fragment topicFragment = TopicFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!topicFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, topicFragment, "topicFragment");
        }
        fragmentTransaction.commit();
    }
}
