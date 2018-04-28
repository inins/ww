package com.wang.social.funshow.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialogOkCancel;
import com.frame.component.ui.dialog.DialogSure;
import com.frame.component.utils.viewutils.EditTextUtil;
import com.frame.utils.StrUtil;
import com.frame.utils.ToastUtil;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;

import butterknife.BindView;

/**
 * 输入弹窗
 */
public class DialogFunshowAddStone extends BaseDialogOkCancel {

    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.text_reset)
    TextView textReset;
    @BindView(R2.id.edit_input)
    EditText editInput;

    public DialogFunshowAddStone(Context context) {
        super(context, "取消", "修改");
    }

    @Override
    protected int getContentView() {
        return R.layout.funshow_lay_dialog_add_stone;
    }

    @Override
    protected void intViewOnCreate(View root) {
        super.intViewOnCreate(root);
        btnOk.setOnClickListener(view -> {
            int count = StrUtil.str2int(editInput.getText().toString());
            if (count == 0) {
                ToastUtil.showToastShort("最少付费1钻石");
                return;
            }
            if (onInputListener != null) {
                onInputListener.onInputText(count);
                dismiss();
            }
        });
        textReset.setOnClickListener(v -> editInput.setText(""));
    }

    //////////////对外方法/////////////

    public void setCount(int count) {
        editInput.setText(count + "");
        EditTextUtil.setTextWithSelectionAtLast(editInput, count + "");
    }

    //////////////////////////

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    public interface OnInputListener {
        void onInputText(int count);
    }

}
