package com.wang.social.topic.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;

import butterknife.BindView;

public class ReleaseTopicActivity extends BasicActivity {

    @BindView(R2.id.title_edit_text)
    EditText mTitleET;
    @BindView(R2.id.title_count_text_view)
    TextView mTitleCountTV;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_release_topic;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshTitleCount(s.length());
            }
        });

        refreshTitleCount(0);
    }

    private void refreshTitleCount(int length) {
        mTitleCountTV.setText(String.format(getString(R.string.topic_title_length_format), length));
    }
}
