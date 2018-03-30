package com.wang.social.personal.mvp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.wang.social.personal.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 确定取消弹窗
 * 可以如下这样调用，更加便捷
 * DialogSure.showDialog(this, "提示信息？", new DialogSure.CallBack() {
 *
 * @Override public void onSure() {
 * ExamActivity.super.onBackPressed();
 * }
 * });
 */
public class DialogInput extends Dialog implements View.OnClickListener {

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_note)
    TextView textNote;
    @BindView(R.id.edit_input)
    EditText editInput;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;
    @BindView(R.id.btn_ok)
    TextView btnOk;
    private Context context;

    private String title;
    private String note;
    private String hint;

    public DialogInput(Context context) {
        this(context, null, null, null);
    }

    public DialogInput(Context context, String title, String note, String hint) {
        super(context, R.style.common_MyDialog);
        this.context = context;
        this.title = title;
        this.note = note;
        this.hint = hint;
        setLoadingDialog();
    }

    private void setLoadingDialog() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.personal_dialog_input, null);
        ButterKnife.bind(this, root);
        btnCancel.setOnClickListener(this);
        btnOk.setOnClickListener(this);

        super.setContentView(root);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = (int) (screenWidth * 0.85); // 宽度
//        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = (int) (lp.width*0.65); // 高度
        this.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                dismiss();
                break;
        }
    }
}
