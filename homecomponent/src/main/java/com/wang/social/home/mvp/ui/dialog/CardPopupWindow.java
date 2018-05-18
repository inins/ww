package com.wang.social.home.mvp.ui.dialog;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Checkable;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.entities.EventBean;
import com.frame.utils.SizeUtils;
import com.wang.social.home.R;
import com.wang.social.home.mvp.entities.Lable;
import com.wang.social.home.mvp.ui.adapter.RecycleAdapterPopLable;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

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
            //性别 0 男 1女 -1 未知
            int gender = -1;
            switch (lable.getIndex()) {
                case 0:
                    gender = -1;
                    break;
                case 1:
                    gender = 0;
                    break;
                case 2:
                    gender = 1;
                    break;
            }
            EventBean eventBean = new EventBean(EventBean.EVENT_HOME_CARD_GENDER_SELECT).put("gender", gender);
            EventBus.getDefault().post(eventBean);
        });
        adapterAge.setOnLableSelectListener((view, lable, position) -> {
            //all,90,95,00,other
            String age = null;
            switch (lable.getIndex()) {
                case 0:
                    age = "all";
                    break;
                case 1:
                    age = "90";
                    break;
                case 2:
                    age = "95";
                    break;
                case 3:
                    age = "00";
                    break;
                case 4:
                    age = "other";
                    break;
            }
            EventBean eventBean = new EventBean(EventBean.EVENT_HOME_CARD_AGE_SELECT).put("age", age);
            EventBus.getDefault().post(eventBean);
        });

        adapterGender.refreshData(new ArrayList<Lable>() {{
            add(new Lable(0, "不限", true));
            add(new Lable(1, "男"));
            add(new Lable(2, "女"));
        }});
        adapterAge.refreshData(new ArrayList<Lable>() {{
            add(new Lable(0, "不限", true));
            add(new Lable(1, "90后"));
            add(new Lable(2, "95后"));
            add(new Lable(3, "00后"));
            add(new Lable(4, "其他"));
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
