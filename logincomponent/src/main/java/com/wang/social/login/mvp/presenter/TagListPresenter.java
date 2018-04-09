package com.wang.social.login.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.login.mvp.contract.TagListContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.Tags;

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
    List<Tag> tagList = new ArrayList<>();

    @Inject
    public TagListPresenter(TagListContract.Model model, TagListContract.View view) {
        super(model, view);
    }

    public int getTagCount() {
        return tagList.size();
    }

    public Tag getTag(int position) {
        if (position >= 0 && position < tagList.size()) {
            return tagList.get(position);
        } else {
            return null;
        }
    }

    /**
     * 加载标签列表
     * @param parentId
     */
    public void loadTagList(int parentId) {
        mApiHelper.execute(mRootView,
                mModel.taglist(100, 0, parentId),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        tagList = tags.getList();

                        mRootView.resetTagListView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
    }

    /**
     * 设置Tag数据为上个页面传过来的选中列表
     * @param list
     */
    public void setSelectedList(List<Tag> list) {
        tagList = list;
    }

    /**
     * 移除Tag
     * @param tag
     */
    public void removeTag(Tag tag) {
        if (tagList.remove(tag)) {
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