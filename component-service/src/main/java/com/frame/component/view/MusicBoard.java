package com.frame.component.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.utils.XMediaPlayer;
import com.frame.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class MusicBoard extends FrameLayout implements XMediaPlayer.StateListener {


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

    private XMediaPlayer mXMediaPlayer;

    ImageView mControlIV;
    TextView mNameTV;
    TextView mTime1TV;
    TextView mTime2TV;
    SeekBar mSeekBar;

    Music mMusic;

    StateListener mStateListener;

    Disposable mDisposable;

    SimpleDateFormat mTimeFormat = new SimpleDateFormat("mm:ss", Locale.CHINA);

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

        resetView(false);

        mControlIV.setOnClickListener(view -> playMusic());

        mSeekBar.setEnabled(false);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTime1TV.setText(mTimeFormat.format(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 播放和暂停的时候才可以拖动
                if (null != mXMediaPlayer &&
                        (mXMediaPlayer.getState() == XMediaPlayer.STATE_PLAYING ||
                                mXMediaPlayer.getState() == XMediaPlayer.STATE_PAUSE)) {
                    mXMediaPlayer.seekTo(seekBar.getProgress());
                    mTime1TV.setText(mTimeFormat.format(seekBar.getProgress()));
                }
            }
        });

        mXMediaPlayer = new XMediaPlayer();
        mXMediaPlayer.setStateListener(this);
    }

    public void setStateListener(StateListener stateListener) {
        mStateListener = stateListener;
    }

    /**
     * 根据播放器状态刷新UI
     *
     * @param prepared 是否准备好了
     */
    private void resetView(boolean prepared) {
        if (prepared) {
            if (null != mXMediaPlayer) {
                mNameTV.setText(null == mMusic ? "" : mMusic.getMusicName());


                mTime1TV.setText(mTimeFormat.format(0L));
                mTime2TV.setText(mTimeFormat.format(TimeUtils.millis2Date(mXMediaPlayer.getDuration())));
                mSeekBar.setMax(mXMediaPlayer.getDuration());

                mSeekBar.setEnabled(true);
            }
        } else {
            mSeekBar.setProgress(0);
            mSeekBar.setEnabled(false);
            mNameTV.setText("");
            mTime1TV.setText("");
            mTime2TV.setText("");
        }
    }

    public void resetMusic(Music music) {
        if (null != mXMediaPlayer && null != music) {
            if (mXMediaPlayer.reset(music.getUrl())) {
                mMusic = music;
            } else {
                resetView(false);
            }
        }
    }

    public void resetMusic(String path) {
        if (null != mXMediaPlayer) {
            mXMediaPlayer.reset(path);
        }
    }

    private void playMusic() {
        if (null != mXMediaPlayer) {
            mXMediaPlayer.play();
        }
    }

    @Override
    public void onStateChange(int state) {
        Timber.i("onStateChange : " + state);
        switch (state) {
            case XMediaPlayer.STATE_IDLE:
            case XMediaPlayer.STATE_STOP: // 未准备好的情况下不能点击，UI回到初始状态
                resetView(false);
                break;
            case XMediaPlayer.STATE_PREPARING: // 开始准备，UI回到初始状态，并且通知状态变化
                if (null != mStateListener) {
                    mStateListener.onStartPrepare();
                }

                resetView(false);
                break;
            case XMediaPlayer.STATE_PREPARED: // 准备好了，更新UI
                resetView(true);

                if (null != mStateListener) {
                    mStateListener.onPrepared();
                }
                break;
            case XMediaPlayer.STATE_PLAYING: // 开始播放
                // 开始一个计时器，每隔一个时间刷新一下进度
                if (null == mDisposable) {
                    mDisposable = Observable.interval(100, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    if (null != mXMediaPlayer && mXMediaPlayer.isPlaying()) {
                                        mSeekBar.setProgress(mXMediaPlayer.getCurrentPosition());
                                        mTime1TV.setText(mTimeFormat.format(mXMediaPlayer.getCurrentPosition()));
                                    }
                                }
                            });
                }
                // 播放图标变化
                mControlIV.setImageResource(R.drawable.common_ic_music_stop_big);
                break;
            case XMediaPlayer.STATE_PAUSE:
                // 播放图标变化
                mControlIV.setImageResource(R.drawable.common_ic_music_start_big);
                break;
            case XMediaPlayer.STATE_COMPLETION:
                // 播放完成
                mControlIV.setImageResource(R.drawable.common_ic_music_start_big);
                break;
        }
    }

    public void setLooping(boolean looping) {
        if (null != mXMediaPlayer) {
            mXMediaPlayer.setCircle(looping);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (null != mDisposable) {
            mDisposable.dispose();
        }

        if (null != mXMediaPlayer) {
            mXMediaPlayer.onDestroy();
        }
    }

    public void onPause() {
        Timber.i("onPause");

        if (null != mXMediaPlayer) {
            mXMediaPlayer.pause();
        }
    }

    public void onStop() {
        Timber.i("onStop");

        if (null != mXMediaPlayer) {
            mXMediaPlayer.stop();
        }
    }
}
