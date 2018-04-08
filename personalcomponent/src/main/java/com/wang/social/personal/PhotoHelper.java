package com.wang.social.personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.frame.utils.ToastUtil;
import com.wang.social.personal.mvp.ui.dialog.DialogBottomPhoto;
import com.wang.social.pictureselector.ActivityPicturePreview;
import com.wang.social.pictureselector.PictureSelector;
import com.wang.social.pictureselector.app.MainActivity;

import java.lang.ref.WeakReference;

public class PhotoHelper {

    private static final int PHOTO_PIC = 0xf312;

    //使用弱引用持有dialog，以便及时回收
    private WeakReference<DialogBottomPhoto> dialogPhoto;
    private Activity activity;
    private OnPhotoCallback callback;

    private PhotoHelper(Activity activity, OnPhotoCallback callback) {
        this.activity = activity;
        this.callback = callback;
        dialogPhoto = new WeakReference(newInstanceDialog());
    }

    public static PhotoHelper newInstance(Activity activity, OnPhotoCallback callback) {
        return new PhotoHelper(activity, callback);
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

            }

            @Override
            public void onPhotoClick(View v) {

            }
        });
        return dialog;
    }

    private void startPic() {
        PictureSelector.from(activity)
                .maxSelection(1)
                .spanCount(2)
                .isClip(true)
                .forResult(PHOTO_PIC);
    }

    private void startCamera() {
        ToastUtil.showToastLong("开发中");
    }

    private void startPhoto() {
        ToastUtil.showToastLong("开发中");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (PHOTO_PIC == requestCode) {
                String[] list = data.getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);
                Intent intent = new Intent(activity, ActivityPicturePreview.class);
                //TODO
                if (callback != null) callback.onResult(list[0]);
            }
        }
    }

    /////////////////////////////

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

    public interface OnPhotoCallback {
        void onResult(String path);
    }
}
