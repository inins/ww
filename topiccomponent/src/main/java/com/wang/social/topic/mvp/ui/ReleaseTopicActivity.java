package com.wang.social.topic.mvp.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.frame.component.helper.sound.AudioRecordManager;
import com.frame.component.ui.acticity.BGMList.BGMListActivity;
import com.frame.component.ui.acticity.BGMList.Music;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.base.BaseAppActivity;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.view.MusicBoard;
import com.frame.component.view.SocialToolbar;
import com.frame.component.view.SpreadView;
import com.frame.component.view.waveview.WaveView;
import com.frame.di.component.AppComponent;
import com.frame.entities.EventBean;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.mvp.IView;
import com.frame.router.facade.annotation.RouteNode;
import com.frame.utils.FrameUtils;
import com.frame.utils.KeyboardUtils;
import com.frame.utils.RegexUtils;
import com.frame.utils.RxLifecycleUtils;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.frame.component.ui.acticity.tags.TagSelectionActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wang.social.pictureselector.helper.PhotoHelper;
import com.wang.social.pictureselector.helper.PhotoHelperEx;
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
import com.wang.social.topic.utils.AudioImageUtil;
import com.wang.social.topic.utils.FileUtil;
import com.wang.social.topic.utils.HtmlUtil;
import com.wang.social.topic.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

