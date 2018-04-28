package com.wang.social.funshow.helper;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.QiNiuManager;
import com.frame.component.utils.ListUtil;
import com.frame.mvp.IView;
import com.frame.utils.FrameUtils;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.wang.social.funshow.mvp.entities.post.FunshowAddPost;
import com.wang.social.funshow.mvp.entities.post.ResourcePost;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BundleUploadhelper {

    QiNiuManager qiNiuManager;

    private String voicePath;
    private String videoPath;
    private List<String> photoPaths;

    private String voiceUrl;
    private String videoUrl;
    private List<String> photoUrls;

    private OnUploadCallback callback;

    @Inject
    public BundleUploadhelper(QiNiuManager qiNiuManager) {
        this.qiNiuManager = qiNiuManager;
    }

    public void startUpload(IView view, String voicePath, String videoPath, List<String> photoPaths, OnUploadCallback callback) {
        this.callback = callback;
        this.voicePath = voicePath;
        this.videoPath = videoPath;
        this.photoPaths = photoPaths;

        uploadVoice(view);
    }

    private void uploadVoice(IView view) {
        qiNiuManager.uploadFile(view, voicePath, new QiNiuManager.OnSingleUploadListener() {
            @Override
            public void onSuccess(String url) {
                voiceUrl = url;
                uploadVideo(view);
            }

            @Override
            public void onFail() {
                ToastUtil.showToastLong("音频上传失败");
            }
        });
    }

    private void uploadVideo(IView view) {
        qiNiuManager.uploadFile(view, videoPath, new QiNiuManager.OnSingleUploadListener() {
            @Override
            public void onSuccess(String url) {
                videoUrl = url;
                uploadPhotos(view);
            }

            @Override
            public void onFail() {
                ToastUtil.showToastLong("视频上传失败");
            }
        });
    }

    private void uploadPhotos(IView view) {
        qiNiuManager.uploadFiles(view, ListUtil.transArrayList(photoPaths), new QiNiuManager.OnBatchUploadListener() {
            @Override
            public void onSuccess(ArrayList<String> urls) {
                photoUrls = urls;
                if (callback != null) {
                    callback.onUploadSuccess(voiceUrl, videoUrl, photoUrls);
                }
            }

            @Override
            public void onFail() {
                ToastUtil.showToastLong("图片上传失败");
            }
        });
    }


    /////////////////////////

    public interface OnUploadCallback {
        void onUploadSuccess(String voiceUrl, String videoUrl, List<String> photoUrls);
    }
}
