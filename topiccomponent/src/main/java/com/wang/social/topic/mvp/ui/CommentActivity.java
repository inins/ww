package com.wang.social.topic.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerCommentComponent;
import com.wang.social.topic.di.module.CommentModule;
import com.wang.social.topic.mvp.contract.CommentContract;
import com.wang.social.topic.mvp.model.entities.Comment;
import com.wang.social.topic.mvp.presenter.CommentPresenter;
import com.wang.social.topic.mvp.ui.adapter.CommentAdapter;
import com.wang.social.topic.mvp.ui.adapter.CommentReplyAdapter;
import com.wang.social.topic.mvp.ui.widget.CommentSortPopWindow;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentActivity extends BaseAppActivity<CommentPresenter> implements CommentContract.View {
    public final static String KEY_TOPIC_ID = "TOPIC_ID";
    public final static String KEY_CREATOR_ID = "CREATOR_ID";
    public final static String KEY_COMMENT_ID = "COMMENT_ID";
    public final static String KEY_UI_LEVEL = "UI_LEVEL";
    public final static String KEY_COMMENT = "KEY_COMMENT";

    // 话题评论
    public final static int FIRST_LEVEL = 1;
    // 评论的评论
    public final static int SECOND_LEVEL = 2;

    @IntDef({FIRST_LEVEL, SECOND_LEVEL})
    @Retention(RetentionPolicy.SOURCE)
    @interface CommentUiLevel {
    }

    /**
     * 启动话题评论页面
     * @param context context
     * @param topicId 话题id
     * @param creatorId 创建者id
     */
    public static void startFirstLevel(Context context, int topicId, int creatorId) {
        start(context, topicId, creatorId, -1, FIRST_LEVEL, null);
    }

    /**
     * 启动评论的评论页面
     * @param context context
     * @param topicId 话题id
     * @param creatorId 创建者id
     * @param commentId 评论id
     */
    public static void startSecondLevel(Context context, int topicId, int creatorId, int commentId, Comment comment) {
        start(context, topicId, creatorId, commentId, SECOND_LEVEL, comment);
    }

    /**
     * 启动评论页面
     * @param context context
     * @param topicId 话题id
     * @param creatorId 创建者id
     * @param commentId 评论id
     * @param level 等级（1.话题评论 2.评论的评论）
     * @param comment 评论，当跳转到二级页面时传入
     */
    private static void start(Context context,
                              int topicId, int creatorId, int commentId, @CommentUiLevel int level,
                              Comment comment) {
        if (null == context) return;

        Intent intent = new Intent(context, CommentActivity.class);
        intent.putExtra(KEY_TOPIC_ID, topicId);
        intent.putExtra(KEY_CREATOR_ID, creatorId);
        intent.putExtra(KEY_COMMENT_ID, commentId);
        intent.putExtra(KEY_UI_LEVEL, level);
        intent.putExtra(KEY_COMMENT, comment);
        context.startActivity(intent);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    // 标题栏显示回复数量
    @BindView(R2.id.title_reply_count_text_view)
    TextView mTitleReplyCountTV;
    // 标题和提示
    @BindView(R2.id.title_text_view)
    TextView mTitleTV;
    @BindView(R2.id.title_hint_text_view)
    TextView mTitleHintTV;
    // 评论数量
    @BindView(R2.id.reply_count_text_view)
    TextView mReplyCountTV;
    // 发送按钮
    @BindView(R2.id.send_text_view)
    TextView mSendTV;
    // 评论输入框
    @BindView(R2.id.comment_edit_text)
    EditText mCommentET;
    // 排序
    @Deprecated
    @BindView(R2.id.sort_type_text_view)
    TextView mSortTypeTV;

    private CommentSortPopWindow mCommentSortPopWindow;
    private RecyclerView.Adapter mAdapter;

    // 话题id
    private int mTopicId;
    // 发布人ID
    private int mCreatorId;
    // 评论id
    private int mCommentId;
    // 几级页面
    private @CommentUiLevel
    int mLevel;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCommentComponent.builder()
                .appComponent(appComponent)
                .commentModule(new CommentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_comment;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        // 话题ID
        mTopicId = getIntent().getIntExtra(KEY_TOPIC_ID, 20);
        // 发帖人ID
        mCreatorId = getIntent().getIntExtra(KEY_CREATOR_ID, -1);
        // 评论ID
        mCommentId = getIntent().getIntExtra(KEY_COMMENT_ID, -1);
        // 页面登记
        mLevel = getIntent().getIntExtra(KEY_UI_LEVEL, FIRST_LEVEL);
        // 评论
        mPresenter.setComment(getIntent().getParcelableExtra(KEY_COMMENT));

        // 二级页面
        if (mLevel == SECOND_LEVEL) {
            mTitleTV.setVisibility(View.GONE);
            mTitleHintTV.setVisibility(View.GONE);
            mReplyCountTV.setVisibility(View.GONE);
            mSpringView.setBackgroundColor(0xFFF2F2F2);
//            mRecyclerView.setBackgroundColor(0xFFF2F2F2);
        }

        // 发送不可点击
        resetSendTV(false);

        mToolbar.setOnButtonClickListener((SocialToolbar.ClickType clickType) -> {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            });

        // 不同页面评论框的提示不同
        resetCommentETHint();

        // 输入监听
        mCommentET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                resetSendTV(s.length() > 0);
            }
        });

        // 评论id有效，则说明是二级页面
        if (mLevel == SECOND_LEVEL) {
            // 评论回复
            mAdapter = new CommentReplyAdapter(mRecyclerView, mPresenter.getCommentList());
            ((CommentReplyAdapter) mAdapter).setClickListener(new CommentReplyAdapter.ClickListener() {
                @Override
                public void onReply(Comment comment) {
                    // 回复某人，修改输入框的提示语
                    mCommentET.setTag(comment);
                    mCommentET.setHint(
                            getString(R.string.topic_reply) +
                                    (TextUtils.isEmpty(comment.getNickname()) ?
                                            Integer.toString(comment.getUserId()) : comment.getNickname()));

                    showSoftInputFromWindow();
                }

                @Override
                public void onSupport(Comment comment) {
                    // 点赞
                    mPresenter.commentSupport(mTopicId, comment);
                }
            });
        } else {
            // 话题评论
            mAdapter = new CommentAdapter(mRecyclerView, mPresenter.getCommentList());
            ((CommentAdapter) mAdapter).setClickListener(new CommentAdapter.ClickListener() {
                /**
                 * 打开评论二级页面
                 * @param comment
                 */
                @Override
                public void onOpenReplyList(Comment comment) {
                    CommentActivity.startSecondLevel(
                            CommentActivity.this,
                            mTopicId,
                            comment.getUserId(),
                            comment.getCommentId(),
                            comment);
                }

                @Override
                public void onReply(Comment comment) {
                    // 回复某人，修改输入框的提示语
                    mCommentET.setTag(comment);
                    mCommentET.setHint(
                            getString(R.string.topic_reply) +
                                    (TextUtils.isEmpty(comment.getNickname()) ?
                                            Integer.toString(comment.getUserId()) : comment.getNickname()));

                    showSoftInputFromWindow();
                }

                @Override
                public void onSupport(Comment comment) {
                    // 点赞
                    mPresenter.commentSupport(mTopicId, comment);
                }
            });
        }

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        // 更新，加载更多
        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadCommentList(mTopicId, mCommentId, true);
