package com.wang.social.funshow.mvp.ui.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.component.helper.sound.AudioRecordManager;
import com.frame.component.ui.base.BaseController;
import com.frame.entities.EventBean;
import com.frame.utils.KeyboardUtils;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.activity.AiteUserListActivity;
import com.wang.social.funshow.mvp.ui.activity.LockActivity;
import com.wang.social.funshow.mvp.ui.dialog.MusicPopupWindow;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class FunshowAddBottomBarController extends BaseController {

    @BindView(R2.id.btn_aite)
    ImageView btnAite;
    @BindView(R2.id.btn_position)
    ImageView btnPosition;
    @BindView(R2.id.btn_lock)
    ImageView btnLock;
    @BindView(R2.id.btn_music)
    ImageView btnMusic;
    @BindView(R2.id.btn_keyboard)
    ImageView btnKeyboard;
    @BindView(R2.id.btn_voice_record)
    ImageView btnVoiceRecord;
    @BindView(R2.id.lay_voice_record)
    View layVoiceRecord;

    private MusicPopupWindow popupWindow;
    private int lock;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()){
            case EventBean.EVENT_CTRL_FUNSHOW_ADD_LOCK:
                lock = (int) event.get("lock");
                break;
        }
    }

    public FunshowAddBottomBarController(View root) {
        super(root);
        int layout = R.layout.funshow_lay_add_bottombar;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @SuppressLint("all")
    @Override
    protected void onInitCtrl() {
        popupWindow = new MusicPopupWindow(getContext());
        popupWindow.setOnMusicSelectListener(new MusicPopupWindow.OnMusicSelectListener() {
            @Override
            public void onVoiceClick(View v) {
                setVoiceRecordVisible(true);
                popupWindow.dismiss();
            }

            @Override
            public void onMusicClick(View v) {
                popupWindow.dismiss();
            }
        });

        AudioRecordManager.getInstance().setRecordListener(new AudioRecordManager.OnRecordListener() {
            @Override
            public void initView(View root) {
                Timber.e("initView");
            }

            @Override
            public void onTimeOut(int counter) {
                Timber.e("onTimeOut:" + counter);
            }

            @Override
            public void onRecording() {
                Timber.e("onRecording");
            }

            @Override
            public void onCancel() {
                Timber.e("onCancel");
            }

            @Override
            public void onCompleted(int duration, String path) {
                Timber.e("onCompleted:" + path);
            }

            @Override
            public void onDBChanged(int db) {
                Timber.e("onDBChanged:" + db);
            }

            @Override
            public void tooShort() {
                Timber.e("tooShort");
            }

            @Override
            public void onDestroy() {
                Timber.e("onDestroy");
            }
        });
        btnVoiceRecord.setOnTouchListener((v, event) -> {
            new RxPermissions((Activity) getContext()).requestEach(Manifest.permission.RECORD_AUDIO)
                    .subscribe((permission) -> {
                        if (permission.granted) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    AudioRecordManager.getInstance().startRecord(v);
                                    break;
                                case MotionEvent.ACTION_MOVE:
//                                    AudioRecordManager.getInstance().continueRecord();
//                                    AudioRecordManager.getInstance().willCancelRecord();
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    AudioRecordManager.getInstance().stopRecord();
                                    break;
                            }
                        }
                    });
            return true;
        });
    }

    @Override
    protected void onInitData() {
    }

    @OnClick({R2.id.btn_aite, R2.id.btn_position, R2.id.btn_lock, R2.id.btn_music, R2.id.btn_keyboard})
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_right) {
        } else if (i == R.id.btn_aite) {
            AiteUserListActivity.start(getContext());
        } else if (i == R.id.btn_position) {

        } else if (i == R.id.btn_lock) {
            LockActivity.start(getContext(),lock);
        } else if (i == R.id.btn_music) {
            popupWindow.showPopupWindow(v);
        } else if (i == R.id.btn_keyboard) {
            setVoiceRecordVisible(false);
            KeyboardUtils.showSoftInput((Activity) getContext());
        }
    }

    ////////////////////////////////////////////////////////

    public boolean isVoiceRecordVisible() {
        return layVoiceRecord.getVisibility() == View.VISIBLE;
    }

    public void setVoiceRecordVisible(boolean visible) {
        if (layVoiceRecord.getVisibility() != View.VISIBLE && visible) {
            layVoiceRecord.setVisibility(View.VISIBLE);
        } else if (layVoiceRecord.getVisibility() != View.GONE && !visible) {
            layVoiceRecord.setVisibility(View.GONE);
        }
    }
}
