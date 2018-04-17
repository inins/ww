package com.wang.social.topic.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.wang.social.topic.R;
import com.wang.social.topic.mvp.ui.fragment.TopicFragment;
import com.wang.social.topic.mvp.ui.fragment.TopicListFragment;

public class TopicListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_activity_main);

        Fragment topicFragment = TopicListFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (!topicFragment.isAdded()) {
            fragmentTransaction.add(R.id.fragment_container, topicFragment, "topicFragment");
        }
        fragmentTransaction.commit();
    }
}
