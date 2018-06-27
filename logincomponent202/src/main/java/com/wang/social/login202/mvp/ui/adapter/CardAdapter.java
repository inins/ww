package com.wang.social.login202.mvp.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.wang.social.login202.R;
import com.wang.social.login202.mvp.ui.widget.Login202CardView;

import java.util.List;

public class CardAdapter extends BaseAdapter {

    private Context mContext;
    private List<Login202CardView> mList;

    public CardAdapter(Context context, List<Login202CardView> list) {
        mContext = context.getApplicationContext();
        mList = list;
    }

    @Override
    public int getCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public Login202CardView getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = mList.get(position);
            convertView.setTag(position);
        }

        return convertView;
    }
}
