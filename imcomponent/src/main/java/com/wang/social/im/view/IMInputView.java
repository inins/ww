package com.wang.social.im.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.frame.utils.KeyboardUtils;
import com.wang.social.im.R;
import com.wang.social.im.view.emotion.EmoticonsEditText;

/**
 * Created by Bo on 2018-03-28.
 */

public class IMInputView extends LinearLayout {

    private ImageView mVoiceToggle,mEmojiToggle,mPluginToggle;
    private EmoticonsEditText mEditText;
    private Button mVoiceInput;

    public IMInputView(Context context) {
        super(context);
        init(context);
    }

    public IMInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IMInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.im_view_input, this);

        mVoiceToggle = findViewById(R.id.vi_iv_toggle_voice);
        mEmojiToggle = findViewById(R.id.vi_iv_toggle_emotion);
        mPluginToggle = findViewById(R.id.vi_iv_toggle_plugin);
        mEditText = findViewById(R.id.vi_et_input);
        mVoiceInput = findViewById(R.id.vi_btn_voice_input);

        initListener();
    }

    private void initListener() {
        mVoiceToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVoiceInput.getVisibility() != VISIBLE){
                    mVoiceInput.setVisibility(VISIBLE);
                    mEditText.setVisibility(GONE);
                    hideInputKeyBoard();
                }else {
                    mEditText.setVisibility(VISIBLE);
                    mVoiceInput.setVisibility(GONE);
                    showInputKeyBoard();
                }
                hideEmotionBoard();
                hidePluginBoard();
            }
        });
    }

    private void showInputKeyBoard(){
        KeyboardUtils.showSoftInput(mEditText);
        mEditText.requestFocus();
    }

    private void hideInputKeyBoard(){
        KeyboardUtils.hideSoftInput(mEditText);
        mEditText.clearFocus();
    }

    private void hideEmotionBoard(){

    }

    private void hidePluginBoard(){

    }
}
