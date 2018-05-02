package com.wang.social.personal.mvp.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.frame.component.helper.LoadingViewHelper;
import com.frame.component.utils.XMediaPlayer;
import com.frame.component.view.ConerEditText;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;

import timber.log.Timber;

public class CounterView extends FrameLayout implements View.OnClickListener {

    private View btn_counter_sub;
    private View btn_counter_add;
    private EditText edit_counter;

    private int MIN = 0;
    private int MAX = Integer.MAX_VALUE;

    public CounterView(Context context) {
        this(context, null);
    }

    public CounterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
//        failViewSrc = typedArray.getResourceId(R.styleable.LoadingLayout_layout_fail, 0);
//        typedArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.personal_view_counter, this, true);
        initView();
        initCtrl();
    }

    private void initView() {
        btn_counter_sub = findViewById(R.id.btn_counter_sub);
        btn_counter_add = findViewById(R.id.btn_counter_add);
        edit_counter = findViewById(R.id.edit_counter);
    }

    private void initCtrl() {
        btn_counter_sub.setOnClickListener(this);
        btn_counter_add.setOnClickListener(this);

        edit_counter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (onCountChangerListener != null) {
                    int count = StrUtil.str2int(edit_counter.getText().toString());
                    onCountChangerListener.onCountChange(count);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int count = StrUtil.str2int(edit_counter.getText().toString());
        int id = v.getId();
        if (id == R.id.btn_counter_sub) {
            if (count - 1 >= MIN) edit_counter.setText(count - 1 + "");
        } else if (id == R.id.btn_counter_add) {
            if (count + 1 <= MAX) edit_counter.setText(count + 1 + "");
        }
    }

    ///////////////////////////////////////////////////////////

    public int getCount() {
        return StrUtil.str2int(edit_counter.getText().toString());
    }

    public void setCount(Integer count) {
        edit_counter.setText(count != null ? String.valueOf(count) : "");
    }

    private OnCountChangerListener onCountChangerListener;

    public void setOnCountChangerListener(OnCountChangerListener onCountChangerListener) {
        this.onCountChangerListener = onCountChangerListener;
    }

    public interface OnCountChangerListener {
        void onCountChange(int count);
    }
}
