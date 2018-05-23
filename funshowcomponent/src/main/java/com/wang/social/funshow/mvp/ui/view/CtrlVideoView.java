package com.wang.social.funshow.mvp.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.funshow.R;
import com.frame.component.utils.VideoCoverUtil;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class CtrlVideoView extends FrameLayout implements View.OnClickListener {

    private VideoView videoview;
    private TextView text_time_now;
    private TextView text_time_all;
    private View btn_play;
    private SeekBar seek;
    private View lay_ctrl_bar;
    private View lay_board;
    private ImageView img_cover;

    private Disposable mDisposable;
    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);

    private boolean isPrepare;
    private boolean isSeeking;
    private long mClickTime;

    public CtrlVideoView(@NonNull Context context) {
        this(context, null);
    }

    public CtrlVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CtrlVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.funshow_lay_detail_video, this, true);
        initView();
        initCtrl();
    }

    private void initView() {
        Log.e("video", "initVideo");
        videoview = findViewById(R.id.citl_videoview);
        text_time_now = findViewById(R.id.text_time_now);
        text_time_all = findViewById(R.id.text_time_all);
        btn_play = findViewById(R.id.btn_play);
        seek = findViewById(R.id.seek);
        lay_ctrl_bar = findViewById(R.id.lay_ctrl_bar);
        lay_board = findViewById(R.id.lay_board);
        img_cover = findViewById(R.id.img_cover);

        btn_play.setOnClickListener(this);
        this.setOnClickListener(v -> {
            if (System.currentTimeMillis() - mClickTime < 200) {
                //双击
                callPlayOrPause();
            } else {
                mClickTime = System.currentTimeMillis();
                //表示单击
                if (!isPrepare) return;
                lay_board.setVisibility(lay_board.getVisibility() == VISIBLE ? GONE : VISIBLE);
            }
        });
    }

    private void initCtrl() {
        videoview.setOnCompletionListener(mp -> {
            btn_play.setSelected(false);
        });
        videoview.setOnPreparedListener(mp -> {
            isPrepare = true;
            lay_board.setVisibility(VISIBLE);
            seek.setProgress(0);
            seek.setMax(videoview.getDuration());
            text_time_now.setText(mTimeFormat.format(0));
            text_time_all.setText(mTimeFormat.format(videoview.getDuration()));
        });
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text_time_now.setText(mTimeFormat.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeeking = false;
                videoview.seekTo(seekBar.getProgress());
                btn_play.setSelected(true);
                text_time_now.setText(mTimeFormat.format(seekBar.getProgress()));
            }
        });
        mDisposable = Observable.interval(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (videoview.isPlaying() && !isSeeking) {
                        refreshProgress();
                    }
                });
    }

    private void refreshProgress() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seek.setProgress(videoview.getCurrentPosition(), true);
        } else {
            seek.setProgress(videoview.getCurrentPosition());
        }
        text_time_now.setText(mTimeFormat.format(videoview.getCurrentPosition()));
    }

    private void callPlayOrPause() {
        if (videoview.isPlaying()) {
            btn_play.setSelected(false);
            pause();
        } else {
            btn_play.setSelected(true);
            start();
            img_cover.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_play) {
            callPlayOrPause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mDisposable) mDisposable.dispose();
    }

    ///////////////////////////////////

    public void setVideoURL(String url) {
        videoview.setVideoURI(Uri.parse(url));
        ImageLoaderHelper.loadImg(img_cover, url);
    }

    public void start() {

        videoview.start();
    }

    public void pause() {
        videoview.pause();
    }
}
