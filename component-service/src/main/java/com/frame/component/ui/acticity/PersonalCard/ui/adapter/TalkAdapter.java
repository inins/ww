package com.frame.component.ui.acticity.PersonalCard.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.frame.component.service.R;
import com.frame.component.ui.acticity.PersonalCard.model.entities.TalkBean;
import com.frame.component.ui.acticity.tags.TagUtils;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;

import java.util.List;

public class TalkAdapter extends RecyclerView.Adapter<TalkAdapter.ViewHolder> {

    private Context mContext;
    private List<TalkBean> mList;

    public TalkAdapter(RecyclerView recyclerView, List<TalkBean> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.personal_card_adapter_talk, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        TalkBean talkBean = mList.get(position);

        if (null == talkBean) return;

        if (!TextUtils.isEmpty(talkBean.getGroupCoverPlan())) {
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.coverIV)
                                    .url(talkBean.getGroupCoverPlan())
                                    .transformation(new RoundedCorners(SizeUtils.dp2px(12)))
                                    .build());
        } else {
            holder.coverIV.setVisibility(View.INVISIBLE);
        }

        holder.nameTV.setText(talkBean.getGroupName());
        holder.numberTV.setText("" + talkBean.getGroupMemberNum() + "人");
        holder.tagsTV.setText(TagUtils.formatTagNames(talkBean.getTags()));
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverIV;
        TextView nameTV;
        TextView numberTV;
        TextView tagsTV;

        public ViewHolder(View itemView) {
            super(itemView);

            coverIV = itemView.findViewById(R.id.image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            numberTV = itemView.findViewById(R.id.number_text_view);
            tagsTV = itemView.findViewById(R.id.tags_text_view);
        }
    }
}
