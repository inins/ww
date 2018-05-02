package com.frame.component.ui.acticity.tags;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

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

    /**
     * 编辑推荐标签
     */
    public void updateRecommendTag() {
        mApiHelper.executeNone(mRootView,
                mModel.updateRecommendTag(selectedList),
                new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson o) {
//                        mRootView.showToast(o.toString());
                        // 编辑推荐标签成功
                        mRootView.onUpdateTagSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });

    }

    /**
     * 获取一级标签列表(标签页Tab标题)
     */
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
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 已选推荐标签列表(兴趣标签)
     */
    public void myRecommendTag() {
        mApiHelper.execute(mRootView,
                // 这里需要一次获取所有的标签，所以给一个很大的数字
                mModel.myRecommendTag(Integer.MAX_VALUE, 0),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        if (null != tags) {
                            selectedList = (ArrayList<Tag>) tags.getList();
                        }
                        mRootView.refreshCountTV();

                        // 开始加载标签数据
                        getParentTagList();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        mRootView.showToast(e.getMessage());
                        // 如果失败则认为没有已选标签
                        // 开始加载标签数据
                        getParentTagList();
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }

    public void setSelectedListFromBundle(String idsStr) {
        if (null != idsStr) {
            selectedList.clear();

            String[] ids = idsStr.split(",");
            for (String id : ids) {
                Tag tag = new Tag();
                tag.setId(Integer.parseInt(id));
                selectedList.add(tag);
            }
        }
    }


    /**
     * 编辑推荐标签
     */
    public void findMyTagCount() {
        mApiHelper.execute(mRootView,
                mModel.findMyTagCount(),
                new ErrorHandleSubscriber<PersonalTagCount>(mErrorHandler) {

                    @Override
                    public void onNext(PersonalTagCount tagCount) {
                        mRootView.setMyTagCount(tagCount.getTagCount());

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
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });
    }

    /**
     * 编辑推荐标签
     */
    public void addPersonalTag() {
        mApiHelper.executeNone(mRootView,
                mModel.addPersonalTag(selectedList),
                new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
                    @Override
                    public void onNext(BaseJson o) {
//                        mRootView.showToast(o.toString());
                        // 编辑标签成功
                        mRootView.onUpdateTagSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                });

    }


    /**
     * 选中Tag
     *
     * @param tag
     * @return
     */
    public void selectTag(Tag tag) {
        selectedList.add(tag);
    }

    /**
     * 取消选择
     *
     * @param tag
     */
    public void unselectTag(Tag tag) {
        if (null == tag) return;

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
     *
     * @return 选中数量
     */
    public int getSelectedTagCount() {
        return selectedList.size();
    }

    /**
     * 设置选中列表
     *
     * @param list
     */
    public void setSelectedTagList(ArrayList<Tag> list) {
        selectedList = list;
    }

    /**
     * 返回选中列表
     *
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