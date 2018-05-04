package com.frame.component.view.bundleimgview;

import android.text.TextUtils;

import lombok.Data;

/**
 * Created by Administrator on 2016/6/12 0012.
 */
@Data
public class BundleImgEntity {

    private boolean isVideo;
    private String path;

    public BundleImgEntity() {
    }

    public BundleImgEntity(String path) {
        this.path = path;
        //TODO:由于返回文件路径只是一个字符串，所以只能从文件后缀判断是视频还是图片，目前APP限定只能录制mp4格式的视频，这里暂且只判断是否.mp4结尾，后期需要拓展优化
        if (!TextUtils.isEmpty(path) && path.endsWith(".mp4")) {
            isVideo = true;
        }
    }
}
