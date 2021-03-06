package com.frame.component.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.common.InputLengthFilter;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.utils.UIUtil;
import com.frame.utils.RegexUtils;
import com.frame.utils.ScreenUtils;
import com.frame.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-07 13:45
 * ============================================
 */
public class EditDialog extends BaseDialog {

    @BindView(R2.id.de_tv_title)
    TextView deTvTitle;
    @BindView(R2.id.de_tv_tip)
    TextView deTvTip;
    @BindView(R2.id.de_et_content)
    EditText deEtContent;

    private String mContent, mTitle;
    private int maxLength;
    private boolean nameFilter;

    private OnInputCompleteListener mInputCompleteListener;

    public EditDialog(Context context, String content, String title, int maxLength, OnInputCompleteListener inputCompleteListener) {
        this(context, content, title, maxLength, inputCompleteListener, true);
    }

    public EditDialog(Context context, String content, String title, int maxLength, OnInputCompleteListener inputCompleteListener, boolean nameFilter) {
        super(context);
        this.mContent = content;
        this.mTitle = title;
        this.maxLength = maxLength;
        this.nameFilter = nameFilter;
        this.mInputCompleteListener = inputCompleteListener;
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void intViewOnCreate(View root) {
        if (!TextUtils.isEmpty(mContent)) {
            deEtContent.setText(mContent);
        }
        deTvTitle.setText(mTitle);
        deTvTip.setText(UIUtil.getString(R.string.com_text_length_format, maxLength));

        Window win = this.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (ScreenUtils.getScreenWidth() * 0.85f);
        lp.height = SizeUtils.dp2px(200f);
        win.setAttributes(lp);

        InputFilter[] filters = new InputFilter[nameFilter ? 2 : 1];
        filters[0] = new InputLengthFilter(maxLength, false);
        if (nameFilter) {
            filters[1] = new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    if (nameFilter && !RegexUtils.isUsernameMe(source)) {
                        return "";
                    }
                    return source;
                }
            };
        }

        deEtContent.setFilters(filters);
    }

    @Override
    protected int getView() {
        return R.layout.common_dialog_edit;
    }

    @Override
    protected void intView(View root) {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        deEtContent.setSelection(deEtContent.getText().length());
    }

    @OnClick({R2.id.de_tv_cancel, R2.id.de_tv_sure})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.de_tv_sure) {
            if (mInputCompleteListener != null) {
                mInputCompleteListener.onComplete(this, deEtContent.getText().toString());
            }
        } else if (view.getId() == R.id.de_tv_cancel) {
            dismiss();
        }
    }

    public interface OnInputCompleteListener {

        void onComplete(Dialog dialog, String content);
    }
}
