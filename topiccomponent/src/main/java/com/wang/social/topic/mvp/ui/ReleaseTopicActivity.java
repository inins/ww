package com.wang.social.topic.mvp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.acticity.BGMList.BGMListActivity;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.view.MusicBoard;
import com.frame.component.view.SocialToolbar;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.di.component.DaggerReleaseTopicComponent;
import com.wang.social.topic.di.module.ReleaseTopicModule;
import com.wang.social.topic.mvp.contract.ReleaseTopicContract;
import com.wang.social.topic.mvp.model.entities.Template;
import com.wang.social.topic.mvp.presenter.ReleaseTopicPresenter;
import com.wang.social.topic.mvp.ui.widget.DFSetPrice;
import com.wang.social.topic.mvp.ui.widget.ReleaseTopicBottomBar;
import com.wang.social.topic.mvp.ui.widget.StylePicker;
import com.wang.social.topic.mvp.ui.widget.richeditor.RichEditor;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

public class ReleaseTopicActivity extends BaseAppActivity<ReleaseTopicPresenter>
        implements ReleaseTopicContract.View, View.OnLayoutChangeListener {

    public final static int REQUEST_CODE_TEMPLATE = 1001;
    public final static int REQUEST_CODE_BGM = 1002;

    // 根View，主要用于监听软键盘的状态
    @BindView(R2.id.root_view)
    View mRootView;
    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;
    @BindView(R2.id.title_edit_text)
    EditText mTitleET;
    @BindView(R2.id.title_count_text_view)
    TextView mTitleCountTV;
    // 标签
    @BindView(R2.id.topic_tags_text_view)
    TextView mTagsTV;
    // 背景音乐
    @BindView(R2.id.music_board_layout)
    MusicBoard mMusicBoard;

    // 底部栏
    @BindView(R2.id.bottom_bar)
    ReleaseTopicBottomBar mBottomBar;
    // 文字样式控制
    @BindView(R2.id.style_picker)
    StylePicker mStylePicker;
    // 内容编辑器
    @BindView(R2.id.rich_editor)
    RichEditor mRichEditor;

    // 判断键盘是否弹起时候的阈值
    private int mKeyboardHeight;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerReleaseTopicComponent.builder()
                .appComponent(appComponent)
                .releaseTopicModule(new ReleaseTopicModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@NonNull Bundle savedInstanceState) {
        return R.layout.topic_activity_release_topic;
    }

    @Override
    public void initData(@NonNull Bundle savedInstanceState) {
        //获取屏幕高度
        int screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        mKeyboardHeight = screenHeight/3;

        // 顶部状态栏控制
        mToolbar.setOnButtonClickListener(new SocialToolbar.OnButtonClickListener() {
            @Override
            public void onButtonClick(SocialToolbar.ClickType clickType) {
                if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                    finish();
                } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                    addTopic();
                }
            }
        });

        //标题输入监听，主要控制文字数量
        mTitleET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshTitleCount(s.length());
            }
        });
        // 标题字数清0
        refreshTitleCount(0);

        // 底部控制栏，选择模板 音乐等
        mBottomBar.setClickListener(new ReleaseTopicBottomBar.ClickListener() {
            @Override
            public void onTemplateClick() {
                // 模板
                TemplateActivity.start(ReleaseTopicActivity.this,
                        mPresenter.getTemplate(),
                        REQUEST_CODE_TEMPLATE);
            }

            @Override
            public void onMusicClick() {
                // 音乐
                BGMListActivity.start(ReleaseTopicActivity.this,
                        mPresenter.getBGMusic(),
                        REQUEST_CODE_BGM);
            }

            @Override
            public void onVoiceClick() {
                // 语音
            }

            @Override
            public void onFontClick() {
                // 隐藏键盘
                KeyboardUtils.hideSoftInput(ReleaseTopicActivity.this);
                // 弹出文本样式选择器
                mStylePicker.setVisibility(View.VISIBLE);
            }

            @Override
            public void onKeyBoardClick() {
                // 弹出软键盘
                KeyboardUtils.showSoftInput(ReleaseTopicActivity.this);
                // 隐藏文本样式选择器
                mStylePicker.setVisibility(View.GONE);
            }

            @Override
            public void onImageClick() {
                // 图片
            }

            @Override
            public void onChargeClick() {
                // 收费设置
                DFSetPrice.showDialog(getSupportFragmentManager());
            }
        });

        // 颜色 字体 等
        mStylePicker.setTextListener(new StylePicker.TextListener() {
            @Override
            public void onColor(int color) {
                resetRETextColor(color);
            }

            @Override
            public void onFont(int position) {
                Timber.i("重置字体 " + position);
            }

            @Override
            public void onStyle(int position) {
                Timber.i("重置样式 " + position);
                resetSymbolStyle(position);
            }

            @Override
            public void onSize(int size) {
                resetRETextSize(size);
            }
        });

        // 文字样式控制，粗体 斜体 对其方式等
        mStylePicker.setTextStyleListener(new StylePicker.TextStyleListener() {
            @Override
            public void onBold(boolean enable) {
                mRichEditor.setBold();
            }

            @Override
            public void onItalic(boolean enable) {
                mRichEditor.setItalic();
            }

            @Override
            public void onUnderline(boolean enable) {
                mRichEditor.setUnderline();
            }

            @Override
            public void onSetting(boolean enable) {
            }

            @Override
            public void onLeftAlign(boolean enable) {
                if (enable) {
                    mRichEditor.setAlignLeft();
                }
            }

            @Override
            public void onRightAlign(boolean enable) {
                if (enable) {
                    mRichEditor.setAlignRight();
                }
            }

            @Override
            public void onCenterAlign(boolean enable) {
                if (enable) {
                    mRichEditor.setAlignCenter();
                }
            }
        });

        // 音乐播放加载状态监听
        mMusicBoard.setStateListener(new MusicBoard.StateListener() {
            @Override
            public void onStartPrepare() {
                // 开始准备
                showLoadingDialog();
            }

            @Override
            public void onPrepared() {
                // 准备完成
                dismissLoadingDialog();
            }
        });

        // 内容编辑器相关设置
        int padding = 5;
        mRichEditor.setPadding(
                SizeUtils.dp2px(padding),
                SizeUtils.dp2px(padding),
                SizeUtils.dp2px(padding),
                SizeUtils.dp2px(padding));

        mRichEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Timber.i(mRichEditor.getHtml());
            }
        });

        mRichEditor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !mRichEditorInited) {
                    Timber.i("初始化富文本编辑器");

                    resetRETextColor(mStylePicker.getDefaultColor());
                    resetRETextSize(mStylePicker.getDefaultSize());
                    resetSymbolStyle(mStylePicker.getDefaultSymbol());

                    mRichEditorInited = true;
                }
            }
        });

        mRichEditor.setPlaceholder("你想说什么?......为保障用户们的友好体验，请最少输入30个字");
    }

    private boolean mRichEditorInited = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TEMPLATE: // 模板选择
                    Template template = data.getParcelableExtra("template");
                    Timber.i("onActivityResult : " + template.getId() + " " + template.getName());

                    mPresenter.setTemplate(template);

                    // 改变内容编辑框背景
                    mRichEditor.setBackground(mPresenter.getTemplate().getUrl());
                    break;
                case REQUEST_CODE_BGM: // 背景音乐
                    Music music = Music.newInstance(data);
                    Timber.i("onActivityResult " + music.getMusicId() + " " + music.getMusicName());

                    mMusicBoard.resetMusic(music);
                    mPresenter.setBGMusic(music);

                    break;
            }
        }
    }

    private void resetRETextColor(int color) {
        Timber.i("重置文字颜色 " + color);
        mRichEditor.setTextColor(color);
    }

    private void resetRETextSize(int size) {
        Timber.i("重置文字大小 " + size);
        mRichEditor.setEditorFontSize(size);
    }

    private boolean isclick;
    private void resetSymbolStyle(int position) {
        if (position == 1) {
            //mRichEditor.setBullets();
            mRichEditor.setOrderNumbers();
        } else if (position == 2) {
            mRichEditor.setNumbersLower();
        } else if (position == 3) {
            mRichEditor.setNumbersUpper();
        } else if (position == 4) {
            mRichEditor.setNumberRoman();
        } else if (position == 5) {
            mRichEditor.setOrderCjk();
        } else if (position == 6) {
            mRichEditor.setOrderCircle();
        } else if (position == 7) {
            mRichEditor.setOrderSquare();
        } else if (position == 8) {
            mRichEditor.setOrderdisc();
        }
    }

    private void addTopic() {
        mPresenter.resetNetParam()
                .setTitle(mTitleET.getText().toString())
                .setBackgroundImage("")
                .setTagIds(mTagIds)
                .setBackgroundMusicId()
                .setContent("")
                .setTemplateId();

        mPresenter.addTopic();
    }

    private void refreshTitleCount(int length) {
        mTitleCountTV.setText(String.format(getString(R.string.topic_title_length_format), length));
    }


    @OnClick(R2.id.cover_layout)
    public void selectCoverImage() {

    }

    private String mTagIds;
    private String mTagNames;

    // 标签
    @OnClick(R2.id.topic_tags_text_view)
    public void topicTags() {
//        Bundle bundle = new Bundle();
//        if (!TextUtils.isEmpty(mTagIds)) {
//            bundle.putString("ids", mTagIds);
//        }
//        bundle.putString("NAME_MODE", "MODE_SELECTION");
//        bundle.putInt("NAME_TAG_TYPE", 3);
//
//        Intent intent = new Intent(this, TagSelectionActivity.class);
//        intent.putExtras(bundle);
//
//        startActivity(intent);
        TagSelectionActivity.startForTagList(this, mTagIds);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENTBUS_TAG_SELECTED_LIST) {

            String ids = (String) event.get("ids");
            String names = (String) event.get("names");

            Timber.i(ids);
            Timber.i(names);

            mTagIds = ids;
            mTagNames = names;

            mTagsTV.setText(mTagNames);
        }
//        else if (event.getEvent() == EventBean.EVENTBUS_BGM_SELECTED) {
//            if (null != event.get("BGM") && event.get("BGM") instanceof Music) {
//                Music music = (Music) event.get("BGM");
//                Timber.i("onCommonEvent : " + music.getMusicId() + " " + music.getMusicName());
//            }
//        }
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
    protected void onResume() {
        super.onResume();

        mRootView.addOnLayoutChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMusicBoard.onPause();

        mRootView.removeOnLayoutChangeListener(this);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        // 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > mKeyboardHeight)){
            // 弹起
            // 隐藏文本样式选择器
            mStylePicker.setVisibility(View.GONE);
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > mKeyboardHeight)){
            // 关闭
        }
    }

    @Override
    public void toastLong(String msg) {
        ToastUtil.showToastLong(msg);
    }
}
