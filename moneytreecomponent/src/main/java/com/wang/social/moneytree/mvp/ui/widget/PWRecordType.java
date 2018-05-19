package com.wang.social.moneytree.mvp.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.frame.component.ui.dialog.BasePopupWindow;
import com.frame.utils.SizeUtils;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.mvp.model.entities.GameRecord;

public class PWRecordType extends BasePopupWindow implements View.OnClickListener {

    public interface TypeListener {
        void onTypeSelected(@GameRecord.RecordType int type);
    }

    private TypeListener mTypeListener;

    public PWRecordType(Context context) {
        super(context);
        setWidth(SizeUtils.dp2px(120));
    }

    public void setTypeListener(TypeListener typeListener) {
        mTypeListener = typeListener;
    }

    @Override
    public int getLayout() {
        return R.layout.mt_view_pw_record_type;
    }

    @Override
    public void initBase() {
        getContentView().findViewById(R.id.all_text_view)
                .setOnClickListener(this);
        getContentView().findViewById(R.id.lose_text_view)
                .setOnClickListener(this);
        getContentView().findViewById(R.id.win_text_view)
                .setOnClickListener(this);
        getContentView().findViewById(R.id.tie_text_view)
                .setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (null != mTypeListener) {
            if (v.getId() == R.id.all_text_view) {
                mTypeListener.onTypeSelected(GameRecord.TYPE_PLAYING);
            } else if (v.getId() == R.id.lose_text_view) {
                mTypeListener.onTypeSelected(GameRecord.TYPE_LOSE);
            } else if (v.getId() == R.id.win_text_view) {
                mTypeListener.onTypeSelected(GameRecord.TYPE_WIN);
            } else if (v.getId() == R.id.tie_text_view) {
                mTypeListener.onTypeSelected(GameRecord.TYPE_TIE);
            }
        }

        dismiss();
    }
}
