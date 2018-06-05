package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.wang.social.personal.R;

import org.greenrobot.eventbus.EventBus;

public class DepositePopupWindow extends BasePopupWindow {

    private RadioGroup radiogroup;
    private int position;

    public DepositePopupWindow(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    @Override
    public int getLayout() {
        return R.layout.personal_pop_deposit;
    }

    @Override
    public void initBase() {
        //0 全部 1收入明细 1支出明细
        radiogroup = getContentView().findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.btn_all) {
                EventBus.getDefault().post(new EventBean(EventBean.EVENT_DEPOSITDETAIL_SORT).put("position", position).put("type", 0));
            } else if (checkedId == R.id.btn_in) {
                EventBus.getDefault().post(new EventBean(EventBean.EVENT_DEPOSITDETAIL_SORT).put("position", position).put("type", 1));
            } else if (checkedId == R.id.btn_out) {
                EventBus.getDefault().post(new EventBean(EventBean.EVENT_DEPOSITDETAIL_SORT).put("position", position).put("type", 2));
            }
            dismiss();
        });
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
