package com.frame.component.utils.imagedownloader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.frame.mvp.IView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;


public class ImageDownloader {
    public interface ImageDownloaderCallback {
        void onDownloadSuccess();
        void onDownloadFailed();
    }

    public static void start(Context context, IView view, String url, String filepath, ImageDownloaderCallback callback) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Timber.i("onResourceReady");

                        saveImageToGallery(context, resource, filepath);

                        if (null != callback) {
                            callback.onDownloadSuccess();
                        }

                        if (null != view) {
                            view.hideLoading();
                        }
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        Timber.i("onLoadStarted");
                        if (null != view) {
                            view.showLoading();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Timber.i("onLoadFailed");

                        if (null != callback) {
                            callback.onDownloadFailed();
                        }


                        if (null != view) {
                            view.hideLoading();
                        }
                    }
                });
    }


    private static void saveImageToGallery(Context context, Bitmap bmp, String filepath) {
        File currentFile;

        if (!TextUtils.isEmpty(filepath)) {
            File file = new File(filepath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            currentFile = new File(filepath);
        } else {
            File file = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).getAbsoluteFile();

            if (!file.exists()) {
                file.mkdirs();
            }

            String fileName = System.currentTimeMillis() + ".jpg";

            currentFile = new File(file, fileName);
        }

        if (currentFile.exists()) {
            currentFile.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(new File(currentFile.getPath()))));
    }
}
