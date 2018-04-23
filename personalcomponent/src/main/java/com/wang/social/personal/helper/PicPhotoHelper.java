package com.wang.social.personal.helper;

import android.app.Activity;
import android.content.Intent;

import com.wang.social.personal.mvp.ui.activity.OfficialPhotoActivity;
import com.wang.social.pictureselector.helper.PhotoHelper;

public class PicPhotoHelper extends PhotoHelper {

    private static final int PHOTO_PIC = 0xf111;

    public PicPhotoHelper(Activity activity, OnPhotoCallback callback) {
        super(activity, callback);
    }

    public void startPic() {
        OfficialPhotoActivity.start(activity, PHOTO_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case PHOTO_PIC:
                    String url = data.getStringExtra(OfficialPhotoActivity.NAME_URL);
                    if (callback != null) callback.onResult(url);
                    break;
            }
        }
    }
}
