package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.frame.component.common.SimpleTextWatcher;
import com.frame.component.ui.base.BaseController;
import com.wang.social.funshow.R;

import butterknife.BindView;

public class FunshowAddEditController extends BaseController {

    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.text_count)
    TextView textCount;

    public FunshowAddEditController(View root) {
        super(root);
//        int layout = R.layout.funshow_lay_add_edit;
    }

    @Override
    protected void onInitCtrl() {
        editContent.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                textCount.setText(text.length() + "/50");
            }
        });
    }

    @Override
    protected void onInitData() {
    }
}
