package com.wang.social.funshow.mvp.ui.view.subevaview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.frame.component.view.ListViewLinearLayout;
import com.wang.social.funshow.R;
import com.wang.social.funshow.mvp.entities.eva.CommentReply;

import java.util.List;

/**
 * Created by Administrator on 2018/3/29.
 */

public class SubEvaView extends FrameLayout {

    private ListViewLinearLayout listline;
    private ListAdapterSubEva adapter;

    public SubEvaView(@NonNull Context context) {
        this(context, null);
    }

    public SubEvaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubEvaView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
//            final TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TitleView, 0, 0);
//            title = ta.getString(R.styleable.TitleView_personal_textTitle);
//            subTitle = ta.getString(R.styleable.TitleView_personal_textSubTitle);
//            note = ta.getString(R.styleable.TitleView_personal_textNote);
//            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        LayoutInflater.from(getContext()).inflate(R.layout.funshow_view_subevaview, this, true);
        super.onFinishInflate();
        initView();
        initCtrl();
    }

    private void initView() {
        listline = findViewById(R.id.listline);
    }

    private void initCtrl() {
        adapter = new ListAdapterSubEva(getContext());
        listline.setAdapter(adapter);
    }

    //////////////////////////////////////////

    public void refreshData(List<CommentReply> subEvaList) {
        if (subEvaList == null) return;
        adapter.getResults().clear();
        adapter.getResults().addAll(subEvaList);
        adapter.notifyDataSetChanged();
    }

    public boolean isShowAll() {
        return adapter.isShowAll();
    }

    public void setShowAll(boolean showAll) {
        adapter.setShowAll(showAll);
    }

    public int getMaxSize() {
        return adapter.getMaxSize();
    }
}
