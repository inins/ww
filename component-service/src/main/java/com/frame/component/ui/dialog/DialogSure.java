package com.frame.component.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.frame.component.service.R;
import com.frame.component.service.R2;

import butterknife.BindView;

/**
 * 输入弹窗
 */
public class DialogSure extends BaseDialogOkCancel {

    @BindView(R2.id.text_title)
    TextView textTitle;

    private CharSequence title;

    public DialogSure(Context context, CharSequence title) {
        this(context, title, "取消", "确认");
    }

    public DialogSure(Context context, CharSequence title, String cancelBtnText, String okBtnText) {
        super(context, cancelBtnText, okBtnText);
        this.title = title;
    }

    @Override
    protected int getContentView() {
        return R.layout.common_lay_dialog_sure;
    }

    @Override
    protected void intViewOnCreate(View root) {
        super.intViewOnCreate(root);
        textTitle.setText(title);
    }

    public static void showDialog(Context context, CharSequence title, OnSureCallback callback) {
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
