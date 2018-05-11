package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
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
 * {@link com.wang.social.im.mvp.ui.SocialHomeActivity} 页面觅聊列表适配器
 * <p>
 * Create by ChenJing on 2018-04-28 17:03
 * ============================================
 */
public class SocialHomeTeamsAdapter extends BaseAdapter<TeamInfo> {

    public SocialHomeTeamsAdapter(List<TeamInfo> teams) {
        this.valueList = teams;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<TeamInfo> {

        @BindView(R2.id.sht_iv_avatar)
        ImageView shtIvAvatar;

        ImageLoader mImageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_social_home_team);
            mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(TeamInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            mImageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .imageView(shtIvAvatar)
                    .url(itemValue.getCover())
                    .transformation(new RoundedCornersTransformation(getContext().getResources().getDimensionPixelSize(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                    .placeholder(R.drawable.im_round_image_placeholder)
                    .errorPic(R.drawable.im_round_image_placeholder)
                    .build());
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }

        @Override
        public void onRelease() {
            super.onRelease();
            mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                    .imageView(shtIvAvatar)
                    .build());
        }
    }
}
