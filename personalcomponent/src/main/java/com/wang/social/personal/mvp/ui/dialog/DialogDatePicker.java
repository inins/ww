package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.frame.component.view.DatePicker;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DialogDatePicker extends BaseDialog implements View.OnClickListener {

    @BindView(R2.id.dayPicker_dialog)
    DatePicker mDatePicker;
    @BindView(R2.id.btn_dialog_date_cancel)
    TextView mCancelButton;
    @BindView(R2.id.btn_dialog_date_decide)
    TextView mDecideButton;
    @BindView(R2.id.text_astro)
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

    @OnClick({R2.id.btn_dialog_date_cancel, R2.id.btn_dialog_date_decide})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_dialog_date_cancel) {
            dismiss();

        } else if (i == R.id.btn_dialog_date_decide) {
            if (mOnDateChooseListener != null) {
                NumberFormat numberFormat = NumberFormat.getNumberInstance();
                numberFormat.setMinimumIntegerDigits(2);
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDay();
                String astro = text_astro.getText().toString();
                String showDate = year + "-" + numberFormat.format(month) + "-" + numberFormat.format(day);
                mOnDateChooseListener.onDateChoose(year, month, day, astro, showDate);
            }
            dismiss();

        }
    }

    ///////////////////////////

    //从生日及其星座字符中解析出年月日，只针对该dialog生成的字符串有效
    //格式必须满足："年-月-日 星座"
    //解析失败返回null
    public static int[] analysisDateStr(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) return null;
        if (dateStr.indexOf(" ") != -1)
            dateStr = dateStr.substring(0, dateStr.indexOf(" "));
        String[] split = dateStr.split("-");
        if (split == null || split.length < 3) return null;
        int year = Integer.parseInt(split[0]);
        int mouth = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        return new int[]{year, mouth, day};
    }

    //把没有解析出星座并
    public static String fixDateStr(String dateStr) {
        int[] split = analysisDateStr(dateStr);
        if (StrUtil.isEmpty(split)) return dateStr;
        return dateStr + " " + TimeUtils.getAstro(split[1], split[2]);
    }

    public void setDate(String dateStr) {
        int[] split = analysisDateStr(dateStr);
        if (StrUtil.isEmpty(split)) return;
        mDatePicker.setDate(split[0], split[1], split[2], false);
    }

    ///////////////////////////

    private OnDateChooseListener mOnDateChooseListener;

    public void setOnDateChooseListener(OnDateChooseListener onDateChooseListener) {
        mOnDateChooseListener = onDateChooseListener;
    }

    public interface OnDateChooseListener {
        void onDateChoose(int year, int month, int day, String astro, String showDate);
    }
}
