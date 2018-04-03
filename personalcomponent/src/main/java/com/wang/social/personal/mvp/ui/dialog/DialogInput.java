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
public class DialogInput extends BaseDialogOkCancel {

    @BindView(R.id.text_title)
    TextView textTitle;
    @BindView(R.id.text_note)
    TextView textNote;
    @BindView(R.id.edit_input)
    EditText editInput;

    private String title;
    private String note;
    private String hint;

    public DialogInput(Context context) {
        this(context, null, null, null);
    }

    public DialogInput(Context context, String title, String note, String hint) {
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
//        textTitle.setText(title);
//        textNote.setText(note);
//        editInput.setHint(hint);
    }
}
