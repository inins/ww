package com.wang.social.topic.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.TopicDetailContract;
import com.wang.social.topic.mvp.model.entities.TopicDetail;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityScope
public class TopicDetailPresenter extends
        BasePresenter<TopicDetailContract.Model, TopicDetailContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;


    @Inject
    public TopicDetailPresenter(TopicDetailContract.Model model, TopicDetailContract.View view) {
        super(model, view);
    }

    private TopicDetail getTestTopicDetail() {
        TopicDetail object = new TopicDetail();

        object.setTitle("纪录片意外走红B站：冷门纪录片要“征服”资本？");
        object.setTags(new ArrayList<String>() {
            {
                add("哔哩哔哩");
                add("哔哩哔哩");
                add("哔哩哔哩");
            }
        });
        object.setBackgroundImage("http://pic.baike.soso.com/p/20131204/20131204144511-2141128146.jpg");
        object.setCreateTime(System.currentTimeMillis());
        object.setAvatar("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1373411777,3992091759&fm=27&gp=0.jpg");
        object.setNickname("埃米尔");
        object.setBirthday(0L);
        object.setContent("<!DOCTYPE html><html lang=\\\"zh\\\"><head><meta charset=\\\"utf-8\\\" \\/><title>wwrichtextTittle<\n" +
                " \\/title><meta name=\\\"HandheldFriendly\\\" content=\\\"True\\\" \\/><meta name=\\\"MobileOptimized\\\" content=\\\"320\\\" \\/>\n" +
                " <meta name=\\\"viewport\\\" content=\\\"width=device-width, initial-scale=1\\\" \\/><script>function playAudio(url){var\n" +
                "  musicPlay=document.getElementById('musicPlay');musicPlay.setAttribute('src',url);musicPlay.play();}<\\/script>\n" +
                " <style>img{max-width: 100%;-webkit-border-radius: 20px;border-radius: 20px;border: 0px solid black; p{line-hei\n" +
                " ght: 1.6rem !important;}}<\\/style><\\/head><body><audio src=\\\"\\\" id=\\\"musicPlay\\\"> <\\/audio>uuu 的回电话。跑步时要学会自己照顾\n" +
                " 自己。我的心已经飞到点开始。 &nbsp; &nbsp; &nbsp;你要去去看<\\/body><\\/html>");

        object.setSex(0);


        return object;
    }

    public void test() {
        mRootView.onTopicDetailLoadSuccess(getTestTopicDetail());
    }

    public void getTopicDetails(int topicId) {

            mApiHelper.execute(mRootView,
                    mModel.getTopicDetails(topicId),
                    new ErrorHandleSubscriber<TopicDetail>(mErrorHandler) {
                        @Override
                        public void onNext(TopicDetail topicDetail) {
                            // 获取详情成功
                            mRootView.onTopicDetailLoadSuccess(topicDetail);
                        }

                        @Override
                        public void onError(Throwable e) {
                            mRootView.showToast(e.getMessage());
                        }
                    }
                    , new Consumer<Disposable>() {
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