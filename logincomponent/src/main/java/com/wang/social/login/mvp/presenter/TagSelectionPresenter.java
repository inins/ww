package com.wang.social.login.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.login.mvp.contract.TagSelectionContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.Tags;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@ActivityScope
public class TagSelectionPresenter extends
        BasePresenter<TagSelectionContract.Model, TagSelectionContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;


    // 已选列表
    ArrayList<Tag> selectedList = new ArrayList<>();

    @Inject
    public TagSelectionPresenter(TagSelectionContract.Model model, TagSelectionContract.View view) {
        super(model, view);
    }

    public void updateRecommendTag() {
        mApiHelper.execute(mRootView,
                mModel.updateRecommendTag(selectedList),
                new ErrorHandleSubscriber(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
//                        mRootView.showToast(o.toString());
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

    public void getParentTagList() {
        mApiHelper.execute(mRootView,
                mModel.parentTagList(),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        mRootView.resetTabView(tags);
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

    public void myRecommendTag() {
        mApiHelper.execute(mRootView,
                // 这里需要一次获取所有的标签，所以给一个很大的数字
                mModel.myRecommendTag(Integer.MAX_VALUE, 0),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        selectedList = (ArrayList<Tag>) tags.getList();
                        mRootView.refreshCountTV();

                        // 开始加载标签数据
                        getParentTagList();
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
     * 选中Tag
     * @param tag
     * @return
     */
    public void selectTag(Tag tag) {
        selectedList.add(tag);
    }

    /**
     * 取消选择
     * @param tag
     */
    public void unselectTag(Tag tag) {
        int i;
        boolean unselect = false;
        for (i = 0; i < selectedList.size(); i++) {
            Tag t = selectedList.get(i);
            if (t.getId() == tag.getId()) {
                unselect = true;
                break;
            }
        }

        if (unselect) {
            selectedList.remove(i);
        }
    }

    public boolean isSelected(Tag tag) {
        for (Tag t : selectedList) {
            if (t.getId() == tag.getId()) {
                return true;
            }
        }

        return false;
    }

    /**
     * 选中数量
     * @return
     */
    public int getSelectedTagCount() {
        return selectedList.size();
    }

    /**
     * 设置选中列表
     * @param list
     */
    public void setSelectedTagList(ArrayList<Tag> list) {
        selectedList = list;
    }

    /**
     * 返回选中列表
     * @return
     */
    public ArrayList<Tag> getSelectedList() {
        return selectedList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}