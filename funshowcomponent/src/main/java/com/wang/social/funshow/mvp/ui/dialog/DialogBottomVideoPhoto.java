package com.wang.social.funshow.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;

import butterknife.BindView;


/**
 * liaoinstan
 * 选择图片弹窗
 */
public class DialogBottomVideoPhoto extends BaseDialog implements View.OnClickListener {
    @BindView(R2.id.btn_cancel)
    TextView btn_cancel;
    @BindView(R2.id.btn_camera)
    TextView btnCamera;
    @BindView(R2.id.btn_photo)
    TextView btnPhoto;

    public DialogBottomVideoPhoto(Context context) {
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
        return R.layout.funshow_dialog_videophoto;
    }

    @Override
    protected void intView(View root) {
        btnCamera.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_camera) {
            if (onVideoPhotoListener != null) onVideoPhotoListener.onCameraClick(view);
            dismiss();

        } else if (i == R.id.btn_photo) {
            if (onVideoPhotoListener != null) onVideoPhotoListener.onPhotoClick(view);
            dismiss();

        } else if (i == R.id.btn_cancel) {
            dismiss();

        }
    }

    private OnVideoPhotoListener onVideoPhotoListener;

    public void setOnVideoPhotoListener(OnVideoPhotoListener onVideoPhotoListener) {
        this.onVideoPhotoListener = onVideoPhotoListener;
    }

    public interface OnVideoPhotoListener {

        void onCameraClick(View v);

        void onPhotoClick(View v);
    }
}
