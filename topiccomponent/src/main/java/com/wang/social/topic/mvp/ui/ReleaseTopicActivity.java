package com.wang.social.topic.mvp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.wang.social.login.mvp.ui.TagSelectionActivity;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ReleaseTopicActivity extends BasicActivity {

    @BindView(R2.id.title_edit_text)
    EditText mTitleET;
    @BindView(R2.id.title_count_text_view)
    TextView mTitleCountTV;
    // 标签
    @BindView(R2.id.topic_tags_text_view)
    TextView mTagsTV;


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


    @OnClick(R2.id.cover_layout)
    public void selectCoverImage() {

    }

    private String mTagIds;
    private String mTagNames;
    @OnClick(R2.id.topic_tags_text_view)
    public void topicTags() {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(mTagIds)) {
            bundle.putString("ids", mTagIds);
        }
        bundle.putString("NAME_MODE", "MODE_SELECTION");
        bundle.putInt("NAME_TAG_TYPE", 3);

        Intent intent = new Intent(this, TagSelectionActivity.class);
        intent.putExtras(bundle);

        startActivity(intent);

    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        // 只接收标签相关事件
        if (event.getEvent() != EventBean.EVENTBUS_TAG_SELECTED_LIST) {
            return;
        }

        String ids = (String)event.get("ids");
        String names = (String)event.get("names");

        Timber.i(ids);
        Timber.i(names);

        mTagIds = ids;
        mTagNames = names;

        mTagsTV.setText(mTagNames);
    }
}
