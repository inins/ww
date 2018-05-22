package com.wang.social.im.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.frame.utils.KeyboardUtils;
import com.tencent.imsdk.TIMConversationType;
import com.wang.social.im.R;
import com.frame.component.enums.ConversationType;
import com.wang.social.im.view.emotion.EmotionAdapter;
import com.wang.social.im.view.emotion.SimpleCommonUtils;
import com.wang.social.im.view.emotion.widget.EmoticonsEditText;
import com.wang.social.im.view.plugin.PluginAdapter;
import com.wang.social.im.view.plugin.PluginModule;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

/**
 * Created by Bo on 2018-03-28.
 */

public class IMInputView extends LinearLayout implements PluginAdapter.OnPluginClickListener {

    private ImageView mVoiceToggle, mEmotionToggle, mPluginToggle, mSendToggle;
    private EmoticonsEditText mEditText;
    private Button mVoiceInput;

    private PluginAdapter mPluginAdapter;
    private EmotionAdapter mEmotionAdapter;
    private ConversationType mConversationType;

    private boolean isKeyBoardActive;

    @Setter
    private IInputViewListener mInputViewListener;

    public IMInputView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        init(context);
    }

    public IMInputView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        init(context);
    }

    public IMInputView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        init(context);
    }

    public EditText getEditText() {
        return mEditText;
    }

    private void init(Context context) {
        inflate(context, R.layout.im_view_input, this);

        mVoiceToggle = findViewById(R.id.vi_iv_toggle_voice);
        mEmotionToggle = findViewById(R.id.vi_iv_toggle_emotion);
        mPluginToggle = findViewById(R.id.vi_iv_toggle_plugin);
        mEditText = findViewById(R.id.vi_et_input);
        mVoiceInput = findViewById(R.id.vi_btn_voice_input);
        mSendToggle = findViewById(R.id.vi_iv_send);

        SimpleCommonUtils.initEmoticonsEditText(mEditText);

        mPluginAdapter = new PluginAdapter();

        initEmotions();
        initListener();
    }

    private void initStyle() {
        if (mConversationType != null) {
            switch (mConversationType) {
                case MIRROR:
                    mVoiceToggle.setImageResource(R.drawable.im_voice_mirror_icon);
                    mEmotionToggle.setImageResource(R.drawable.im_emoji_mirror_icon);
                    mPluginToggle.setImageResource(R.drawable.im_plugin_mirror_icon);
                    mSendToggle.setImageResource(R.drawable.im_send_mirror_icon);
                    break;
                default:
                    mVoiceToggle.setImageResource(R.drawable.im_voice_icon);
                    mEmotionToggle.setImageResource(R.drawable.im_emoji_icon);
                    mPluginToggle.setImageResource(R.drawable.im_plugin_icon);
                    mSendToggle.setImageResource(R.drawable.im_send_icon);
                    break;
            }
        }
    }

    private void initListener() {
        mVoiceToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVoiceInput.getVisibility() != VISIBLE) {
                    mVoiceInput.setVisibility(VISIBLE);
                    mEditText.setVisibility(GONE);
                    mEmotionToggle.setVisibility(GONE);
                    hideInputKeyBoard();

                    if (mInputViewListener != null) {
                        mInputViewListener.onInputViewCollapsed();
                    }
                } else {
                    mEditText.setVisibility(VISIBLE);
                    mEmotionToggle.setVisibility(VISIBLE);
                    mVoiceInput.setVisibility(GONE);
                    showInputKeyBoard();

                    if (mInputViewListener != null) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mInputViewListener.onInputViewExpanded();
                            }
                        }, 350L);
                    }
                }
                hideEmotionBoard();
                hidePluginBoard();
            }
        });
        mPluginToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPluginAdapter.isInitialed()) {
                    if (mPluginAdapter.getVisibility() == VISIBLE) {
                        mPluginAdapter.setVisibility(GONE);
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
                                    mPluginAdapter.setVisibility(VISIBLE);
                                    if (mInputViewListener != null) {
                                        mInputViewListener.onInputViewExpanded();
                                    }
                                }
                            }, 200L);
                        } else {
                            mPluginAdapter.setVisibility(VISIBLE);
                            if (mInputViewListener != null) {
                                mInputViewListener.onInputViewExpanded();
                            }
                        }
                        hideInputKeyBoard();
                        hideEmotionBoard();
                    }
                } else {
                    mPluginAdapter.bindView(IMInputView.this);
                    mPluginAdapter.setVisibility(VISIBLE);
                    hideInputKeyBoard();
                    hideEmotionBoard();
                    if (mInputViewListener != null) {
                        mInputViewListener.onInputViewExpanded();
                    }
                }
                mEditText.setVisibility(VISIBLE);
                mEmotionToggle.setVisibility(VISIBLE);
                mVoiceInput.setVisibility(GONE);
            }
        });
        mEditText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    hideEmotionBoard();
                    hidePluginBoard();
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
        mEmotionToggle.setOnClickListener(new OnClickListener() {
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
                        hidePluginBoard();
                    }
                } else {
                    mEmotionAdapter.bindView(IMInputView.this);
                    mPluginAdapter.setVisibility(VISIBLE);
                    hideInputKeyBoard();
                    hidePluginBoard();

                    if (mInputViewListener != null) {
                        mInputViewListener.onInputViewExpanded();
                    }
                }
                mEditText.setVisibility(VISIBLE);
                mVoiceInput.setVisibility(GONE);
            }
        });
        mVoiceInput.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mInputViewListener != null) {
                    mInputViewListener.onVoiceInputTouch(view, motionEvent);
                }
                return false;
            }
        });

        mSendToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mEditText.getText().toString();
                if (mInputViewListener != null) {
                    mInputViewListener.onSendClick(message);
                }
                mEditText.setText("");
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(mEditText.getText().toString())) {
                    mSendToggle.setVisibility(GONE);
                    mPluginToggle.setVisibility(VISIBLE);
                } else {
                    mSendToggle.setVisibility(VISIBLE);
                    mPluginToggle.setVisibility(GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * 初始化表情列表
     */
    private void initEmotions() {
        mEmotionAdapter = new EmotionAdapter(mEditText);
        mEmotionAdapter.setOnEmotionClickListener(new EmotionAdapter.OnEmotionClickListener() {
            @Override
            public void onEmotionClick(String identity, String name) {
                if (mInputViewListener != null) {
                    mInputViewListener.onEmotionClick(identity, name);
                }
            }
        });
    }

    /**
     * 初始化插件列表
     */
    private void initPlugins() {
        mPluginAdapter.setPluginClickListener(this);

        switch (mConversationType) {
            case PRIVATE:
                mPluginAdapter.setPlugins(provideC2CPlugins());
                break;
            case SOCIAL:
                mPluginAdapter.setPlugins(provideGroupPlugins());
                break;
            case TEAM:
                mPluginAdapter.setPlugins(provideTeamPlugins());
                break;
            case MIRROR:
                mPluginAdapter.setPlugins(provideMirrorPlugins());
                break;
        }
    }

    /**
     * 设置会话类型{@link TIMConversationType}，根据会话类型判断加载插件数量
     *
     * @param conversationType
     */
    public void setConversationType(ConversationType conversationType) {
        mConversationType = conversationType;

        initStyle();
        initPlugins();
    }

    public void collapse() {
        hideInputKeyBoard();
        hideEmotionBoard();
        hidePluginBoard();
    }

    public void updateShadowText(int textRes) {
        if (mPluginAdapter != null && mPluginAdapter.getPlugins() != null) {
            for (PluginModule pluginModule : mPluginAdapter.getPlugins()) {
                if (pluginModule.getPluginType() == PluginModule.PluginType.SHADOW) {
                    pluginModule.setPluginName(textRes);
                    break;
                }
            }
            mPluginAdapter.refresh();
        }
    }

    private void showInputKeyBoard() {
        KeyboardUtils.showSoftInput(mEditText);
        mEditText.requestFocus();
        isKeyBoardActive = true;
    }

    private void hideInputKeyBoard() {
        KeyboardUtils.hideSoftInput(mEditText);
        mEditText.clearFocus();
        isKeyBoardActive = false;
    }

    private void hideEmotionBoard() {
        mEmotionAdapter.setVisibility(GONE);
    }

    private void hidePluginBoard() {
        mPluginAdapter.setVisibility(GONE);
    }

    private List<PluginModule> provideC2CPlugins() {
        List<PluginModule> plugins = new ArrayList<>();
        plugins.add(new PluginModule(R.drawable.im_plugin_image, R.string.im_chat_input_plugin_image, PluginModule.PluginType.IMAGE));
        plugins.add(new PluginModule(R.drawable.im_plugin_shoot, R.string.im_chat_input_plugin_shoot, PluginModule.PluginType.SHOOT));
        plugins.add(new PluginModule(R.drawable.im_plugin_location, R.string.im_chat_input_plugin_location, PluginModule.PluginType.LOCATION));
        plugins.add(new PluginModule(R.drawable.im_plugin_red_envelope, R.string.im_chat_input_plugin_redpacket, PluginModule.PluginType.REDPACKET));
        return plugins;
    }

    private List<PluginModule> provideGroupPlugins() {
        List<PluginModule> plugins = new ArrayList<>();
        plugins.add(new PluginModule(R.drawable.im_plugin_image, R.string.im_chat_input_plugin_image, PluginModule.PluginType.IMAGE));
        plugins.add(new PluginModule(R.drawable.im_plugin_shoot, R.string.im_chat_input_plugin_shoot, PluginModule.PluginType.SHOOT));
        plugins.add(new PluginModule(R.drawable.im_plugin_location, R.string.im_chat_input_plugin_location, PluginModule.PluginType.LOCATION));
        plugins.add(new PluginModule(R.drawable.im_plugin_red_envelope, R.string.im_chat_input_plugin_redpacket, PluginModule.PluginType.REDPACKET));
        plugins.add(new PluginModule(R.drawable.im_plugin_image, R.string.im_chat_input_plugin_game, PluginModule.PluginType.GAME));
        plugins.add(new PluginModule(R.drawable.im_plugin_shadow, R.string.im_chat_input_plugin_shadow, PluginModule.PluginType.SHADOW));
        return plugins;
    }

    private List<PluginModule> provideTeamPlugins() {
        List<PluginModule> plugins = new ArrayList<>();
        plugins.add(new PluginModule(R.drawable.im_plugin_image, R.string.im_chat_input_plugin_image, PluginModule.PluginType.IMAGE));
        plugins.add(new PluginModule(R.drawable.im_plugin_shoot, R.string.im_chat_input_plugin_shoot, PluginModule.PluginType.SHOOT));
        plugins.add(new PluginModule(R.drawable.im_plugin_location, R.string.im_chat_input_plugin_location, PluginModule.PluginType.LOCATION));
        plugins.add(new PluginModule(R.drawable.im_plugin_red_envelope, R.string.im_chat_input_plugin_redpacket, PluginModule.PluginType.REDPACKET));
        return plugins;
    }

    private List<PluginModule> provideMirrorPlugins() {
        List<PluginModule> plugins = new ArrayList<>();
        plugins.add(new PluginModule(R.drawable.im_plugin_image_mirror, R.string.im_chat_input_plugin_image, PluginModule.PluginType.IMAGE));
        plugins.add(new PluginModule(R.drawable.im_plugin_shoot_mirror, R.string.im_chat_input_plugin_shoot, PluginModule.PluginType.SHOOT));
        plugins.add(new PluginModule(R.drawable.im_plugin_location_mirror, R.string.im_chat_input_plugin_location, PluginModule.PluginType.LOCATION));
        return plugins;
    }

    @Override
    public void onPluginClick(PluginModule pluginModule) {
        if (mInputViewListener != null) {
            mInputViewListener.onPluginClick(pluginModule);
        }
    }

    public interface IInputViewListener {

        /**
         * 扩展栏点击
         *
         * @param pluginModule
         */
        void onPluginClick(PluginModule pluginModule);

        /**
         * 语言输入
         *
         * @param view
         * @param event
         */
        void onVoiceInputTouch(View view, MotionEvent event);

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