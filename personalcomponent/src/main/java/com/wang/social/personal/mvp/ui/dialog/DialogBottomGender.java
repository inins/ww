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
public class DialogBottomGender extends Dialog {
    @BindView(R.id.btn_m)
    TextView btn_m;
    @BindView(R.id.btn_fm)
    TextView btn_fm;
    @BindView(R.id.btn_cancel)
    TextView btn_cancel;
    private Context context;

    public DialogBottomGender(Context context) {
        super(context, R.style.common_PopupDialog);
        this.context = context;
        setMsgDialog();
    }

    private void setMsgDialog() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.personal_dialog_choose_gender, null);
        ButterKnife.bind(this, root);
        btn_cancel.setOnClickListener(listener);
        btn_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGenderSelectListener != null) onGenderSelectListener.onGenderSelect("男");
            }
        });
        btn_fm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onGenderSelectListener != null) onGenderSelectListener.onGenderSelect("女");
            }
        });

        this.setCanceledOnTouchOutside(true);    //点击外部关闭
        super.setContentView(root);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window win = this.getWindow();
        win.setGravity(Gravity.BOTTOM);    //从下方弹出
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogBottomGender.this.dismiss();
        }
    };

    ///////////////////////////

    private OnGenderSelectListener onGenderSelectListener;

    public void setOnGenderSelectListener(OnGenderSelectListener onGenderSelectListener) {
        this.onGenderSelectListener = onGenderSelectListener;
    }

    public interface OnGenderSelectListener {
        void onGenderSelect(String gender);
    }
}
