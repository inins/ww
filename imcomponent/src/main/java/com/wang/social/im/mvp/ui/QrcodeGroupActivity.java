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
import com.wang.social.im.mvp.model.entities.SocialInfo;
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

    private void setUserData(SocialInfo groupInfo) {
        if (groupInfo != null) {
            ImageLoaderHelper.loadCircleImg(imgHeader, groupInfo.getCover());
            ImageLoaderHelper.loadImg(imgQrcode, groupInfo.getQrcodeImg());
            textName.setText(groupInfo.getName());
            textDetail.setText(groupInfo.getTagText());
        }
    }

    /////////////////////////////////

    public void netGetGroupInfo(int groupId) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(GroupService.class).getSocialInfo("2.0.0", String.valueOf(groupId)),
                new ErrorHandleSubscriber<BaseJson<SocialDTO>>() {
                    @Override
                    public void onNext(BaseJson<SocialDTO> basejson) {
                        SocialDTO dto = basejson.getData();
                        SocialInfo groupInfo = dto.transform();
                        setUserData(groupInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
