package com.wang.social.moneytree.mvp.presenter;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;
import com.wang.social.moneytree.mvp.contract.GameRoomContract;
import com.wang.social.moneytree.mvp.model.MemberListHelper;
import com.frame.component.view.moneytree.PayHelper;
import com.wang.social.moneytree.mvp.model.entities.GameBean;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.model.entities.GameRecord;
import com.wang.social.moneytree.mvp.model.entities.GameRecordDetail;
import com.wang.social.moneytree.mvp.model.entities.GameScore;
import com.wang.social.moneytree.mvp.model.entities.JoinGame;
import com.wang.social.moneytree.mvp.model.entities.Member;
import com.wang.social.moneytree.mvp.model.entities.MemberList;
import com.wang.social.moneytree.mvp.model.entities.RoomMsg;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@ActivityScope
public class GameRoomPresenter extends
        BasePresenter<GameRoomContract.Model, GameRoomContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    // 游戏信息
    private GameBean mGameBean;
    // 游戏记录
    private GameRecord mGameRecord;
    // 游戏结果列表（在记录详情中使用）
    private List<GameScore> mGameScoreList = new ArrayList<>();
    // 成员列表
    private List<Member> mMemberList = new ArrayList<>();
    // 房间信息
    private RoomMsg mRoomMsg;

    @Inject
    public GameRoomPresenter(GameRoomContract.Model model, GameRoomContract.View view) {
        super(model, view);
    }

    public int getRoomId() {
        return null == mGameBean ? -1 : mGameBean.getRoomId();
    }

    public int getGameId() {
        if (null != mRoomMsg) {
            return mRoomMsg.getGameId();
        }
        else {
            return null == mGameBean ? -1 : mGameBean.getGameId();
        }
    }

    public void setGameBean(GameBean gameBean) {
        mGameBean = gameBean;
    }

    public void setGameRecord(GameRecord gameRecord) {
        mGameRecord = gameRecord;
    }

    public GameBean getGameBean() {
        return mGameBean;
    }

    public GameRecord getGameRecord() {
        return mGameRecord;
    }

    public List<GameScore> getGameScoreList() {
        return mGameScoreList;
    }

    public List<Member> getMemberList() {
        return mMemberList;
    }

    public RoomMsg getRoomMsg() {
        return mRoomMsg;
    }

    public boolean isJoined() {
        if (null != mRoomMsg) {
            return mRoomMsg.getIsJoin() != 0;
        } else {
            return null == mGameBean ? false : mGameBean.getIsJoined() != 0;
        }
    }

    /**
     * 获取游戏记录游戏id
     * @return
     */
    public int getGameRecordGameId() {
        return null == mGameRecord ? -1 : mGameRecord.getGameId();
    }

    /**
     * 获取游戏房间展示项
     * @param roomId 房间id
     */
    public void getRoomMsg(int roomId) {
        mApiHelper.execute(mRootView,
                mModel.getRoomMsg(roomId),
                new ErrorHandleSubscriber<RoomMsg>(mErrorHandler) {
                    @Override
                    public void onNext(RoomMsg roomMsg) {
                        mRoomMsg = roomMsg;
                        // 更新 游戏信息
                        if (null != mGameBean) {
                            mGameBean.copyRoomMsg(roomMsg);
                        }

                        mRootView.onGetRoomMsgSuccess(roomMsg);
                    }
                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToastLong(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 获取游戏记录详情
     * @param gameId 游戏id
     */
    public void loadRecordDetail(int gameId) {
        mApiHelper.execute(mRootView,
                mModel.getRecordDetail(gameId),
                new ErrorHandleSubscriber<GameRecordDetail>(mErrorHandler) {
                    @Override
                    public void onNext(GameRecordDetail recordDetail) {
                        if (null != recordDetail) {
                            if (null != recordDetail.getList()) {
                                mGameScoreList.clear();
                                mGameScoreList.addAll(recordDetail.getList());
                            }
                        }
                        mRootView.onGetRecordDetailSuccess(recordDetail);
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 加入游戏
     * @param roomId 房间id
     */
    public void joinGame(int roomId) {
        mApiHelper.execute(mRootView,
                mModel.joinGame(roomId),
                new ErrorHandleSubscriber<JoinGame>(mErrorHandler) {
                    @Override
                    public void onNext(JoinGame joinGame) {
                        // 显示支付对话框
                        mRootView.popupJoinGamePayDialog(joinGame);
                    }
                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToastLong(e.getMessage());
                        mRootView.hideLoading();
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 参与游戏支付
     * @param joinGame 加入游戏返回的信息
     */
    public void payJoinGame(JoinGame joinGame) {
        PayHelper.newInstance().payJoinGame(mRootView,
                joinGame.getApplyId(),
                joinGame.getDiamond(),
                new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        // 支付成功
                        mRootView.onPayJoinGameSuccess();
                    }
                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToastLong(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    /**
     * 游戏成员列表
     */
    public void loadMemberList() {
        MemberListHelper.newInstance().getMemberList(mRootView,
                null == mGameBean ? -1 : mGameBean.getGameId(),
                new ErrorHandleSubscriber<MemberList>(mErrorHandler) {
                    @Override
                    public void onNext(MemberList memberList) {
                        // 获取成员列表成功
                        if (null != memberList && null != memberList.getList()) {
                            mMemberList.clear();
                            for (int i = 0; i < Math.min(5 , memberList.getList().size()); i++) {
                                mMemberList.add(memberList.getList().get(i));
                            }
                            Member member = new Member();
                            member.setUserId(-1);
                            mMemberList.add(member);
                        }

                        mRootView.onLoadMemberListSuccess();
                    }
                },
                disposable -> {},
                () -> {});
    }

    /**
     * 加载游戏结果
     * @param gameId 游戏id
     */
    public void loadGameEnd(int gameId) {
        mApiHelper.execute(mRootView,
                mModel.getGameEnd(gameId),
                new ErrorHandleSubscriber<GameEnd>(mErrorHandler) {
                    @Override
                    public void onNext(GameEnd gameEnd) {
                        mRootView.onLoadGameEndSuccess(gameEnd);
                    }
                    @Override
                    public void onError(Throwable e) {
                        mRootView.showToastLong(e.getMessage());
                    }
                },
                disposable -> mRootView.showLoading(),
                () -> mRootView.hideLoading());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;
    }
}