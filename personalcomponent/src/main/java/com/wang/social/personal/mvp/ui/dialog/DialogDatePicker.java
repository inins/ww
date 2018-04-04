package com.wang.social.personal.mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.frame.component.view.DatePicker;
import com.frame.utils.TimeUtils;
import com.wang.social.personal.R;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DialogDatePicker extends BaseDialog implements View.OnClickListener {

    @BindView(R.id.dayPicker_dialog)
    DatePicker mDatePicker;
    @BindView(R.id.btn_dialog_date_cancel)
    TextView mCancelButton;
    @BindView(R.id.btn_dialog_date_decide)
    TextView mDecideButton;
    @BindView(R.id.text_astro)
    TextView text_astro;

    public DialogDatePicker(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogBottom();
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected int getView() {
        return R.layout.personal_dialog_date;
    }

    @Override
    protected void intView(View root) {
        text_astro.setText(TimeUtils.getAstro(mDatePicker.getMonth(), mDatePicker.getDay()));
        mDatePicker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                text_astro.setText(TimeUtils.getAstro(month, day));
            }
        });
    }

    @OnClick({R.id.btn_dialog_date_cancel, R.id.btn_dialog_date_decide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_date_cancel:
                dismiss();
                break;
            case R.id.btn_dialog_date_decide:
                if (mOnDateChooseListener != null) {
                    NumberFormat numberFormat = NumberFormat.getNumberInstance();
                    numberFormat.setMinimumIntegerDigits(2);
                    int year = mDatePicker.getYear();
                    int month = mDatePicker.getMonth();
                    int day = mDatePicker.getDay();
                    String astro = text_astro.getText().toString();
                    String showText = year + "-" + numberFormat.format(month) + "-" + numberFormat.format(day) + " " + astro;
                    mOnDateChooseListener.onDateChoose(year, month, day, astro, showText);
                }
                dismiss();
                break;
        }
    }

    ///////////////////////////

    public void setDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) return;
        dateStr = dateStr.substring(dateStr.indexOf(" "));
        String[] split = dateStr.split("-");
        if (split == null || split.length < 3) return;
        int year = Integer.parseInt(split[0]);
        int mouth = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        mDatePicker.setDate(year, mouth, day);
    }

    ///////////////////////////

    private OnDateChooseListener mOnDateChooseListener;

    public void setOnDateChooseListener(OnDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
    }

    public interface OnDateChooseListener {
        void onDateChoose(int year, int month, int day, String astro, String showText);
    }
}
