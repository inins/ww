package com.wang.social.pictureselector;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.frame.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.pictureselector.model.SelectorSpec;

import java.lang.ref.WeakReference;

import io.reactivex.functions.Consumer;

/**
 * Created by King on 2018/3/29.
 *
 * PictureSelector.from(context)
 * .maxSelection(int)
 * .isClip(true)
 * .spanCount(int)
 * .forResult(requestCode)
 */

public class PictureSelector {
    public static final String TAG = PictureSelector.class.getSimpleName().toString();

    public static final String NAME_FILE_PATH = "NAME_FILE_PATH";
    public static final String NAME_FILE_PATH_LIST = "NAME_FILE_PATH_LIST";

    public static final int REQUEST_CODE_CLIP = 10101;

    public static PictureSelector from(@NonNull Activity activity) {
        return new PictureSelector(activity);
    }

    public static PictureSelector from(@NonNull Fragment fragment) {
        return new PictureSelector(fragment);
    }

    /**
     * 裁切图片
     * @param activity activity
     * @param image 图片路径
     * @param requestCode requestCode
     */
    public static void clip(@NonNull Activity activity, String image, int requestCode) {
        Intent intent = new Intent(activity, ActivityPictureClip.class);
        intent.putExtra(PictureSelector.NAME_FILE_PATH, image);
        activity.startActivityForResult(intent, requestCode);
    }

    private final WeakReference<Activity> activity;
    private final WeakReference<Fragment> fragment;
    private final SelectorSpec selectorSpec;

    private PictureSelector(Activity activity) {
        this(activity, null);
    }

    private PictureSelector(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private PictureSelector(Activity activity, Fragment fragment) {
        this.activity = new WeakReference<>(activity);
        this.fragment = new WeakReference<>(fragment);
        selectorSpec = SelectorSpec.getCleanInstance();
    }

    /**
     * 最多可选择多少张图片
     * @param maxSelection
     * @return
     */
    public PictureSelector maxSelection(int maxSelection) {
        selectorSpec.setMaxSelection(maxSelection);

        return this;
    }

    /**
     * 预览时显示的图片列数
     * @param spanCount
     * @return
     */
    public PictureSelector spanCount(int spanCount) {
        selectorSpec.setSpanCount(spanCount);

        return this;
    }

    /**
     * 是否对选中图片进行裁切（裁切只在选择单张图片时有效）
     * @param isClip
     * @return
     */
    public PictureSelector isClip(boolean isClip) {
        selectorSpec.setClip(isClip);

        return this;
    }

    @SuppressLint("CheckResult")
    public void forResult(int requestCode) {
        Activity activity = null;
        if (this.activity.get() != null) {
            activity = this.activity.get();
        } else if (null != this.fragment.get()) {
            activity = this.fragment.get().getActivity();
        }

        new RxPermissions(activity)
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {

                            Intent intent = new Intent();
                            if (PictureSelector.this.activity != null) {
                                intent.setClass(PictureSelector.this.activity.get(), ActivityPictureSelector.class);
                                PictureSelector.this.activity.get().startActivityForResult(intent, requestCode);
                            } else if (PictureSelector.this.fragment != null) {
                                intent.setClass(fragment.get().getActivity(), ActivityPictureSelector.class);
                                fragment.get().startActivityForResult(intent, requestCode);
                            } else {
                                throw new IllegalArgumentException("you must call from() first");
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            ToastUtil.showToastLong("请打开文件读写权限");
                        }
                    }
                });

    }
}
