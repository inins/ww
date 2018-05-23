package com.wang.social.im.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.helper.RepositoryHelper;
import com.wang.social.im.interfaces.ImCallBack;
import com.wang.social.im.mvp.contract.MemberListContract;
import com.wang.social.im.mvp.model.entities.ListData;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.mvp.model.entities.dto.ListDataDTO;
import com.wang.social.im.mvp.model.entities.dto.MemberInfoDTO;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Create by ChenJing on 2018-05-09 20:02
 * ============================================
 */
@ActivityScope
public class MemberListPresenter extends BasePresenter<MemberListContract.Model, MemberListContract.View> {

    @Inject
    ApiHelper mApiHelper;
    @Inject
    RxErrorHandler mErrorHandler;

    @Inject
    public MemberListPresenter(MemberListContract.Model model, MemberListContract.View view) {
        super(model, view);
    }

    public void getMembers(String groupId) {
        mModel.getMembers(groupId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mRootView.showLoading();
                    }
                })
                .map(new Function<BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>>, ListData<MemberInfo>>() {
                    @Override
                    public ListData<MemberInfo> apply(BaseJson<ListDataDTO<MemberInfoDTO, MemberInfo>> t) throws Exception {
                        return t.getData().transform();
                    }
                })
                .map(new Function<ListData<MemberInfo>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> apply(ListData<MemberInfo> simpleGroupInfoListData) throws Exception {
                        int memberCount = 0;
                        int friendCount = 0;
                        MemberInfo master = null;
                        List<MemberInfo> groupOne = new ArrayList<>(); //90后
                        List<MemberInfo> groupTwo = new ArrayList<>(); //95后
                        List<MemberInfo> groupThree = new ArrayList<>(); //00后
                        List<MemberInfo> groupFour = new ArrayList<>(); //其他
                        //组装数据
                        for (MemberInfo memberInfo : simpleGroupInfoListData.getList()) {
                            memberCount++;
                            if (memberInfo.getRole() == MemberInfo.ROLE_MASTER) {
                                master = memberInfo;
                                continue;
                            }
                            if (memberInfo.isFriendly()) {
                                friendCount++;
                            }
                            //根据年龄段划分
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(memberInfo.getBirthMills());
                            int year = calendar.get(Calendar.YEAR);
                            if (1990 <= year && year < 1995) {
                                groupOne.add(memberInfo);
                            } else if (1995 <= year && year < 2000) {
                                groupTwo.add(memberInfo);
                            } else if (year > 2000) {
                                groupThree.add(memberInfo);
                            } else {
                                groupFour.add(memberInfo);
                            }
                        }
                        List<MembersLevelOne> groups = new ArrayList<>();
                        if (groupOne.size() > 0) {
                            groups.add(getLevelOne("90后", groupOne));
                        }
                        if (groupTwo.size() > 0) {
                            groups.add(getLevelOne("95后", groupTwo));
                        }
                        if (groupThree.size() > 0) {
                            groups.add(getLevelOne("00后", groupThree));
                        }
                        if (groupFour.size() > 0) {
                            groups.add(getLevelOne("其他", groupFour));
                        }
                        Map<String, Object> map = new HashMap<>();
                        map.put("memberCount", memberCount);
                        map.put("friendCount", friendCount);
                        map.put("master", master);
                        map.put("groups", groups);
                        return map;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mRootView.hideLoading();
                    }
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Map<String, Object>>() {
                    @Override
                    public void onNext(Map<String, Object> map) {
                        mRootView.showMembers((int) map.get("memberCount"), (int) map.get("friendCount"),
                                (MemberInfo) map.get("master"), ((List<MembersLevelOne>) map.get("groups")));
                    }
                });
    }

    /**
     * 踢出成员
     *
     * @param groupId
     * @param memberInfo
     */
    public void kickOutMember(String groupId, MemberInfo memberInfo) {
        mApiHelper.executeNone(mRootView, mModel.kickOutMember(groupId, memberInfo.getMemberId()),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson baseJson) {
                        mRootView.onKickOutComplete(memberInfo);
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
     * 好友申请
     *
     * @param userId
     * @param reason
     */
    public void friendRequest(String userId, String reason) {
        mRootView.showLoading();
        RepositoryHelper.getInstance().sendFriendlyApply(mRootView, userId, reason, new ImCallBack() {
            @Override
            public void onSuccess(Object object) {
                mRootView.hideLoading();
                ToastUtil.showToastShort("申请成功");
            }

            @Override
            public boolean onFail(Throwable e) {
                mRootView.hideLoading();
                return super.onFail(e);
            }
        });
    }

    private MembersLevelOne getLevelOne(String title, List<MemberInfo> members) {
        MembersLevelOne levelOne = new MembersLevelOne();
        levelOne.setTitle(title);
        levelOne.setMembers(members);
        return levelOne;
    }
}

