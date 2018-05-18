package com.frame.component.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.common.SimpleTextWatcher;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.utils.ToastUtil;

import butterknife.BindView;

/**
 * 输入弹窗
 */
public class DialogValiRequest extends BaseDialogOkCancel {

    @BindView(R2.id.edit_content)
    EditText editContent;
    @BindView(R2.id.text_count)
    TextView textCount;

    public DialogValiRequest(Context context) {
        this(context, "取消", "发送");
    }

    public DialogValiRequest(Context context, String cancelBtnText, String okBtnText) {
        super(context, cancelBtnText, okBtnText);
    }

    @Override
    protected int getContentView() {
        return R.layout.common_lay_dialog_valirequest;
    }

    @Override
    protected void intViewOnCreate(View root) {
        super.intViewOnCreate(root);
        setCanceledOnTouchOutside(false);
        editContent.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                textCount.setText(text.length() + "/100");
            }
        });
    }

    private String getContent() {
        return editContent.getText().toString();
    }

    public interface OnSureCallback {
        void onOkClick(String content);
    }

    public static void showDialog(Context context, OnSureCallback callback) {
        DialogValiRequest dialogSure = new DialogValiRequest(context);
        dialogSure.setOkClickListener(view -> {
            if (!TextUtils.isEmpty(dialogSure.getContent())) {
                if (callback != null) callback.onOkClick(dialogSure.getContent());
            } else {
                ToastUtil.showToastShort("请输入验证信息");
            }
            dialogSure.dismiss();
        });
        dialogSure.show();
    }
}
