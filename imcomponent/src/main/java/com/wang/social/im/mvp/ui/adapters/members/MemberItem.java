package com.wang.social.im.mvp.ui.adapters.members;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.enums.Gender;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.ui.acticity.tags.Tag;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.MemberInfo;
import com.wang.social.im.view.SwipeMenuLayout;
import com.wang.social.im.view.expand.viewholder.AbstractExpandableAdapterItem;

import java.util.Calendar;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:14
 * ============================================
 */
public class MemberItem extends AbstractExpandableAdapterItem {

    private ConstraintLayout imClContent;
    private ImageView ivPortrait;
    private TextView tvNickname;
    private TextView tvAge;
    private TextView tvConstellation;
    private TextView tvTags;
    private ImageView ivFriendly;
    private TextView tvbFriendly;
    private TextView tvbTakeOut;
    private SwipeMenuLayout menuLayout;

    ImageLoader imageLoader;
    Context context;

    private boolean isMaster;
    private MembersAdapter.OnHandleListener mHandleListener;


    public MemberItem(boolean isMaster, MembersAdapter.OnHandleListener mHandleListener) {
        this.mHandleListener = mHandleListener;
        this.isMaster = isMaster;
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.im_item_member;
    }

    @Override
    public void onBindViews(View root) {
        menuLayout = (SwipeMenuLayout) root;
        context = root.getContext();
        imageLoader = FrameUtils.obtainAppComponentFromContext(root.getContext()).imageLoader();

        ivPortrait = root.findViewById(R.id.if_iv_portrait);
        tvNickname = root.findViewById(R.id.if_tv_nickname);
        tvAge = root.findViewById(R.id.if_tv_age);
        tvConstellation = root.findViewById(R.id.if_tv_constellation);
        tvTags = root.findViewById(R.id.if_tv_tags);
        ivFriendly = root.findViewById(R.id.im_iv_friendly);
        tvbFriendly = root.findViewById(R.id.im_tvb_friendly);
        tvbTakeOut = root.findViewById(R.id.im_tvb_take_out);
        imClContent = root.findViewById(R.id.im_cl_content);
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
            tvAge.setText(member.getAgeRange());
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

            if (member.getMemberId().equals(String.valueOf(AppDataHelper.getUser().getUserId()))) {
                ivFriendly.setVisibility(View.GONE);
                tvbFriendly.setVisibility(View.GONE);
            } else if (member.isFriendly()) {
                ivFriendly.setVisibility(View.VISIBLE);
                tvbFriendly.setVisibility(View.GONE);
            } else {
                ivFriendly.setVisibility(View.GONE);
                tvbFriendly.setVisibility(View.VISIBLE);
            }

            if (isMaster) {
                tvbTakeOut.setVisibility(View.VISIBLE);
            } else {
                tvbTakeOut.setVisibility(View.GONE);
            }
            menuLayout.quickClose();
            tvbTakeOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuLayout.smoothClose();
                    if (mHandleListener != null) {
                        mHandleListener.onTakeOut(member, position);
                    }
                }
            });

            tvbFriendly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menuLayout.smoothClose();
                    if (mHandleListener != null) {
                        mHandleListener.onFriendly(member);
                    }
                }
            });
            imClContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHandleListener != null) {
                        mHandleListener.onItemClick(member, position);
                    }
                }
            });
        }
    }
}
