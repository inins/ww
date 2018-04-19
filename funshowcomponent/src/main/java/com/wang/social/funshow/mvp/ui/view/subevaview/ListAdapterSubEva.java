package com.wang.social.funshow.mvp.ui.view.subevaview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.frame.component.helper.SelectHelper;
import com.frame.component.utils.SpannableStringUtil;
import com.wang.social.funshow.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterSubEva extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<SubEva> results = new ArrayList<>();

    private int MAXSIZE = 2;

    public List<SubEva> getResults() {
        return results;
    }

    public ListAdapterSubEva(Context context) {
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
        if (results.size() <= MAXSIZE) {
            return results.size();
        } else {
            return MAXSIZE + 1;
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.funshow_item_subeva, parent, true);
            holder = new ViewHolder();
            holder.text_content = convertView.findViewById(R.id.text_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position + 1 > MAXSIZE) {
            holder.text_content.setText(R.string.funshow_home_funshow_detail_eva_more);
            holder.text_content.setTextColor(ContextCompat.getColor(context, R.color.common_blue_deep));
        } else {
            final SubEva bean = results.get(position);
            String[] strings = {"楼主", "回复", "婕欧巴的锅", "：谢谢你的嗷！"};
            int[] colors = {ContextCompat.getColor(context, R.color.common_blue_deep),
                    ContextCompat.getColor(context, R.color.common_text_blank_dark),
                    ContextCompat.getColor(context, R.color.common_blue_deep),
                    ContextCompat.getColor(context, R.color.common_text_blank_dark)
            };
            SpannableStringBuilder spanText = SpannableStringUtil.createV2(strings, colors);
            holder.text_content.setText(spanText);
        }
        return convertView;
    }

    public class ViewHolder {
        TextView text_content;
    }
}
