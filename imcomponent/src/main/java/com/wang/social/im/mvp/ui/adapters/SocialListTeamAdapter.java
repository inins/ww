package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.TeamInfo;

import java.util.List;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 15:09
 * ============================================
 */
public class SocialListTeamAdapter extends BaseAdapter<TeamInfo> {

    public SocialListTeamAdapter(List<TeamInfo> data) {
        this.valueList = data;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<TeamInfo> {

        @BindView(R2.id.slt_iv_avatar)
        ImageView sltIvAvatar;
        @BindView(R2.id.slt_tv_name)
        TextView sltTvName;
        @BindView(R2.id.slt_tv_member_count)
        TextView sltTvMemberCount;

        private ImageLoader mImageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_social_list_team);
            mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(TeamInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl
                    .builder()
                    .placeholder(R.drawable.im_round_image_placeholder)
                    .errorPic(R.drawable.im_round_image_placeholder)
                    .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                    .imageView(sltIvAvatar)
                    .url((itemValue.getCover()))
                    .build());

            sltTvName.setText(itemValue.getName());
            sltTvMemberCount.setText(UIUtil.getString(R.string.im_member_count_format, itemValue.getMemberSize()));
        }
    }
}
