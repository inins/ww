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
import com.wang.social.personal.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * liaoinstan
 * 选择图片弹窗
 */
public class DialogBottomPhoto extends BaseDialog implements View.OnClickListener {
    @BindView(R2.id.btn_cancel)
    TextView btn_cancel;
    @BindView(R2.id.btn_pic)
    TextView btnPic;
    @BindView(R2.id.btn_camera)
    TextView btnCamera;
    @BindView(R2.id.btn_photo)
    TextView btnPhoto;
    @BindView(R2.id.lay_official)
    View lay_official;

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
            case R2.id.btn_pic:
                if (onPhotoListener != null) onPhotoListener.onPicClick(view);
                dismiss();
                break;
            case R2.id.btn_camera:
                if (onPhotoListener != null) onPhotoListener.onCameraClick(view);
                dismiss();
                break;
            case R2.id.btn_photo:
                if (onPhotoListener != null) onPhotoListener.onPhotoClick(view);
                dismiss();
                break;
            case R2.id.btn_cancel:
                dismiss();
                break;
        }
    }

    public void needOfficialPhoto(boolean needOfficialPhoto) {
        lay_official.setVisibility(needOfficialPhoto ? View.VISIBLE : View.GONE);
    }

    private OnPhotoListener onPhotoListener;

    public void setOnPhotoListener(OnPhotoListener onPhotoListener) {
        this.onPhotoListener = onPhotoListener;
    }

    public interface OnPhotoListener {
        void onPicClick(View v);

        void onCameraClick(View v);

        void onPhotoClick(View v);
    }
}
