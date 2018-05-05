package com.wang.social.im.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;

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

    private ImageSelectHelper(Activity activity, PhotoHelper.OnPhotoCallback callback) {
        super(activity, callback);
        dialogPhoto = new WeakReference(newDialogInstance());
    }

    public static ImageSelectHelper newInstance(Activity activity, PhotoHelper.OnPhotoCallback callback) {
        return new ImageSelectHelper(activity, callback);
    }

    private ImageSelectDialog newDialogInstance() {
        ImageSelectDialog dialog = new ImageSelectDialog(activity, new ImageSelectDialog.OnItemSelectedListener() {
            @Override
            public void onGallerySelected() {

            }

            @SuppressLint("CheckResult")
            @Override
            public void onShootSelected() {
                new RxPermissions(activity)
                        .requestEach(Manifest.permission.CAMERA)
                        .subscribe(new Consumer<Permission>() {
                            @Override
                            public void accept(Permission permission) throws Exception {
                                if (permission.granted){
                                    startCamera();
                                }else if (permission.shouldShowRequestPermissionRationale){
                                    ToastUtil.showToastShort("请在设置中打开相机权限");
                                }
                            }
                        });

            }

            @Override
            public void onAlbumSelected() {
                startPhoto();
            }
        });
        return dialog;
    }

    public void showDialog() {
        if (dialogPhoto == null || dialogPhoto.get() == null) {
            dialogPhoto = new WeakReference(newDialogInstance());
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
