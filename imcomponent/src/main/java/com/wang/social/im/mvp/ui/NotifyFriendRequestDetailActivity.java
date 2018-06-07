package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetFriendHelper;
import com.frame.component.helper.NetReportHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.component.ui.dialog.DialogActionSheet;
import com.frame.entities.EventBean;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.notify.RequestBean;
import com.wang.social.pictureselector.helper.PhotoHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R2.id.lay_deal)
    LinearLayout layDeal;
    @BindView(R2.id.text_reason_title)
    TextView textReasonTitle;
    private RequestBean requestBean;
    private PhotoHelper photoHelper;

    private boolean isGroup;

    public static void start(Context context, RequestBean requestBean) {
        Intent intent = new Intent(context, NotifyFriendRequestDetailActivity.class);
        intent.putExtra("requestBean", requestBean);
        intent.putExtra("isGroup", requestBean.isGroup());
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_friendrequest_detail;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        requestBean = (RequestBean) getIntent().getSerializableExtra("requestBean");
        isGroup = getIntent().getBooleanExtra("isGroup", false);
        photoHelper = new PhotoHelper(this);
        setFriendData();
    }

    private void setFriendData() {
        if (requestBean != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader, requestBean.getAvatar());
            textReasonTitle.setText(isGroup ? R.string.im_notify_friend_request_detail_group_reason_title : R.string.im_notify_friend_request_detail_reason_title);
            textName.setText(requestBean.getNickname());
            textLableGender.setSelected(!requestBean.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(requestBean.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(requestBean.getBirthday()));
            textTag.setText(requestBean.getTagText());
            textReason.setText(requestBean.getReason());
            layDeal.setVisibility(requestBean.isDeal() ? View.GONE : View.VISIBLE);
        }
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.lay_nameboard) {
            CommonHelper.HomeHelper.startUserDetailActivity(this, requestBean.getUserId());
        } else if (id == R.id.btn_agree) {
            if (isGroup) {
                NetFriendHelper.newInstance().netAgreeFindChatApply(this, requestBean.getGroupId(), requestBean.getUserId(), requestBean.getMsgId(), true, () -> {
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_NOTIFY_DETAIL_DEAL).put("isDeal", true));
                    finish();
                });
            } else {
                NetFriendHelper.newInstance().netAgreeFriendApply(this, requestBean.getUserId(), requestBean.getMsgId(), true, () -> {
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_NOTIFY_DETAIL_DEAL).put("isDeal", true));
                    finish();
                });
            }
        } else if (id == R.id.btn_disagree) {
            if (isGroup) {
                NetFriendHelper.newInstance().netAgreeFindChatApply(this, requestBean.getGroupId(), requestBean.getUserId(), requestBean.getMsgId(), false, () -> {
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_NOTIFY_DETAIL_DEAL).put("isDeal", false));
                    finish();
                });
            } else {
                NetFriendHelper.newInstance().netAgreeFriendApply(this, requestBean.getUserId(), requestBean.getMsgId(), false, () -> {
                    EventBus.getDefault().post(new EventBean(EventBean.EVENT_NOTIFY_DETAIL_DEAL).put("isDeal", false));
                    finish();
                });
            }
        } else if (id == R.id.btn_report) {
            String[] strings = {"语言谩骂/骚扰信息", "存在欺诈骗钱行为", "发布不适当内容"};
            DialogActionSheet.show(getSupportFragmentManager(), strings)
                    .setActionSheetListener((position, text) -> {
                        photoHelper.setOnPhotoCallback(path -> {
                            QiNiuManager.newInstance().uploadFile(NotifyFriendRequestDetailActivity.this, path, new QiNiuManager.OnSingleUploadListener() {
                                @Override
                                public void onSuccess(String url) {
                                    NetReportHelper.newInstance().netReportPerson(NotifyFriendRequestDetailActivity.this, requestBean.getUserId()
                                            , text, url, () -> {
                                                finish();
                                            });
                                }

                                @Override
                                public void onFail() {
                                    ToastUtil.showToastShort("上传失败");
                                }
                            });
                        });
                        photoHelper.startPhoto();
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        photoHelper.onActivityResult(requestCode, resultCode, data);
    }
}
