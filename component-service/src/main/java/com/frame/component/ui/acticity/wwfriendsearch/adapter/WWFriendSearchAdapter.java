package com.frame.component.ui.acticity.wwfriendsearch.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchBase;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchFriend;
import com.frame.component.ui.acticity.wwfriendsearch.entities.SearchGroup;
import com.frame.component.view.CircularAutoSizeTextView;
import com.frame.utils.SizeUtils;
import java.util.List;

public class WWFriendSearchAdapter extends RecyclerView.Adapter<WWFriendSearchAdapter.ViewHolder> {

    public interface ClickListener {
        void onFriendClick(SearchFriend friend);
        void onGroupClick(SearchGroup group);
    }

    private Context mContext;
    private List<SearchBase> mList;
    private int mVisibleCount = 3;
    private ClickListener mClickListener;

    public void setVisibleCount(int visibleCount) {
        mVisibleCount = visibleCount;
    }

    public int getVisibleCount() {
        return mVisibleCount;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public WWFriendSearchAdapter(Context context, List<SearchBase> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.layout_search_friend_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        SearchBase bean = mList.get(position);

        if (null == bean) return;

        if (bean instanceof SearchFriend) {
            SearchFriend friend = (SearchFriend) bean;

            holder.mFlagTV.setVisibility(View.GONE);
            ImageLoaderHelper.loadCircleImg(holder.mAvatarIV, friend.getAvatar());
            holder.mNameTV.setText(friend.getNickname());

            if (null != mClickListener) {
                holder.mRootView.setTag(friend);
                holder.mRootView.setOnClickListener(v -> {
                    if (v.getTag() instanceof SearchFriend) {
                        mClickListener.onFriendClick((SearchFriend) v.getTag());
                    }
                });
            }
        } else if (bean instanceof SearchGroup) {
            SearchGroup group = (SearchGroup) bean;

            holder.mFlagTV.setVisibility(View.VISIBLE);
            holder.mFlagTV.setTextYOffset(-SizeUtils.dp2px(1));
            if (group.getPid() <= 0) {
                // 觅聊
                holder.mFlagTV.setBackgroundColor(0xFFFF743D);
                holder.mFlagTV.setText("觅");
            } else {
                // 趣聊
                holder.mFlagTV.setBackgroundColor(0xFF156AC0);
                holder.mFlagTV.setText("趣");
            }
            ImageLoaderHelper.loadCircleImg(holder.mAvatarIV, group.getHead_url());
            holder.mNameTV.setText(group.getGroup_name());

            if (null != mClickListener) {
                holder.mRootView.setTag(group);
                holder.mRootView.setOnClickListener(v -> {
                    if (v.getTag() instanceof SearchGroup) {
                        mClickListener.onGroupClick((SearchGroup) v.getTag());
                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : Math.min(mVisibleCount, mList.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mRootView;
        CircularAutoSizeTextView mFlagTV;
        ImageView mAvatarIV;
        TextView mNameTV;

        public ViewHolder(View itemView) {
            super(itemView);

            mRootView = itemView.findViewById(R.id.root_view);
            mFlagTV = itemView.findViewById(R.id.flag_text_view);
            mAvatarIV = itemView.findViewById(R.id.avatar_image_view);
            mNameTV = itemView.findViewById(R.id.name_text_view);
        }
    }
}
