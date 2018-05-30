package com.wang.social.topic.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private static Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.PNG;

    public static String createAudioImage(Context context, int maxWidth, int duration) {

        Bitmap bitmap = createBitmap(context, maxWidth, duration);

        String result = saveBitmap(context, bitmap);

        bitmap.recycle();

        return result;
    }

    public static Bitmap createBitmap(Context context, int maxWidth, int duration) {
        // 语音图标
        Drawable drawable = context.getResources().getDrawable(R.drawable.topic_icon_voice2);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap icon = bitmapDrawable.getBitmap();

        // 文字Paint
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#18C3FF"));
        textPaint.setTextSize(SizeUtils.sp2px(6));
        // 文字矩形
        Rect textRect = new Rect();
        String text = "" + duration + "s";
        textPaint.getTextBounds(text, 0, text.length(), textRect);

        // 图标距离左边的距离
        int leftPadding = 10;
        // 上下边距
        int tbPadding = 6;
        // 文字距离图标的距离
        int textLeftPadding = 4;

        // 语音图标宽度 = 左边距 + 图片宽度 + 右边距 + 语音长度
        int audioW =  leftPadding + icon.getWidth() + leftPadding + duration;
        // 图片高度 = 语音图标宽度 + 文字距离 + 文字宽度
        int bitmapW = audioW + textLeftPadding + textRect.width();
        // 图片高度 = 上下边距 + 图片高度
        int bitmapH = tbPadding * 2 + icon.getHeight();

        // 创建图片
        Bitmap bitmap = Bitmap.createBitmap(
                bitmapW,
                bitmapH,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        // 背景透明
        canvas.drawColor(Color.TRANSPARENT);

        // 绘制语音图标
        Paint rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(Color.parseColor("#18C3FF"));
        rectPaint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        final float[] radiusArray = {
                14f, 14f,
                14f, 14f,
                14f, 14f,
                0f, 0f };
        RectF rectRoundCW =  new RectF(0, 0, audioW, bitmapH);
        path.addRoundRect(rectRoundCW, radiusArray, Path.Direction.CCW);
        canvas.drawPath(path, rectPaint);

        // 绘制图标
        canvas.drawBitmap(icon,
                leftPadding,
                tbPadding,
                new Paint());
        // 绘制文字
        canvas.drawText(
                text,
                audioW + textLeftPadding,
                bitmap.getHeight() - 2,
                textPaint);

        return bitmap;
    }

    private static File getCacheDir(Context context) {
        File file = FrameUtils.obtainAppComponentFromContext(context).cacheFile();
        return DataHelper.makeDirs(new File(file, "audioPic"));
    }

    private static String getDefaultFilepath(Context context) {
        String suffix = ".png";
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
            bitmap.compress(compressFormat, 100, out);
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
