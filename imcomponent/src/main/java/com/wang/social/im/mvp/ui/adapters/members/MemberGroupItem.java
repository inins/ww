package com.wang.social.im.mvp.ui.adapters.members;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.utils.UIUtil;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.MembersLevelOne;
import com.wang.social.im.view.expand.viewholder.AbstractExpandableAdapterItem;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 20:14
 * ============================================
 */
public class MemberGroupItem extends AbstractExpandableAdapterItem {

    private TextView tvTitle, tvMemberCount;
    private ImageView ivArrow;
    private View vDivider;

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            start = 0f;
            target = 90f;
            vDivider.setVisibility(View.VISIBLE);
        } else {
            start = 90f;
            target = 0f;
            vDivider.setVisibility(View.GONE);
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivArrow, View.ROTATION, start, target);
        objectAnimator.setDuration(260);
        objectAnimator.start();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.im_item_members_group;
    }

    @Override
    public void onBindViews(View root) {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExpandOrUnexpand();
            }
        });

        tvTitle = root.findViewById(R.id.mg_tv_title);
        tvMemberCount = root.findViewById(R.id.mg_tv_member_count);
        ivArrow = root.findViewById(R.id.mg_iv_arrow);
        vDivider = root.findViewById(R.id.mg_bottom_divider);
    }

    @Override
    public void onSetViews() {
        ivArrow.setImageResource(0);
        ivArrow.setImageResource(R.drawable.common_ic_next_dark);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);

        onSetViews();
        onExpansionToggled(getExpandableListItem().isExpanded());

        if (model instanceof MembersLevelOne) {
            MembersLevelOne levelOne = (MembersLevelOne) model;
            tvTitle.setText(levelOne.getTitle());
            tvMemberCount.setText(UIUtil.getString(R.string.im_member_size_format, levelOne.getMembers().size()));
        }
    }
}
