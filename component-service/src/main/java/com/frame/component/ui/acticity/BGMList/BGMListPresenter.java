package com.frame.component.ui.acticity.BGMList;

import android.media.MediaPlayer;

import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityScope
public class BGMListPresenter extends
        BasePresenter<BGMListContract.Model, BGMListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    // 音乐列表
    List<Music> mMusicList = new ArrayList<>();

    // 音乐播放器
    MediaPlayer mMediaPlayer;
    // 正在播放的音乐
    Music mPlayingMusic;
    // 当前选中的音乐
    Music mSelectMusic;
    // 上层传过来的Music
    Music mOrigialMusic;
    // 正在准备
    boolean mPreparing;

    @Inject
    public BGMListPresenter(BGMListContract.Model model, BGMListContract.View view) {
        super(model, view);
    }

    public List<Music> getMusicList() {
        return mMusicList;
    }

    public void resetMusicList() {
        mMusicList.clear();

        mMusicList.add(Music.defaultMusic());
    }

    /**
     * 从服务器加载音乐列表
     */
    public void loadBGMList() {
        mApiHelper.execute(mRootView,
                mModel.musicList(10, 0),
                new ErrorHandleSubscriber<Musics>(mErrorHandler) {
                    @Override
                    public void onNext(Musics musics) {
                        mMusicList.addAll(musics.getList());

                        mRootView.onNotifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                }, new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                    }
                });
    }

    /**
     * 初始化音乐播放器 MediaPlayer
     */
    public void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPreparing = false;
                // 准备好了，直接开始播放
                playMusic();

                mRootView.hideLoading();
            }
        });
    }

    private void preparePlayer(Music music) {
        try {
            mMediaPlayer.setDataSource(music.getUrl());
            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();

            mPreparing = true;

            mRootView.showLoading();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Music getOrigialMusic() {
        return mOrigialMusic;
    }

    public void setOrigialMusic(Music origialMusic) {
        mOrigialMusic = origialMusic;
    }

    /**
     * 选中音乐
     *
     * @param music 选中的音乐
     * @return
     */
    public boolean selectMusic(Music music) {
        if (null == mSelectMusic) {
            mSelectMusic = music;
            return true;
        } else if (mSelectMusic.getMusicId() != music.getMusicId()) {
            mSelectMusic = music;
            return true;
        }

        return false;
    }

    /**
     * 音乐是否在播放
     *
     * @param music music
     * @return 是否正在播放
     */
    public boolean isPlaying(Music music) {
        if (null != mPlayingMusic && mPlayingMusic.getMusicId() == music.getMusicId()) {
            return mMediaPlayer.isPlaying();
        }

        return false;
    }

    /**
     * 是否是选中的音乐
     *
     * @param music music
     * @return 是否选中
     */
    public boolean isSelected(Music music) {
        if (mSelectMusic != null) {
            return mSelectMusic.getMusicId() == music.getMusicId();
        }

        return false;
    }

    /**
     * 设置当前播放音乐
     *
     * @param playingMusic music
     */
    public void setPlayingMusic(Music playingMusic) {
        mPlayingMusic = playingMusic;
    }

    /**
     * 设置选中音乐
     *
     * @param selectMusic music
     */
    public void setSelectMusic(Music selectMusic) {
        mSelectMusic = selectMusic;
    }

    public Music getSelectMusic() {
        return mSelectMusic;
    }

    /**
     * 点击列表中 播放或者暂停 时的操作
     *
     * @param music music
     */
    public void controlPlayer(Music music) {
        if (null == mMediaPlayer) return;

        // 如果当前没有正在播放的音乐
        // 或者当前播放的音乐不是选择的音乐
        // 那么都是开始新的播放
        if (null == mPlayingMusic ||
                (null != mPlayingMusic && mPlayingMusic.getMusicId() != music.getMusicId())) {
            if (null != mPlayingMusic) {
                stopMusic();
            }

            setPlayingMusic(music);

            preparePlayer(music);
        } else {
            if (mMediaPlayer.isPlaying()) {
                pauseMusic();
            } else {
                playMusic();
            }
        }
    }

    /**
     * 播放音乐
     */
    public void playMusic() {
        if (null == mMediaPlayer) return;
        if (mPreparing) return;

        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();

            // 顶部播放状态动画开始
            mRootView.resetStateAnim(true);

            mRootView.onNotifyDataSetChanged();
        }
    }

    /**
     * 暂停播放
     */
    public void pauseMusic() {
        if (null == mMediaPlayer) return;
        if (mPreparing) return;

        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();

            // 顶部播放状态动画开始
            mRootView.resetStateAnim(false);

            mRootView.onNotifyDataSetChanged();
        }
    }

    /**
     * 停止播放
     */
    public void stopMusic() {
        if (null == mMediaPlayer) return;
        if (mPreparing) return;

        mMediaPlayer.stop();
        mMediaPlayer.reset();

        // 顶部播放状态动画开始
        mRootView.resetStateAnim(false);

        mRootView.onNotifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;

        if (null != mMediaPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
    }
}