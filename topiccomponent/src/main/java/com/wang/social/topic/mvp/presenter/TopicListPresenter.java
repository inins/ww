package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicListContract;
import com.wang.social.topic.mvp.model.entities.Tag;
import com.wang.social.topic.mvp.model.entities.Tags;
import com.wang.social.topic.mvp.model.entities.Topic;
import com.wang.social.topic.mvp.model.entities.TopicRsp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


@FragmentScope
public class TopicListPresenter extends
        BasePresenter<TopicListContract.Model, TopicListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    List<Topic> mTopicList = new ArrayList<>();

    @Inject
    public TopicListPresenter(TopicListContract.Model model, TopicListContract.View view) {
        super(model, view);
    }

    private static int count = 0;
    private Topic getTestTopic() {
        Topic topic = new Topic();

        topic.setCreateTime(System.currentTimeMillis());
        topic.setIsFree((count++) % 2);
        List<Tag> tags = new ArrayList<>();
        Tag tag = new Tag();
        tag.setTagName("社交软件");
        tags.add(tag);
        tags.add(tag);
        tags.add(tag);
        topic.setTags(tags);
        topic.setTitle("大家对往往社交怎么看？");
        topic.setFirstStrff("往往一款专注兴趣交友的APP，独特的群部落文化，帮助用户寻找“知音”！");
        topic.setUserCover("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1373411777,3992091759&fm=27&gp=0.jpg");
        topic.setUserName("往往官方");
        topic.setTopicReadNum(11);
        topic.setTopicSupportNum(22);
        topic.setTopicCommentNum(33);

        return topic;
    }

    public Topic getTopic(int position) {
//        return getTestTopic();

        if (position >= 0 && position < mTopicList.size()) {
            return mTopicList.get(position);
        }

        return null;
    }

    public int getTopicCount() {
        return mTopicList.size();
//        return 10;
    }

    /**
     * 或者最新话题列表
     */
    public void getNewsList() {
        mApiHelper.execute(mRootView,
                mModel.getNewsList("1", 20, 1),
                new ErrorHandleSubscriber<TopicRsp>(mErrorHandler) {

                    @Override
                    public void onNext(TopicRsp rsp) {
                        if (null != rsp) {
                            mTopicList.addAll(rsp.getList());

                            mRootView.onTopicLoaded();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToast(e.getMessage());
//                        mRootView.onTopicLoaded();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}