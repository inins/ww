package com.wang.social.im.mvp.ui.adapters.socials;

import android.animation.ObjectAnimator;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.enums.ConversationType;
import com.frame.component.helper.CommonHelper;
import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.SocialListLevelTwo;
import com.wang.social.im.view.expand.model.ExpandableListItem;
import com.wang.social.im.view.expand.viewholder.AbstractExpandableAdapterItem;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 10:39
 * ============================================
 */
public class LevelTwoItem extends AbstractExpandableAdapterItem {

    private ConstraintLayout clContent;
    private ImageView ivAvatar, ivArrow;
    private TextView tvName, tvMemberCount;
    private FrameLayout flExpand;

    ImageLoader mImageLoader;

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            start = 0f;
            target = 180f;
            if (getExpandableListItem().getChildItemList() != null && !getExpandableListItem().getChildItemList().isEmpty()) {
                clContent.setBackgroundResource(R.drawable.common_bg_white_top_corners_normal);
            }
        } else {
            start = 180f;
            target = 0f;
            clContent.setBackgroundResource(R.drawable.common_bg_white_corners_normal);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivArrow, View.ROTATION, start, target);
        objectAnimator.setDuration(260);
        objectAnimator.start();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.im_item_social_list_level_two;
    }

    @Override
    public void onBindViews(View root) {
        mImageLoader = FrameUtils.obtainAppComponentFromContext(root.getContext()).imageLoader();

        clContent = root.findViewById(R.id.llt_cl_content);
        ivAvatar = root.findViewById(R.id.llt_iv_avatar);
        ivArrow = root.findViewById(R.id.llt_arrow);
        tvName = root.findViewById(R.id.llt_tv_name);
        tvMemberCount = root.findViewById(R.id.llt_tv_member_count);
        flExpand = root.findViewById(R.id.llt_fl_expand);

        flExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExpandOrUnexpand();
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableListItem item = getExpandableListItem();
                if (item instanceof SocialListLevelTwo) {
                    SocialListLevelTwo levelTwo = (SocialListLevelTwo) item;
                    CommonHelper.ImHelper.gotoGroupConversation(root.getContext(), levelTwo.getId(), ConversationType.SOCIAL, false);
                }
            }
        });
    }

    @Override
    public void onSetViews() {
        ivArrow.setImageResource(0);
        ivArrow.setImageResource(R.drawable.im_ic_collapse);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        SocialListLevelTwo levelTwo = (SocialListLevelTwo) model;

        onSetViews();
        onExpansionToggled(levelTwo.isExpanded());

        mImageLoader.loadImage(ivAvatar.getContext(), ImageConfigImpl
                .builder()
                .placeholder(R.drawable.im_round_image_placeholder)
                .errorPic(R.drawable.im_round_image_placeholder)
                .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .imageView(ivAvatar)
                .url((levelTwo.getAvatar()))
                .build());
        tvName.setText(levelTwo.getName());
        tvMemberCount.setText(UIUtil.getString(R.string.im_member_count_format, levelTwo.getMemberCount()));
    }
}