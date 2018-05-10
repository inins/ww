package com.wang.social.moneytree.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.component.entities.User;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.moneytree.R;

import java.util.List;

public class GameRoomMemberListAdapter extends RecyclerView.Adapter<GameRoomMemberListAdapter.ViewHolder> {

    public interface ClickListener {
        void onMemberMore();
    }

    private Context mContext;
    private List<User> mList;
    private ClickListener mClickListener;

    public GameRoomMemberListAdapter(RecyclerView recyclerView, List<User> list) {
        mContext = recyclerView.getContext();
        mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.mt_adapter_game_room_member_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        User user = mList.get(position);

        if (null == user) return;

        holder.iv.setOnClickListener(null);
        if (user.getId() == -1) {
            holder.iv.setImageResource(R.drawable.mt_game_more);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mClickListener) {
                        mClickListener.onMemberMore();
                    }
                }
            });
        } else {
            ImageLoaderHelper.loadCircleImg(holder.iv, user.getAvatar());
        }
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        public ViewHolder(View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.image_view);
        }
    }
}
