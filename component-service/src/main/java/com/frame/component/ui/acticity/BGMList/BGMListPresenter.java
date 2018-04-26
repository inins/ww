package com.frame.component.ui.acticity.BGMList;

import com.frame.component.utils.XMediaPlayer;
import com.frame.di.scope.ActivityScope;
import com.frame.http.api.ApiHelper;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

@ActivityScope
public class BGMListPresenter extends
        BasePresenter<BGMListContract.Model, BGMListContract.View> {

    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    ApiHelper mApiHelper;

    // 音乐列表
    private List<Music> mMusicList = new ArrayList<>();

    // 音乐播放器
    private XMediaPlayer mXMediaPlayer;
    // 正在播放的音乐
    private Music mPlayingMusic;
    // 当前选中的音乐
    private Music mSelectMusic;
    // 上层传过来的Music
    private Music mOriginalMusic;
    // 正在准备
    private int mCurrent = 0;

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
    public void loadBGMList(boolean refresh) {
        if (refresh) {
            mCurrent = 0;
        }

        mApiHelper.execute(mRootView,
                mModel.musicList(10, mCurrent++),
                new ErrorHandleSubscriber<Musics>(mErrorHandler) {
                    @Override
                    public void onNext(Musics musics) {
                        mCurrent = musics.current;

                        mMusicList.addAll(musics.getList());

                        mRootView.onNotifyDataSetChanged();

                        mRootView.onLoadBGMListCompleted();
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
        mXMediaPlayer = new XMediaPlayer();
        mXMediaPlayer.setStateListener(new XMediaPlayer.StateListener() {
            @Override
            public void onStateChange(int state) {

                Timber.i("onStateChange : " + state);
                switch (state) {
                    case XMediaPlayer.STATE_IDLE:
                    case XMediaPlayer.STATE_STOP:
                    case XMediaPlayer.STATE_COMPLETION:
                        // 顶部播放状态动画开始
                        mRootView.resetStateAnim(false);

                        mRootView.onNotifyDataSetChanged();
                        break;
                    case XMediaPlayer.STATE_PREPARING: // 开始准备，UI回到初始状态，并且通知状态变化
                        mRootView.showLoading();
                        break;
                    case XMediaPlayer.STATE_PREPARED: // 准备好了，更新UI
                        mRootView.hideLoading();
                        break;
                    case XMediaPlayer.STATE_PLAYING: // 开始播放
                        // 顶部播放状态动画开始
                        mRootView.resetStateAnim(true);

                        mRootView.onNotifyDataSetChanged();
                        break;
                    case XMediaPlayer.STATE_PAUSE:
                        // 顶部播放状态动画开始
                        mRootView.resetStateAnim(false);

                        mRootView.onNotifyDataSetChanged();
                        break;
                }
            }
        });
    }

    private void preparePlayer(Music music) {
        mXMediaPlayer.reset(music.getUrl(), true);
    }

    public Music getOriginalMusic() {
        return mOriginalMusic;
    }

    public void setOriginalMusic(Music originalMusic) {
        mOriginalMusic = originalMusic;
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
            return mXMediaPlayer.isPlaying();
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
        if (null == mXMediaPlayer) return;

        // 如果当前没有正在播放的音乐
        // 或者当前播放的音乐不是选择的音乐
        // 那么都是开始新的播放
        if (null == mPlayingMusic ||
                (null != mPlayingMusic && mPlayingMusic.getMusicId() != music.getMusicId())) {
            setPlayingMusic(music);

            preparePlayer(music);
        } else {
            mXMediaPlayer.play();
        }
    }

    public void pauseMusic() {
        if (null != mXMediaPlayer) {
            mXMediaPlayer.pause();
        }
    }

    public void stopMusic() {
        if (null != mXMediaPlayer) {
            mXMediaPlayer.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mErrorHandler = null;
        mApiHelper = null;

        if (null != mXMediaPlayer) {
            mXMediaPlayer.onDestroy();
        }
    }
}