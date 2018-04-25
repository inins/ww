package com.frame.component.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.utils.TimeUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class MusicBoard extends FrameLayout implements MediaPlayer.OnPreparedListener {

    public interface StateListener {
        /**
         * 开始准备
         */
        void onStartPrepare();

        /**
         * 准备完成
         */
        void onPrepared();
    }

    private MediaPlayer mMediaPlayer;
    // MediaPlayer是否在准备中，调用 prepareAsync 后改变状态
    private boolean isPreparing;
    // 是否准备好了
    private boolean isPrepared;
    // 播放是否循环
    private boolean looping;
    // 上一次播放的音乐
    private Music mMusic;

    ImageView mControlIV;
    TextView mNameTV;
    TextView mTime1TV;
    TextView mTime2TV;
    SeekBar mSeekBar;

    StateListener mStateListener;

    Disposable mDisposable;

    SimpleDateFormat mTimeFormat = new SimpleDateFormat("mm:ss");

    public MusicBoard(Context context) {
        this(context, null);
    }

    public MusicBoard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicBoard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        Timber.i("init");

        LayoutInflater.from(context).inflate(R.layout.view_music_board, this);

        mNameTV = findViewById(R.id.text_musicboard_title);
        mTime1TV = findViewById(R.id.text_musicboard_time);
        mTime2TV = findViewById(R.id.text_musicboard_time_all);
        mSeekBar = findViewById(R.id.seek_musicboard);

        mControlIV = findViewById(R.id.btn_musicboard_play);
        mControlIV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic();
            }
        });

        mSeekBar.setEnabled(false);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (null != mMediaPlayer && isPrepared) {
                    mMediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(this);
    }

    public void setStateListener(StateListener stateListener) {
        mStateListener = stateListener;
    }

    private void resetView() {
        mSeekBar.setProgress(0);
        mSeekBar.setEnabled(false);
        mNameTV.setText("");
        mTime1TV.setText(mTimeFormat.format(0L));
        mTime2TV.setText(mTimeFormat.format(0L));
    }

    public void resetMusic(Music music) {
        if (null == mMediaPlayer) return;
        if (null == music) return;
        if (null != mMusic) {
            if (mMusic.getMusicId() == music.getMusicId()) return;
        }

        mMusic = music;
        isPrepared = false;

        mMediaPlayer.stop();
        mMediaPlayer.reset();

        resetView();

        // 可能是无音乐
        if (TextUtils.isEmpty(music.getUrl())) return;

        try {
            mMediaPlayer.setDataSource(mMusic.getUrl());
            mMediaPlayer.setLooping(isLooping());
            mMediaPlayer.prepareAsync();

            isPreparing = true;

            if (null != mStateListener) {
                mStateListener.onStartPrepare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMusic() {
        if (null == mMediaPlayer) return;
        if (isPreparing) return;
        if (!isPrepared) return;

        if (mMediaPlayer.isPlaying()) {
            pause();
        } else {
            start();
        }
    }

    private void start() {
        if (null != mMediaPlayer) {
            mMediaPlayer.start();

            mControlIV.setImageResource(R.drawable.common_ic_music_stop_big);

            if (null == mDisposable) {
                mDisposable = Observable.interval(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Long>() {
                            @Override
                            public void accept(Long aLong) throws Exception {
                                if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
                                    mSeekBar.setProgress(mMediaPlayer.getCurrentPosition());
                                    mTime1TV.setText(mTimeFormat.format(mMediaPlayer.getCurrentPosition()));
                                }
                            }
                        });
            }
        }
    }

    private void pause() {
        if (null != mMediaPlayer) {
            mMediaPlayer.pause();

            mControlIV.setImageResource(R.drawable.common_ic_music_start_big);
        }
    }

    private void stop() {
        if (null != mMediaPlayer) {
            mMediaPlayer.stop();

            mControlIV.setImageResource(R.drawable.common_ic_music_start_big);
        }
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (null != mDisposable) {
            mDisposable.dispose();
        }

        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Timber.i("onPrepared");

        isPreparing = false;
        isPrepared = true;

        if (null != mMusic) {
            mNameTV.setText(mMusic.getMusicName());


            mTime1TV.setText(mTimeFormat.format(0L));
            mTime2TV.setText(mTimeFormat.format(TimeUtils.millis2Date(mp.getDuration())));
            mSeekBar.setMax(mp.getDuration());

            mSeekBar.setEnabled(true);
        }

        if (null != mStateListener) {
            mStateListener.onPrepared();
        }
    }

    public void onPause() {
        Timber.i("onPause");

        pause();
    }

    public void onStop() {
        Timber.i("onStop");

        stop();
    }

    class ProgressThread implements Runnable {

        @Override
        public void run() {

        }
    }
}
