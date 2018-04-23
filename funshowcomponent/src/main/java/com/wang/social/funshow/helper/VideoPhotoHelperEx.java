package com.wang.social.funshow.helper;

import android.app.Activity;
import android.view.View;

import com.wang.social.funshow.mvp.ui.dialog.DialogBottomVideoPhoto;
import com.wang.social.pictureselector.helper.PhotoHelper;

import java.lang.ref.WeakReference;

public class VideoPhotoHelperEx extends PhotoHelper {

    //使用弱引用持有dialog，以便及时回收
    private WeakReference<DialogBottomVideoPhoto> dialogPhoto;
    //是否显示官方图库选项，默认不显示
    private boolean needOfficialPhoto;

    private VideoPhotoHelperEx(Activity activity, PhotoHelper.OnPhotoCallback callback) {
        super(activity, callback);
        dialogPhoto = new WeakReference(newInstanceDialog());
    }

    public static VideoPhotoHelperEx newInstance(Activity activity, PhotoHelper.OnPhotoCallback callback) {
        return new VideoPhotoHelperEx(activity, callback);
    }

    private DialogBottomVideoPhoto newInstanceDialog() {
        DialogBottomVideoPhoto dialog = new DialogBottomVideoPhoto(activity);
        dialog.setOnVideoPhotoListener(new DialogBottomVideoPhoto.OnVideoPhotoListener() {

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
