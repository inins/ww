package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;

import butterknife.BindView;


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
        int i = view.getId();
        if (i == R.id.btn_pic) {
            if (onPhotoListener != null) onPhotoListener.onPicClick(view);
            dismiss();

        } else if (i == R.id.btn_camera) {
            if (onPhotoListener != null) onPhotoListener.onCameraClick(view);
            dismiss();

        } else if (i == R.id.btn_photo) {
            if (onPhotoListener != null) onPhotoListener.onPhotoClick(view);
            dismiss();

        } else if (i == R.id.btn_cancel) {
            dismiss();

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
