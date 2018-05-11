package com.frame.component.ui.acticity.PersonalCard.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.component.ui.acticity.PersonalCard.model.entities.UserInfo;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.utils.TimeUtils;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context mContext;
    private List<UserInfo> mList;

    public UserListAdapter(RecyclerView recyclerView, List<UserInfo> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.personal_card_adapter_friend, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        UserInfo user = mList.get(position);
        if (null == user) return;

        ImageLoaderHelper.loadCircleImg(holder.avatarIV, user.getAvatar());
        holder.nameTV.setText(user.getNickname());
        holder.genderLableTV.setSelected(!(user.getSex() == 0));
        holder.genderLableTV.setText(TimeUtils.getBirthdaySpan(user.getBirthday()));
        String tags = "";
        if (null != user.getTags()) {
            for (int i = 0; i < Math.min(5, user.getTags().size()); i++) {
                Tag tag = user.getTags().get(i);
                tags += "#" + tag.getTagName() + "  ";
            }
        }
        holder.tagsTV.setText(tags);
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarIV;
        TextView nameTV;
        TextView genderLableTV;
        TextView astroTV;
        TextView tagsTV;

        public ViewHolder(View itemView) {
            super(itemView);

            avatarIV = itemView.findViewById(R.id.img_header);
            nameTV = itemView.findViewById(R.id.text_name);
            genderLableTV = itemView.findViewById(R.id.text_lable_gender);
            astroTV = itemView.findViewById(R.id.text_lable_astro);
            tagsTV = itemView.findViewById(R.id.text_tags);
        }
    }
}
