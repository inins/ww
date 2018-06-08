package com.wang.social.funshow.mvp.ui.controller;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.ui.dialog.DialogFunshowAddStone;

import butterknife.BindView;

public class FunshowAddTagController extends FunshowAddBaseController implements View.OnClickListener {

    @BindView(R2.id.check_hidename)
    CheckBox checkHidename;
    @BindView(R2.id.check_pay)
    CheckedTextView checkPay;
    @BindView(R2.id.text_amount)
    TextView textAmount;

    private int diamond;

    private DialogFunshowAddStone dialogStone;

    public FunshowAddTagController(View root) {
        super(root);
        int layout = R.layout.funshow_lay_add_bottomtag;
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        dialogStone = new DialogFunshowAddStone(getContext());
        checkPay.setOnClickListener(this);
        textAmount.setOnClickListener(this);
        dialogStone.setOnInputListener(count -> {
            diamond = count;
            textAmount.setText(":" + diamond + "宝石");
            checkPay.setChecked(true);
        });
    }

    @Override
    protected void onInitData() {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.check_pay) {
            if (checkPay.isChecked()) {
                checkPay.setChecked(false);
                textAmount.setText("");
                dialogStone.clearEdit();
                diamond = 0;
            } else {
                dialogStone.show();
            }
        } else if (id == R.id.text_amount) {
            dialogStone.setCount(diamond);
            dialogStone.show();
        }
    }

    //////////////////////////////////////

    public boolean isPay() {
        return checkPay.isChecked();
    }

    public boolean isHideName() {
        return checkHidename.isChecked();
    }

    public int getDiamond() {
        return diamond;
    }
}
