package com.wang.social.login.mvp.ui.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.base.BaseFragment;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.di.component.DaggerTagListComponent;
import com.wang.social.login.di.module.TagListModule;
import com.wang.social.login.mvp.contract.TagListContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.presenter.TagListPresenter;
import com.wang.social.login.mvp.ui.widget.adapter.TagAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TagListFragment extends BaseFragment<TagListPresenter> implements
        TagListContract.View {
    public final static String MODE_SELECTION = "MODE_SELECTION";
    public final static String MODE_DELETE = "MODE_DELETE";
    public final static String NAME_SELECTED_LIST = "NAME_SELECTED_LIST";
    public final static String NAME_PARENT_ID = "NAME_PARENT_ID";

    /**
     *  返回TagListFragment
     * @param list 选中列表(主要在 兴趣大杂烩时使用)
     * @return
     */
    public static TagListFragment newInstance(int parentId, ArrayList<Tag> list) {
        TagListFragment fragment = new TagListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(NAME_PARENT_ID, parentId);
        if (null != list) {
            bundle.putParcelableArrayList(NAME_SELECTED_LIST, list);
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {
        ToastUtil.showToastLong(msg);
    }

    @Override
    public void tagListChanged() {
        tagAdapter.notifyDataSetChanged();
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    TagAdapter tagAdapter;
    String mode = MODE_SELECTION;

    // 父标签id
    int parentId = 0;

    private TagAdapter.TagDataProvider tagDataProvider = new TagAdapter.TagDataProvider() {
        @Override
        public int getItemCount() {
            return mPresenter.getTagCount();
        }

        @Override
        public Tag getItem(int position) {
            return mPresenter.getTag(position);
        }

        @Override
        public boolean isDeleteMode() {
            return mode.equals(MODE_DELETE);
        }
    };

    private TagAdapter.TagClickListener tagClickListener = new TagAdapter.TagClickListener() {
        @Override
        public void onTagClick(Tag tag) {
            boolean selected = mPresenter.tagClick(tag);
        }

        @Override
        public void onDelete(Tag tag) {
            // 将tag从列表删除
            mPresenter.removeTag(tag);
        }
    };

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerTagListComponent.builder()
                .appComponent(appComponent)
                .tagListModule(new TagListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.login_fragment_tag_list;
    }



    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        List<Tag> selectedList = null;
        if (getArguments() != null) {
            selectedList = getArguments().getParcelableArrayList(NAME_SELECTED_LIST);
            parentId = getArguments().getInt(NAME_PARENT_ID);
        }

        if (null != selectedList && selectedList.size() > 0) {
            // 如果有传选中list做来，则说明是删除模式
            mPresenter.setSelectedList(selectedList);
            mode = MODE_DELETE;

            resetTagListView();
        } else {
            // 没有传入选中列表，则需要加载数据
            mPresenter.loadTagList(parentId);
        }
    }


    @Override
    public void resetTagListView() {
        tagAdapter = new TagAdapter(getContext(), tagDataProvider, tagClickListener);

        // RecyclerView 相关设置
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(
                ChipsLayoutManager.newBuilder(getContext())
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .build());
        recyclerView.addItemDecoration(
                new SpacingItemDecoration(SizeUtils.dp2px(5), SizeUtils.dp2px(5)));
        recyclerView.setAdapter(tagAdapter);
    }


    @Override
    public void setData(@Nullable Object data) {
    }
}
