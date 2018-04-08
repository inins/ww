package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wang.social.personal.R;

import butterknife.BindView;

/**
 * 输入弹窗
 */
public class DialogSure extends BaseDialogOkCancel {

    @BindView(R.id.text_title)
    TextView textTitle;

    private String title;

    public DialogSure(Context context, String title) {
        this(context, title, "取消", "确认");
    }

    public DialogSure(Context context, String title, String cancelBtnText, String okBtnText) {
        super(context, cancelBtnText, okBtnText);
        this.title = title;
    }

    @Override
    protected int getContentView() {
        return R.layout.personal_lay_dialog_sure;
    }

    @Override
    protected void intViewOnCreate(View root) {
        super.intViewOnCreate(root);
        textTitle.setText(title);
    }

    public static void showDialog(Context context, String title, OnSureCallback callback) {
        DialogSure dialogSure = new DialogSure(context, title);
        dialogSure.setOkClickListener((view -> {
            if (callback != null) callback.onOkClick();
            dialogSure.dismiss();
        }));
        dialogSure.show();
    }

    public interface OnSureCallback {
        void onOkClick();
    }
}
