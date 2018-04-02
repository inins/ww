package com.wang.social.personal.mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wang.social.personal.R;
import com.wang.social.personal.R2;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * liaoinstan
 * 选择性别弹窗
 */
public class DialogBottomGender extends BaseDialog implements View.OnClickListener {
    @BindView(R.id.btn_m)
    TextView btn_m;
    @BindView(R.id.btn_fm)
    TextView btn_fm;
    @BindView(R.id.btn_cancel)
    TextView btn_cancel;

    public DialogBottomGender(Context context) {
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
        return R.layout.personal_dialog_choose_gender;
    }

    @Override
    protected void intView(View root) {
        btn_cancel.setOnClickListener(this);
        btn_m.setOnClickListener(this);
        btn_fm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_m:
                if (onGenderSelectListener != null) onGenderSelectListener.onGenderSelect("男");
                break;
            case R.id.btn_fm:
                if (onGenderSelectListener != null) onGenderSelectListener.onGenderSelect("女");
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    ///////////////////////////

    private OnGenderSelectListener onGenderSelectListener;

    public void setOnGenderSelectListener(OnGenderSelectListener onGenderSelectListener) {
        this.onGenderSelectListener = onGenderSelectListener;
    }

    public interface OnGenderSelectListener {
        void onGenderSelect(String gender);
    }
}
