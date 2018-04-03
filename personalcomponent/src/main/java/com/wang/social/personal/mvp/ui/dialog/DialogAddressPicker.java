package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.view.DatePicker;
import com.frame.component.view.WheelPicker;
import com.frame.utils.TimeUtils;
import com.wang.social.personal.R;
import com.wang.social.personal.data.db.AddressDataBaseManager;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DialogAddressPicker extends BaseDialog implements View.OnClickListener {

    @BindView(R.id.wheel_province)
    WheelPicker<Province> wheel_province;
    @BindView(R.id.wheel_city)
    WheelPicker<City> wheel_city;
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
        //省份
        List<Province> provinces = AddressDataBaseManager.getInstance().queryProvince();
        wheel_province.setDataList(provinces);

        wheel_province.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener<Province>() {
            @Override
            public void onWheelSelected(Province item, int position) {
                List<City> cities = AddressDataBaseManager.getInstance().queryCityByProvinceId(item.getId());
                wheel_city.setDataList(cities);
            }
        });
        wheel_city.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener<City>() {
            @Override
            public void onWheelSelected(City item, int position) {
            }
        });
    }

    @OnClick({R.id.btn_dialog_date_cancel, R.id.btn_dialog_date_decide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_date_cancel:
                dismiss();
                break;
            case R.id.btn_dialog_date_decide:
                if (onAddressSelectListener != null) {
                    Province province = wheel_province.getSelectData();
                    City city = wheel_city.getSelectData();
                    onAddressSelectListener.onAddressSelect(province != null ? province.getName() : "", city != null ? city.getName() : "");
                }
                dismiss();
                break;
        }
    }

    //////////////////////////////////
    //////////////////////////////////

    private OnAddressSelectListener onAddressSelectListener;

    public void setOnAddressSelectListener(OnAddressSelectListener onAddressSelectListener) {
        this.onAddressSelectListener = onAddressSelectListener;
    }

    public interface OnAddressSelectListener {
        void onAddressSelect(String province, String city);
    }
}
