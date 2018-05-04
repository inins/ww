package com.wang.social.pictureselector.helper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.wang.social.pictureselector.ui.dialog.DialogBottomPhoto;

import java.lang.ref.WeakReference;

/**
 * PhotoHelper拓展：
 * 提供通用的dialog弹窗
 * 如果需要自定义弹窗样式，请直接使用PhotoHelper
 * {@link PhotoHelper.startCamera()}
 * {@link PhotoHelper.startPhoto()}
 */
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
        dialog.setOnVideoPhotoListener(new DialogBottomPhoto.OnVideoPhotoListener() {

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
