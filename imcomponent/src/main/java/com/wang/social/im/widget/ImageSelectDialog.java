package com.wang.social.im.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.wang.social.im.R;
import com.wang.social.im.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ============================================
 * 图片选择弹框
 * <p>
 * Create by ChenJing on 2018-05-03 19:37
 * ============================================
 */
public class ImageSelectDialog extends BaseDialog {

    private OnItemSelectedListener mItemSelectedListener;

    public ImageSelectDialog(Context context, OnItemSelectedListener itemSelectedListener) {
        super(context);
        this.mItemSelectedListener = itemSelectedListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogBottom();
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected int getView() {
        return R.layout.im_dialog_image_select;
    }

    @Override
    protected void intView(View root) {

    }

    @OnClick({R2.id.dis_tv_gallery, R2.id.dis_tv_shoot, R2.id.dis_tv_album, R2.id.dis_tv_cancel})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.dis_tv_gallery) {
            if (mItemSelectedListener != null) {
                mItemSelectedListener.onGallerySelected();
            }
        } else if (view.getId() == R.id.dis_tv_shoot) {
            if (mItemSelectedListener != null) {
                mItemSelectedListener.onShootSelected();
            }
        } else if (view.getId() == R.id.dis_tv_album) {
            if (mItemSelectedListener != null) {
                mItemSelectedListener.onAlbumSelected();
            }
        }
        dismiss();
    }

    public interface OnItemSelectedListener {

        void onGallerySelected();

        void onShootSelected();

        void onAlbumSelected();
    }
}
