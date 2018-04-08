package com.wang.social.login.mvp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.frame.base.BaseActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.login.R;
import com.wang.social.login.di.component.DaggerTagSelectionComponent;
import com.wang.social.login.di.module.TagSelectionModule;
import com.wang.social.login.mvp.contract.TagSelectionContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.Tags;
import com.wang.social.login.mvp.presenter.TagSelectionPresenter;
import com.wang.social.login.mvp.ui.widget.TagListFragment;
import com.wang.social.login.mvp.ui.widget.adapter.TagAdapter;

import java.util.HashMap;

import butterknife.BindView;

public class TagSelectionActivity extends BaseActivity<TagSelectionPresenter> implements
        TagSelectionContract.View {

    @BindView(R.id.toolbar)
    SocialToolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.selected_count_text_view)
    TextView selectedCountTV;

    // 已选列表
    HashMap<Integer, Tag> selectedList = new HashMap<>();

    private TagListFragment.TagListListener tagListListener = new TagListFragment.TagListListener() {
        @Override
        public void onSelected(Tag tag) {
            selectedList.put(tag.getId(), tag);

            resetCountTV();
        }

        @Override
        public void onUnselected(Tag tag) {
            selectedList.remove(tag.getId());

            resetCountTV();
        }

        @Override
        public void onDelete(Tag tag) {

        }
    };

    private void resetCountTV() {
        selectedCountTV.setText("(" + selectedList.size() + ")");
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
        toolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    TagSelectionActivity.this.finish();
                }
            }
        });

//        mPresenter.getParentTagList();

        final String[] parent = {
                "第1页",
                "第2页",
                "第3页",
                "第4页",
                "第5页",
                "第6页"
        };

        Tags tags = new Tags();
        for (int i = 0; i < parent.length; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setTagName(parent[i]);
            tags.getList().add(tag);
        }

        resetTabView(tags);

        resetCountTV();
    }

    private void resetTabView(Tags tags) {
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
                return TagListFragment.newInstance(tagListListener);
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
}
