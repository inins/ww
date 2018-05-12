package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.UserInfo;
import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.frame.utils.TimeConstants;
import com.wang.social.im.R;
import com.wang.social.im.R2;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-10 15:58
 * ============================================
 */
public class NewUsersAdapter extends BaseAdapter<UserInfo> {

    public NewUsersAdapter(List<UserInfo> list) {
        this.valueList = list;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<UserInfo> {

        @BindView(R2.id.if_iv_portrait)
        ImageView ifIvPortrait;
        @BindView(R2.id.if_tv_nickname)
        TextView ifTvNickname;
        @BindView(R2.id.if_tv_age)
        TextView ifTvAge;
        @BindView(R2.id.if_tv_constellation)
        TextView ifTvConstellation;
        @BindView(R2.id.if_tv_tags)
        TextView ifTvTags;
        @BindView(R2.id.inu_tv_join_time)
        TextView inuTvJoinTime;

        private ImageLoader imageLoader;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.im_item_new_user);
            imageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
        }

        @Override
        protected void bindData(UserInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            imageLoader.loadImage(getContext(), ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(ifIvPortrait)
                    .url(itemValue.getPortrait())
                    .build());
            ifTvNickname.setText(itemValue.getNickname());
            ifTvAge.setText(itemValue.getAgeRange());
            Drawable genderDrawable;
            if (itemValue.getGender() == Gender.MALE) {
                ifTvAge.setBackgroundResource(R.drawable.im_bg_male);
                genderDrawable = ContextCompat.getDrawable(getContext(), R.drawable.common_ic_man);
            } else {
                ifTvAge.setBackgroundResource(R.drawable.im_bg_female);
                genderDrawable = ContextCompat.getDrawable(getContext(), R.drawable.common_ic_women);
            }
            genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
            ifTvAge.setCompoundDrawables(genderDrawable, null, null, null);
            ifTvConstellation.setText(itemValue.getConstellation());
            String tags = "";
            for (Tag tag : itemValue.getTags()) {
                tags = tags + "#" + tag.getTagName() + " ";
            }
            ifTvTags.setText(tags);

            long registerTime = itemValue.getRegisterTime();
            long now = System.currentTimeMillis();
            long span = now - registerTime;
            if (span < TimeConstants.MIN) {
                inuTvJoinTime.setText("刚刚加入");
            } else if (span < TimeConstants.HOUR) {
                inuTvJoinTime.setText(String.format(Locale.getDefault(), "%d分钟前加入", span / TimeConstants.MIN));
            } else if (span < TimeConstants.DAY) {
                inuTvJoinTime.setText(String.format(Locale.getDefault(), "%d小时前加入", span / TimeConstants.HOUR));
            } else {
                inuTvJoinTime.setText(String.format(Locale.getDefault(), "%d天前加入", span / TimeConstants.DAY));
            }
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
