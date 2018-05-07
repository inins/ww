package com.wang.social.topic.app;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.frame.base.BasicActivity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.di.component.AppComponent;
import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.utils.AudioImageUtil;

import butterknife.BindView;

public class TestActivity extends BasicActivity {

    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.image_view2)
    ImageView mImageView2;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_test;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        Bitmap bitmap = AudioImageUtil.createBitmap(this,
                800, 5);
        mImageView.setImageBitmap(bitmap);

        String path = AudioImageUtil.createAudioImage(this, SizeUtils.dp2px(300), 5);
        ImageLoaderHelper.loadImg(mImageView2, path);
    }



    private Bitmap getBitmap() {
        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(sp2px(16));

        Bitmap icon = fromRes(R.drawable.topic_icon_voice2);

        Bitmap bitmap = Bitmap.createBitmap(400, icon.getHeight() + 10 * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.BLACK);

//        float[] radiusArray = { 21f, 21f, 20f, 20f, 20f, 20f, 0f, 0f };
//        Path path = new Path();
//        path.addRoundRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), radiusArray, Path.Direction.CW);
//        Paint pathPait = new Paint();
//        pathPait.setAntiAlias(true);
//        pathPait.setColor(Color.BLUE);
//        pathPait.setStyle(Paint.Style.FILL);
//        canvas.drawPath(path, pathPait);


        canvas.drawBitmap(icon, 10, 10, new Paint());
        canvas.drawText("120s", 200, bitmap.getHeight(), textPaint);


        return bitmap;
    }

    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private Bitmap fromRes(int id) {
        Drawable drawable = getResources().getDrawable(id);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }
}
