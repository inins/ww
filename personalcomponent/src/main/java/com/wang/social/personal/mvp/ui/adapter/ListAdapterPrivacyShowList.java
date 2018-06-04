package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.SelectHelper;
import com.wang.social.personal.R;
import com.wang.social.personal.mvp.entities.ShowListGroup;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterPrivacyShowList extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<ShowListGroup> results = new ArrayList<>();

    public List<ShowListGroup> getResults() {
        return results;
    }

    public ListAdapterPrivacyShowList(Context context) {
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.personal_item_privacy_showlist_child, parent, true);
            holder = new ViewHolder();
            holder.text_title = convertView.findViewById(R.id.text_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ShowListGroup bean = results.get(position);

        holder.text_title.setSelected(bean.isSelect());
        holder.text_title.setText(bean.getTitle());
        holder.text_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setSelect(!bean.isSelect());
                notifyDataSetChanged();
                if (onCheckCallback != null) {
                    onCheckCallback.onCheck(!SelectHelper.isUnSelectAll(results));
                }
            }
        });

        return convertView;
    }

    public class ViewHolder {
        TextView text_title;
    }

    ////////////////

    private OnCheckCallback onCheckCallback;

    public void setOnCheckCallback(OnCheckCallback onCheckCallback) {
        this.onCheckCallback = onCheckCallback;
    }

    public interface OnCheckCallback{
        void onCheck(boolean hasCheck);
    }
}
