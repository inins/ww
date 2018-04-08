package com.wang.social.personal.helper;

import android.app.Activity;
import android.view.View;

import com.wang.social.personal.mvp.ui.dialog.DialogBottomPhoto;

import java.lang.ref.WeakReference;

public class PhotoHelperEx extends PhotoHelper {

    //使用弱引用持有dialog，以便及时回收
    private WeakReference<DialogBottomPhoto> dialogPhoto;

    private PhotoHelperEx(Activity activity, OnPhotoCallback callback) {
        super(activity, callback);
        dialogPhoto = new WeakReference(newInstanceDialog());
    }

    public static PhotoHelperEx newInstance(Activity activity, OnPhotoCallback callback) {
        return new PhotoHelperEx(activity, callback);
    }

    private DialogBottomPhoto newInstanceDialog() {
        DialogBottomPhoto dialog = new DialogBottomPhoto(activity);
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

    public void showDefaultDialog() {
        if (dialogPhoto.get() == null) {
            dialogPhoto = new WeakReference(newInstanceDialog());
        }
        dialogPhoto.get().show();
    }

    public void hideDefaultDialog() {
        if (dialogPhoto.get() != null) {
            dialogPhoto.get().hide();
        }
    }

    public void dismissDefaultDialog() {
        if (dialogPhoto.get() != null) {
            dialogPhoto.get().dismiss();
        }
    }
}
