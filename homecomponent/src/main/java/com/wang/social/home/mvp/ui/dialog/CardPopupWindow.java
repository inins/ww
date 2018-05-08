package com.wang.social.home.mvp.ui.dialog;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.Lable;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterPopLable;

import java.util.ArrayList;

import butterknife.BindView;

public class CardPopupWindow extends BasePopupWindow {

    RecyclerView recyclerGender;
    RecyclerView recyclerAge;
    private RecycleAdapterPopLable adapterGender;
    private RecycleAdapterPopLable adapterAge;

    private Checkable checkable;

    public CardPopupWindow(Context context) {
        super(context);
    }

    @Override
    public int getLayout() {
        return R.layout.home_pop_card;
    }

    @Override
    public void initBase() {
        recyclerGender = getContentView().findViewById(R.id.recycler_gender);
        recyclerAge = getContentView().findViewById(R.id.recycler_age);

        adapterGender = new RecycleAdapterPopLable();
        recyclerGender.setLayoutManager(ChipsLayoutManager.newBuilder(context).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        recyclerGender.addItemDecoration(new SpacingItemDecoration(SizeUtils.dp2px(16), SizeUtils.dp2px(14)));
        recyclerGender.setAdapter(adapterGender);

        adapterAge = new RecycleAdapterPopLable();
        recyclerAge.setLayoutManager(ChipsLayoutManager.newBuilder(context).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        recyclerAge.addItemDecoration(new SpacingItemDecoration(SizeUtils.dp2px(16), SizeUtils.dp2px(14)));
        recyclerAge.setAdapter(adapterAge);

        adapterGender.setOnLableSelectListener((view, lable, position) -> {
            ToastUtil.showToastShort(lable.getName());
        });
        adapterAge.setOnLableSelectListener((view, lable, position) -> {
            ToastUtil.showToastShort(lable.getName());
        });

        adapterGender.refreshData(new ArrayList<Lable>() {{
            add(new Lable("不限", true));
            add(new Lable("男"));
            add(new Lable("女"));
        }});
        adapterAge.refreshData(new ArrayList<Lable>() {{
            add(new Lable("不限", true));
            add(new Lable("90后"));
            add(new Lable("95后"));
            add(new Lable("00后"));
            add(new Lable("其他"));
        }});
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (checkable != null) checkable.setChecked(false);
    }

    @Override
    public void showPopupWindow(View parent) {
        super.showPopupWindow(parent);
        if (checkable != null) checkable.setChecked(true);
    }

    ///////////////////

    public void setCheckable(Checkable checkable) {
        this.checkable = checkable;
    }
}
