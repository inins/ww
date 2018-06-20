package com.wang.social.funshow.mvp.ui.view.subevaview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.SelectHelper;
import com.frame.component.utils.SpannableStringUtil;
import com.frame.component.view.WWClickableSpan;
import com.wang.social.funshow.R;
import com.wang.social.funshow.mvp.entities.eva.Comment;
import com.wang.social.funshow.mvp.entities.eva.CommentReply;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class ListAdapterSubEva extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<CommentReply> results = new ArrayList<>();

    private boolean showAll = false;
    private int MAXSIZE = 2;

    public List<CommentReply> getResults() {
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
        if (showAll) {
            return results.size();
        } else {
            return results.size() <= MAXSIZE ? results.size() : MAXSIZE;
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

        final CommentReply bean = results.get(position);
        String[] strings = {bean.getNickname(), "回复", bean.getTargetNickname(), "：" + bean.getContent()};
        int[] colors = {ContextCompat.getColor(context, R.color.common_blue_deep),
                ContextCompat.getColor(context, R.color.common_text_blank_dark),
                ContextCompat.getColor(context, R.color.common_blue_deep),
                ContextCompat.getColor(context, R.color.common_text_blank_dark)
        };
        ClickableSpan[] clickableSpans = {
                new WWClickableSpan(colors[0], v -> startPersonalCard(bean.getUserId())),
                null,
                new WWClickableSpan(colors[2], v -> startPersonalCard(bean.getTargetUserId())),
                null
        };
        // 设置后SpannableString才能起效
        holder.text_content.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableStringBuilder spanText = SpannableStringUtil.createV2(strings, colors, clickableSpans);
        holder.text_content.setText(spanText);

        return convertView;
    }

    private void startPersonalCard(int userId) {
        CommonHelper.ImHelper.startPersonalCardForBrowse(context, userId);
    }

    public class ViewHolder {
        TextView text_content;
    }

    ///////////////////////////


    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public int getMaxSize() {
        return MAXSIZE;
    }

    public void setMaxSize(int MAXSIZE) {
        this.MAXSIZE = MAXSIZE;
    }
}
