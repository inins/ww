package com.wang.social.funshow.mvp.ui.controller;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.frame.component.common.NetParam;
import com.frame.component.ui.base.BaseController;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.event.CommentEvent;
import com.wang.social.funshow.mvp.model.api.FunshowService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;

public class FunshowDetailEditController extends BaseController implements View.OnClickListener {

    @BindView(R2.id.img_emoji)
    ImageView imgEmoji;
    @BindView(R2.id.edit_eva)
    EditText editEva;
    @BindView(R2.id.btn_send)
    View btnSend;

    private int talkId;
    private int userId;
    private Integer talkCommentId;
    private Integer answeredUserId;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommentEvent(CommentEvent commentEvent) {
        talkCommentId = commentEvent.getTalkCommentId();
        answeredUserId = commentEvent.getAnsweredUserId();
        if (!TextUtils.isEmpty(commentEvent.getAnsweredUserName())) {
            editEva.setHint("回复" + commentEvent.getAnsweredUserName());
        } else {
            editEva.setHint(getContext().getString(R.string.funshow_home_funshow_detail_eva_hint));
        }
    }

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_CTRL_FUNSHOW_DETAIL_DATA:
                userId = (int) event.get("userId");
        }
    }

    public FunshowDetailEditController(View root, int talkId) {
        super(root);
        this.talkId = talkId;
        int layout = R.layout.funshow_lay_detail_edit;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        btnSend.setOnClickListener(this);
    }

    @Override
    protected void onInitData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_send) {
            String content = editEva.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                netCommitComment(content);
            } else {
                ToastUtil.showToastLong("请输入评论内容");
            }
        }
    }

    //////////////////////////////////////////////

    public void netCommitComment(String content) {
        Map<String, Object> param = NetParam.newInstance()
                .put("talkId", talkId)
                .put("talkCommentId", talkCommentId)
                .put("answeredUserId", answeredUserId != null ? answeredUserId : userId)
                .put("content", content)
                .build();
        ApiHelperEx.execute(getIView(), true,
                ApiHelperEx.getService(FunshowService.class).commentReply(param),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        //通知其他组件添加了一条评论
                        EventBean eventBean = new EventBean(EventBean.EVENT_FUNSHOW_DETAIL_ADD_EVA);
                        eventBean.put("talkId", talkId);
                        EventBus.getDefault().post(eventBean);
                        editEva.setText("");
                        KeyboardUtils.hideSoftInput(editEva);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }


    //////////////////////////////////////////////

    public Integer getTalkCommentId() {
        return talkCommentId;
    }

    public void setTalkCommentId(Integer talkCommentId) {
        this.talkCommentId = talkCommentId;
    }

    public Integer getAnsweredUserId() {
        return answeredUserId;
    }

    public void setAnsweredUserId(Integer answeredUserId) {
        this.answeredUserId = answeredUserId;
    }
}
