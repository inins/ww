package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.fragment.TopicListFragment;
import com.frame.component.view.ConerEditText;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;

import butterknife.BindView;
import butterknife.OnClick;

public class SearchActivity extends BaseAppActivity implements IView {
    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    // 输入框
    @BindView(R2.id.edit_search)
    EditText mSearchET;

    private String mKeyword;
    private String mTags;

    TopicListFragment mListFragment;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_search;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mSearchET.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                mKeyword = mSearchET.getText().toString();
                mListFragment.doSearch(mKeyword, "");
            }

            return false;
        });

        mListFragment = TopicListFragment.newTopicSearch();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout, mListFragment)
                .commit();
    }

    // 右上角取消
    @OnClick(R2.id.cancel_text_view)
    public void quit() {
        finish();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
