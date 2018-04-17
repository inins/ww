package com.wang.social.personal.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.frame.component.entities.config.MsgConfig;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.ui.base.BasicAppActivity;
import com.frame.di.component.AppComponent;
import com.kyleduo.switchbutton.SwitchButton;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingMsgActivity extends BasicAppActivity implements CompoundButton.OnCheckedChangeListener {

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
        setMsgData();
        switchMsg.setOnCheckedChangeListener(this);
        switchShowdetail.setOnCheckedChangeListener(this);
        switchVoice.setOnCheckedChangeListener(this);
        switchVibration.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        MsgConfig msgConfig = AppDataHelper.getMsgConfig();
        if (msgConfig == null) msgConfig = new MsgConfig();
        int id = compoundButton.getId();
        if (id == R.id.switch_msg) {
            msgConfig.setEnableMsg(checked);
        } else if (id == R.id.switch_showdetail) {
            msgConfig.setShowDetail(checked);
        } else if (id == R.id.switch_voice) {
            msgConfig.setMsgVoice(checked);
        } else if (id == R.id.switch_vibration) {
            msgConfig.setMsgVibration(checked);
        }
        AppDataHelper.saveMsgConfig(msgConfig);
    }

    private void setMsgData() {
        MsgConfig msgConfig = AppDataHelper.getMsgConfig();
        if (msgConfig != null) {
            switchMsg.setChecked(msgConfig.isEnableMsg());
            switchShowdetail.setChecked(msgConfig.isShowDetail());
            switchVoice.setChecked(msgConfig.isMsgVoice());
            switchVibration.setChecked(msgConfig.isMsgVibration());
        }
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }
}
