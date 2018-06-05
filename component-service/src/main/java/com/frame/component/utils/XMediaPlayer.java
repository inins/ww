package com.frame.component.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import timber.log.Timber;

public class XMediaPlayer extends MediaPlayer
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSE = 4;
    public static final int STATE_COMPLETION = 5;
    public static final int STATE_STOP = 6;

    @IntDef({
            STATE_IDLE,
            STATE_PREPARING,
            STATE_PREPARED,
            STATE_PLAYING,
            STATE_PAUSE,
            STATE_COMPLETION,
            STATE_STOP

    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MP_State {
    }

    public interface StateListener {
        void onStateChange(@MP_State int state);
    }

    // 是否准备好了
    private boolean mIsPrepared = false;
    // 当前播放的音乐
    private String mPath;
    private Uri mUri;
    private FileDescriptor mFileDescriptor;

    // 是否在准备好后开始播放
    private boolean mPlayWhenPrepared;
    // 是否循环播放
    private boolean mCircle;

    private @MP_State int mState;
    private StateListener mStateListener;

    @Override
    public void onPrepared(MediaPlayer mp) {
        mIsPrepared = true;
        setState(STATE_PREPARED);

        if (mPlayWhenPrepared) {
            start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        setState(STATE_COMPLETION);
    }

    public XMediaPlayer() {
        super();

        setOnPreparedListener(this);
        setOnCompletionListener(this);
    }

    /**
     * 重置资源
     * @param path 资源路径
     * @param playWhenPrepared 准备完成后是否开始播放
     *
     * @return 调用是否成功
     */
    public boolean reset(String path, boolean playWhenPrepared) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }

        if (path.equals(mPath)) {
            return false;
        }

        preResetDataSource(playWhenPrepared);

        resetDataSource(path);

        return true;
    }

    public boolean reset(String path) {
        return reset(path, false);
    }

    public boolean reset(Context context, Uri uri, boolean playWhenPrepared) {
        if (null == context) return false;
        if (null == uri) return false;

        if (null != mUri && mUri.equals(uri)) {
            return false;
        }

        preResetDataSource(playWhenPrepared);

        resetDataSource(context, uri);

        return true;
    }

    public boolean reset(Context context, Uri uri) {
        return reset(context, uri, false);
    }

    public boolean reset(FileDescriptor fileDescriptor, boolean playWhenPrepared) {
        if (null == fileDescriptor) return false;
        if (null != mFileDescriptor && mFileDescriptor == fileDescriptor) return false;

        preResetDataSource(playWhenPrepared);

        resetDataSource(fileDescriptor);

        return true;
    }

    public boolean reset(FileDescriptor fileDescriptor) {
        return reset(fileDescriptor, false);
    }

    /**
     * 重置前的操作
     * @param playWhenPrepared 准备完成后是否开始播放
     */
    private void preResetDataSource(boolean playWhenPrepared) {
        mPlayWhenPrepared = playWhenPrepared;
        mIsPrepared = false;

        stop();
        reset();
    }

    /**
     * 通过资源路径重置播放资源
     * @param path 资源路径
     */
    private void resetDataSource(String path) {
        try {
            mPath = path;
            setDataSource(path);
            prepareAsync();
            setState(STATE_PREPARING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过FileDescriptor重置播放资源
     * @param fileDescriptor FileDescriptor
     */
    private void resetDataSource(FileDescriptor fileDescriptor) {
        try {
            mFileDescriptor = fileDescriptor;
            setDataSource(fileDescriptor);
            prepareAsync();
            setState(STATE_PREPARING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过Content URI重置播放资源
     * @param context context
     * @param uri 资源路劲Uri
     */
    private void resetDataSource(Context context, Uri uri) {
        try {
            mUri = uri;
            setDataSource(context, uri);
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
                Timber.i("暂停");
                pause();
            } else {
                Timber.i("播放");
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

            setLooping(isCircle());

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

    public boolean isCircle() {
        return mCircle;
    }

    public void setCircle(boolean circle) {
        mCircle = circle;
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

    public void onDestroy() {
        super.stop();
        super.release();
    }

    /**
     * 设置状态变化监听
     * @param stateListener listener
     */
    public void setStateListener(StateListener stateListener) {
        mStateListener = stateListener;
    }
}
