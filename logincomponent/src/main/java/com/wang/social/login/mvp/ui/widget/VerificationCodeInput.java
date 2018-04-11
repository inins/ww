package com.wang.social.login.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.wang.social.login.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by King on 2018/3/30.
 */

public class VerificationCodeInput extends LinearLayout implements TextWatcher, View.OnKeyListener {
    // 几个输入框
    private int boxCount = 4;
    // 输入框宽高
    private int boxWidth = 80;
    private int boxHeight = 80;
    // 间距
    private int childPadding = 14;
    // EditText输入类型
    private String inputType = "number";
    // 输入框选中和未选中背景
    private Drawable boxBgFocus = null;
    private Drawable boxBgNormal = null;

    private Listener listener;

    private boolean focus = false;
    // 输入框列表
    private List<EditText> mEditTextList = new ArrayList<>();
    // 当前输入框
    private int currentPosition = 0;

    public VerificationCodeInput(Context context, AttributeSet attrs) {
        super(context, attrs);

        boxCount = 4;
        childPadding = getResources().getDimensionPixelSize(R.dimen.login_verification_code_input_child_padding);
        boxBgFocus = getResources().getDrawable(R.drawable.login_verification_edit_bg_focus);
        boxBgNormal = getResources().getDrawable(R.drawable.login_verification_edit_bg_normal);

        initViews();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initViews() {
        final float scale = getResources().getDisplayMetrics().density;

        for (int i = 0; i < boxCount; i++) {
            // 输入框
            EditText editText = new EditText(getContext());
            editText.setGravity(Gravity.CENTER);
            editText.setCursorVisible(false);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            // 与右边输入框距离
            if (i < boxCount - 1) {
                layoutParams.rightMargin = childPadding;
            }

            editText.setPadding(0, 0, 0, 0);

            editText.setOnKeyListener(this);
            // 默认选中第一个
            if (i == 0) {
                setBg(editText, true);
            } else {
                setBg(editText, false);
            }

            editText.setTextColor(Color.parseColor("#333333"));

            editText.setTextSize(
                    getResources().getDimensionPixelSize(
                            R.dimen.login_verification_code_input_text_size) / scale);
            editText.setLayoutParams(layoutParams);
            // 输入数字
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            // 最多输入一个字符
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setId(i);
            editText.setEms(1);
            editText.setMaxEms(1);
            editText.addTextChangedListener(this);
            addView(editText, i);
            mEditTextList.add(editText);
        }
    }

    private void backFocus() {
        int count = getChildCount();
        EditText editText;
        for (int i = count - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() == 1) {
                editText.requestFocus();
                setBg(mEditTextList.get(i), true);
                //setBg(mEditTextList.get(i-1),true);
                editText.setSelection(1);
                return;
            }
        }
    }

    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.requestFocus();
                return;
            }
        }
    }

    private void setBg(EditText editText, boolean focus) {
        if (boxBgNormal != null && !focus) {
            editText.setBackground(boxBgNormal);
        } else if (boxBgFocus != null && focus) {
            editText.setBackground(boxBgFocus);
        }
    }

    private void checkAndCommit() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean full = true;
        for (int i = 0; i < boxCount; i++) {
            EditText editText = (EditText) getChildAt(i);
            String content = editText.getText().toString();
            if (content.length() == 0) {
                full = false;
                break;
            } else {
                stringBuilder.append(content);
            }

        }
        if (full) {
            if (listener != null) {
                listener.onComplete(stringBuilder.toString());
                setEnabled(false);
            }

        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    public void setOnCompleteListener(Listener listener) {
        this.listener = listener;
    }

    @Override

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LinearLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (start == 0 && count >= 1 && currentPosition != mEditTextList.size() - 1) {
            currentPosition++;
            mEditTextList.get(currentPosition).requestFocus();
            setBg(mEditTextList.get(currentPosition), true);
            setBg(mEditTextList.get(currentPosition - 1), false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
        } else {
            focus();
            checkAndCommit();
        }
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        EditText editText = (EditText) view;
        if (keyCode == KeyEvent.KEYCODE_DEL && editText.getText().length() == 0) {
            int action = event.getAction();
            if (currentPosition != 0 && action == KeyEvent.ACTION_DOWN) {
                currentPosition--;
                mEditTextList.get(currentPosition).requestFocus();
                setBg(mEditTextList.get(currentPosition), true);
                setBg(mEditTextList.get(currentPosition + 1), false);
                mEditTextList.get(currentPosition).setText("");
            }
        }
        return false;
    }

    public interface Listener {
        void onComplete(String content);
    }
}
