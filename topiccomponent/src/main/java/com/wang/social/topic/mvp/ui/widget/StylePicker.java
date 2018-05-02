package com.wang.social.topic.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wang.social.topic.R;

import java.util.ArrayList;
import java.util.List;

public class StylePicker extends LinearLayout {
    public StylePicker(Context context) {
        this(context, null);
    }

    public StylePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StylePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        this.setOrientation(VERTICAL);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.weight = 1;

        CarouselPicker picker = new CarouselPicker(getContext());
        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new CarouselPicker.TextItem("20", 20));
        textItems.add(new CarouselPicker.TextItem("22", 22));
        textItems.add(new CarouselPicker.TextItem("24", 24));
        textItems.add(new CarouselPicker.TextItem("26", 26));
        textItems.add(new CarouselPicker.TextItem("28", 28));
        CarouselPicker.CarouselViewAdapter textAdapter =
                new CarouselPicker.CarouselViewAdapter(getContext(), textItems, 0);
        picker.setAdapter(textAdapter);
        picker.setLayoutParams(params);

        addView(picker);
    }
}
