package com.wang.social.topic.mvp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerBGMListComponent;
import com.wang.social.topic.di.module.BGMListModule;
import com.wang.social.topic.mvp.contract.BGMListContract;
import com.wang.social.topic.mvp.model.entities.Music;
import com.wang.social.topic.mvp.presenter.BGMListPresenter;
import com.wang.social.topic.mvp.ui.adapter.BGMListAdapter;
import com.wang.social.topic.mvp.ui.widget.GradualImageView;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class BGMListActivity extends BaseAppActivity<BGMListPresenter> implements BGMListContract.View {

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    // 音乐列表
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    BGMListAdapter mAdapter;
    // 顶部播放状态图标
    @BindView(R2.id.play_state_image_view)
    GradualImageView mPlayStateIV;


    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerBGMListComponent.builder()
                .appComponent(appComponent)
                .bGMListModule(new BGMListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_bgm_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            }
        });

        // 顶部播放状态显示
        mPlayStateIV.setDrawable(R.drawable.topic_icon_playing2, R.drawable.topic_icon_playing2);

        // 音乐列表
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BGMListAdapter(mRecyclerView, mPresenter.getMusicList());
        mRecyclerView.setAdapter(mAdapter);

        // 点击监听
        mAdapter.setClickListener(new BGMListAdapter.ClickListener() {
            /**
             * 播放按钮点击
             * @param music 当前音乐
             */
            @Override
            public void onControl(Music music) {
                Timber.i("onControl : " + music.getMusicId() + " " + music.getMusicName());
                mPresenter.controlPlayer(music);
            }

            /**
             * 选中音乐
             * @param music 当前音乐
             */
            @Override
            public void onSelect(Music music) {
                if (mPresenter.selectMusic(music)) {
                    onNotifyDataSetChanged();
                }
            }
        });

        mAdapter.setStateProvider(new BGMListAdapter.StateProvider() {
            @Override
            public boolean isPlaying(Music music) {
                return mPresenter.isPlaying(music);
            }

            @Override
            public boolean isSelected(Music music) {
                return mPresenter.isSelected(music);
            }
        });

        // 已选中的音乐
        mPresenter.setSelectMusic(Music.newInstance(getIntent()));

        // 初始化播放器
        mPresenter.initMediaPlayer();

        // 开始加载音乐列表
        mPresenter.resetMusicList();
        mPresenter.loadBGMList();
    }

    @Override
    public void resetStateAnim(boolean playing) {
        if (playing) {
            mPlayStateIV.startAnimation();
        } else {
            mPlayStateIV.stopAnimation();
        }
    }

    /**
     * 选定
     */
    @OnClick(R2.id.selected_text_view)
    public void selectMusic() {
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void onNotifyDataSetChanged() {
        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public String getDefaultMusicName() {
        return getString(R.string.topic_no_bgm);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.pauseMusic();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mPresenter.stopMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
