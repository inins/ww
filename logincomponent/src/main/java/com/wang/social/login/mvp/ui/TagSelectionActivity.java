package com.wang.social.login.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.di.component.DaggerTagSelectionComponent;
import com.wang.social.login.di.module.TagSelectionModule;
import com.wang.social.login.mvp.contract.TagSelectionContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.Tags;
import com.wang.social.login.mvp.presenter.TagSelectionPresenter;
import com.wang.social.login.mvp.ui.widget.TagListFragment;
import com.wang.social.login.utils.Keys;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.security.Key;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class TagSelectionActivity extends BaseActivity<TagSelectionPresenter> implements
        TagSelectionContract.View {
    public final static String NAME_MODE = "NAME_MODE";
    public final static String NAME_SELECTED_LIST = "NAME_SELECTED_LIST";
    // 标签选择
    public final static String MODE_SELECTION = "MODE_SELECTION";
    // 兴趣大杂烩
    public final static String MODE_CONFIRM = "MODE_CONFIRM";
    public final static String TAG_DELETE = "TAG_DELETE";

    public static void start(Context context, String mode, ArrayList<Tag> selectedList) {
        Intent intent = new Intent(context, TagSelectionActivity.class);
        intent.putExtra(NAME_MODE, mode);
        if (null != selectedList) {
            intent.putParcelableArrayListExtra(NAME_SELECTED_LIST, selectedList);
        }
        context.startActivity(intent);
    }

    public static void startSelection(Context context) {
        start(context, MODE_SELECTION, null);
    }

    public static void startConfirm(Context context, ArrayList<Tag> selectedList) {
        start(context, MODE_CONFIRM, selectedList);
    }

    @BindView(R.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.selected_count_hint_text_view)
    TextView selectedCountHintTV;
    @BindView(R.id.selected_count_text_view)
    TextView selectedCountTV;
    @BindView(R.id.content_layout)
    LinearLayout contentLayout;
    @BindView(R.id.title_text_view)
    TextView titleTV;
    @BindView(R.id.title_hint_text_view)
    TextView titleHintTV;

    String mode = MODE_SELECTION;

    /**
     * 选中数量文字初始化
     */
    private void resetCountTV() {
        // 如果是兴趣大杂烩，则显示确定
        if (mode.equals(MODE_CONFIRM)) {
            selectedCountHintTV.setText(getString(R.string.login_confirm));
        }

        refreshCountTV();
    }

    /**
     * 选中数量格文字式化
     */
    private void refreshCountTV() {
        selectedCountTV.setText("(" + mPresenter.getSelectedTagCount() + ")");
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerTagSelectionComponent.builder()
                .appComponent(appComponent)
                .tagSelectionModule(new TagSelectionModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.login_activity_tag_selection;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        if (getIntent().hasExtra(NAME_MODE)) {
            mode = getIntent().getStringExtra(NAME_MODE);
        }

        // ToolBar左边按钮，返回
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    TagSelectionActivity.this.finish();
                }
            }
        });

        switch (mode) {
            case MODE_SELECTION:
                initSelectionData();
                break;
            case MODE_CONFIRM:
                initConfirmData();
                break;
        }

        // 根据不同模式确定ToolBar右边文字
        resetCountTV();
    }

    /**
     * 初始化 兴趣大杂烩 UI和数据
     */
    private void initConfirmData() {
        titleTV.setText(getString(R.string.login_tag_confirm_title));
        titleHintTV.setText(getString(R.string.login_tag_confirm_title_hint));

        // 移除选择模式时需要的View
        contentLayout.removeAllViews();

        // 读取传入的 选中列表 参数
        if (getIntent().hasExtra(NAME_SELECTED_LIST)) {
            mPresenter.setSelectedTagList(
                    getIntent().getParcelableArrayListExtra(NAME_SELECTED_LIST));
        }

        // 这里不需要parentId，所以传入-1
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_layout,
                        TagListFragment.newDeleteMode(-1, mPresenter.getSelectedList()))
                .commit();
    }

    private void initSelectionData() {
        mPresenter.getParentTagList();
    }

    @Override
    public void resetTabView(Tags tags) {
        if (tags == null) return;
        if (tags.getList().size() <= 0) return;

        tabLayout.removeAllTabs();

        for (Tag tag : tags.getList()) {
            tabLayout.addTab(tabLayout.newTab().setText(tag.getTagName()));
        }

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Tag parentTag = tags.getList().get(position);
                return TagListFragment.newSelectionMode(parentTag.getId(), mPresenter.getSelectedList());
            }

            @Override
            public int getCount() {
                return tags.getList().size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tags.getList().get(position).getTagName();
            }
        });
    }

    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick(R.id.selected_count_layout)
    public void selectedCountClicked() {
        switch (mode) {
            case MODE_SELECTION:
                // 标签选择模式
                // 跳转到兴趣大杂烩
                startConfirm(TagSelectionActivity.this, mPresenter.getSelectedList());
                break;
            case MODE_CONFIRM:
                // 兴趣大杂烩
                break;
        }
    }

    @Subscriber(tag = Keys.EVENTBUS_TAG_SELECTED)
    public void tagSelected(Tag tag) {
        // 将Tag加入已选列表
        mPresenter.selectTag(tag);

        refreshCountTV();
    }

    @Subscriber(tag = Keys.EVENTBUS_TAG_UNSELECT)
    public void tagUnselect(Tag tag) {
        mPresenter.unselectTag(tag);

        refreshCountTV();
    }

    @Subscriber(tag = Keys.EVENTBUS_TAG_DELETE)
    public void tagDelete(Tag tag) {
        mPresenter.unselectTag(tag);

        refreshCountTV();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
