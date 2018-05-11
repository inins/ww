package com.wang.social.im.mvp.ui.adapters.socials;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.TeamInfo;
import com.wang.social.im.mvp.ui.adapters.SocialListTeamAdapter;
import com.wang.social.im.view.expand.viewholder.AbstractExpandableAdapterItem;

import java.util.List;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-09 10:39
 * ============================================
 */
public class TeamsItem extends AbstractExpandableAdapterItem {

    private RecyclerView rlvTeams;

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.im_item_social_list_teams;
    }

    @Override
    public void onBindViews(View root) {
        rlvTeams = root.findViewById(R.id.slt_rlv_teams);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        if (model instanceof List) {
            List<TeamInfo> teams = (List<TeamInfo>) model;
            rlvTeams.setLayoutManager(new LinearLayoutManager(rlvTeams.getContext(), LinearLayoutManager.HORIZONTAL, false));
//            rlvTeams.addItemDecoration(new RecyclerView.ItemDecoration() {
//                @Override
//                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                    super.getItemOffsets(outRect, view, parent, state);
//                    outRect.left = SizeUtils.dp2px(8);
//                    outRect.right = SizeUtils.dp2px(8);
//                }
//            });
            rlvTeams.setAdapter(new SocialListTeamAdapter(teams));
        }
    }
}
