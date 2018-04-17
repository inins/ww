package com.frame.component.view.barview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.common.HDHeadItemDecoration;
import com.frame.component.helper.LoadingViewHelper;
import com.frame.component.service.R;
import com.frame.utils.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by liaoinstan
 */

public class BarView extends FrameLayout {

    private RecyclerView recycler;
    private TextView text_title;
    private TextView text_note;
    private RecycleAdapterBarView adapter;

    private String title;
    private String note;

    private static final String TAG = "BarView";

    public BarView(Context context) {
        this(context, null);
    }

    public BarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.BarView);
        title = ta.getString(R.styleable.BarView_title);
        note = ta.getString(R.styleable.BarView_note);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.view_bar, this, true);
        initBase();
        initView();
        initCtrl();
    }

    private void initBase() {
    }

    private void initView() {
        recycler = findViewById(R.id.recycler);
        text_title = findViewById(R.id.text_title);
        text_note = findViewById(R.id.text_note);

        if (!TextUtils.isEmpty(title)) setTitle(title);
        if (!TextUtils.isEmpty(note)) setNote(note);
    }

    private void initCtrl() {
        adapter = new RecycleAdapterBarView();
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        if (!isInEditMode()) {
            recycler.addItemDecoration(new HDHeadItemDecoration(SizeUtils.dp2px(-20)));
        } else {
            recycler.addItemDecoration(new HDHeadItemDecoration(40));
        }
        recycler.setAdapter(adapter);
    }


    /////////////////////////////////////
    ///////////// 对外接口 //////////////
    /////////////////////////////////////

    public void refreshData(List<BarUser> results) {
        adapter.refreshData(results);
    }

    public void setOnLoadImageCallBack(OnLoadImageCallBack onLoadImageCallBack) {
        adapter.setOnLoadImageCallBack(onLoadImageCallBack);
    }

    public interface OnLoadImageCallBack {
        void OnImageLoad(ImageView imageView, String imgUrl);
    }

    /////////////////////////////////////

    public void setTitle(String title) {
        this.title = title;
        text_title.setText(title);
    }

    public void setNote(String note) {
        this.note = note;
        text_note.setText(note);
    }
}
