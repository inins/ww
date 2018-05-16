package com.wang.social.moneytree.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.ui.acticity.PersonalCard.PersonalCardActivity;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.component.ui.acticity.tags.TagUtils;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.TimeUtils;
import com.wang.social.moneytree.R;

import java.util.Calendar;
import java.util.List;

import com.wang.social.moneytree.mvp.model.entities.Member;

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {

    public interface ClickListener {
        void onMemeberClick(Member member);
    }

    private Context mContext;
    private List<Member> mList;
    private ClickListener mClickListener;

    public MemberListAdapter(RecyclerView recyclerView, List<Member> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.mt_adapter_member_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mList == null) return;
        if (position < 0 || position >= mList.size()) return;

        Member member = mList.get(position);

        if (null == member) return;

        // 头像
        holder.mAvatarIV.setVisibility(View.INVISIBLE);
        if (!TextUtils.isEmpty(member.getAvatar())) {
            holder.mAvatarIV.setVisibility(View.VISIBLE);
            FrameUtils.obtainAppComponentFromContext(mContext)
                    .imageLoader()
                    .loadImage(mContext,
                            ImageConfigImpl.builder()
                                    .imageView(holder.mAvatarIV)
                                    .url(member.getAvatar())
                                    .isCircle(true)
                                    .build());
        }
        // 昵称
        holder.mNameTV.setText(member.getNickname());
        // 性别
        if (member.getSex() == 0) {
            // 男
            holder.mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_blue_conerfull);
            holder.mGenderIV.setImageResource(R.drawable.common_ic_man);
        } else {
            // 女
            holder.mGenderLayout.setBackgroundResource(R.drawable.common_shape_rect_redgray_conerfull);
            holder.mGenderIV.setImageResource(R.drawable.common_ic_women);
        }
        // 年代
        holder.mPropertyTV.setText(getBirthYears(member.getBirthday()));
        // 星座
        if (TextUtils.isEmpty(member.getConstellation())) {
            holder.mZodiacTV.setText(TimeUtils.getZodiac(member.getBirthday()));
        } else {
            holder.mZodiacTV.setText(member.getConstellation());
        }

        // 标签
//        String tags = "";
//        for (Tag tag : member.getTags()) {
//            tags += "#" + tag.getTagName() + "  ";
//        }
        holder.mTagsTV.setText(TagUtils.formatTagNames(member.getTags()));

        holder.rootView.setTag(member);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickListener && v.getTag() instanceof Member) {
                    mClickListener.onMemeberClick((Member) v.getTag());
                }
            }
        });
    }

    private String getBirthYears(long mills) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mills);
        int year = c.get(Calendar.YEAR);
        year = year - (year % 10);
        year = year % 100;
        if (year == 0) return "00";
        return Integer.toString(year);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView mAvatarIV;
        TextView mNameTV;
        View mGenderLayout;
        ImageView mGenderIV;
        TextView mPropertyTV;
        TextView mZodiacTV;
        TextView mTagsTV;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            mAvatarIV = itemView.findViewById(R.id.avatar_image_view);
            mNameTV = itemView.findViewById(R.id.name_text_view);
            mGenderLayout = itemView.findViewById(R.id.gender_layout);
            mGenderIV = itemView.findViewById(R.id.gender_image_view);
            mPropertyTV = itemView.findViewById(R.id.property_text_view);
            mZodiacTV = itemView.findViewById(R.id.zodiac_text_view);
            mTagsTV = itemView.findViewById(R.id.tags_text_view);
        }
    }
}
