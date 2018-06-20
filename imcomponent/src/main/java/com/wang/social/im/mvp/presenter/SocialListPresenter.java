package com.wang.social.im.mvp.presenter;

import com.frame.component.utils.UIUtil;
import com.frame.di.scope.ActivityScope;
import com.frame.entities.EventBean;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.BasePresenter;
import com.frame.utils.RxLifecycleUtils;
import com.tencent.imsdk.TIMManager;
import com.wang.social.im.R;
import com.wang.social.im.mvp.contract.SocialListContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.SimpleGroupInfo;
import com.wang.social.im.mvp.model.entities.SocialListLevelOne;
import com.wang.social.im.mvp.model.entities.SocialListLevelThree;
import com.wang.social.im.mvp.model.entities.SocialListLevelTwo;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.SimpleGroupInfoDTO;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 9:54
 * ============================================
 */
@ActivityScope
public class SocialListPresenter extends BasePresenter<SocialListContract.Model, SocialListContract.View> {

    @Inject
    public SocialListPresenter(SocialListContract.Model model, SocialListContract.View view) {
        super(model, view);
    }

    public void getSocials(boolean needLoading) {
        mModel.getBeinGroups()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (needLoading)
                            mRootView.showLoading();
                    }
                })
                .map(new Function<BaseJson<ListDataDTO<SimpleGroupInfoDTO, SimpleGroupInfo>>, ListData<SimpleGroupInfo>>() {
                    @Override
                    public ListData<SimpleGroupInfo> apply(BaseJson<ListDataDTO<SimpleGroupInfoDTO, SimpleGroupInfo>> t) throws Exception {
                        if (t.getData() == null) {
                            ListData<SimpleGroupInfo> listData = new ListData<SimpleGroupInfo>();
                            listData.setList(new ArrayList<SimpleGroupInfo>());
                            return listData;
                        }
                        return t.getData().transform();
                    }
                })
                .map(new Function<ListData<SimpleGroupInfo>, List<SocialListLevelOne>>() {
                    @Override
                    public List<SocialListLevelOne> apply(ListData<SimpleGroupInfo> simpleGroupInfoListData) throws Exception {
                        //组装数据
                        List<SocialListLevelOne> list = new ArrayList<>();
                        List<SocialListLevelTwo> master = new ArrayList<>();
                        List<SocialListLevelTwo> member = new ArrayList<>();
                        for (SimpleGroupInfo groupInfo : simpleGroupInfoListData.getList()) {
                            SocialListLevelTwo levelTwo = new SocialListLevelTwo();
                            levelTwo.setName(groupInfo.getName());
                            levelTwo.setAvatar(groupInfo.getAvatar());
                            levelTwo.setId(groupInfo.getId());
                            levelTwo.setMemberCount(groupInfo.getMemberCount());

                            if (groupInfo.isSocial()) {
                                List<TeamInfo> teams = new ArrayList<>();
                                //获取觅聊列表
                                for (SimpleGroupInfo group : simpleGroupInfoListData.getList()) {
                                    //判断是否为当前趣聊的觅聊
                                    if (!group.isSocial() && group.getPid().equals(groupInfo.getId())) {
                                        TeamInfo teamInfo = new TeamInfo();
                                        teamInfo.setTeamId(group.getId());
                                        teamInfo.setName(group.getName());
                                        teamInfo.setCover(group.getAvatar());
                                        teamInfo.setMemberSize(group.getMemberCount());
                                        teams.add(teamInfo);
                                    }
                                }

                                SocialListLevelThree levelThree = new SocialListLevelThree();
                                levelThree.setTeams(teams);
                                List<SocialListLevelThree> levelThrees = new ArrayList<>();
                                if (!teams.isEmpty()) {
                                    levelThrees.add(levelThree);
                                }
                                levelTwo.setTeams(levelThrees);

                                if (groupInfo.getMasterUid().equals(TIMManager.getInstance().getLoginUser())) {
                                    master.add(levelTwo);
                                } else {
                                    member.add(levelTwo);
                                }
                            }
                        }
                        SocialListLevelOne levelOneMaster = new SocialListLevelOne();
                        levelOneMaster.setTitle(UIUtil.getString(R.string.im_me_created_social));
                        levelOneMaster.setSocials(master);
                        SocialListLevelOne levelOneMember = new SocialListLevelOne();
                        levelOneMember.setTitle(UIUtil.getString(R.string.im_me_joined_social));
                        levelOneMember.setSocials(member);
                        list.add(levelOneMaster);
                        list.add(levelOneMember);
                        return list;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (needLoading)
                            mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<List<SocialListLevelOne>>() {
                    @Override
                    public void onNext(List<SocialListLevelOne> socialListLevelOnes) {
                        mRootView.showSocials(socialListLevelOnes);
                    }

//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof NullPointerException) {
//                            List<SocialListLevelOne> list = new ArrayList<>();
//                            SocialListLevelOne levelOneMaster = new SocialListLevelOne();
//                            levelOneMaster.setTitle(UIUtil.getString(R.string.im_me_created_social));
//                            levelOneMaster.setSocials(new ArrayList<>());
//                            SocialListLevelOne levelOneMember = new SocialListLevelOne();
//                            levelOneMember.setTitle(UIUtil.getString(R.string.im_me_joined_social));
//                            levelOneMember.setSocials(new ArrayList<>());
//                            mRootView.showSocials(list);
//                        } else {
//                            super.onError(e);
//                        }
//                    }
                });
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onCommentEvent(EventBean eventBean) {
        if (eventBean.getEvent() == EventBean.EVENT_NOTIFY_GROUP_ADD
                || eventBean.getEvent() == EventBean.EVENT_NOTIFY_GROUP_DELETE) {
            getSocials(false);
        }
    }
}
