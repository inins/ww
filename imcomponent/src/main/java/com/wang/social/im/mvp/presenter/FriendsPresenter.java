package com.wang.social.im.mvp.presenter;

import com.frame.component.entities.FriendInfo;
import com.frame.component.enums.Gender;
import com.frame.di.scope.FragmentScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.BasePresenter;
import com.wang.social.im.mvp.contract.FriendsContract;
import com.wang.social.im.mvp.model.entities.IndexFriendInfo;
import com.wang.social.im.mvp.model.entities.ListData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 9:40
 * ============================================
 */
@FragmentScope
public class FriendsPresenter extends BasePresenter<FriendsContract.Model, FriendsContract.View> {

    @Inject
    ApiHelper mApiHelper;

    @Inject
    public FriendsPresenter(FriendsContract.Model model, FriendsContract.View view) {
        super(model, view);
    }

    public void getFriendsList() {
        mRootView.hideLoading();
        List<IndexFriendInfo> indexFriendInfos = new ArrayList<>();
        indexFriendInfos.add(getFriend("阿炳"));
        indexFriendInfos.add(getFriend("刘洋"));
        indexFriendInfos.add(getFriend("乐山大佛"));
        indexFriendInfos.add(getFriend("认为"));
        indexFriendInfos.add(getFriend("哒"));
        indexFriendInfos.add(getFriend("地方"));
        indexFriendInfos.add(getFriend("阿拉贡"));
        indexFriendInfos.add(getFriend("士大夫"));
        indexFriendInfos.add(getFriend("大师傅似的"));
        indexFriendInfos.add(getFriend("和任何人"));
        indexFriendInfos.add(getFriend("去地方"));
        indexFriendInfos.add(getFriend("插"));
        indexFriendInfos.add(getFriend("和人有体温"));
        indexFriendInfos.add(getFriend("@sdfgasf"));
        mRootView.showFriends(indexFriendInfos);
//        mApiHelper.execute(mRootView, mModel.getFriendList(),
//                new ErrorHandleSubscriber<ListData<IndexFriendInfo>>() {
//                    @Override
//                    public void onNext(ListData<IndexFriendInfo> friendInfoListData) {
//                        mRootView.showFriends(friendInfoListData.getList());
//                    }
//                }, new Consumer<Disposable>() {
//                    @Override
//                    public void accept(Disposable disposable) throws Exception {
//                        mRootView.showLoading();
//                    }
//                }, new Action() {
//                    @Override
//                    public void run() throws Exception {
//                        mRootView.hideLoading();
//                    }
//                });
    }

    private IndexFriendInfo getFriend(String name){
        IndexFriendInfo indexFriendInfo = new IndexFriendInfo();
        indexFriendInfo.setNickname(name);
        indexFriendInfo.setPortrait("");
        indexFriendInfo.setAge(18);
        indexFriendInfo.setGender(Gender.FEMALE);
        indexFriendInfo.setConstellation("白羊座");
        indexFriendInfo.setTags(new ArrayList<>());
        return indexFriendInfo;
    }
}