package com.wang.social.im.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.frame.utils.KeyboardUtils;
import com.tencent.imsdk.TIMConversationType;
import com.wang.social.im.R;
import com.wang.social.im.view.emotion.EmoticonsEditText;
import com.wang.social.im.view.plugin.PluginAdapter;
import com.wang.social.im.view.plugin.PluginModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bo on 2018-03-28.
 */

public class IMInputView extends LinearLayout implements PluginAdapter.OnPluginClickListener {

    private ImageView mVoiceToggle, mEmotionToggle, mPluginToggle;
    private EmoticonsEditText mEditText;
    private Button mVoiceInput;

    private PluginAdapter mPluginAdapter;
    private TIMConversationType mConversationType;

    private boolean isKeyBoardActive;

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

    private void init(Context context) {
        inflate(context, R.layout.im_view_input, this);

        mVoiceToggle = findViewById(R.id.vi_iv_toggle_voice);
        mEmotionToggle = findViewById(R.id.vi_iv_toggle_emotion);
        mPluginToggle = findViewById(R.id.vi_iv_toggle_plugin);
        mEditText = findViewById(R.id.vi_et_input);
        mVoiceInput = findViewById(R.id.vi_btn_voice_input);

        mPluginAdapter = new PluginAdapter();

        initEmotions();
        initListener();
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
                } else {
                    mEditText.setVisibility(VISIBLE);
                    mEmotionToggle.setVisibility(VISIBLE);
                    mVoiceInput.setVisibility(GONE);
                    showInputKeyBoard();
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
                    } else {
                        if (isKeyBoardActive) {
                            getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mPluginAdapter.setVisibility(VISIBLE);
                                }
                            }, 200L);
                        } else {
                            mPluginAdapter.setVisibility(VISIBLE);
                        }
                        hideInputKeyBoard();
                        hideEmotionBoard();
                    }
                } else {
                    mPluginAdapter.bindView(IMInputView.this);
                    mPluginAdapter.setVisibility(VISIBLE);
                    hideInputKeyBoard();
                    hideEmotionBoard();
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
                }
                return false;
            }
        });
        mEmotionToggle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    /**
     * 初始化表情列表
     */
    private void initEmotions() {

    }

    /**
     * 初始化插件列表
     */
    private void initPlugins() {
        mPluginAdapter.setPluginClickListener(this);

        if (mConversationType == TIMConversationType.Group) {
            mPluginAdapter.setPlugins(provideGroupPlugins());
        } else {
            mPluginAdapter.setPlugins(provideC2CPlugins());
        }
    }

    /**
     * 设置会话类型{@link TIMConversationType}，根据会话类型判断加载插件数量
     *
     * @param conversationType
     */
    public void setConversationType(TIMConversationType conversationType) {
        mConversationType = conversationType;
        initPlugins();
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

    }

    private void hidePluginBoard() {
        mPluginAdapter.setVisibility(GONE);
    }

    private List<PluginModule> provideC2CPlugins() {
        List<PluginModule> plugins = new ArrayList<>();
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_image, PluginModule.PluginType.IMAGE));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_shoot, PluginModule.PluginType.SHOOT));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_location, PluginModule.PluginType.LOCATION));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_redpacket, PluginModule.PluginType.REDPACKET));
        return plugins;
    }

    private List<PluginModule> provideGroupPlugins() {
        List<PluginModule> plugins = new ArrayList<>();
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_image, PluginModule.PluginType.IMAGE));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_shoot, PluginModule.PluginType.SHOOT));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_location, PluginModule.PluginType.LOCATION));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_redpacket, PluginModule.PluginType.REDPACKET));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_game, PluginModule.PluginType.GAME));
        plugins.add(new PluginModule(R.mipmap.ic_launcher, R.string.im_chat_input_plugin_shadow, PluginModule.PluginType.SHADOW));
        return plugins;
    }

    @Override
    public void onPluginClick(PluginModule pluginModule) {
        Toast.makeText(getContext(), getContext().getText(pluginModule.getPluginName()), Toast.LENGTH_SHORT).show();
    }
}
