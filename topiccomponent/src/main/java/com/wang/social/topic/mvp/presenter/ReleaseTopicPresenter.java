package com.wang.social.topic.mvp.presenter;

import android.support.annotation.IntDef;

import com.frame.component.helper.QiNiuManager;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;
import com.wang.social.topic.mvp.model.entities.Template;
import com.wang.social.topic.utils.StringUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

@ActivityScope
public class ReleaseTopicPresenter extends
        BasePresenter<ReleaseTopicContract.Model, ReleaseTopicContract.View> {

    private final static int COMMIT_STATE_IDLE = 0;
    private final static int COMMIT_STATE_COVER_IMAGE = 1;
    private final static int COMMIT_STATE_CONTENT_ATTACHMENT = 2;
    private final static int COMMIT_STATE_TOPIC = 3;

    @IntDef( {
            COMMIT_STATE_IDLE,
            COMMIT_STATE_COVER_IMAGE,
            COMMIT_STATE_CONTENT_ATTACHMENT,
            COMMIT_STATE_TOPIC
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface CommitState {}

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;
    @Inject
    QiNiuManager qiNiuManager;
    // 背景音乐
    private Music mBGMusic;
    // 模板
    private Template mTemplate;
    // 背景图片 封面图片
    private String mBackgroundImage;
    // 内容
    private String mContent;

    private Map<String, Object> mNetParam = new LinkedHashMap<>();

    private @CommitState int mCommitState = COMMIT_STATE_IDLE;

    public ReleaseTopicPresenter resetNetParam() {
        mNetParam.clear();
        mNetParam.put("v", "2.0.0");
        return this;
    }

    public ReleaseTopicPresenter setInfoUserIds(String informUserIds) {
        mNetParam.put("informUserIds", informUserIds);
        return this;
    }

    public ReleaseTopicPresenter setBackgroundMusicId() {
        if (null != mBGMusic) {
            mNetParam.put("backgroundMusicId", mBGMusic.getMusicId());
        }
        return this;
    }

    public ReleaseTopicPresenter setBackgroundMusicId(int id) {
        if (null != mBGMusic) {
            mNetParam.put("backgroundMusicId", id);
        }
        return this;
    }

    public ReleaseTopicPresenter setBackgroundImage(String backgroundImage) {
        mNetParam.put("backgroundImage", backgroundImage);
        return this;
    }

    public ReleaseTopicPresenter setTemplateId() {
        if (null != mTemplate) {
            mNetParam.put("templateId", mTemplate.getId());
        }
        return this;
    }

    public ReleaseTopicPresenter setAddress(String address) {
        mNetParam.put("address", address);
        return this;
    }

    public ReleaseTopicPresenter setAdcode(String adcode) {
        mNetParam.put("adcode", adcode);
        return this;
    }

    public ReleaseTopicPresenter setLatitude(String latitude) {
        mNetParam.put("latitude", latitude);
        return this;
    }

    public ReleaseTopicPresenter setLongitude(String longitude) {
        mNetParam.put("longitude", longitude);
        return this;
    }

    public ReleaseTopicPresenter setGemston(int gemstone) {
        mNetParam.put("gemstone", gemstone);
        return this;
    }

    public ReleaseTopicPresenter setRelateState(int relateState) {
        mNetParam.put("relateState", relateState);
        return this;
    }

    public ReleaseTopicPresenter setTitle(String title) {
        mNetParam.put("title", title);
        return this;
    }

    public ReleaseTopicPresenter setContent(String content) {
        mNetParam.put("content", content);
        return this;
    }

    public ReleaseTopicPresenter setFirstStrff(String firstStrff) {
        mNetParam.put("firstStrff", firstStrff);
        return this;
    }

    public ReleaseTopicPresenter setTagIds(String tagIds) {
        mNetParam.put("tagIds", tagIds);
        return this;
    }

    @Inject
    public ReleaseTopicPresenter(ReleaseTopicContract.Model model, ReleaseTopicContract.View view) {
        super(model, view);
    }

    public Music getBGMusic() {
        return mBGMusic;
    }

    public void setBGMusic(Music BGMusic) {
        mBGMusic = BGMusic;
    }

    public Template getTemplate() {
        return mTemplate;
    }

    public void setTemplate(Template template) {
        mTemplate = template;
    }

    public void addTopic(String title, String content, String firstStrff,
                         String tagIds, int relateState, int gemstone,
                         String longitude, String latitude, String adcode,
                         String address, int templateId, String backgroundImage,
                         int backgroundMusicId, String informUserIds) {
        setCommitState(COMMIT_STATE_IDLE);

        // 不需要上传的参数先封装
        resetNetParam();
        setTitle(title);
        setTagIds(tagIds);
        setBackgroundMusicId(backgroundMusicId);

        // 记录可能需要上传的参数
        mBackgroundImage = backgroundImage;
        mContent = content;

        // 检测封面图片是否是url
        if (!StringUtil.isURL(backgroundImage)) {
            // 封面图片需要上传
            commitBackgroundImage(backgroundImage);
            return;
        } else {
            setBackgroundImage(backgroundImage);
        }

        // 检测内容里面有哪些内容需要上传
        if (checkContent(content)) {
            // 上传内容附件
            commitContentAttachment();
            return;
        } else {
            setContent(content);
        }

        addTopic();
    }

    /**
     * 检测content是否有内容需要上传
     *
     * @param content 内容
     */
    public boolean checkContent(String content) {
        // 查找内功中是否有需要上传的资源

        setContent(content);
        return false;
    }

    /**
     * 上传内容里面的附件（图片 音频 等）
     */
    public void commitContentAttachment() {
        if (!checkContent(mContent)) {
            // 没有内容需要上传了，直接上传话题
            addTopic();
            return;
        }

        setCommitState(COMMIT_STATE_CONTENT_ATTACHMENT);

        mRootView.showLoading();
    }

    /**
     * 上传背景图片
     * @param path 图片路径
     */
    public void commitBackgroundImage(String path) {
        Timber.i("上传背景图片 " + path);
        setCommitState(COMMIT_STATE_COVER_IMAGE);

        netUploadCommit(path);

        // 显示加载对话框
        mRootView.showLoading();
    }

    public void setCommitState(int commitState) {
        mCommitState = commitState;
    }

    /**
     * 上传话题
     */
    public void addTopic() {
        setCommitState(COMMIT_STATE_TOPIC);

        mApiHelper.executeForData(mRootView,
                mModel.addTopic(mNetParam),
                new ErrorHandleSubscriber(mErrorHandler) {

                    @Override
                    public void onNext(Object o) {

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


    //上传图片
    private void netUploadCommit(String path) {
        qiNiuManager.uploadFile(mRootView, path, new QiNiuManager.OnSingleUploadListener() {
            @Override
            public void onSuccess(String url) {
                Timber.i("上传资源成功 " + url);
                if (mCommitState == COMMIT_STATE_COVER_IMAGE) {
                     // 上传封面图片成功
                    mBackgroundImage = url;
                    setBackgroundImage(mBackgroundImage);

                    // 上传内容附件
                    commitContentAttachment();
                } else if (mCommitState == COMMIT_STATE_CONTENT_ATTACHMENT) {
                    // 上传内容附件成功

                }
            }

            @Override
            public void onFail() {
                // 上传附件失败
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