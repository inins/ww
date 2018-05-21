package com.frame.component.helper.sound;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.frame.utils.DataHelper;
import com.frame.utils.FrameUtils;
import com.frame.utils.Utils;

import java.io.File;

import timber.log.Timber;

public class AudioRecordManager implements Callback {
    private static final String TAG = "AudioRecordManager";
    private int RECORD_INTERVAL;
    private IAudioState mCurAudioState;
    private View mRootView;
    private Context mContext;
    private Handler mHandler;
    private AudioManager mAudioManager;
    private MediaRecorder mMediaRecorder;
    private Uri mAudioPath;
    private long smStartRecTime;
    private OnAudioFocusChangeListener mAfChangeListener;
    IAudioState idleState;
    IAudioState recordState;
    IAudioState sendingState;
    IAudioState cancelState;
    IAudioState timerState;

    private OnRecordListener recordListener;

    public static AudioRecordManager getInstance() {
        return AudioRecordManager.SingletonHolder.sInstance;
    }

    private AudioRecordManager(Object o) {
        this.RECORD_INTERVAL = 60;
        this.idleState = new AudioRecordManager.IdleState();
        this.recordState = new AudioRecordManager.RecordState();
        this.sendingState = new AudioRecordManager.SendingState();
        this.cancelState = new AudioRecordManager.CancelState();
        this.timerState = new AudioRecordManager.TimerState();

        if (VERSION.SDK_INT < 21) {
            try {
                TelephonyManager e = (TelephonyManager) Utils.getContext().getSystemService(Activity.TELEPHONY_SERVICE);
                e.listen(new PhoneStateListener() {
                    public void onCallStateChanged(int state, String incomingNumber) {
                        switch (state) {
                            case 1:
                                AudioRecordManager.this.sendEmptyMessage(6);
                            case 0:
                            case 2:
                            default:
                                super.onCallStateChanged(state, incomingNumber);
                        }
                    }
                }, 32);
            } catch (SecurityException var2) {
                var2.printStackTrace();
            }
        }

        this.mCurAudioState = this.idleState;
        this.idleState.enter();
    }

    public final boolean handleMessage(Message msg) {
        AudioStateMessage m;
        switch (msg.what) {
            case 2:
                this.sendEmptyMessage(2);
                break;
            case 7:
                m = AudioStateMessage.obtain();
                m.what = msg.what;
                m.obj = msg.obj;
                this.sendMessage(m);
                break;
            case 8:
                m = AudioStateMessage.obtain();
                m.what = 7;
                m.obj = msg.obj;
                this.sendMessage(m);
        }

        return false;
    }

    public void setRecordListener(OnRecordListener recordListener) {
        this.recordListener = recordListener;
    }

    private void initView(View root) {
        this.mHandler = new Handler(root.getHandler().getLooper(), this);
        if (recordListener != null) {
            recordListener.initView(root);
        }
    }

    private void setTimeoutView(int counter) {
        if (recordListener != null) {
            recordListener.onTimeOut(counter);
        }
    }

    private void setRecordingView() {
        Timber.tag(TAG).d("setRecordingView");
        if (recordListener != null) {
            recordListener.onRecording();
        }
    }

    private void setCancelView() {
        Timber.tag(TAG).d("setCancelView");
        if (recordListener != null) {
            recordListener.onCancel();
        }
    }

    private void destroyView() {
        Timber.tag(TAG).d("destroyView");
        if (this.recordListener != null) {
            this.mHandler.removeMessages(7);
            this.mHandler.removeMessages(8);
            this.mHandler.removeMessages(2);
            recordListener.onDestroy();
            this.mHandler = null;
            this.mContext = null;
            this.mRootView = null;
        }

    }

    public void setMaxVoiceDuration(int maxVoiceDuration) {
        this.RECORD_INTERVAL = maxVoiceDuration;
    }

    public int getMaxVoiceDuration() {
        return this.RECORD_INTERVAL;
    }

