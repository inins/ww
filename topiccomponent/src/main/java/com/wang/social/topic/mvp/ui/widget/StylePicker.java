package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.utils.ToastUtil;
import com.wang.social.topic.R;
import com.wang.social.topic.R2;
import com.wang.social.topic.mvp.ui.widget.circlepicker.CirclePicker;
import com.wang.social.topic.mvp.ui.widget.circlepicker.CirclePickerAdapter;
import com.wang.social.topic.mvp.ui.widget.circlepicker.CirclePickerItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StylePicker extends FrameLayout {

    public interface TextListener {
        void onColor(int color);
        void onFont(int position);
        void onStyle(int position);
        void onSize(int size);
    }

    /**
     * 文本样式监听
     */
    public interface TextStyleListener {
        /**
         * 粗体
         *
         * @param enable 是否选中
         */
        void onBold(boolean enable);

        /**
         * 斜体
         *
         * @param enable 是否选中
         */
        void onItalic(boolean enable);

        /**
         * 下划线
         *
         * @param enable 是否选中
         */
        void onUnderline(boolean enable);

        /**
         * 设置
         *
         * @param enable 是否选中
         */
        void onSetting(boolean enable);

        /**
         * 左对齐
         *
         * @param enable 是否选中
         */
        void onLeftAlign(boolean enable);

        /**
         * 右对齐
         *
         * @param enable 是否选中
         */
        void onRightAlign(boolean enable);

        /**
         * 居中
         *
         * @param enable 是否选中
         */
        void onCenterAlign(boolean enable);
    }


    private View mRootView;
    private Unbinder mUnbinder;

    private TextStyleListener mTextStyleListener;
    private TextListener mTextListener;

    @BindView(R2.id.symbol_cp)
    CirclePicker mSymbolCP;
    @BindView(R2.id.text_font_cp)
    CirclePicker mFontCP;
    @BindView(R2.id.text_color_cp)
    CirclePicker mTextColorCP;
    @BindView(R2.id.text_size_cp)
    CirclePicker mTextSizeCP;

    private int mDefaultColorIndex = 0;
    private int mDefaultSizeIndex = 4;
    private int mDefaultFontIndex = 0;
    private int mDefaultSymbolIndex = 0;

    private static final int[] symbolRes = {
            R.drawable.topic_font_nine,
            R.drawable.topic_font_one,
            R.drawable.topic_font_two,
            R.drawable.topic_font_three,
            R.drawable.topic_font_four,
            R.drawable.topic_font_five,
            R.drawable.topic_font_six,
            R.drawable.topic_font_seven,
            R.drawable.topic_font_eight};

    private static final int[] fontRes = {
            R.drawable.topic_font_kaiti,
            R.drawable.topic_font_pingfang_n,
            R.drawable.topic_font_pingfang_c,
            R.drawable.topic_font_pingfang_x,
            R.drawable.topic_font_songti,
            R.drawable.topic_font_wawa,
            R.drawable.topic_font_youyuan};

    // 颜色列表
    private static final String[] colorRes = {
            "#000000", "#ffd200", "#ff9500",
            "#ff2d3c", "#f677fe", "#995200",
            "#8659f7", "#0064cf", "#009e4a",
            "#f06a31", "#5f5f5f", "#9b9b9b",
            "#adadad", "#cdcdcd", "#dcdcdc",
            "#eaeaea"};
    // 字体大小列表
    private static final int[] sizeRes = {10, 13, 16, 18, 24, 32, 36/*, 36, 38, 40, 46*/};

    public StylePicker(Context context) {
        this(context, null);
    }

    public StylePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StylePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mRootView = LayoutInflater.from(getContext())
                .inflate(R.layout.topic_view_edit_style_pan,
                        this,
                        true);

        mUnbinder = ButterKnife.bind(mRootView);

        // 默认左对齐
        mLeftAlignCB.setChecked(true);

        // 初始为不可见
        setVisibility(GONE);

        initCP();

        // 设置默认选中项
        mTextSizeCP.setCurrentItem(mDefaultSizeIndex);
        mTextColorCP.setCurrentItem(mDefaultColorIndex);
        mFontCP.setCurrentItem(mDefaultFontIndex);
        mSymbolCP.setCurrentItem(mDefaultSymbolIndex);
    }

    /**
     * 初始化滚轮选择器
     */
    private void initCP() {
        initSymbolCP();
        initFontCP();
        initTextColorCP();
        initTextSizeCP();
    }


    private void initSymbolCP() {
        List<CirclePickerItem> list = new ArrayList<>();
        for (int id : symbolRes) {
            list.add(new CirclePickerItem(getResources().getDrawable(id)));
        }
        mSymbolCP.setAdapter(new CirclePickerAdapter(mSymbolCP, list));
        mSymbolCP.setSelectListener(new CirclePicker.SelectListener() {
            @Override
            public void onItemSelected(int position, CirclePickerItem item) {
//                ToastUtil.showToastShort("Symbol选择 " + position);
                if (null != mTextListener) {
                    mTextListener.onStyle(position);
                }
            }
        });
    }

    private void initFontCP() {
        List<CirclePickerItem> list = new ArrayList<>();
        for (int id : fontRes) {
            list.add(new CirclePickerItem(getResources().getDrawable(id)));
        }
        mFontCP.setAdapter(new CirclePickerAdapter(mFontCP, list));
        mFontCP.setSelectListener(new CirclePicker.SelectListener() {
            @Override
            public void onItemSelected(int position, CirclePickerItem item) {
//                ToastUtil.showToastShort("字体选择 " + position);
                if (null != mTextListener) {
                    mTextListener.onFont(position);
                }
            }
        });
    }

    private void initTextColorCP() {
        List<CirclePickerItem> list = new ArrayList<>();
        for (String color : colorRes) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.topic_cr_bg);
            GradientDrawable grad = (GradientDrawable) imageView.getDrawable();
            grad.setColor(Color.parseColor(color));
            list.add(new CirclePickerItem(imageView.getDrawable()));
        }

        mTextColorCP.setAdapter(new CirclePickerAdapter(mTextColorCP, list));
        mTextColorCP.setSelectListener(new CirclePicker.SelectListener() {
            @Override
            public void onItemSelected(int position, CirclePickerItem item) {
//                ToastUtil.showToastShort("颜色选择 " + colorRes[position]);
                if (null != mTextListener) {
                    mTextListener.onColor(Color.parseColor(colorRes[position]));
                }
            }
        });
    }

    /**
     * 初始化字体大小
     */
    private void initTextSizeCP() {
        List<CirclePickerItem> list = new ArrayList<>();
        for (int size : sizeRes) {
            list.add(new CirclePickerItem(Integer.toString(size), size));
        }
        CirclePickerAdapter adapter = new CirclePickerAdapter(mTextSizeCP, list);
        mTextSizeCP.setAdapter(adapter);
        mTextSizeCP.setSelectListener(new CirclePicker.SelectListener() {
            @Override
            public void onItemSelected(int position, CirclePickerItem item) {
//                ToastUtil.showToastShort("字体大小 " + sizeRes[position]);
                if (null != mTextListener) {
                    // 字体大小RichEditor那边规定了时1-7
                    mTextListener.onSize(formatTextSizeForRichEditor(position + 1));
                }
            }
        });
    }

    public void setTextStyleListener(TextStyleListener textStyleListener) {
        mTextStyleListener = textStyleListener;
    }

    public void setTextListener(TextListener textListener) {
        mTextListener = textListener;
    }

    @OnCheckedChanged(R2.id.cuti_cb)
    public void boldChanged(CompoundButton buttonView, boolean isChecked) {
        if (null != mTextStyleListener) {
            mTextStyleListener.onBold(isChecked);
        }
    }

    @OnCheckedChanged(R2.id.xieti_cb)
    public void italicChanged(CompoundButton buttonView, boolean isChecked) {
        if (null != mTextStyleListener) {
            mTextStyleListener.onItalic(isChecked);
        }
    }

    @OnCheckedChanged(R2.id.xiahuaxian_cb)
    public void underlineChanged(CompoundButton buttonView, boolean isChecked) {
        if (null != mTextStyleListener) {
            mTextStyleListener.onUnderline(isChecked);
        }
    }

    @OnCheckedChanged(R2.id.setting_cb)
    public void settingChanged(CompoundButton buttonView, boolean isChecked) {
        if (null != mTextStyleListener) {
            mTextStyleListener.onSetting(isChecked);
        }
    }

    @BindView(R2.id.zuoduiqi_cb)
    CheckBox mLeftAlignCB;

    @OnCheckedChanged(R2.id.zuoduiqi_cb)
    public void leftAlignedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
