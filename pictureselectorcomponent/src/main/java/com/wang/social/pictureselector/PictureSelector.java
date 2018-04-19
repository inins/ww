package com.wang.social.pictureselector;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.wang.social.pictureselector.model.SelectorSpec;

import java.lang.ref.WeakReference;

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

    public void forResult(int requestCode) {
        Intent intent = new Intent();
        if (activity != null) {
            intent.setClass(activity.get(), ActivityPictureSelector.class);
            activity.get().startActivityForResult(intent, requestCode);
        } else if (fragment != null) {
            intent.setClass(fragment.get().getActivity(), ActivityPictureSelector.class);
            fragment.get().startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalArgumentException("you must call from() first");
        }
    }
}
