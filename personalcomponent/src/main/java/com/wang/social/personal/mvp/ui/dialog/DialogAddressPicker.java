package com.wang.social.personal.mvp.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.frame.component.ui.dialog.BaseDialog;
import com.frame.component.view.WheelPicker;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.data.db.AddressDataBaseManager;
import com.wang.social.personal.mvp.entities.City;
import com.wang.social.personal.mvp.entities.Province;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 时间选择器，弹出框
 * Created by ycuwq on 2018/1/6.
 */
public class DialogAddressPicker extends BaseDialog implements View.OnClickListener {

    @BindView(R2.id.wheel_province)
    WheelPicker<Province> wheel_province;
    @BindView(R2.id.wheel_city)
    WheelPicker<City> wheel_city;
    @BindView(R2.id.btn_dialog_date_cancel)
    TextView mCancelButton;
    @BindView(R2.id.btn_dialog_date_decide)
    TextView mDecideButton;
    @BindView(R2.id.text_astro)
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

    @OnClick({R2.id.btn_dialog_date_cancel, R2.id.btn_dialog_date_decide})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_dialog_date_cancel) {
            dismiss();

        } else if (i == R.id.btn_dialog_date_decide) {
            if (onAddressSelectListener != null) {
                Province province = wheel_province.getSelectData();
                City city = wheel_city.getSelectData();
                onAddressSelectListener.onAddressSelect(province, city);
            }
            dismiss();

        }
    }

    //////////////////////////////////
    //////////////////////////////////

    private OnAddressSelectListener onAddressSelectListener;

    public void setOnAddressSelectListener(OnAddressSelectListener onAddressSelectListener) {
        this.onAddressSelectListener = onAddressSelectListener;
    }

    public interface OnAddressSelectListener {
        void onAddressSelect(Province province, City city);
    }
}
