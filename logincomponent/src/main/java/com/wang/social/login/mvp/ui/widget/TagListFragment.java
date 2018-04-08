package com.wang.social.login.mvp.ui.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.base.BasicFragment;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.login.R;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.ui.widget.adapter.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class TagListFragment extends BasicFragment {
    public final static String MODE_SELECTION = "MODE_SELECTION";
    public final static String MODE_DELETE = "MODE_DELETE";

    public static TagListFragment newInstance(TagListListener tagListListener) {
        TagListFragment fragment = new TagListFragment();
        fragment.setTagListListener(tagListListener);
        return fragment;
    }

    public interface TagListListener {
        /**
         * 选中
         * @param tag
         */
        void onSelected(Tag tag);

        /**
         *  取消选中
         * @param tag
         */
        void onUnselected(Tag tag);

        /**
         * 删除
         * @param tag
         */
        void onDelete(Tag tag);
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    TagAdapter tagAdapter;
    String mode = MODE_SELECTION;

    TagListListener tagListListener;

    private TagAdapter.TagDataProvider tagDataProvider = new TagAdapter.TagDataProvider() {
        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public Tag getItem(int position) {
            return list.get(position);
        }

        @Override
        public boolean isDeleteMode() {
            return mode.equals(MODE_DELETE);
        }
    };

    private TagAdapter.TagClickListener tagClickListener = new TagAdapter.TagClickListener() {
        @Override
        public void onTagClick(Tag tag) {
            if (null != tag) {
                tag.clickTag();

                if (null != tagListListener) {
                    if (tag.isPersonalTag()) {
                        // 选中
                        tagListListener.onSelected(tag);
                    } else {
                        // 取消
                        tagListListener.onUnselected(tag);
                    }
                }

                tagAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onDelete(Tag tag) {
            ToastUtil.showToastLong("delete " + tag.getId() + " " + tag.getTagName());
        }
    };

    public void setTagListListener(TagListListener tagListListener) {
        this.tagListListener = tagListListener;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.login_fragment_tag_list;
    }

    private Tag getTagById(int id) {
        for (Tag tag : list) {
            if (tag.getId() == id) {
                return tag;
            }
        }

        return null;
    }


    final String[] names = {
        "徒步旅行",
            "猫咪",
            "容多肉植物",
            "火锅",
            "狗狗",
            "互联网",
            "火锅",
            "狗狗",
            "互联网",
            "火锅",
            "狗狗",
            "互联网",
            "火锅",
            "狗狗",
            "互联网",
            "中国好声音",
            "健身",
            "互联网",
            "成都麻将",
            "容多肉植物"
    };
    private List<Tag> list = new ArrayList<>();
    private void initTestData() {
        for (int i = 0; i < 50; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setTagName(names[i % names.length]);
            list.add(tag);
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTestData();

        tagAdapter = new TagAdapter(getContext(), tagDataProvider, tagClickListener);

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