@RouteNode(path = "/topic_release", desc = "话题发布")
public class ReleaseTopicActivity extends BaseAppActivity<ReleaseTopicPresenter>
        implements ReleaseTopicContract.View, View.OnLayoutChangeListener, PhotoHelper.OnPhotoCallback {

    public static void start(Context context) {
        context.startActivity(new Intent(context, ReleaseTopicActivity.class));
    }

    public final static int REQUEST_CODE_TEMPLATE = 1001;
    public final static int REQUEST_CODE_BGM = 1002;
    public final static int REQUEST_CODE_COVER_IMAGE = 1003;

    private final static int IMAGE_TYPE_COVER = 1;  // 封面图片
    private final static int IMAGE_TYPE_CONTENT = 2;// 内容插入图片

    @IntDef({
            IMAGE_TYPE_COVER,
            IMAGE_TYPE_CONTENT
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface ImageType {
    }

    // 根View，主要用于监听软键盘的状态
    @BindView(R2.id.root_view)
    View mRootView;
    // 话题编辑滚动View
    @BindView(R2.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;
    @BindView(R2.id.toolbar)
    SocialToolbar mToolbar;

    // 标题输入框
    @BindView(R2.id.title_edit_text_layout)
    View mTitleETLayout;
    @BindView(R2.id.title_edit_text)
    EditText mTitleET;
    @BindView(R2.id.title_count_text_view)
    TextView mTitleCountTV;
    // 封面图片
    @BindView(R2.id.cover_image_view)
    ImageView mCoverIV;
    // 标签
    @BindView(R2.id.topic_tags_text_view)
    TextView mTagsTV;
    // 背景音乐
    @BindView(R2.id.bg_music_layout)
    View mBgMusicLayout;
    @BindView(R2.id.music_board_layout)
    MusicBoard mMusicBoard;

    // 底部栏
    @BindView(R2.id.bottom_layout)
    View mBottomLayout;
    @BindView(R2.id.bottom_bar)
    ReleaseTopicBottomBar mBottomBar;
    // 文字样式控制
    @BindView(R2.id.style_picker)
    StylePicker mStylePicker;


    @BindView(R2.id.rich_editor_layout)
    FrameLayout mRichEditorLayout;
    @BindView(R2.id.rich_editor_bg_image_view)
    ImageView mRichEditorBGIV;
    // 内容编辑器
    @BindView(R2.id.rich_editor)
    RichEditor mRichEditor;
    // 录音
    @BindView(R2.id.voice_layout)
    View mVoicLayout;
    @BindView(R2.id.btn_voice_record)
    ImageView mVoiceRecordIV;
    @BindView(R2.id.wave_view)
    WaveView mWaveView;
    // 内容编辑框标题区域
    @BindView(R2.id.content_editor_title_layout)
    View mContentEditorTitleLayout;

    @BindView(R2.id.spreadView)
    SpreadView mSpreadView;


    // 判断键盘是否弹起时候的阈值
    private int mKeyboardHeight;
    // 键盘是否弹出
    private boolean mSoftInputPopuped;
    // 标题编辑器是否获取焦点
    private boolean mTitltEditorFocused;
    // 内容编辑器是否获取焦点
    private boolean mRichEditorFocused;
    // 点击字体按钮时如果键盘已弹起，那么要等键盘落下后再显示样式选择器
    // 记录状态
    private boolean mNeedShowStylePicker;
    // 点击语音输入如果键盘已弹起，需要键盘落下后再显示
    private boolean mNeedShowVoiceLayout;
    // 封面图片路径
    private String mCoverImage;
    // 图片选择封装
    private PhotoHelperEx mPhotoHelperEx;
    // 记录标签选择返回
    private String mTagIds;
    private String mTagNames;
    // 图片选择的类型
    private @ImageType
    int mImageType;
    // 价格
    private int mPrice = 0;
    // 内容编辑器点击Y位置
    private int mRichEditorTouchY;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

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
        mKeyboardHeight = screenHeight / 3;

        mPhotoHelperEx = PhotoHelperEx.newInstance(this, this);

        // 顶部状态栏控制
        mToolbar.setOnButtonClickListener(clickType -> {
            if (clickType == SocialToolbar.ClickType.LEFT_ICON) {
                quit();
            } else if (clickType == SocialToolbar.ClickType.RIGHT_TEXT) {
                addTopic();
            }
        });

        mTitleETLayout.setFocusable(true);
        mTitleETLayout.setFocusableInTouchMode(true);

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
                if (mVoicLayout.getVisibility() == View.VISIBLE) {
                    showVoiceLayout(false);
                } else {
                    if (mSoftInputPopuped) {
                        // 隐藏键盘
                        KeyboardUtils.hideSoftInput(ReleaseTopicActivity.this);
                        mNeedShowVoiceLayout = true;
                    } else {
                        // 显示语音输入
                        showVoiceLayout(true);

                    }
                }
                // 隐藏样式选择器
                showStylePicker(false);
            }

            @Override
            public void onFontClick() {
                // 如果软键盘已弹出
                if (mSoftInputPopuped) {
                    // 隐藏键盘
                    KeyboardUtils.hideSoftInput(ReleaseTopicActivity.this);
                    mNeedShowStylePicker = true;
                } else {
//                    mStylePicker.setVisibility(View.VISIBLE);
                    showStylePicker(true);
                }

                // 隐藏语音输入
                showVoiceLayout(false);
            }

            @Override
            public void onKeyBoardClick() {
                // 隐藏文本样式选择器
//                mStylePicker.setVisibility(View.GONE);
                showStylePicker(false);
                // 隐藏语音输入
                showVoiceLayout(false);
                // 弹出软键盘
                KeyboardUtils.showSoftInput(ReleaseTopicActivity.this);
            }

            @Override
            public void onImageClick() {
                // 图片
                showPhotoDialog(IMAGE_TYPE_CONTENT);
            }

            @Override
            public void onChargeClick() {
                // 收费设置
                DFSetPrice.showDialog(getSupportFragmentManager(), mPrice, new DFSetPrice.PriceCallback() {
                    @Override
                    public void onPrice(int price) {
                        mPrice = price;
                    }
                });
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

                resetREFont(position);
            }

            @Override
            public void onStyle(int position) {
                resetListStyleType(position);
//                setListStyleType(position);
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
//                if (enable) {
                mRichEditor.setAlignLeft();
//                }
            }

            @Override
            public void onRightAlign(boolean enable) {
//                if (enable) {
                mRichEditor.setAlignRight();
//                }
            }

            @Override
            public void onCenterAlign(boolean enable) {
//                if (enable) {
                mRichEditor.setAlignCenter();
//                }
            }
        });

        // 音乐播放加载状态监听
        mMusicBoard.setStateListener(new MusicBoard.StateListener() {
            @Override
            public void onStartPrepare() {
                // 开始准备
                showLoading();
            }

            @Override
            public void onPrepared() {
                // 准备完成
                hideLoading();
            }
        });

        // 录音
        mVoiceRecordIV.setOnTouchListener((v, event) -> {
            new RxPermissions(this)
                    .requestEach(Manifest.permission.RECORD_AUDIO)
                    .subscribe((permission) -> {
                        if (permission.granted) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    AudioRecordManager.getInstance().startRecord(v);
                                    mSpreadView.start();
                                    break;
                                case MotionEvent.ACTION_MOVE:
//                                    AudioRecordManager.getInstance().continueRecord();
//                                    AudioRecordManager.getInstance().willCancelRecord();
                                    break;
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    v.performClick();
                                    AudioRecordManager.getInstance().stopRecord();
                                    showVoiceLayout(false);
                                    mSpreadView.end();
                                    break;
                            }
                        }
                    });
            return true;
        });
        // 录音返回
        AudioRecordManager.getInstance().setRecordListener(new AudioRecordManager.OnRecordListener() {
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
            public void onCompleted(int duration, String path) {
                mWaveView.setExheight(0);

                Observable.create((ObservableEmitter<String> emitter) -> {
                    emitter.onNext(AudioImageUtil.createAudioImage(
                            ReleaseTopicActivity.this,
                            mRichEditor.getWidth(),
                            duration));
                    emitter.onComplete();
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                mCompositeDisposable.add(d);
                                showLoading();
                            }

                            @Override
                            public void onNext(String s) {
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                /**
                                 * 最关键在此，把options.inJustDecodeBounds = true;
                                 * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
                                 */
                                options.inJustDecodeBounds = true;
                                Bitmap bitmap = BitmapFactory.decodeFile(s, options); // 此时返回的bitmap为null
                                Timber.i("音频图片大小 : " + options.outWidth + " " + options.outHeight);
                                mRichEditor.insertAudioImage(path, s, options.outWidth, options.outHeight, "录音");
//                                mRichEditor.insertAudioImage(path, s, 100, 40, "录音");
                            }

                            @Override
                            public void onError(Throwable e) {
                                hideLoading();
                            }

                            @Override
                            public void onComplete() {
                                hideLoading();
                            }
                        });

//                String image = AudioImageUtil.createAudioImage(
//                        ReleaseTopicActivity.this,
//                        mRichEditor.getWidth(),
//                        duration);
//                mRichEditor.insertAudioImage(path, image, "录音");
            }

            @Override
            public void onDBChanged(int db) {
                mWaveView.setExheight(db * 3);
            }

            @Override
            public void tooShort() {

            }

            @Override
            public void onDestroy() {

            }
        });

        // 内容编辑器相关设置
        //        mRichEditor.setEditorBackgroundColor(Color.TRANSPARENT);
        int padding = SizeUtils.dp2px(5);
        int paddingSide = SizeUtils.dp2px(5);
        mRichEditor.setPadding(
                paddingSide,
                padding,
                paddingSide,
                padding);
        mRichEditor.setBackgroundColor(Color.TRANSPARENT);

        mRichEditor.setOnDecorationChangeListener((String text, List<RichEditor.Type> types) -> {
            Timber.i("OnDecorationChangeListener : " + text);
        });

        mRichEditor.setOnTextChangeListener((String text) -> {});

        mRichEditor.setOnTouchListener((View v, MotionEvent event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    Timber.i("RichEditor touch : x=%1$f y=%2$f", event.getX(), event.getY());
                    mRichEditorTouchY = (int) event.getY();
                    break;
            }
            v.performClick();
            return false;
        });


        mTitleET.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            Timber.i("标题编辑器焦点 " + Boolean.toString(hasFocus));
            mTitltEditorFocused = hasFocus;
            if (hasFocus) {
                editTextHasFocused();
            }
        });

        mTitleET.setOnClickListener((View v) -> {
            Timber.i("标题编辑器点击");
            if (mTitltEditorFocused) {
                editTextHasFocused();
            }
        });

        mRichEditor.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            Timber.i("内容编辑器焦点 " + Boolean.toString(hasFocus));
            mRichEditorFocused = hasFocus;
            if (hasFocus) {
                editTextHasFocused();
//                KeyboardUtils.showSoftInput(this);
            }
        });

        mRichEditor.setPlaceholder("你想说什么?......为保障用户们的友好体验，请最少输入30个字");
    }

    /**
     * 重设编辑器字体
     * @param position 字体索引
     */
    private void resetREFont(int position) {
//        if (position == 0) {
//            mRichEditor.setFont("kt,STKaiti-SC-Regular,STKaiti-SC-Regular");
//        } else if (position == 1) {
//            mRichEditor.setFont("pfb,PingFangSC-Regular,sans-serif");
//        } else if (position == 2) {
//            mRichEditor.setFont("pfc,PingFangSC-Semibold,sans-serif");
//        } else if (position == 3) {
//            mRichEditor.setFont("pfx,PingFangSC-Light, sans-serif");
//        } else if (position == 4) {
//            mRichEditor.setFont("st,STSongti-SC-Regular");
//        } else if (position == 5) {
//            mRichEditor.setFont("ww,DFWaWaSC-W5");
//        } else if (position == 6) {
//            mRichEditor.setFont("yy,STYuanti-SC-Regular");
//        }
        // 2018-07-02 去掉平方字体
        if (position == 0) {
            mRichEditor.setFont("kt,STKaiti-SC-Regular,STKaiti-SC-Regular");
        } else if (position == 1) {
            mRichEditor.setFont("st,STSongti-SC-Regular");
        } else if (position == 2) {
            mRichEditor.setFont("ww,DFWaWaSC-W5");
        } else if (position == 3) {
            mRichEditor.setFont("yy,STYuanti-SC-Regular");
        }
    }

    private void resetRichEditorStyle() {
        resetRETextColor(mStylePicker.getDefaultColor());
        resetRETextSize(mStylePicker.getDefaultSize());
//        resetListStyleType(mStylePicker.getDefaultSymbol());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mPhotoHelperEx.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TEMPLATE: // 模板选择
                    Template template = data.getParcelableExtra("template");
                    Timber.i("onActivityResult : " + template.getId() + " " + template.getName());
                    Timber.i(template.getUrl());

                    mPresenter.setTemplate(template);

                    // 改变内容编辑框背景
                    if (template.getId() == -1) {
                        mRichEditorBGIV.setBackgroundColor(0xFFF2F2F2);
                    } else {
                        SimpleTarget<Drawable> simpleTarget = new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                BitmapDrawable bd = (BitmapDrawable) resource;
                                bd.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                                mRichEditorBGIV.setBackground(bd);
                            }
                        };

                        Glide.with(this).load(mPresenter.getTemplate().getUrl()).into(simpleTarget);

