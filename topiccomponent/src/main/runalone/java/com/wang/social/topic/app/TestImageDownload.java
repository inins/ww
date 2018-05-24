package com.wang.social.topic.app;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.utils.imagedownloader.ImageDownloader;
import com.frame.di.component.AppComponent;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;

import butterknife.OnClick;

public class TestImageDownload extends BaseAppActivity implements IView {


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_test_image_download;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {

    }

    private void downloadImage(String url) {
        ImageDownloader.start(this, this,
                url,
                null,
                new ImageDownloader.ImageDownloaderCallback() {
                    @Override
                    public void onDownloadSuccess() {
                        ToastUtil.showToastShort("下载成功");
                    }

                    @Override
                    public void onDownloadFailed() {
                        ToastUtil.showToastShort("下载失败");
                    }
                });
    }

    @OnClick(R.id.download_image_failed_btn)
    public void downloadFailed() {
        downloadImage("https://goss3.vcg.com/creative/vcg/800/new/VCG21115433");
    }

    @OnClick(R.id.download_image_btn)
    public void download() {
        downloadImage("https://goss3.vcg.com/creative/vcg/800/new/VCG211154338902.jpg");
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
    }
}
