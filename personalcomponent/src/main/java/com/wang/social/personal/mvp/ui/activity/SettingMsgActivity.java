package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.config.MsgConfig;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.router.Router;
import com.frame.component.service.funshow.FunshowService;
import com.frame.component.service.im.ImService;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.component.ui.base.BasicAppNoDiActivity;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.msg.MsgSetting;
import com.wang.social.personal.mvp.model.api.UserService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingMsgActivity extends BasicAppNoDiActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R2.id.switch_msg)
    SwitchButton switchMsg;
    @BindView(R2.id.switch_showdetail)
    SwitchButton switchShowdetail;
    @BindView(R2.id.switch_voice)
    SwitchButton switchVoice;
    @BindView(R2.id.switch_vibration)
    SwitchButton switchVibration;

    public static void start(Context context) {
        Intent intent = new Intent(context, SettingMsgActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.personal_activity_setting_msg;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        switchMsg.setOnCheckedChangeListener(this);
        switchShowdetail.setOnCheckedChangeListener(this);
        switchVoice.setOnCheckedChangeListener(this);
        switchVibration.setOnCheckedChangeListener(this);

        netGetMsgSetting(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        int id = compoundButton.getId();
        if (id == R.id.switch_msg) {
            netSetMsgSetting(checked);
        } else if (id == R.id.switch_showdetail) {
        } else if (id == R.id.switch_voice) {
        } else if (id == R.id.switch_vibration) {
        }
    }

    private void setMsgData(MsgSetting msgSetting) {
        if (msgSetting != null) {
            switchMsg.setCheckedNoEvent(msgSetting.isPushMsg());
        }
    }

    public void netGetMsgSetting(boolean needloading) {
        ApiHelperEx.execute(this, needloading,
                ApiHelperEx.getService(UserService.class).msgSetting(),
                new ErrorHandleSubscriber<BaseJson<MsgSetting>>() {
                    @Override
                    public void onNext(BaseJson<MsgSetting> basejson) {
                        MsgSetting msgSetting = basejson.getData();
                        setMsgData(msgSetting);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public void netSetMsgSetting(boolean isOpen) {
        ApiHelperEx.execute(this, true,
                ApiHelperEx.getService(UserService.class).msgSettingOpen(isOpen ? 1 : 0),
                new ErrorHandleSubscriber<BaseJson<MsgSetting>>() {
                    @Override
                    public void onNext(BaseJson<MsgSetting> basejson) {
                        ImService imService = (ImService) Router.getInstance().getService(ImService.class.getName());
                        if (imService != null) imService.setOfflinePushEnable(isOpen);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        switchMsg.setCheckedNoEvent(false);
                    }
                });
    }
}
