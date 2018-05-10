package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.utils.StringUtil;
import com.wang.social.topic.mvp.model.entities.SearchResult;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    public interface ClickListener {
        void onClick(SearchResult result);
    }

    Context mContext;
    List<SearchResult> mList;
    ClickListener mClickListener;

    public SearchResultAdapter(RecyclerView recyclerView, List<SearchResult> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        SearchResult result = mList.get(position);
        if (null == result) return;

        // 是否收费
        if (result.getRelateState() == 0) {
            // 不收费
            holder.payIV.setVisibility(View.GONE);
        } else {
            holder.payIV.setVisibility(View.VISIBLE);
        }
        // 标签
        for (int i = 0; i < Math.min(3, result.getTags().size()); i++) {
            switch (i) {
                case 0:
                    holder.tag1TV.setVisibility(View.VISIBLE);
                    holder.tag1TV.setText(result.getTags().get(i).getTagName());
                    break;
                case 1:
                    holder.tag2TV.setVisibility(View.VISIBLE);
                    holder.tag2TV.setText(result.getTags().get(i).getTagName());
                    break;
                case 2:
                    holder.tag3TV.setVisibility(View.VISIBLE);
                    holder.tag3TV.setText(result.getTags().get(i).getTagName());
                    break;
            }
        }
        // 创建日期
        holder.createDateTV.setText(StringUtil.formatCreateDate(mContext, result.getCreateTime()));
        // 图片
        holder.bgIV.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(result.getBackgroundImage())) {
            holder.bgIV.setVisibility(View.VISIBLE);
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.bgIV)
                                    .url(result.getBackgroundImage())
                                    .transformation(new RoundedCorners(SizeUtils.dp2px(8)))
                                    .build());
        }
        // 标题
        holder.titleTV.setText(result.getTitle());
        // 内容
        holder.contentTV.setText(result.getFirstStrff());

        holder.rootView.setTag(result);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() instanceof SearchResult) {
                    if (null != mClickListener) {
                        mClickListener.onClick((SearchResult) v.getTag());
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView payIV;
        TextView tag1TV;
        TextView tag2TV;
        TextView tag3TV;
        TextView createDateTV;
        TextView titleTV;
        TextView contentTV;
        ImageView bgIV;


        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            payIV = itemView.findViewById(R.id.pay_flag_image_view);
            tag1TV = itemView.findViewById(R.id.tag_1_text_view);
            tag2TV = itemView.findViewById(R.id.tag_2_text_view);
            tag3TV = itemView.findViewById(R.id.tag_3_text_view);
            createDateTV = itemView.findViewById(R.id.create_date_text_view);
            titleTV = itemView.findViewById(R.id.title_text_view);
            contentTV = itemView.findViewById(R.id.content_text_view);
            bgIV = itemView.findViewById(R.id.image_view);
        }
    }
}
