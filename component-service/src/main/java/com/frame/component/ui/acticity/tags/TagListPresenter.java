package com.frame.component.ui.acticity.tags;

import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


@FragmentScope
public class TagListPresenter extends
        BasePresenter<TagListContract.Model, TagListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

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
     * 新加载的标签列表，可能在已选列表中，所以需要检测,这应该只需要在兴趣标签模式下进行
     *
     * @param tags 从服务器获取的标签数据
     * @param list 已经选择的标签列表
     */
    private void checkTagState(Tags tags, List<Tag> list) {
        tagList = tags.getList();

        // 获取的新标签需要先判断是否已经被选择了
        for (Tag t1 : tagList) {
            // 先设置为未选中，因为存在可能，加载的新列表里面是已选中状态，但是在
            // 兴趣大杂烩里面已经将他删除了,所以只有选中列表的Tag才是选中状态
            t1.setUnselected();
            for (Tag t2 : list) {
                if (t1.getId() == t2.getId()) {
                    t1.setSelected();
                    t2.setTagName(t1.getTagName());
                }
            }
        }
    }

    /**
     * 加载标签列表
     *
     * @param parentId
     */
    public void loadTagList(int parentId, List<Tag> list) {
        mApiHelper.execute(mRootView,
                mModel.taglist(Integer.MAX_VALUE, 0, parentId),
                new ErrorHandleSubscriber<Tags>(mErrorHandler) {

                    @Override
                    public void onNext(Tags tags) {
                        Observable.create(new ObservableOnSubscribe<Integer>() {

                            @Override
                            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                                checkTagState(tags, list);

                                emitter.onComplete();
                            }
                        })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Integer>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        compositeDisposable.add(d);
                                    }

                                    @Override
                                    public void onNext(Integer integer) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        mRootView.resetTagListView();
                                    }
                                });
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
     *
     * @param list
     */
    public void setSelectedList(List<Tag> list) {
        tagList = list;
    }

    /**
     * 移除Tag
     *
     * @param tag
     */
    public void removeTag(Tag tag) {
        if (tagList.remove(tag)) {
            // 更新UI
            mRootView.tagListChanged();
        }
    }


    public void unselectTag(Tag tag) {
        for (Tag t : tagList) {
            if (t.getId() == tag.getId()) {
                t.setUnselected();

                mRootView.tagListChanged();
                return;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        mErrorHandler = null;
        mApiHelper = null;
    }
}