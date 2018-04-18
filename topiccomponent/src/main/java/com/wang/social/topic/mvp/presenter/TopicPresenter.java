package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.TopicRsp;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@FragmentScope
public class TopicPresenter extends
        BasePresenter<TopicContract.Model, TopicContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    // 已选列表
    ArrayList<Tag> selectedList = new ArrayList<>();

    @Inject
    public TopicPresenter(TopicContract.Model model, TopicContract.View view) {
        super(model, view);
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

                            // 更新已选标签列表UI
                            mRootView.refreshSelectedTagLise();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
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

    public String getSelectedTagName(int position) {
        if (null != selectedList && selectedList.size() > position) {
            Tag tag = selectedList.get(position);
            if (null != tag) {
                return tag.getTagName();
            }
        }

        return "";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}