//        mLeftAlignCB.setChecked(false);
            mRightAlignCB.setChecked(false);
            mCenterAlignCB.setChecked(false);
        }
        if (null != mTextStyleListener) {
            mTextStyleListener.onLeftAlign(isChecked);
        }
    }

    @BindView(R2.id.youduiqi_cb)
    CheckBox mRightAlignCB;

    @OnCheckedChanged(R2.id.youduiqi_cb)
    public void rightAlignedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mLeftAlignCB.setChecked(false);
//        mRightAlignCB.setChecked(false);
            mCenterAlignCB.setChecked(false);
        }
        if (null != mTextStyleListener) {
            mTextStyleListener.onRightAlign(isChecked);
        }
    }

    @BindView(R2.id.juzhong_cb)
    CheckBox mCenterAlignCB;

    @OnCheckedChanged(R2.id.juzhong_cb)
    public void centerAlignedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            mLeftAlignCB.setChecked(false);
            mRightAlignCB.setChecked(false);
//        mCenterAlignCB.setChecked(false);
        }
        if (null != mTextStyleListener) {
            mTextStyleListener.onCenterAlign(isChecked);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        mUnbinder.unbind();
    }

    public int getDefaultColor() {
        return Color.parseColor(colorRes[mDefaultColorIndex]);
    }

    public int getDefaultSize() {
        return formatTextSizeForRichEditor(mDefaultSizeIndex + 1);
    }

    private int formatTextSizeForRichEditor(int size) {
        return Math.max(1, Math.min(size, 7));
    }

    public int getDefaultFont() {
        return mDefaultFontIndex;
    }

    public int getDefaultSymbol() {
        return mDefaultSymbolIndex;
    }
}
