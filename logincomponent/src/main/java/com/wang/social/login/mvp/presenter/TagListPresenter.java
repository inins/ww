package com.wang.social.login.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.login.mvp.contract.TagListContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.Tags;
import com.wang.social.login.mvp.ui.widget.adapter.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class TagListPresenter extends
        BasePresenter<TagListContract.Model, TagListContract.View>  {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    // 存放Tag的List
    List<Tag> tags = new ArrayList<>();


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

    private void initTestData() {
        for (int i = 0; i < 50; i++) {
            Tag tag = new Tag();
            tag.setId(i);
            tag.setTagName(names[i % names.length]);
            tags.add(tag);
        }
    }

    @Inject
    public TagListPresenter(TagListContract.Model model, TagListContract.View view) {
        super(model, view);
    }

    public int getTagCount() {
        return tags.size();
    }

    public Tag getTag(int position) {
        if (position >= 0 && position < tags.size()) {
            return tags.get(position);
        } else {
            return null;
        }
    }

    /**
     * 加载标签列表
     * @param parentId
     */
    public void loadTagList(int parentId) {
        // 测试数据
        initTestData();
    }

    /**
     * 设置Tag数据为上个页面传过来的选中列表
     * @param list
     */
    public void setSelectedList(List<Tag> list) {
        tags = list;
    }

    /**
     * 移除Tag
     * @param tag
     */
    public void removeTag(Tag tag) {
        if (tags.remove(tag)) {
            // 更新UI
            mRootView.tagListChanged();
        }
    }

    /**
     * Tag点击
     * @param tag
     * @return
     */
    public boolean tagClick(Tag tag) {
        if (null != tag) {
            tag.clickTag();

            return tag.isPersonalTag();
        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}