package com.wang.social.im.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frame.utils.KeyboardUtils;
import com.wang.social.im.R;
import com.wang.social.im.view.emotion.EmotionAdapter;
import com.wang.social.im.view.emotion.SimpleCommonUtils;
import com.wang.social.im.view.emotion.widget.EmoticonsEditText;

import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-11 11:07
 * ============================================
 */
public class GameInputView extends LinearLayout {

    private TextView tvSend;
    private ImageView ivEmojiToggle;
    private EmoticonsEditText etInput;

    @Setter
    private IGInputViewListener mInputViewListener;

    private EmotionAdapter mEmotionAdapter;
    private boolean isKeyBoardActive;

    public GameInputView(Context context) {
        super(context);
        init(context);
    }

    public GameInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        inflate(context, R.layout.im_view_game_conversation_input, this);

        tvSend = findViewById(R.id.gvi_iv_send);
        ivEmojiToggle = findViewById(R.id.gvi_iv_toggle_emotion);
        etInput = findViewById(R.id.gvi_et_input);

        SimpleCommonUtils.initEmoticonsEditText(etInput);
        initListener();

        initEmotions();
    }

    /**
     * 初始化表情列表
     */
    private void initEmotions() {
        mEmotionAdapter = new EmotionAdapter(etInput);
    }

    private void initListener() {
        etInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    hideEmotionBoard();
                    showInputKeyBoard();
                    isKeyBoardActive = true;

                    if (mInputViewListener != null) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mInputViewListener.onInputViewExpanded();
                            }
                        }, 350L);
                    }
                }
                return false;
            }
        });

        ivEmojiToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmotionAdapter.isInitialed()) {
                    if (mEmotionAdapter.getVisibility() == VISIBLE) {
                        mEmotionAdapter.setVisibility(GONE);
                        showInputKeyBoard();

                        if (mInputViewListener != null) {
                            postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mInputViewListener.onInputViewExpanded();
                                }
                            }, 350L);
                        }
                    } else {
                        if (isKeyBoardActive) {
                            getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mEmotionAdapter.setVisibility(VISIBLE);

                                    if (mInputViewListener != null) {
                                        mInputViewListener.onInputViewExpanded();
                                    }
                                }
                            }, 200L);
                        } else {
                            mEmotionAdapter.setVisibility(VISIBLE);

                            if (mInputViewListener != null) {
                                mInputViewListener.onInputViewExpanded();
                            }
                        }
                        hideInputKeyBoard();
                    }
                } else {
                    mEmotionAdapter.bindView(GameInputView.this);
                    hideInputKeyBoard();

                    if (mInputViewListener != null) {
                        mInputViewListener.onInputViewExpanded();
                    }
                }
            }
        });

        tvSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etInput.getText().toString();
                if (mInputViewListener != null) {
                    mInputViewListener.onSendClick(message);
                }
                etInput.setText("");
            }
        });

        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(etInput.getText().toString())) {
                    tvSend.setVisibility(GONE);
                } else {
                    tvSend.setVisibility(VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideEmotionBoard() {
        mEmotionAdapter.setVisibility(GONE);
    }

    private void showInputKeyBoard() {
        KeyboardUtils.showSoftInput(etInput);
        etInput.requestFocus();
        isKeyBoardActive = true;
    }

    private void hideInputKeyBoard() {
        KeyboardUtils.hideSoftInput(etInput);
        etInput.clearFocus();
        isKeyBoardActive = false;
    }

    public interface IGInputViewListener {

        /**
         * 表情选择
         *
         * @param codeName
         * @param showName
         */
        void onEmotionClick(String codeName, String showName);

        /**
         * 点击发送按钮
         *
         * @param content
         */
        void onSendClick(String content);

        /**
         * 输入键盘展开
         */
        void onInputViewExpanded();

        /**
         * 输入键盘收起
         */
        void onInputViewCollapsed();
    }
}
