package com.wang.social.personal.helper;

import android.app.Activity;
import android.view.View;

import com.wang.social.personal.mvp.ui.dialog.DialogBottomPhoto;

import java.lang.ref.WeakReference;

public class PicPhotoHelperEx extends PicPhotoHelper {

    //使用弱引用持有dialog，以便及时回收
    private WeakReference<DialogBottomPhoto> dialogPhoto;
    //是否显示官方图库选项，默认不显示
    private boolean needOfficialPhoto;

    private PicPhotoHelperEx(Activity activity, OnPhotoCallback callback) {
        super(activity, callback);
        dialogPhoto = new WeakReference(newInstanceDialog());
    }

    public static PicPhotoHelperEx newInstance(Activity activity, OnPhotoCallback callback) {
        return new PicPhotoHelperEx(activity, callback);
    }

    private DialogBottomPhoto newInstanceDialog() {
        DialogBottomPhoto dialog = new DialogBottomPhoto(activity);
        dialog.needOfficialPhoto(needOfficialPhoto);
        dialog.setOnPhotoListener(new DialogBottomPhoto.OnPhotoListener() {
            @Override
            public void onPicClick(View v) {
                startPic();
            }

            @Override
            public void onCameraClick(View v) {
                startCamera();
            }

            @Override
            public void onPhotoClick(View v) {
                startPhoto();
            }
        });
        return dialog;
    }

    //是否显示官方图库选项，默认不显示
    public PicPhotoHelperEx needOfficialPhoto(boolean needOfficialPhoto) {
        this.needOfficialPhoto = needOfficialPhoto;
        if (dialogPhoto == null || dialogPhoto.get() == null) {
            dialogPhoto = new WeakReference(newInstanceDialog());
        }
        dialogPhoto.get().needOfficialPhoto(needOfficialPhoto);
        return this;
    }

    public void showDefaultDialog() {
        if (dialogPhoto == null || dialogPhoto.get() == null) {
            dialogPhoto = new WeakReference(newInstanceDialog());
        }
        dialogPhoto.get().show();
    }

    public void hideDefaultDialog() {
        if (dialogPhoto == null || dialogPhoto.get() != null) {
            dialogPhoto.get().hide();
        }
    }

    public void dismissDefaultDialog() {
        if (dialogPhoto == null || dialogPhoto.get() != null) {
            dialogPhoto.get().dismiss();
        }
    }
}
