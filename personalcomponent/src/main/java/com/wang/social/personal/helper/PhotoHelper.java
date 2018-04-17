package com.wang.social.personal.helper;

import android.app.Activity;
import android.content.Intent;

import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.personal.mvp.ui.activity.OfficialPhotoActivity;
import com.wang.social.pictureselector.ActivityPictureClip;
import com.wang.social.pictureselector.PictureSelector;

public class PhotoHelper {

    private static final int PHOTO_PIC = 0xf111;
    private static final int PHOTO_PHOTO = 0xf112;
    private static final int PHOTO_CROP = 0xf113;

    //最大选择数量，默认为1，超过1则不会裁剪
    protected int maxSelectCount = 1;
    protected boolean isClip = true;

    protected Activity activity;
    protected OnPhotoCallback callback;
    private CropHelper cropHelper;

    public PhotoHelper(Activity activity, OnPhotoCallback callback) {
        this.activity = activity;
        this.callback = callback;
        this.cropHelper = new CropHelper(activity, new CropHelper.CropInterface() {
            @Override
            public void cropResult(String path) {
                if (isClip) {
                    Intent intent = new Intent(activity, ActivityPictureClip.class);
                    intent.putExtra(PictureSelector.NAME_FILE_PATH, path);
                    activity.startActivityForResult(intent, PHOTO_CROP);
                }else {
                    if (callback != null) callback.onResult(path);
                }
            }

            @Override
            public void cancel() {
            }
        });
    }

    public void startPic() {
        OfficialPhotoActivity.start(activity, PHOTO_PIC);
    }

    public void startCamera() {
        cropHelper.startCamera();
    }

    public void startPhoto() {
        PictureSelector.from(activity)
                .maxSelection(maxSelectCount)
                .spanCount(2)
                .isClip(isClip)
                .forResult(PHOTO_PHOTO);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        cropHelper.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            if (PHOTO_PHOTO == requestCode) {
                String[] list = data.getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);
                if (callback != null) callback.onResult(format2Str(list));
            } else if (PHOTO_CROP == requestCode) {
                String path = data.getStringExtra(PictureSelector.NAME_FILE_PATH);
                if (callback != null) callback.onResult(path);
            } else if (PHOTO_PIC == requestCode) {
                String url = data.getStringExtra(OfficialPhotoActivity.NAME_URL);
                if (callback != null) callback.onResult(url);
            }
        }
    }

    /////////////////////////////

    public static String[] format2Array(String pathListStr) {
        return pathListStr.split(",");
    }

    public static String format2Str(String[] pathList) {
        if (StrUtil.isEmpty(pathList)) return "";
        String ret = "";
        for (String path : pathList) {
            ret += path + ",";
        }
        return StrUtil.subLastChart(ret, ",");
    }

    public interface OnPhotoCallback {
        void onResult(String path);
    }

    public int getMaxSelectCount() {
        return maxSelectCount;
    }

    public void setMaxSelectCount(int maxSelectCount) {
        this.maxSelectCount = maxSelectCount;
    }

    public boolean isClip() {
        return isClip;
    }

    public void setClip(boolean clip) {
        isClip = clip;
    }
}
