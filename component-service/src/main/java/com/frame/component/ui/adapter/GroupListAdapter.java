package com.frame.component.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.frame.component.entities.GroupBean;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.component.ui.acticity.tags.TagUtils;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;

import java.security.acl.Group;
import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    public interface GroupClickListener {
        void onGroupClick(GroupBean groupBean);
    }

    private Context mContext;
    private List<GroupBean> mList;
    private GroupClickListener mGroupClickListener;

    public GroupListAdapter(RecyclerView recyclerView, List<GroupBean> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    public void setGroupClickListener(GroupClickListener groupClickListener) {
        mGroupClickListener = groupClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_group_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        GroupBean groupBean = mList.get(position);

        if (null == groupBean) return;

        ImageLoaderHelper.loadImg(holder.coverIV, groupBean.getGroupCoverPlan());
//        if (!TextUtils.isEmpty(groupBean.getGroupCoverPlan())) {
//            FrameUtils.obtainAppComponentFromContext(mContext)
//                    .imageLoader()
//                    .loadImage(mContext,
//                            ImageConfigImpl.builder()
//                                    .imageView(holder.coverIV)
//                                    .url(groupBean.getGroupCoverPlan())
//                                    .transformation(new RoundedCorners(SizeUtils.dp2px(12)))
//                                    .build());
//        } else {
//            holder.coverIV.setVisibility(View.INVISIBLE);
//        }

        holder.nameTV.setText(groupBean.getGroupName());
        holder.numberTV.setText("" + groupBean.getGroupMemberNum() + "人");

        if (groupBean.getTags().size() > 0) {
            holder.tagsTV.setText(TagUtils.formatTagNames(groupBean.getTags()));
        } else if (!TextUtils.isEmpty(groupBean.getTagName())) {
            holder.tagsTV.setText("#" + groupBean.getTagName());
        }

        holder.rootView.setTag(groupBean);
        holder.rootView.setOnClickListener(v -> {
            if (null != mGroupClickListener && v.getTag() instanceof GroupBean) {
                GroupBean clickBean = (GroupBean) v.getTag();
                mGroupClickListener.onGroupClick(clickBean);
            }
        });
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
        View rootView;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView;
            coverIV = itemView.findViewById(R.id.image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            numberTV = itemView.findViewById(R.id.number_text_view);
            tagsTV = itemView.findViewById(R.id.tags_text_view);
        }
    }
}
