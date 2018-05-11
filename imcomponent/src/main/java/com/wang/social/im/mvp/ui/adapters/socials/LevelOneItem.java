package com.wang.social.im.mvp.ui.adapters.socials;

import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.SocialListLevelOne;
import com.wang.social.im.view.expand.viewholder.AbstractExpandableAdapterItem;

import java.util.Locale;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 10:29
 * ============================================
 */
public class LevelOneItem extends AbstractExpandableAdapterItem {

    private ImageView ivArrow;
    private TextView tvTitle, tvChildSize;

    @Override
    public void onExpansionToggled(boolean expanded) {
        float start, target;
        if (expanded) {
            start = 0f;
            target = 90f;
        } else {
            start = 90f;
            target = 0f;
        }
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivArrow, View.ROTATION, start, target);
        objectAnimator.setDuration(260);
        objectAnimator.start();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.im_item_social_list_level_one;
    }

    @Override
    public void onBindViews(View root) {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doExpandOrUnexpand();
            }
        });

        ivArrow = root.findViewById(R.id.llo_arrow);
        tvTitle = root.findViewById(R.id.llo_title);
        tvChildSize = root.findViewById(R.id.llo_child_count);
    }

    @Override
    public void onSetViews() {
        ivArrow.setImageResource(0);
        ivArrow.setImageResource(R.drawable.im_ic_level_one_collapse);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        SocialListLevelOne levelOne = (SocialListLevelOne) model;

        onSetViews();
        onExpansionToggled(levelOne.isExpanded());

        tvTitle.setText(levelOne.getTitle());
        tvChildSize.setText(String.format(Locale.getDefault(), "(%d)", levelOne.getChildItemList().size()));
    }
}
