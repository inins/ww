package com.frame.component.utils;

import android.media.MediaPlayer;
import android.support.annotation.IntDef;

import com.frame.component.ui.acticity.BGMList.Music;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class XMediaPlayer extends MediaPlayer implements MediaPlayer.OnPreparedListener {
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSE = 4;
    public static final int STATE_STOP = 5;

    @IntDef({
            STATE_IDLE,
            STATE_PREPARING,
            STATE_PREPARED,
            STATE_PLAYING,
            STATE_PAUSE,
            STATE_STOP

    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MP_State {
    }

    public interface StateListener {
        void onStateChange(@MP_State int state);
    }

    // 是否准备好了
    boolean mIsPrepared = false;
    // 当前播放的音乐
    Music mMusic;
    // 是否在准备好后开始播放
    boolean mPlayWhenPrepared;

    @MP_State int mState;
    StateListener mStateListener;

    @Override
    public void onPrepared(MediaPlayer mp) {
        mIsPrepared = true;
        setState(STATE_PREPARED);

        if (mPlayWhenPrepared) {
            start();
        }
    }

    public XMediaPlayer() {
        super();

        setOnPreparedListener(this);
    }

    /**
     * 重设音乐
     * @param music 音乐
     */
    public void resetMusic(Music music) {
        resetMusic(music, false);
    }

    /**
     * 重设音乐
     * @param music 音乐
     * @param playWhenPrepared 是否在准备好后开始播放
     */
    public void resetMusic(Music music, boolean playWhenPrepared) {
        if (null == music) return;
        // 音乐没变化
        if (null != mMusic &&
                mMusic.getMusicId() == music.getMusicId()) {
            return;
        }

        mMusic = music;
        mPlayWhenPrepared = playWhenPrepared;

        mIsPrepared = false;

        stop();
        reset();

        try {
            setDataSource(mMusic.getUrl());
            setLooping(isLooping());
            prepareAsync();

            setState(STATE_PREPARING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放按钮控制，播放或者暂停
     */
    public void play() {
        if (mIsPrepared) {
            if (isPlaying()) {
                pause();
            } else {
                start();
            }
        }
    }

    /**
     * 开始播放
     */
    public void start() {
        if (mIsPrepared) {
            super.start();

            setState(STATE_PLAYING);
        }
    }

    /**
     * 暂停播放
     */
    public void pause() {
        if (mIsPrepared) {
            super.pause();

            setState(STATE_PAUSE);
        }
    }


    /**
     * 停止
     */
    public void stop() {
        if (mIsPrepared) {
            super.stop();

            setState(STATE_STOP);
        }
    }

    private void setState(@MP_State int state) {
        if (mState != state) {
            mState = state;

            if (null != mStateListener) {
                mStateListener.onStateChange(state);
            }
        }
    }

    public int getState() {
        return mState;
    }

    public Music getMusic() {
        return mMusic;
    }

    /**
     * 设置状态变化监听
     * @param stateListener listener
     */
    public void setStateListener(StateListener stateListener) {
        mStateListener = stateListener;
    }
}