    public void startRecord(View rootView) {
        this.mRootView = rootView;
        this.mContext = rootView.getContext().getApplicationContext();
        this.mAudioManager = (AudioManager) this.mContext.getSystemService(Activity.AUDIO_SERVICE);
        if (this.mAfChangeListener != null) {
            this.mAudioManager.abandonAudioFocus(this.mAfChangeListener);
            this.mAfChangeListener = null;
        }

        this.mAfChangeListener = new OnAudioFocusChangeListener() {
            public void onAudioFocusChange(int focusChange) {
                if (focusChange == -1) {
                    AudioRecordManager.this.mAudioManager.abandonAudioFocus(AudioRecordManager.this.mAfChangeListener);
                    AudioRecordManager.this.mAfChangeListener = null;
                    AudioRecordManager.this.sendEmptyMessage(6);
                }

            }
        };
        this.sendEmptyMessage(1);
    }

    public void willCancelRecord() {
        this.sendEmptyMessage(3);
    }

    public void continueRecord() {
        this.sendEmptyMessage(4);
    }

    public void stopRecord() {
        this.sendEmptyMessage(5);
    }

    public void destroyRecord() {
        AudioStateMessage msg = new AudioStateMessage();
        msg.obj = Boolean.valueOf(true);
        msg.what = 5;
        this.sendMessage(msg);
    }

    void sendMessage(AudioStateMessage message) {
        this.mCurAudioState.handleMessage(message);
    }

    void sendEmptyMessage(int event) {
        AudioStateMessage message = AudioStateMessage.obtain();
        message.what = event;
        this.mCurAudioState.handleMessage(message);
    }

    private void startRec() {
        Timber.tag(TAG).d("startRec");

        try {
            this.muteAudioFocus(this.mAudioManager, true);
            this.mAudioManager.setMode(0);
            this.mMediaRecorder = new MediaRecorder();

            try {
                Resources e = this.mContext.getResources();
                int bps = e.getInteger(e.getIdentifier("rc_audio_encoding_bit_rate", "integer", this.mContext.getPackageName()));
                this.mMediaRecorder.setAudioSamplingRate(8000);
                this.mMediaRecorder.setAudioEncodingBitRate(bps);
            } catch (NotFoundException var3) {
                var3.printStackTrace();
            }

            this.mMediaRecorder.setAudioChannels(1);
            this.mMediaRecorder.setAudioSource(1);
            this.mMediaRecorder.setOutputFormat(3);
            this.mMediaRecorder.setAudioEncoder(1);
            this.mAudioPath = Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + "temp.voice"));
            this.mMediaRecorder.setOutputFile(this.mAudioPath.getPath());
            this.mMediaRecorder.prepare();
            this.mMediaRecorder.start();
            Message e1 = Message.obtain();
            e1.what = 7;
            e1.obj = Integer.valueOf(10);
            this.mHandler.sendMessageDelayed(e1, (long) (this.RECORD_INTERVAL * 1000 - 10000));
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    private File getCacheDir() {
        File file = FrameUtils.obtainAppComponentFromContext(mContext).cacheFile();
        return DataHelper.makeDirs(new File(file, "Sound"));
    }

    private boolean checkAudioTimeLength() {
        long delta = SystemClock.elapsedRealtime() - this.smStartRecTime;
        return delta < 1000L;
    }

