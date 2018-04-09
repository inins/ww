package com.wang.social.login.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.ToastUtil;
import com.wang.social.login.mvp.contract.TagSelectionContract;
import com.wang.social.login.mvp.model.entities.Tag;
import com.wang.social.login.mvp.model.entities.dto.Tags;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

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

    public void getParentTagList() {
        mApiHelper.execute(mRootView,
                mModel.passwordLogin(),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        mRootView.resetTabView(tags);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 加载失败，加载测试数据
                        final String[] parent = {
                                "第1页",
                                "第2页",
                                "第3页",
                                "第4页",
                                "第5页",
                                "第6页"
                        };

                        Tags tags = new Tags();
                        for (int i = 0; i < parent.length; i++) {
                            Tag tag = new Tag();
                            tag.setId(i);
                            tag.setTagName(parent[i]);
                            tags.getList().add(tag);
                        }

                        mRootView.resetTabView(tags);
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


    @Subscriber
    private void selectedTagDeletedByConfirm(Tag tag) {
        ToastUtil.showToastLong("删除");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}