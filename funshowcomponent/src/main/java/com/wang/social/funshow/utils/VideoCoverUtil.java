package com.wang.social.funshow.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.util.Hashtable;

public class VideoCoverUtil {

    public static Bitmap createVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            if (filePath.startsWith("http://")
                    || filePath.startsWith("https://")
                    || filePath.startsWith("widevine://")) {
                retriever.setDataSource(filePath, new Hashtable<String, String>());
            } else {
                retriever.setDataSource(filePath);
            }
            bitmap = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            }
        }
        if (bitmap == null) {
            return null;
        }
        // Scale down the bitmap if it's too large.
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        int max = Math.max(width, height);
//        if (max > 512) {
//            float scale = 512f / max;
//            int w = Math.round(scale * width);
//            int h = Math.round(scale * height);
//            bitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
//        }
//        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 96, 96, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
}
