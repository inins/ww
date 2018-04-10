package com.wang.social.personal.helper;

import android.app.Activity;
import android.content.Intent;

import com.frame.utils.ToastUtil;
import com.wang.social.personal.mvp.ui.activity.OfficialPhotoActivity;
import com.wang.social.pictureselector.ActivityPicturePreview;
import com.wang.social.pictureselector.PictureSelector;

public class PhotoHelper {

    private static final int PHOTO_PIC = 0xf311;
    private static final int PHOTO_PHOTO = 0xf312;

    protected Activity activity;
    protected OnPhotoCallback callback;

    public PhotoHelper(Activity activity, OnPhotoCallback callback) {
        this.activity = activity;
        this.callback = callback;
    }

    public void startPic() {
        OfficialPhotoActivity.start(activity, PHOTO_PIC);
    }

    public void startCamera() {
        ToastUtil.showToastLong("开发中");
    }

    public void startPhoto() {
        PictureSelector.from(activity)
                .maxSelection(1)
                .spanCount(2)
                .isClip(true)
                .forResult(PHOTO_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (PHOTO_PHOTO == requestCode) {
                String[] list = data.getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);
                //Intent intent = new Intent(activity, ActivityPicturePreview.class);
                if (callback != null) callback.onResult(list[0]);
            } else if (PHOTO_PIC == requestCode) {
                String url = data.getStringExtra(OfficialPhotoActivity.NAME_URL);
                if (callback != null) callback.onResult(url);
            }
        }
    }

    /////////////////////////////

    public interface OnPhotoCallback {
        void onResult(String path);
    }
}
