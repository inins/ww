package com.wang.social.im.mvp.ui.OfficialChatRobot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.im.R;
import com.wang.social.im.mvp.ui.OfficialChatRobot.entities.LeavingMsg;
import com.wang.social.im.view.emotion.ImageLoader;

import java.util.List;

public class LeavingMsgAdapter extends RecyclerView.Adapter<LeavingMsgAdapter.ViewHolder> {
    // 我的消息
    public final static int VIEW_TYPE_MINE = 1;
    // 别人的消息
    public final static int VIEW_TYPE_OTHERS = 2;

    private Context mContext;
    private List<LeavingMsg> mList;

    public LeavingMsgAdapter(Context context, List<LeavingMsg> list) {
        mContext = context.getApplicationContext();
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        if (viewType == VIEW_TYPE_MINE) {
            layoutId = R.layout.im_item_msg_text_right;
        } else {
            layoutId = R.layout.im_item_msg_text_left;
        }

        View view = LayoutInflater.from(mContext)
                .inflate(layoutId, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        LeavingMsg msg = mList.get(position);
        if (null == msg) return;

        holder.contentTV.setText(msg.getMsgContent());

        if (getItemViewType(position) == VIEW_TYPE_MINE) {
            if (null != AppDataHelper.getUser()) {
                ImageLoaderHelper.loadCircleImg(holder.avatarIV,
                        AppDataHelper.getUser().getAvatar());
            }
        } else {
            ImageLoaderHelper.loadCircleImg(holder.avatarIV, "");
        }
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (null == mList) return VIEW_TYPE_MINE;
        if (position < 0 || position >= mList.size()) return VIEW_TYPE_MINE;

        LeavingMsg msg = mList.get(position);

        int localUserId = -111;
        if (null != AppDataHelper.getUser()) {
            localUserId = AppDataHelper.getUser().getUserId();
        }

        return msg.getSendUserId() == localUserId ? VIEW_TYPE_MINE : VIEW_TYPE_OTHERS;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarIV;
        TextView contentTV;
        TextView nameTV;
        public ViewHolder(View itemView) {
            super(itemView);

            avatarIV = itemView.findViewById(R.id.msg_iv_portrait);
            contentTV = itemView.findViewById(R.id.msg_tv_text);
            nameTV = itemView.findViewById(R.id.msg_tv_name);
            if (null != nameTV) {
                nameTV.setVisibility(View.GONE);
            }
        }
    }
}
