package com.wang.social.topic.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.frame.base.BasicActivity;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.mvp.ui.adapter.CommentAdapter;
import com.wang.social.topic.mvp.ui.adapter.TopUserAdapter;
import com.wang.social.topic.mvp.ui.widget.CommentSortPopWindow;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentActivity extends BasicActivity {

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.sort_type_text_view)
    TextView mSortTypeTV;

    CommentSortPopWindow mCommentSortPopWindow;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_comment;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mRecyclerView.setAdapter(new CommentAdapter(mRecyclerView));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @OnClick(R2.id.sort_type_text_view)
    public void sort(View v) {
        if (null == mCommentSortPopWindow) {
            mCommentSortPopWindow = new CommentSortPopWindow(this);
            mCommentSortPopWindow.setSortChangeListener(new CommentSortPopWindow.SortChangeListener() {
                @Override
                public void onSortChanged(int sort, String name) {
                    mSortTypeTV.setText(name);
                }
            });
        }

        mCommentSortPopWindow.showAsDropDown(v, -SizeUtils.dp2px(5), 0, Gravity.RIGHT);
    }
}
