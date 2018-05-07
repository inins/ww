package com.wang.social.topic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.utils.DataHelper;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import timber.log.Timber;

public class AudioImageUtil {
    // 最大时长
    public static final int MAX_DURATION = 120;
    private static Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    public static String createAudioImage(Context context, int maxWidth, int duration) {

        Bitmap bitmap = createBitmap(context, maxWidth, duration);

        String result = saveBitmap(context, bitmap);

        bitmap.recycle();

        return result;
    }

    public static Bitmap createBitmap(Context context, int maxWidth, int duration) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.topic_icon_voice2);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap icon = bitmapDrawable.getBitmap();

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#18C3FF"));
        textPaint.setTextSize(SizeUtils.sp2px(12));

        Rect rect = new Rect();
        String text = "" + duration + "s";
        textPaint.getTextBounds(text, 0, text.length(), rect);

        int offset = SizeUtils.dp2px(6);

        duration = Math.min(duration, MAX_DURATION);
        int minAudioW = icon.getWidth() * 2 + offset;
        int maxAudioW = maxWidth - offset - rect.width() - minAudioW;
        int audioW = (int) (maxAudioW * duration / MAX_DURATION);
        audioW += minAudioW;

        Bitmap bitmap = Bitmap.createBitmap(
                audioW + offset + rect.width(),
                icon.getHeight() + SizeUtils.dp2px(5), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);


        Paint rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.parseColor("#18C3FF"));
        rectPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, audioW, bitmap.getHeight(), rectPaint);

        canvas.drawBitmap(icon,
                SizeUtils.dp2px(6),
                (bitmap.getHeight() - icon.getHeight()) / 2,
                new Paint());

        canvas.drawText(text,
                bitmap.getWidth() - rect.width() - SizeUtils.dp2px(1),
                bitmap.getHeight() - SizeUtils.dp2px(2),
                textPaint);


        return bitmap;
    }

    private static File getCacheDir(Context context) {
        File file = FrameUtils.obtainAppComponentFromContext(context).cacheFile();
        return DataHelper.makeDirs(new File(file, "audioPic"));
    }

    private static String getDefaultFilepath(Context context) {
        String suffix = ".jpg";
        if (compressFormat == Bitmap.CompressFormat.JPEG) {
            suffix = ".jpg";
        } else if (compressFormat == Bitmap.CompressFormat.PNG) {
            suffix = ".png";
        }

//        return getCacheDir(context).getAbsolutePath() + "/" + System.currentTimeMillis() + suffix;
        return Environment.getExternalStorageDirectory() + "/WW/AudioPics/" + System.currentTimeMillis() + suffix;
    }


    private static String saveBitmap(Context context, Bitmap bitmap) {
        if (null == bitmap) return "";

        String tempPath = getDefaultFilepath(context);

        Timber.i("保存图片 : " + tempPath);

        File file = new File(tempPath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(compressFormat, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bitmap.recycle();

        return tempPath;
    }
}
