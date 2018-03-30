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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * liaoinstan
 * 选择图片弹窗
 */
public class DialogBottomPhoto extends Dialog implements View.OnClickListener {
    @BindView(R.id.btn_cancel)
    TextView btn_cancel;
    @BindView(R.id.btn_pic)
    TextView btnPic;
    @BindView(R.id.btn_camera)
    TextView btnCamera;
    @BindView(R.id.btn_photo)
    TextView btnPhoto;
    private Context context;

    public DialogBottomPhoto(Context context) {
        super(context, R.style.common_PopupDialog);
        this.context = context;
        setMsgDialog();
    }

    private void setMsgDialog() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.personal_dialog_choose_photo, null);
        ButterKnife.bind(this, root);
        btnPic.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

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
            DialogBottomPhoto.this.dismiss();
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pic:
                dismiss();
                break;
            case R.id.btn_camera:
                dismiss();
                break;
            case R.id.btn_photo:
                dismiss();
                break;
        }
    }
}
