package com.frame.component.ui.acticity.BGMList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.GradualImageView;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.liaoinstan.springview.aliheader.AliFooter;
import com.liaoinstan.springview.aliheader.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class BGMListActivity extends BaseAppActivity<BGMListPresenter> implements BGMListContract.View {

    public static void start(Activity activity, Music music, int requestCode) {
        Intent intent;
        if (null != music) {
            intent = Music.newIntent(music);
        } else {
            intent = new Intent();
        }

        intent.setClass(activity, BGMListActivity.class);
        intent.putExtra("code", requestCode);

        if (requestCode == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void start(Activity activity, Music music) {
        start(activity, music, -1);
    }

    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.spring_view)
    SpringView mSpringView;
    // 音乐列表
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    BGMListAdapter mAdapter;
    // 顶部播放状态图标
    @BindView(R2.id.play_state_image_view)
    GradualImageView mPlayStateIV;
    // 右上角选定文字
    @BindView(R2.id.selected_text_view)
    TextView mSelectedTV;

    int mRequestCode;


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
        return R.layout.activity_bgm_list;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        mRequestCode = getIntent().getIntExtra("code", -1);

        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                }
            }
        });

        // 顶部播放状态显示
        mPlayStateIV.setDrawable(R.drawable.common_ic_playing2, R.drawable.common_ic_playing2);

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
                    // 选择变化，判断是否和原来传入的音乐相同，如果不同则右上角可点击
                    if (null != mPresenter.getOriginalMusic() &&
                            mPresenter.getOriginalMusic().getMusicId() != music.getMusicId()) {
                        resetSelectedTextView(true);
                    } else {
                        resetSelectedTextView(false);
                    }

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


        // 右上角不可点击
        resetSelectedTextView(false);

        // 已选中的音乐
        Music music = Music.newInstance(getIntent());
        mPresenter.setSelectMusic(music);
        mPresenter.setOriginalMusic(music);

        // 初始化播放器
        mPresenter.initMediaPlayer();


        mSpringView.setHeader(new AliHeader(mSpringView.getContext(), false));
        mSpringView.setFooter(new AliFooter(mSpringView.getContext(), false));
        mSpringView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                // 开始加载音乐列表
                mPresenter.resetMusicList();
                mPresenter.loadBGMList(true);
            }

            @Override
            public void onLoadmore() {
                mPresenter.loadBGMList(false);
            }
        });
        mSpringView.callFreshDelay();
    }

    @Override
    public void resetStateAnim(boolean playing) {
        if (playing) {
            mPlayStateIV.startAnimation();
        } else {
            mPlayStateIV.stopAnimation();
        }
    }

    public void resetSelectedTextView(boolean clickable) {
        mSelectedTV.setEnabled(clickable);

        if (clickable) {
            mSelectedTV.setTextColor(getResources().getColor(R.color.common_blue_deep));
        } else {
            mSelectedTV.setTextColor(getResources().getColor(R.color.common_text_dark));
        }
    }

    /**
     * 选定
     */
    @OnClick(R2.id.selected_text_view)
    public void selectMusic() {
        if (mRequestCode != -1) {
            Intent intent = Music.newIntent(mPresenter.getSelectMusic());
            setResult(Activity.RESULT_OK, intent);
        }

        // 通知
        EventBean bean = new EventBean(EventBean.EVENTBUS_BGM_SELECTED);
        bean.put("BGM", mPresenter.getSelectMusic());
        EventBus.getDefault().post(bean);

        finish();
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        dismissLoadingDialog();
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
    public void onLoadBGMListCompleted() {
        mSpringView.onFinishFreshAndLoadDelay();
    }

    @Override
    public String getDefaultMusicName() {
        return getString(R.string.no_bgm);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mPresenter.pauseMusic();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
