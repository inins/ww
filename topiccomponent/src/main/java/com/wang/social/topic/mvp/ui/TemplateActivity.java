package com.wang.social.topic.mvp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerTemplateComponent;
import com.wang.social.topic.di.module.TemplateModule;
import com.wang.social.topic.mvp.contract.TemplateContract;
import com.wang.social.topic.mvp.model.entities.Template;
import com.wang.social.topic.mvp.presenter.TemplatePresenter;
import com.wang.social.topic.mvp.ui.adapter.TemplateAdapter;
import com.wang.social.topic.mvp.ui.widget.SpacingItemDecoration;

import butterknife.BindView;

public class TemplateActivity extends BaseAppActivity<TemplatePresenter> implements TemplateContract.View {
    public final static String NAME_ID = "NAME_ID";

    /**
     * 启动模板选择
     * @param activity activity
     * @param currentId 当前模板id
     */
    public static void start(Activity activity, int currentId, int requestCode) {
        Intent intent = new Intent(activity, TemplateActivity.class);
        intent.putExtra(NAME_ID, currentId);
        activity.startActivityForResult(intent, requestCode);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    TemplateAdapter mAdapter;
    @BindView(R2.id.selected_text_view)
    TextView mSelectedTV;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTemplateComponent.builder()
                .appComponent(appComponent)
                .templateModule(new TemplateModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_template;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                    // 返回数据
                    Intent intent = new Intent();

                    intent.putExtra("id", mPresenter.getCurrentTemplateId());
                    if (null != mPresenter.getCurrentTemplate()) {
                        intent.putExtra("url", mPresenter.getCurrentTemplate().getUrl());
                    }

                    setResult(Activity.RESULT_OK, intent);

                    finish();
                }
            }
        });

        refreshSelectedTextView(false);

        // 当前模板
        mPresenter.setCurrentTemplateId(getIntent().getIntExtra(NAME_ID, -1));
        // 加载模板列表
        mPresenter.loadTemplateList();
    }

    private void refreshSelectedTextView(boolean enable) {
        mSelectedTV.setEnabled(enable);
        if (enable) {
            mSelectedTV.setTextColor(getResources().getColor(R.color.common_blue_deep));
        } else {
            mSelectedTV.setTextColor(getResources().getColor(R.color.common_text_dark));
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
    public String getDefaultName() {
        return getString(R.string.topic_template_default);
    }

    @Override
    public void onTemplateListLoaded() {
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addItemDecoration(new SpacingItemDecoration());

        if (null == mAdapter) {
            mAdapter = new TemplateAdapter(mRecyclerView);
            mAdapter.setDataProvider(new TemplateAdapter.DataProvider() {
                @Override
                public Template getTemplate(int position) {
                    return mPresenter.getTemplates().get(position);
                }

                @Override
                public int getTemplateCount() {
                    return mPresenter.getTemplates().size();
                }

                @Override
                public boolean isSelected(int id) {
                    return mPresenter.isCurrentTemplate(id);
                }
            });
            mAdapter.setSelectListener(new TemplateAdapter.SelectListener() {
                @Override
                public void onTemplateSelected(Template template) {
                    if (mPresenter.setCurrentTemplateId(template.getId())) {
                        refreshSelectedTextView(true);
                    }

                    mPresenter.setCurrentTemplate(template);
                }
            });
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onNotifyTemplatesChanged() {
        mAdapter.notifyDataSetChanged();
    }
}
