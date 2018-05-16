package com.wang.social.im.mvp.ui.adapters.socials;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.frame.base.BaseAdapter;
import com.frame.component.enums.ConversationType;
import com.frame.component.helper.CommonHelper;
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
            SocialListTeamAdapter adapter = new SocialListTeamAdapter(teams);
            rlvTeams.setAdapter(adapter);

            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<TeamInfo>() {
                @Override
                public void onItemClick(TeamInfo teamInfo, int position) {
                    CommonHelper.ImHelper.gotoGroupConversation(rlvTeams.getContext(), teamInfo.getTeamId(), ConversationType.TEAM);
                }
            });
        }
    }
}
