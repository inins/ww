package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.view.DatePicker;
import com.frame.component.view.WheelPicker;
import com.frame.utils.TimeUtils;
import com.wang.social.personal.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DialogAddressPicker extends BaseDialog implements View.OnClickListener {

    @BindView(R.id.wheel_province)
    WheelPicker<String> wheel_province;
    @BindView(R.id.wheel_city)
    WheelPicker<String> wheel_city;
    @BindView(R.id.btn_dialog_date_cancel)
    TextView mCancelButton;
    @BindView(R.id.btn_dialog_date_decide)
    TextView mDecideButton;
    @BindView(R.id.text_astro)
    TextView text_astro;

    public DialogAddressPicker(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogBottom();
        setSize(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected int getView() {
        return R.layout.personal_dialog_address;
    }

    @Override
    protected void intView(View root) {
        wheel_province.setDataList(new ArrayList<String>() {{
            add("云南省");
            add("湖南省");
            add("四川省");
            add("陕西省");
            add("海南省");
            add("重庆");
            add("内蒙古");
        }});
        wheel_city.setDataList(new ArrayList<String>() {{
            add("乐山市");
            add("成都市");
            add("巴中市");
            add("泸州市");
            add("重庆市");
            add("上海市");
            add("北京市");
        }});
    }

    @OnClick({R.id.btn_dialog_date_cancel, R.id.btn_dialog_date_decide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_date_cancel:
                dismiss();
                break;
            case R.id.btn_dialog_date_decide:
                dismiss();
                break;
        }
    }

}
