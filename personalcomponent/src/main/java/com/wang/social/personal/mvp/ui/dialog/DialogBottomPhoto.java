package com.wang.social.personal.mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wang.social.personal.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * liaoinstan
 * 选择图片弹窗
 */
public class DialogBottomPhoto extends BaseDialog implements View.OnClickListener {
    @BindView(R.id.btn_cancel)
    TextView btn_cancel;
    @BindView(R.id.btn_pic)
    TextView btnPic;
    @BindView(R.id.btn_camera)
    TextView btnCamera;
    @BindView(R.id.btn_photo)
    TextView btnPhoto;

    public DialogBottomPhoto(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogBottom();
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected int getView() {
        return R.layout.personal_dialog_choose_photo;
    }

    @Override
    protected void intView(View root) {
        btnPic.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pic:
                dismiss();
                break;
            case R.id.btn_camera:
                dismiss();
                break;
            case R.id.btn_photo:
                dismiss();
                break;
        }
    }
}
