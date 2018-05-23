package com.wang.social.im.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

import com.frame.component.helper.CommonHelper;
import com.frame.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.im.widget.ImageSelectDialog;
import com.wang.social.pictureselector.helper.PhotoHelper;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

public class ImageSelectHelper extends PhotoHelper {

    //使用弱引用持有dialog，以便及时回收
    private WeakReference<ImageSelectDialog> dialogPhoto;

    ImageSelectDialog.OnItemSelectedListener mItemSelectedListener;

    private ImageSelectHelper(Activity activity, PhotoHelper.OnPhotoCallback callback, ImageSelectDialog.OnItemSelectedListener itemSelectedListener) {
        super(activity, callback);
        this.mItemSelectedListener = itemSelectedListener;
        dialogPhoto = new WeakReference(newDialogInstance(mItemSelectedListener));
    }

    public static ImageSelectHelper newInstance(Activity activity, PhotoHelper.OnPhotoCallback callback, ImageSelectDialog.OnItemSelectedListener itemSelectedListener) {
        return new ImageSelectHelper(activity, callback, itemSelectedListener);
    }

    private ImageSelectDialog newDialogInstance(ImageSelectDialog.OnItemSelectedListener itemSelectedListener) {
        ImageSelectDialog dialog = new ImageSelectDialog(activity, itemSelectedListener);
        return dialog;
    }

    public void showDialog() {
        if (dialogPhoto == null || dialogPhoto.get() == null) {
            dialogPhoto = new WeakReference(newDialogInstance(mItemSelectedListener));
        }
        dialogPhoto.get().show();
    }

    public void hideDialog() {
        if (dialogPhoto == null || dialogPhoto.get() != null) {
            dialogPhoto.get().hide();
        }
    }

    public void dismissDialog() {
        if (dialogPhoto == null || dialogPhoto.get() != null) {
            dialogPhoto.get().dismiss();
        }
    }
}
