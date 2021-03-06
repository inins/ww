package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.mvp.ui.MemberListActivity;
import com.wang.social.im.mvp.ui.PersonalCard.PersonalCardActivity;

import java.util.List;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-28 17:34
 * ============================================
 */
public class HomeMemberAdapter extends BaseAdapter<MemberInfo> {

    private String groupId;
    private boolean isSocial;

    public HomeMemberAdapter(List<MemberInfo> members, String groupId, boolean isSocial) {
        valueList = members;
        this.groupId = groupId;
        this.isSocial = isSocial;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<MemberInfo> {

        @BindView(R2.id.ghm_iv_portrait)
        ImageView ghmIvPortrait;
        @BindView(R2.id.ghm_tv_master)
        TextView ghmTvMaster;

        ImageLoader imageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_group_home_member);
            imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(MemberInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            if (position == getData().size() - 1) {
                ghmIvPortrait.setImageResource(R.drawable.im_img_member_more);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MemberListActivity.start(getContext(), groupId, isSocial);
                    }
                });
            } else {
                imageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                        .imageView(ghmIvPortrait)
                        .url(itemValue.getPortrait())
                        .isCircle(true)
                        .placeholder(R.drawable.common_default_circle_placeholder)
                        .errorPic(R.drawable.common_default_circle_placeholder)
                        .build());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PersonalCardActivity.start(getContext(), Integer.valueOf(itemValue.getMemberId()));
                    }
                });
            }
            if (position == 0) {
                ghmTvMaster.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onRelease() {
            super.onRelease();
            imageLoader.clear(getContext(), ImageConfigImpl.builder()
                    .imageView(ghmIvPortrait)
                    .build());
        }
    }
}
