package com.wang.social.im.mvp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StatusBarUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.api.GroupService;
import com.wang.social.im.mvp.model.entities.dto.SocialDTO;

import butterknife.BindView;

public class QrcodeGroupActivity extends BasicAppNoDiActivity implements IView {

    @BindView(R2.id.img_header)
    ImageView imgHeader;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_detail)
    TextView textDetail;
    @BindView(R2.id.img_qrcode)
    ImageView imgQrcode;
    @BindView(R2.id.text_lable_gender)
    TextView textLableGender;
    @BindView(R2.id.text_lable_astro)
    TextView textLableAstro;

    private int groupId;

    public static void start(Context context, int groupId) {
        Intent intent = new Intent(context, QrcodeGroupActivity.class);
        intent.putExtra("groupId", groupId);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.im_activity_qrcode_group;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        groupId = getIntent().getIntExtra("groupId", 0);
        StatusBarUtil.setTranslucent(this);
        StatusBarUtil.setTextLight(this);

        netGetGroupInfo(groupId);
    }

    private void setUserData(SocialDTO groupInfo) {
        if (groupInfo != null) {
//            ImageLoaderHelper.loadCircleImg(imgHeader, groupInfo.getAvatar());
//            ImageLoaderHelper.loadImg(imgQrcode, groupInfo.getAvatar());
//            textName.setText(groupInfo.getNickname());
//            textDetail.setText(groupInfo.getTagTextDot());
//            textLableGender.setSelected(!groupInfo.isMale());
//            textLableGender.setText(TimeUtils.getBirthdaySpan(groupInfo.getBirthday()));
//            textLableAstro.setText(TimeUtils.getAstro(groupInfo.getBirthday()));
        }
    }

    /////////////////////////////////

    public void netGetGroupInfo(int groupId) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(GroupService.class).getSocialInfo("2.0.0", String.valueOf(groupId)),
                new ErrorHandleSubscriber<BaseJson<SocialDTO>>() {
                    @Override
                    public void onNext(BaseJson<SocialDTO> basejson) {
                        SocialDTO groupInfo = basejson.getData();
                        setUserData(groupInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
    public void netGetQrcodeImg(int groupId) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(GroupService.class).getGroupQrcodeBygroupId("2.0.0", String.valueOf(groupId)),
                new ErrorHandleSubscriber<BaseJson>() {
                    @Override
                    public void onNext(BaseJson basejson) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
