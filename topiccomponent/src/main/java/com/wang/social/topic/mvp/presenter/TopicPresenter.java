package com.wang.social.topic.mvp.presenter;

import com.frame.component.view.barview.BarUser;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.TopicTopUser;
import com.wang.social.topic.mvp.model.entities.TopicTopUsers;

import java.util.ArrayList;
import java.util.List;

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
     * 加载知识魔列表
     */
    public void getReleaseTopicTopUser() {
        mApiHelper.execute(mRootView,
                mModel.getReleaseTopicTopUser(3, 0, "square"),
                new ErrorHandleSubscriber<TopicTopUsers>(mErrorHandler) {
                    @Override
                    public void onNext(TopicTopUsers topicTopUsers) {
                        List<BarUser> list = new ArrayList<>();
                        for (int i = 0; i < Math.min(5, topicTopUsers.getList().size()); i++) {
                            TopicTopUser user = topicTopUsers.getList().get(i);
                            list.add(new BarUser(user.getAvatar()));
                        }

                        mRootView.onTopicTopUserLoaded(list);

                        // 加载标签数据
                        myRecommendTag();
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 加载标签数据
                        myRecommendTag();
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

                            // 更新已选标签列表UI
                            mRootView.onMyRecommendTagListLoad();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {


                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
//                        mRootView.hideLoading();
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

    /**
     * 搜索
     */
    public void search() {
        mRootView.showToast("搜索");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}