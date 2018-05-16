package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.mvp.IView;
import com.frame.utils.TimeUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.notify.FriendRequest;
import com.wang.social.im.mvp.model.entities.notify.RequestBean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendRequestDetailActivity extends BasicAppNoDiActivity {

    @BindView(R.id.img_header)
    ImageView imgHeader;
    @BindView(R.id.text_lable_gender)
    TextView textLableGender;
    @BindView(R.id.text_lable_astro)
    TextView textLableAstro;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.text_tag)
    TextView textTag;
    @BindView(R.id.text_reason)
    TextView textReason;
    private FriendRequest friendRequest;

    public static void start(Context context, FriendRequest friendRequest) {
        Intent intent = new Intent(context, FriendRequestDetailActivity.class);
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
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lay_nameboard) {

        } else if (id == R.id.btn_agree) {

        } else if (id == R.id.btn_disagree) {

        } else if (id == R.id.btn_report) {

        }
    }
}
