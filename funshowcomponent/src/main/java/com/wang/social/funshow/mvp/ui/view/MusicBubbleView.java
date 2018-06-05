package com.wang.social.funshow.mvp.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.utils.XMediaPlayer;
import com.frame.utils.TimeUtils;
import com.wang.social.funshow.R;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class MusicBubbleView extends FrameLayout implements XMediaPlayer.StateListener {

    private XMediaPlayer mXMediaPlayer;

    ImageView btnPlay;
    TextView textName;
    TextView textTime;

    // 播放是否循环
    private boolean looping;
    Disposable mDisposable;
    SimpleDateFormat mTimeFormat = new SimpleDateFormat("mm:ss");
    private Music music;

    public MusicBubbleView(Context context) {
        this(context, null);
    }

    public MusicBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.funshow_lay_detail_music, this, true);
        textName = findViewById(R.id.text_nusic_name);
        textTime = findViewById(R.id.text_nusic_time);
        btnPlay = findViewById(R.id.btn_play);

        btnPlay.setOnClickListener(v -> playMusic());
        resetView(false);

        if (!isInEditMode()) {
            mXMediaPlayer = new XMediaPlayer();
            mXMediaPlayer.setCircle(false);
            mXMediaPlayer.setStateListener(this);
        }
    }

    private void init(Context context) {
    }

    /**
     * 根据播放器状态刷新UI
     */
    private void resetView(boolean prepared) {
        if (prepared) {
            if (null != mXMediaPlayer) {
                textName.setText(music.getMusicName());
                textTime.setText(mTimeFormat.format(TimeUtils.millis2Date(mXMediaPlayer.getDuration())));
            }
        } else {
            textName.setText("");
            textTime.setText(mTimeFormat.format(0L));
        }
    }

    @Override
    public void onStateChange(int state) {
        switch (state) {
            case XMediaPlayer.STATE_IDLE:
            case XMediaPlayer.STATE_STOP: // 未准备好的情况下不能点击，UI回到初始状态
                resetView(false);
                break;
            case XMediaPlayer.STATE_PREPARING: // 开始准备，UI回到初始状态，并且通知状态变化
                resetView(false);
                break;
            case XMediaPlayer.STATE_PREPARED: // 准备好了，更新UI
                resetView(true);
                break;
            case XMediaPlayer.STATE_PLAYING: // 开始播放
                // 开始一个计时器，每隔一个时间刷新一下进度
                if (null == mDisposable) {
                    mDisposable = Observable.interval(100, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> {
                                if (null != mXMediaPlayer && mXMediaPlayer.isPlaying()) {
                                    textTime.setText(mTimeFormat.format(mXMediaPlayer.getCurrentPosition()));
                                }
                            });
                }
                // 播放图标变化
                btnPlay.setSelected(true);
                break;
            case XMediaPlayer.STATE_PAUSE:
                // 播放图标变化
                btnPlay.setSelected(false);
                break;
            case XMediaPlayer.STATE_COMPLETION:
                // 播放完成
                btnPlay.setSelected(false);
                textTime.setText(mTimeFormat.format(0L));
                break;
        }
    }

    //////////////////////////////////////////////////

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != mDisposable) {
            mDisposable.dispose();
        }
        if (null != mXMediaPlayer) {
            mXMediaPlayer.stop();
            mXMediaPlayer.release();
        }
    }

    public void onPause() {
        if (null != mXMediaPlayer) mXMediaPlayer.pause();
    }

    public void onStop() {
        if (null != mXMediaPlayer) mXMediaPlayer.stop();
    }

    public void resetMusic(Music music) {
        this.music = music;
        if (null != mXMediaPlayer) mXMediaPlayer.reset(music.getUrl(), true);
    }

    private void playMusic() {
        if (null != mXMediaPlayer) mXMediaPlayer.play();
    }

    ////////////////////////////////////////////////////

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }
}
