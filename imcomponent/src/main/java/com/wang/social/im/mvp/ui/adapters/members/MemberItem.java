package com.wang.social.im.mvp.ui.adapters.members;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.enums.Gender;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.view.expand.viewholder.AbstractExpandableAdapterItem;

import java.util.Calendar;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:14
 * ============================================
 */
public class MemberItem extends AbstractExpandableAdapterItem {

    private ImageView ivPortrait;
    private TextView tvNickname;
    private TextView tvAge;
    private TextView tvConstellation;
    private TextView tvTags;

    ImageLoader imageLoader;
    Context context;

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.im_item_member;
    }

    @Override
    public void onBindViews(View root) {
        context = root.getContext();
        imageLoader = FrameUtils.obtainAppComponentFromContext(root.getContext()).imageLoader();

        ivPortrait = root.findViewById(R.id.if_iv_portrait);
        tvNickname = root.findViewById(R.id.if_tv_nickname);
        tvAge = root.findViewById(R.id.if_tv_age);
        tvConstellation = root.findViewById(R.id.if_tv_constellation);
        tvTags = root.findViewById(R.id.if_tv_tags);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        if (model instanceof MemberInfo) {
            MemberInfo member = (MemberInfo) model;

            imageLoader.loadImage(context, ImageConfigImpl.builder()
                    .placeholder(R.drawable.common_default_circle_placeholder)
                    .errorPic(R.drawable.common_default_circle_placeholder)
                    .isCircle(true)
                    .imageView(ivPortrait)
                    .url(member.getPortrait())
                    .build());
            tvNickname.setText(member.getNickname());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            String birYear = String.valueOf(year - member.getAge());
            if (birYear.length() > 2) {
                String range = birYear.charAt(birYear.length() - 2) + "0后";
                tvAge.setText(range);
            }
            Drawable genderDrawable;
            if (member.getGender() == Gender.MALE) {
                tvAge.setBackgroundResource(R.drawable.im_bg_male);
                genderDrawable = ContextCompat.getDrawable(context, R.drawable.common_ic_man);
            } else {
                tvAge.setBackgroundResource(R.drawable.im_bg_female);
                genderDrawable = ContextCompat.getDrawable(context, R.drawable.common_ic_women);
            }
            genderDrawable.setBounds(0, 0, genderDrawable.getMinimumWidth(), genderDrawable.getMinimumHeight());
            tvAge.setCompoundDrawables(genderDrawable, null, null, null);
            tvConstellation.setText(member.getConstellation());
            String tags = "";
            for (Tag tag : member.getTags()) {
                tags = tags + "#" + tag.getTagName() + " ";
            }
            tvTags.setText(tags);
        }
    }
}