    private void stopRec() {
        Timber.tag(TAG).d("stopRec");

        try {
            this.muteAudioFocus(this.mAudioManager, false);
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.stop();
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

    }

    private void deleteAudioFile() {
        Timber.tag(TAG).d("deleteAudioFile");
        if (this.mAudioPath != null) {
            File file = new File(this.mAudioPath.getPath());
            if (file.exists()) {
                file.delete();
            }
        }

    }

    private void sendAudioFile() {
        int duration = (int) (SystemClock.elapsedRealtime() - this.smStartRecTime) / 1000;
        if (recordListener != null){
            recordListener.onCompleted(duration, mAudioPath.getPath());
        }
    }

    private void audioDBChanged() {
        if (this.mMediaRecorder != null) {
            int db = this.mMediaRecorder.getMaxAmplitude() / 600;
            if (recordListener != null){
                recordListener.onDBChanged(db);
            }
        }

    }

    private void muteAudioFocus(AudioManager audioManager, boolean bMute) {
        if (VERSION.SDK_INT < 8) {
            Timber.tag(TAG).d("muteAudioFocus Android 2.1 and below can not stop music");
        } else {
            if (bMute) {
                audioManager.requestAudioFocus(this.mAfChangeListener, 3, 2);
            } else {
                audioManager.abandonAudioFocus(this.mAfChangeListener);
                this.mAfChangeListener = null;
            }

        }
    }

    class TimerState extends IAudioState {
        TimerState() {
        }

        void handleMessage(AudioStateMessage msg) {
            Timber.tag(TAG).d(this.getClass().getSimpleName() + " handleMessage : " + msg.what);
            switch (msg.what) {
                case 3:
                    AudioRecordManager.this.setCancelView();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.cancelState;
                case 4:
                default:
                    break;
                case 5:
                    AudioRecordManager.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            AudioRecordManager.this.stopRec();
                            AudioRecordManager.this.sendAudioFile();
                            AudioRecordManager.this.destroyView();
                        }
                    }, 500L);
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                    AudioRecordManager.this.idleState.enter();
                    break;
                case 6:
                    AudioRecordManager.this.stopRec();
                    AudioRecordManager.this.destroyView();
                    AudioRecordManager.this.deleteAudioFile();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                    AudioRecordManager.this.idleState.enter();
                    break;
                case 7:
                    int counter = ((Integer) msg.obj).intValue();
                    if (counter > 0) {
                        Message message = Message.obtain();
                        message.what = 8;
                        message.obj = Integer.valueOf(counter - 1);
                        AudioRecordManager.this.mHandler.sendMessageDelayed(message, 1000L);
                        AudioRecordManager.this.setTimeoutView(counter);
                    } else {
                        AudioRecordManager.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                AudioRecordManager.this.stopRec();
                                AudioRecordManager.this.sendAudioFile();
                                AudioRecordManager.this.destroyView();
                            }
                        }, 500L);
                        AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                    }
            }

        }
    }

    class CancelState extends IAudioState {
        CancelState() {
        }

        void handleMessage(AudioStateMessage msg) {
            Timber.tag(TAG).d(this.getClass().getSimpleName() + " handleMessage : " + msg.what);
            switch (msg.what) {
                case 1:
                case 2:
                case 3:
                default:
                    break;
                case 4:
                    AudioRecordManager.this.setRecordingView();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.recordState;
                    AudioRecordManager.this.sendEmptyMessage(2);
                    break;
                case 5:
                case 6:
                    AudioRecordManager.this.stopRec();
                    AudioRecordManager.this.destroyView();
                    AudioRecordManager.this.deleteAudioFile();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                    AudioRecordManager.this.idleState.enter();
                    break;
                case 7:
                    int counter = ((Integer) msg.obj).intValue();
                    if (counter > 0) {
                        Message message = Message.obtain();
                        message.what = 8;
                        message.obj = Integer.valueOf(counter - 1);
                        AudioRecordManager.this.mHandler.sendMessageDelayed(message, 1000L);
                    } else {
                        AudioRecordManager.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                AudioRecordManager.this.stopRec();
                                AudioRecordManager.this.sendAudioFile();
                                AudioRecordManager.this.destroyView();
                            }
                        }, 500L);
                        AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                        AudioRecordManager.this.idleState.enter();
                    }
            }

        }
    }

    class SendingState extends IAudioState {
        SendingState() {
        }

        void handleMessage(AudioStateMessage message) {
            Timber.tag("AudioRecordManager").d("SendingState handleMessage " + message.what);
            switch (message.what) {
                case 9:
                    AudioRecordManager.this.stopRec();
                    if (((Boolean) message.obj).booleanValue()) {
                        AudioRecordManager.this.sendAudioFile();
                    }

                    AudioRecordManager.this.destroyView();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                default:
            }
        }
    }

    class RecordState extends IAudioState {
        RecordState() {
        }

        void handleMessage(AudioStateMessage msg) {
            Timber.tag(TAG).d(this.getClass().getSimpleName() + " handleMessage : " + msg.what);
            switch (msg.what) {
                case 2:
                    AudioRecordManager.this.audioDBChanged();
                    AudioRecordManager.this.mHandler.sendEmptyMessageDelayed(2, 150L);
                    break;
                case 3:
                    AudioRecordManager.this.setCancelView();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.cancelState;
                case 4:
                default:
                    break;
                case 5:
                    final boolean checked = AudioRecordManager.this.checkAudioTimeLength();
                    boolean activityFinished = false;
                    if (msg.obj != null) {
                        activityFinished = ((Boolean) msg.obj).booleanValue();
                    }

                    if (checked && !activityFinished) {
                        if (recordListener != null){
                            recordListener.tooShort();
                        }
                        AudioRecordManager.this.mHandler.removeMessages(2);
                    }

                    if (!activityFinished && AudioRecordManager.this.mHandler != null) {
                        AudioRecordManager.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                AudioStateMessage message = AudioStateMessage.obtain();
                                message.what = 9;
                                message.obj = Boolean.valueOf(!checked);
                                AudioRecordManager.this.sendMessage(message);
                            }
                        }, 500L);
                        AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.sendingState;
                    } else {
                        AudioRecordManager.this.stopRec();
                        if (!checked && activityFinished) {
                            AudioRecordManager.this.sendAudioFile();
                        }

                        AudioRecordManager.this.destroyView();
                        AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                    }
                    break;
                case 6:
                    AudioRecordManager.this.stopRec();
                    AudioRecordManager.this.destroyView();
                    AudioRecordManager.this.deleteAudioFile();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                    AudioRecordManager.this.idleState.enter();
                    break;
                case 7:
                    int counter = ((Integer) msg.obj).intValue();
                    AudioRecordManager.this.setTimeoutView(counter);
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.timerState;
                    if (counter > 0) {
                        Message message = Message.obtain();
                        message.what = 8;
                        message.obj = Integer.valueOf(counter - 1);
                        AudioRecordManager.this.mHandler.sendMessageDelayed(message, 1000L);
                    } else {
                        AudioRecordManager.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                AudioRecordManager.this.stopRec();
                                AudioRecordManager.this.sendAudioFile();
                                AudioRecordManager.this.destroyView();
                            }
                        }, 500L);
                        AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.idleState;
                    }
            }

        }
    }

    class IdleState extends IAudioState {
        public IdleState() {
            Timber.tag(TAG).d("IdleState");
        }

        void enter() {
            super.enter();
            if (AudioRecordManager.this.mHandler != null) {
                AudioRecordManager.this.mHandler.removeMessages(7);
                AudioRecordManager.this.mHandler.removeMessages(8);
                AudioRecordManager.this.mHandler.removeMessages(2);
            }

        }

        void handleMessage(AudioStateMessage msg) {
            Timber.d("IdleState handleMessage : " + msg.what);
            switch (msg.what) {
                case 1:
                    AudioRecordManager.this.initView(AudioRecordManager.this.mRootView);
                    AudioRecordManager.this.setRecordingView();
                    AudioRecordManager.this.startRec();
                    AudioRecordManager.this.smStartRecTime = SystemClock.elapsedRealtime();
                    AudioRecordManager.this.mCurAudioState = AudioRecordManager.this.recordState;
                    AudioRecordManager.this.sendEmptyMessage(2);
                default:
            }
        }
    }

    static class SingletonHolder {
        static AudioRecordManager sInstance = new AudioRecordManager(null);

        SingletonHolder() {
        }
    }

    public interface OnRecordListener {

        /**
         * 初始化
         */
        void initView(View root);

        /**
         * 录音时间即将结束
         *
         * @param counter
         */
        void onTimeOut(int counter);

        /**
         * 录音中
         */
        void onRecording();

        /**
         * 取消录音
         */
        void onCancel();

        /**
         * 录音完成
         */
        void onCompleted(int duration, String path);

        /**
         * 录音音量变化
         *
         * @param db
         */
        void onDBChanged(int db);

        /**
         * 录音时间太短
         */
        void tooShort();

        /**
         * 销毁
         */
        void onDestroy();
    }

    public abstract static class OnSimpleRecordListener implements OnRecordListener{

        @Override
        public void initView(View root) {
        }

        @Override
        public void onTimeOut(int counter) {
        }

        @Override
        public void onRecording() {
        }

        @Override
        public void onCancel() {
        }

        @Override
        public void onDBChanged(int db) {
        }

        @Override
        public void tooShort() {
        }

        @Override
        public void onDestroy() {
        }
    }
}
