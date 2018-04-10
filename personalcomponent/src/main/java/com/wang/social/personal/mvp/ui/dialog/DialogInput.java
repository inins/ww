package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.utils.viewutils.EditTextUtil;

import butterknife.BindView;

/**
 * 输入弹窗
 */
public class DialogInput extends BaseDialogOkCancel {

    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.text_note)
    TextView textNote;
    @BindView(R2.id.edit_input)
    EditText editInput;

    private String title;
    private String note;
    private String hint;

    public static DialogInput newDialogName(Context context) {
        DialogInput dialogInput = new DialogInput(context, context.getResources().getString(R.string.personal_medetail_dialog_inputname_title)
                , context.getResources().getString(R.string.personal_medetail_dialog_inputname_note)
                , context.getResources().getString(R.string.personal_medetail_dialog_inputname_hint));
        dialogInput.editInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
        return dialogInput;
    }

    public static DialogInput newDialogSign(Context context) {
        DialogInput dialogInput = new DialogInput(context, context.getResources().getString(R.string.personal_medetail_dialog_inputsign_title)
                , context.getResources().getString(R.string.personal_medetail_dialog_inputsign_note)
                , context.getResources().getString(R.string.personal_medetail_dialog_inputsign_hint));
        dialogInput.editInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15)});
        return dialogInput;
    }

    private DialogInput(Context context, String title, String note, String hint) {
        super(context, "取消", "修改");
        this.title = title;
        this.note = note;
        this.hint = hint;
    }

    @Override
    protected int getContentView() {
        return R.layout.personal_lay_dialog_input;
    }

    @Override
    protected void intViewOnCreate(View root) {
        super.intViewOnCreate(root);
        textTitle.setText(title);
        textNote.setText(note);
        editInput.setHint(hint);
        btnOk.setOnClickListener(view -> {
            if (onInputListener != null) {
                onInputListener.onInputText(editInput.getText().toString());
            }
            dismiss();
        });
    }

    //////////////对外方法/////////////

    public void setText(String text) {
        editInput.setText(text);
        EditTextUtil.setTextWithSelectionAtLast(editInput, text);
    }

    //////////////////////////

    private OnInputListener onInputListener;

    public void setOnInputListener(OnInputListener onInputListener) {
        this.onInputListener = onInputListener;
    }

    public interface OnInputListener {
        void onInputText(String text);
    }
}
