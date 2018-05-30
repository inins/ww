package com.frame.component.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.PersonalInfo;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.utils.TimeUtils;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private Context mContext;
    private List<PersonalInfo> mList;

    public UserListAdapter(RecyclerView recyclerView, List<PersonalInfo> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_friend_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        PersonalInfo user = mList.get(position);
        if (null == user) return;

        // 头像
        ImageLoaderHelper.loadCircleImg(holder.avatarIV, user.getAvatar());
        // 昵称
        holder.nameTV.setText(user.getNickname());
        // 性别 年代
        holder.genderLableTV.setSelected(!(user.getSex() == 0));
        holder.genderLableTV.setText(TimeUtils.getBirthdaySpan(user.getBirthday()));
        // 星座
        holder.astroTV.setText(TimeUtils.getAstro(user.getBirthday()));
        // 标签
        String tags = "";
        if (null != user.getTags()) {
            for (int i = 0; i < Math.min(5, user.getTags().size()); i++) {
                Tag tag = user.getTags().get(i);
                tags += "#" + tag.getTagName() + "  ";
            }
        }
        holder.tagsTV.setText(tags);

        // 点击打开用户名片
        holder.rootView.setTag(user);
        holder.rootView.setOnClickListener(v -> {
            if (v.getTag() instanceof PersonalInfo) {
                PersonalInfo pi = (PersonalInfo) v.getTag();

                CommonHelper.ImHelper.startPersonalCardForBrowse(mContext, pi.getUserId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView avatarIV;
        TextView nameTV;
        TextView genderLableTV;
        TextView astroTV;
        TextView tagsTV;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            avatarIV = itemView.findViewById(R.id.img_header);
            nameTV = itemView.findViewById(R.id.text_name);
            genderLableTV = itemView.findViewById(R.id.text_lable_gender);
            astroTV = itemView.findViewById(R.id.text_lable_astro);
            tagsTV = itemView.findViewById(R.id.text_tags);
        }
    }
}
