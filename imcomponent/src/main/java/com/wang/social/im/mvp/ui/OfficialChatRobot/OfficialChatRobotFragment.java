package com.wang.social.im.mvp.ui.OfficialChatRobot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.base.BasicFragment;
import com.frame.component.api.CommonService;
import com.frame.component.common.NetParam;
import com.frame.component.enums.ConversationType;
import com.frame.component.utils.EntitiesUtil;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.BaseJson;
import com.frame.http.api.PageList;
import com.frame.http.api.PageListDTO;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.integration.IRepositoryManager;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.ui.OfficialChatRobot.adapter.LeavingMsgAdapter;
import com.wang.social.im.mvp.ui.OfficialChatRobot.api.OfficialChatRobotService;
import com.wang.social.im.mvp.ui.OfficialChatRobot.entities.LeavingMsg;
import com.wang.social.im.mvp.ui.OfficialChatRobot.entities.dto.LeavingMsgDTO;
import com.wang.social.im.mvp.ui.adapters.MessageListAdapter;
import com.wang.social.im.view.GameInputView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observable;

public class OfficialChatRobotFragment extends BasicFragment implements IView,
        GameInputView.IGInputViewListener {


    // 消息列表
    @BindView(R2.id.fc_message_list)
    RecyclerView fcMessageList;
    // 输入栏
    @BindView(R2.id.fc_input)
    GameInputView fcInput;

    private LeavingMsgAdapter mAdapter;

    private ApiHelper mApiHelper;
    private IRepositoryManager mRepositoryManager;

    private List<LeavingMsg> mLeavingMsgs = new ArrayList<>();

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.im_fragment_official_chat_robot;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mApiHelper = FrameUtils.obtainAppComponentFromContext(getContext()).apiHelper();
        mRepositoryManager = FrameUtils.obtainAppComponentFromContext(getContext()).repoitoryManager();

        fcMessageList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new LeavingMsgAdapter(getContext(), mLeavingMsgs);
        fcMessageList.setAdapter(mAdapter);

        fcInput.hideEmojiToggle();
        fcInput.setMInputViewListener(this);

        userLeavingMsgList();
    }

    @Override
    public void setData(@Nullable Object data) {

    }


    private void userLeavingMsgList() {
        mApiHelper.execute(this,
                netUserLeavingMsgList(),
                new ErrorHandleSubscriber<PageList<LeavingMsg>>() {
                    @Override
                    public void onNext(PageList<LeavingMsg> pageList) {
                        mLeavingMsgs.clear();
                        mLeavingMsgs.addAll(pageList.getList());
                        if (null != mAdapter) {
                            mAdapter.notifyDataSetChanged();
                        }
                        fcMessageList.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    }
                });
    }

    private Observable<BaseJson<PageListDTO<LeavingMsgDTO, LeavingMsg>>> netUserLeavingMsgList() {
        Map<String, Object> param = new NetParam()
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(OfficialChatRobotService.class)
                .userLeavingMsgList(param);
    }

    /**
     * 保存用户留言
     * @param msgContent 消息内容
     */
    private void userLeavingMsgSave(String msgContent) {
        mApiHelper.executeForData(this,
                netUserLeavingMsgSave(msgContent),
                new ErrorHandleSubscriber() {
                    @Override
                    public void onNext(Object o) {
                        userLeavingMsgList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastShort(e.getMessage());
                    }
                });
    }

    /**
     * 保存用户留言
     * @param msgContent 消息内容
     */
    private Observable<BaseJson> netUserLeavingMsgSave(String msgContent) {
        Map<String, Object> param = new NetParam()
                .put("msgContent", EntitiesUtil.assertNotNull(msgContent))
                .put("v", "2.0.0")
                .build();
        return mRepositoryManager
                .obtainRetrofitService(OfficialChatRobotService.class)
                .userLeavingMsgSave(param);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onEmotionClick(String codeName, String showName) {

    }

    @Override
    public void onSendClick(String content) {
        userLeavingMsgSave(content);
    }

    @Override
    public void onInputViewExpanded(int height) {

    }

    @Override
    public void onInputViewCollapsed() {

    }
}
