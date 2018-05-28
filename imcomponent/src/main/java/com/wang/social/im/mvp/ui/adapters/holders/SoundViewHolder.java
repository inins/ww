package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.component.common.AppConstant;
import com.frame.component.helper.sound.AudioPlayManager;
import com.frame.component.helper.sound.IAudioPlayListener;
import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMSoundElem;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.helper.ImHelper;
import com.wang.social.im.mvp.model.entities.UIMessage;

import java.io.File;

import butterknife.BindView;

/**
 * ============================================
 * 语音消息
 * <p>
 * Create by ChenJing on 2018-04-18 15:16
 * ============================================
 */
public class SoundViewHolder extends BaseMessageViewHolder<UIMessage> {

    @BindView(R2.id.msg_iv_sound)
    ImageView msgIvSound;
    @BindView(R2.id.msg_tv_sound_duration)
    TextView msgTvSoundDuration;
    @BindView(R2.id.msg_ll_sound)
    LinearLayout msgLlSound;
    @Nullable
    @BindView(R2.id.msg_iv_error)
    ImageView msgIvError;
    @BindView(R2.id.msg_pb_progress)
    @Nullable
    ContentLoadingProgressBar msgPbProgress;

    public SoundViewHolder(Context context, ViewGroup root, int layoutRes) {
        super(context, root, layoutRes);
    }

    @Override
    protected void bindData(UIMessage itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        initStyle(itemValue);

        super.bindData(itemValue, position, onItemClickListener);

        TIMMessage timMessage = itemValue.getTimMessage();
        TIMSoundElem soundElem = null;
        for (int i = 0, max = (int) timMessage.getElementCount(); i < max; i++) {
            TIMElem timElem = timMessage.getElement(i);
            if (timElem instanceof TIMSoundElem) {
                soundElem = (TIMSoundElem) timElem;
                String playUUid = AudioPlayManager.getInstance().getPlayUUid();
                if (playUUid != null && playUUid.equals(soundElem.getUuid())) {
                    showSound(itemValue, soundElem, true);
                    AudioPlayManager.getInstance().setPlayListener(new VoicePlayListener(itemValue, soundElem));
                } else {
                    showSound(itemValue, soundElem, false);
                }
                break;
            }
        }

        if (timMessage.isSelf()) {
            showStatus(itemValue, msgIvError, msgPbProgress);
            setErrorListener(msgIvError, itemValue, position);
        } else {
            setPortraitListener(msgIvPortrait, itemValue, position);
        }
        setContentListener(msgLlSound, itemValue, position, false, true);

        TIMSoundElem finalSoundElem = soundElem;
        if (finalSoundElem != null) {
            msgLlSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String playUuid = AudioPlayManager.getInstance().getPlayUUid();
                    if (playUuid != null && playUuid.equals(finalSoundElem.getUuid())) {
                        AudioPlayManager.getInstance().stopPlay();
                    } else {
                        final File tempAudio = ImHelper.getTempFile(AppConstant.Constant.SOUND_CACHE_DIR);
                        if (tempAudio == null) {
                            return;
                        }
                        finalSoundElem.getSoundToFile(tempAudio.getAbsolutePath(), new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {

                            }

                            @Override
                            public void onSuccess() {
                                AudioPlayManager.getInstance().startPlay(getContext(), Uri.parse(tempAudio.getPath()), finalSoundElem.getUuid(), new VoicePlayListener(itemValue, finalSoundElem));
                            }
                        });
                    }
                }
            });
        }
    }

    private void showSound(UIMessage uiMessage, TIMSoundElem soundElem, boolean isPlaying) {
        msgTvSoundDuration.setText(UIUtil.getString(R.string.im_seconds, soundElem.getDuration()));
        AnimationDrawable animationDrawable;
        if (uiMessage.getTimMessage().isSelf()) {
            if (conversationType == ConversationType.MIRROR) {
                animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.im_anim_voice_mirror_sent);
            } else {
                animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.im_anim_voice_sent);
            }
            if (isPlaying) {
                msgIvSound.setImageDrawable(animationDrawable);
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            } else {
                msgIvSound.setImageResource(conversationType == ConversationType.MIRROR ? R.drawable.im_voice_mirror_sent_play3 : R.drawable.im_voice_sent_play3);
                if (animationDrawable != null) {
                    animationDrawable.stop();
                }
            }
        } else {
            if (conversationType == ConversationType.MIRROR) {
                animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.im_anim_voice_mirror_receive);
            } else {
                animationDrawable = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.im_anim_voice_receive);
            }
            if (isPlaying) {
                msgIvSound.setImageDrawable(animationDrawable);
                if (animationDrawable != null) {
                    animationDrawable.start();
                }
            } else {
                msgIvSound.setImageResource(conversationType == ConversationType.MIRROR ? R.drawable.im_voice_mirror_receive_play3 : R.drawable.im_voice_receive_play3);
                if (animationDrawable != null) {
                    animationDrawable.stop();
                }
            }
        }
    }

    @Override
    protected void initStyle(UIMessage uiMessage) {
        if (conversationType == ConversationType.MIRROR) {
            if (uiMessage.getTimMessage().isSelf()) {
                msgLlSound.setBackgroundResource(R.drawable.im_bg_message_right_mirror);
                msgTvSoundDuration.setTextColor(Color.WHITE);
            } else {
                msgLlSound.setBackgroundResource(R.drawable.im_bg_message_left_mirror);
                msgTvSoundDuration.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_mirror_left_text));
            }
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_bg_message_mirror_left));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_mirror_left_text));
        } else {
            if (uiMessage.getTimMessage().isSelf()) {
                msgLlSound.setBackgroundResource(R.drawable.im_bg_message_right);
                msgTvSoundDuration.setTextColor(Color.WHITE);
            } else {
                if (conversationType == ConversationType.TEAM) {
                    msgLlSound.setBackgroundResource(R.drawable.im_bg_message_left_team);
                } else {
                    msgLlSound.setBackgroundResource(R.drawable.im_bg_message_left);
                }
                msgTvSoundDuration.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_txt_receive));
            }
            if (msgTvName != null) {
                msgTvName.setTextColor(ContextCompat.getColor(getContext(), R.color.im_message_txt_receive));
            }
            msgTvTime.setTextColor(ContextCompat.getColor(getContext(), R.color.common_text_dark_light));
        }
    }

    private class VoicePlayListener implements IAudioPlayListener {

        private UIMessage uiMessage;
        private TIMSoundElem soundElem;

        public VoicePlayListener(UIMessage uiMessage, TIMSoundElem soundElem) {
            this.uiMessage = uiMessage;
            this.soundElem = soundElem;
        }

        @Override
        public void onStart(Uri var1, String var2) {
            if (var2.equals(soundElem.getUuid())) {
                showSound(uiMessage, soundElem, true);
            }
        }

        @Override
        public void onStop(Uri var1, String var2) {
            if (var2.equals(soundElem.getUuid())) {
                showSound(uiMessage, soundElem, false);
            }
        }

        @Override
        public void onComplete(Uri var1, String var2) {
            if (var2.equals(soundElem.getUuid())) {
                showSound(uiMessage, soundElem, false);
            }
        }
    }
}