//                mPresenter.testLoadCommentList();
            }

            @Override
            public void onLoadmore() {
                mPresenter.loadCommentList(mTopicId, mCommentId, false);
//                mPresenter.testLoadCommentList();
            }
        });
        mSpringView.callFreshDelay();
    }

    private void resetSendTV(boolean enable) {
        mSendTV.setEnabled(enable);

        if (enable) {
            mSendTV.setTextColor(getResources().getColor(R.color.common_blue_deep));
        } else {
            mSendTV.setTextColor(getResources().getColor(R.color.common_text_dark));
        }
    }

    @Deprecated
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

    @OnClick(R2.id.send_text_view)
    public void sendComment() {
        if (mCommentET.getText().length() > 150) {
            ToastUtil.showToastLong("评论不能超过150字");
            return;
        }

        if (mCommentET.getTag() instanceof Comment) {
            Comment comment = (Comment) mCommentET.getTag();
            // 回复
            mPresenter.commitComment(mTopicId,
                    comment.getCommentId(),
                    comment.getUserId(),
                    mCommentET.getText().toString());
        } else {
            // 评论
            mPresenter.commitComment(mTopicId,
                    mCommentId,
                    mCreatorId,
                    mCommentET.getText().toString());
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void showToastLong(String msg) {
        ToastUtil.showToastLong(msg);
    }

    /**
     * 加载评论列表成功
     */
    @Override
    public void onLoadCommentSuccess() {
        refreshCommentList();
    }

    private void refreshCommentList() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 加载评论列表完成（成功或失败）
     */
    @Override
    public void onLoadCommentCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();
    }

    /**
     * 设置总共的评论条数
     *
     * @param count
     */
    @Override
    public void setReplyCount(int count) {
        // 一级页面
        if (mLevel == FIRST_LEVEL) {
            mReplyCountTV.setVisibility(View.VISIBLE);
            mReplyCountTV.setText(String.format(getString(R.string.topic_comment_count_format), count));
        } else {
            // 二级页面
            mTitleReplyCountTV.setText(String.format(getString(R.string.topic_comment_count_format), count));
        }
    }

    /**
     * 提交评论成功
     */
    @Override
    public void onCommitCommentSuccess() {
        resetCommentETHint();
    }

    private void resetCommentETHint() {
        mCommentET.setText("");
        mCommentET.setTag(null);
        if (mLevel == FIRST_LEVEL) {
            mCommentET.setHint(R.string.topic_comment_edit_hint);
        } else {
            mCommentET.setHint(R.string.topic_reply_comment);
        }

        // 刷新列表
        mSpringView.callFreshDelay();
    }

    @Override
    public void onSupportSuccess(Comment comment) {
        refreshCommentList();
    }


    private void showSoftInputFromWindow() {
        mCommentET.setFocusable(true);
        mCommentET.setFocusableInTouchMode(true);
        mCommentET.requestFocus();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mCommentET, 0);
    }
}