//                        mRichEditor.setBackground(mPresenter.getTemplate().getUrl());
                    }
                    break;
                case REQUEST_CODE_BGM: // 背景音乐
                    Music music = Music.newInstance(data);
                    Timber.i("onActivityResult " + music.getMusicId() + " " + music.getMusicName());

                    if (music.getMusicId() == -1) {
                        mBgMusicLayout.setVisibility(View.GONE);
                    } else {
                        mBgMusicLayout.setVisibility(View.VISIBLE);
                    }

                    mMusicBoard.resetMusic(music);
                    mPresenter.setBGMusic(music);

                    break;
                case REQUEST_CODE_COVER_IMAGE:
                    // 封面图片
//                    String[] list = data.getStringArrayExtra(PictureSelector.NAME_FILE_PATH_LIST);
//
//                    ActivityPicturePreview.start(this, 2, list);
//                    mCoverImage = list[0];
//
//                    if (TextUtils.isEmpty(mCoverImage)) {
//                        FrameUtils.obtainAppComponentFromContext(this)
//                                .imageLoader()
//                                .loadImage(this, ImageConfigImpl.builder()
//                                .imageView(mCoverIV)
//                                .url(mCoverImage)
//                                .build());
//                    }

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
//        mRichEditor.setFontSize(size);
    }

    private void editTextHasFocused() {
        Timber.i("编辑器获取焦点");
        // 当文本编辑器获取焦点后软键盘会弹出，这个时候要隐藏样式选择器
//        mStylePicker.setVisibility(View.GONE);
        showStylePicker(false);
        // 隐藏语音输入框
        showVoiceLayout(false);
    }

    private void showVoiceLayout(boolean visible) {
        mVoicLayout.setVisibility(visible ? View.VISIBLE : View.GONE);

        if (visible) {
            scrollRichEditorWrapper();
        }
    }

    private int mSymbolPosition;
    private boolean isclick;

    /**
     * 设置列表项标志的类型
     * @param position 索引
     */
    private void resetListStyleType(int position) {
        Timber.i("重设列表项标志的类型 : " + position);

        if (!TextUtils.isEmpty(mRichEditor.getHtml()) &&
                !mRichEditor.getHtml().endsWith("</ol>") &&
                mRichEditor.getHtml().endsWith("</div>") &&
                !mRichEditor.getHtml().endsWith("</ol></div>")) {
            isclick = false;
        }
        if (isclick) {
            mRichEditor.setOrderNumbers();
        }
        if (position != 0) {
            isclick = true;
        }

        if (TextUtils.isEmpty(mRichEditor.getHtml()) || (position == 0 && mRichEditor.getHtml().endsWith("</ol>"))) {
            mRichEditor.setNumbersNone();
            isclick = false;
        } else if (position == 1) {
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

    /**
     * 设置列表项标志的类型
     * @param position 索引
     */
    private void setListStyleType(int position) {
        Timber.i("列表项标志的类型 : " + position);
        if (position == 0) {
            mRichEditor.setNumbersNone();
        } else if (position == 1) {
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

    private boolean checkInput(boolean toast) {
        String title = mTitleET.getText().toString();
        if (TextUtils.isEmpty(title)) {
            if (toast) {
                ToastUtil.showToastShort("请输入话题名称");
            }
            return false;
        }

        if (title.length() < 5) {
            if (toast) {
                ToastUtil.showToastShort("话题名称不能少于5字");
            }
            return false;
        }

        if (title.length() > 30) {
            if (toast) {
                ToastUtil.showToastShort("话题名称不能多于30字");
            }
            return false;
        }

        if (!RegexUtils.isTopicName(title)) {
            if (toast) {
                ToastUtil.showToastShort("话题名称不能输入特殊字符和表情");
            }

            return false;
        }

        if (TextUtils.isEmpty(mTagIds)) {
            if (toast) {
                ToastUtil.showToastShort("请选择标签");
            }
            return false;
        }

//        if (null == mPresenter.getBGMusic()) {
//            if (toast) {
//                ToastUtil.showToastShort("请选择背景音乐");
//            }
//
//            return false;
//        }

        if (TextUtils.isEmpty(mRichEditor.getHtml())) {
            if (toast) {
                ToastUtil.showToastShort("请输入内容");
            }
            return false;
        }

        String content = HtmlUtil.delHTMLTag(mRichEditor.getHtml());

        Timber.i("\n");
        Timber.i(content == null ? "null" : content);
        Timber.i("\n");

        int audioCount = StringUtil.countAudio(mRichEditor.getHtml());
        Timber.i("包含 " + audioCount + " 个语音");
        int contentLength = audioCount * 50 + content.length();

        if (contentLength < 30) {
            if (toast) {
                ToastUtil.showToastShort("为保障用户们的友好体验，请最少输入30个字");
            }
            return false;
        }

        if (contentLength > 800) {
            if (toast) {
                ToastUtil.showToastShort("内容字符数（含每条语音50字）超过800字！");
            }

            return false;
        }

        return true;
    }

    private void addTopic() {
        // 打印内容
        Timber.i(TextUtils.isEmpty(mRichEditor.getHtml()) ? "内容为空" : mRichEditor.getHtml());

        if (!checkInput(true)) {
            return;
        }

//        mPresenter.resetNetParam()
//                .setTitle(mTitleET.getText().toString())
//                .setBackgroundImage("")
//                .setTagIds(mTagIds)
//                .setBackgroundMusicId()
//                .setContent(mRichEditor.getHtml())
//                .setTemplateId();
//
//        mPresenter.addTopic();

        String content = HtmlUtil.delHTMLTag(mRichEditor.getHtml());

        if (mPresenter.getTemplate() != null && mPresenter.getTemplate().getId() != -1) {
            mRichEditor.setBackground(mPresenter.getTemplate().getUrl());
        }

        mPresenter.addTopic(
                mTitleET.getText().toString(),
                mRichEditor.getHtml(),
                content.substring(0, Math.min(30, content.length())),
                mTagIds,
                mPrice > 0 ? 1 : 0,
                mPrice,
                "",
                "",
                "",
                "",
                mPresenter.getTemplate() == null ? 0 : mPresenter.getTemplate().getId(),
                mCoverImage,
                mPresenter.getBGMusic() == null ? 0 : mPresenter.getBGMusic().getMusicId(),
                "");
    }

    private void refreshTitleCount(int length) {
        mTitleCountTV.setText(String.format(getString(R.string.topic_title_length_format), length));
    }

    @OnClick(R2.id.music_board_layout)
    public void selectBackgroundMusic() {
        BGMListActivity.start(ReleaseTopicActivity.this,
                mPresenter.getBGMusic(),
                REQUEST_CODE_BGM);
    }


    @OnClick(R2.id.cover_layout)
    public void selectCoverImage() {
//        PictureSelector.from(this)
//                .spanCount(3)
//                .isClip(false)
//                .maxSelection(3)
//                .forResult(REQUEST_CODE_COVER_IMAGE);

        showPhotoDialog(IMAGE_TYPE_COVER);
    }

    private void showPhotoDialog(@ImageType int type) {
        mImageType = type;

        if (type == IMAGE_TYPE_COVER) {
            mPhotoHelperEx.setMaxSelectCount(1);
            mPhotoHelperEx.setClip(false);
        } else {
            mPhotoHelperEx.setMaxSelectCount(9);
            mPhotoHelperEx.setClip(false);
        }
        mPhotoHelperEx.showDefaultDialog();
    }

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
        TagSelectionActivity.startForTagListWithTitle(this, mTagLit, 3,
                "为话题贴上标签",
                "选择话题所属的兴趣标签，最多选择3个");
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    private ArrayList<Tag> mTagLit;

    @Override
    public void onCommonEvent(EventBean event) {
        if (event.getEvent() == EventBean.EVENTBUS_TAG_SELECTED_LIST) {

            mTagLit = (ArrayList<Tag>) event.get(TagSelectionActivity.NAME_SELECTED_LIST);

//            String ids = (String) event.get("ids");
//            String names = (String) event.get("names");
//
//            Timber.i(ids);
//            Timber.i(names);

            mTagIds = TagSelectionActivity.formatTagIds(mTagLit);
            mTagNames = TagSelectionActivity.formatTagNames(mTagLit);

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
        Timber.i("显示加载对话框");
        showLoadingDialog();
    }

    @Override
    public void hideLoading() {
        Timber.i("隐藏加载对话框");
        dismissLoadingDialog();
    }

    private int mOffsetY = 10;
    /**
     * 滚动，让屏幕发生变化且内容编辑框获取焦点时始终能看到编辑的内容
     */
    private void scrollRichEditorWrapper() {
        if (mRichEditor.isFocused()) {
            Timber.i("scrollRichEditorWrapper : 焦点在内容编辑器");
            Rect rect = new Rect();
            mNestedScrollView.getLocalVisibleRect(rect);
            // 可见高度
            int visibleH = rect.bottom - rect.top;
            Timber.i(String.format("ScrollView : 高度=%1$d 可见高度=%2$d 编辑框高度= %3$d RichEditor高度=%4$d Y=%5$f",
                    mNestedScrollView.getHeight(),
                    visibleH,
                    mRichEditorLayout.getHeight(),
                    mRichEditor.getHeight(),
                    mRichEditor.getY()));

            // 如果内容小于可见高度，则滚动到内容标题位置
            if (mRichEditor.getHeight() < visibleH - mRichEditor.getY()) {
                Timber.i("内容高度小于可见高度，滚动到内容标题位置");
                Observable.timer(500, TimeUnit.MILLISECONDS)
                        .compose(RxLifecycleUtils.bindToLifecycle((IView) this))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> mNestedScrollView.scrollTo(0,
                                (int) (mContentEditorTitleLayout.getY() - mOffsetY)));
            } else {
                Timber.i("内容高度大于可见高度，滚动到触摸位置");

                Observable.timer(500, TimeUnit.MILLISECONDS)
                        .compose(RxLifecycleUtils.bindToLifecycle((IView) this))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aLong -> mNestedScrollView.scrollTo(0,
                                (int) (mContentEditorTitleLayout.getY() - mOffsetY + mRichEditorTouchY)));
            }
        }
    }

    /**
     * WebView富文本编辑框layout变化
     */
    private View.OnLayoutChangeListener mRichEditorLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int oldH = oldBottom - oldTop;
            int newH = bottom - top;
            if (oldH == newH) return;
            Timber.i("富文本编辑框高度变化 : " + oldH + " -> " + newH);
            Rect rect = new Rect();
            mNestedScrollView.getLocalVisibleRect(rect);
            // 可见高度
            int visibleH = rect.bottom - rect.top;
            Timber.i(String.format("ScrollView : 高度=%1$d 可见高度=%2$d 编辑框高度= %3$d RichEditor高度=%4$d Y=%5$f",
                    mNestedScrollView.getHeight(),
                    visibleH,
                    mRichEditorLayout.getHeight(),
                    mRichEditor.getHeight(),
                    mRichEditor.getY()));

//            if (mRichEditorTouchY + (newH - oldH) > mNestedScrollView.getHeight()) {
//
//                mRichEditorTouchY += newH - oldH;
//
//                Timber.i("滚动RichEditor : " + mRichEditorTouchY);
//
//                mRichEditor.scrollTo(0, (int) (mRichEditorTouchY + mRichEditor.getY()));
//            }


        }
    };

    @Override
    protected void onResume() {
        super.onResume();



        mRootView.addOnLayoutChangeListener(this);
        mRichEditor.addOnLayoutChangeListener(mRichEditorLayoutChangeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMusicBoard.onPause();

        mRootView.removeOnLayoutChangeListener(this);
        mRichEditor.removeOnLayoutChangeListener(mRichEditorLayoutChangeListener);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
        Timber.i(String.format("bottom=%d oldBottom=%d", bottom, oldBottom));

        // 现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > mKeyboardHeight)) {
            // 弹起
            // 隐藏文本样式选择器
            Timber.i("键盘打开");
            mSoftInputPopuped = true;
            // 键盘打开后要将底部的图标改变
            mBottomBar.setToSoftInputMode();
            // 确保键盘打开时选择器隐藏和语音输入
            if (mStylePicker.getVisibility() == View.VISIBLE) {
                mBottomLayout.setVisibility(View.GONE);

                showStylePicker(false);

                mBottomLayout.setVisibility(View.VISIBLE);
            }
            if (mVoicLayout.getVisibility() == View.VISIBLE) {
                mBottomLayout.setVisibility(View.GONE);

                showVoiceLayout(false);

                mBottomLayout.setVisibility(View.VISIBLE);
            }


            scrollRichEditorWrapper();
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > mKeyboardHeight)) {
            // 关闭
            Timber.i("键盘关闭");
            mSoftInputPopuped = false;

            // 是否需要打开样式选择器
            if (mNeedShowStylePicker) {
                // 弹出文本样式选择器
                showStylePicker(true);
                mNeedShowStylePicker = false;
            }

            if (mNeedShowVoiceLayout) {
                showVoiceLayout(true);
                mNeedShowVoiceLayout = false;
            }

            scrollRichEditorWrapper();
        }
    }

    private void showStylePicker(boolean visible) {
        mStylePicker.setVisibility(visible ? View.VISIBLE : View.GONE);

        if (visible) {
            scrollRichEditorWrapper();
        }

        // 当样式选择器可见时，内容编辑器不可点击，解决键盘和选择器同时显示的问题
//        mRichEditor.setFocusable(!visible);
    }

    @Override
    public void toastLong(String msg) {
        ToastUtil.showToastLong(msg);
    }

    /**
     * 话题发布成功
     */
    @Override
    public void onReleaseTopicSuccess() {
        ToastUtil.showToastShort("发布成功");

        EventBus.getDefault().post(new EventBean(EventBean.EVENTBUS_ADD_TOPIC_SUCCESS));

        finish();
    }

    @Override
    public void onResult(String path) {
        Timber.i("图片返回 : " + path);
        String[] pathArray = PhotoHelper.format2Array(path);

        if (pathArray.length <= 0) return;

        Timber.i(String.format("文件大小 : " + FileUtil.getFileSize(pathArray[0])));

        switch (mImageType) {
            case IMAGE_TYPE_CONTENT:
                // 内容插入图片
                for (String imagePath : pathArray) {
                    mRichEditor.insertImage(imagePath, "图片");
                }
                break;
            case IMAGE_TYPE_COVER:
                mCoverImage = pathArray[0];
                // 封面图片
                if (!TextUtils.isEmpty(mCoverImage)) {
                    FrameUtils.obtainAppComponentFromContext(this)
                            .imageLoader()
                            .loadImage(this, ImageConfigImpl.builder()
                                    .imageView(mCoverIV)
                                    .url(mCoverImage)
                                    .transformation(new RoundedCorners(SizeUtils.dp2px(16)))
                                    .build());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mStylePicker.getVisibility() == View.VISIBLE) {
            showStylePicker(false);
        } else if (mVoicLayout.getVisibility() == View.VISIBLE) {
            showVoiceLayout(false);
        } else {
//            finish();
            quit();
        }
    }

    public void quit() {
        DialogSure.showDialog(this,
                "确认退出编辑？",
                "取消" ,
                "确定",
                () -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (null != mCompositeDisposable) {
            mCompositeDisposable.dispose();
        }
    }
}
