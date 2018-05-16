package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetFriendHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.utils.TimeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.notify.FriendRequest;

import butterknife.BindView;

public class NotifyFriendRequestDetailActivity extends BasicAppNoDiActivity {

    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_lable_gender)
    TextView textLableGender;
    @BindView(R2.id.text_lable_astro)
    TextView textLableAstro;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_tag)
    TextView textTag;
    @BindView(R2.id.text_reason)
    TextView textReason;
    private FriendRequest friendRequest;

    public static void start(Context context, FriendRequest friendRequest) {
        Intent intent = new Intent(context, NotifyFriendRequestDetailActivity.class);
        intent.putExtra("friendRequest", friendRequest);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_friendrequest_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        friendRequest = (FriendRequest) getIntent().getSerializableExtra("friendRequest");
        setFriendData();
    }

    private void setFriendData() {
        if (friendRequest != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader, friendRequest.getAvatar());
            textName.setText(friendRequest.getNickname());
            textLableGender.setSelected(!friendRequest.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(friendRequest.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(friendRequest.getBirthday()));
            textTag.setText(friendRequest.getTagText());
            textReason.setText(friendRequest.getReason());
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lay_nameboard) {

        } else if (id == R.id.btn_agree) {
            NetFriendHelper.newInstance().netAgreeFriendApply(this, friendRequest.getUserId(), friendRequest.getMsgId(), true, () -> {
                finish();
            });
        } else if (id == R.id.btn_disagree) {
            NetFriendHelper.newInstance().netAgreeFriendApply(this, friendRequest.getUserId(), friendRequest.getMsgId(), false, () -> {
                finish();
            });
        } else if (id == R.id.btn_report) {

        }
    }
}
