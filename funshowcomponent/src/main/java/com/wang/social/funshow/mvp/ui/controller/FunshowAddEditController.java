package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;
import android.widget.TextView;

import com.frame.component.common.SimpleTextWatcher;
import com.frame.entities.EventBean;
import com.frame.utils.StrUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.post.UserPost;
import com.wang.social.funshow.mvp.entities.user.Friend;
import com.wang.social.funshow.mvp.ui.view.AiteEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FunshowAddEditController extends FunshowAddBaseController {

    @BindView(R2.id.edit_content)
    AiteEditText editContent;
    @BindView(R2.id.text_count)
    TextView textCount;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_CTRL_FUNSHOW_ADD_USER:
                List<Friend> friends = (ArrayList<Friend>) event.get("users");
                editContent.appendAiteStr(friends);
                break;
        }
    }


    public FunshowAddEditController(View root) {
        super(root);
        int layout = R.layout.funshow_lay_add_edit;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        editContent.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                textCount.setText(text.length() + "/50");
            }
        });
    }

    @Override
    protected void onInitData() {
    }

    ////////////////////////

    public String getContent() {
        return editContent.getText().toString().trim();
    }

    public List<Friend> getFriends() {
        return editContent.getFriends();
    }

    public List<UserPost> getAiteUsers() {
        List<UserPost> userPosts = new ArrayList<>();
        if (!StrUtil.isEmpty(getFriends())) {
            for (Friend friend : getFriends()) {
                userPosts.add(new UserPost(friend.getFriendId()));
            }
        }
        return userPosts;
    }
}
