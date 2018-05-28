package com.wang.social.moneytree.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.http.imageloader.ImageLoader;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.mvp.model.entities.GameEnd;
import com.wang.social.moneytree.mvp.model.entities.GameEndScore;

import java.util.List;

public class GameEndRankingAdapter extends RecyclerView.Adapter<GameEndRankingAdapter.ViewHolder> {

    private Context mContext;
    private List<GameEndScore> mList;

    public GameEndRankingAdapter(RecyclerView recyclerView, List<GameEndScore> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.mt_adapter_game_over_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.rootView.setVisibility(View.GONE);
        holder.rankingTV.setVisibility(View.GONE);
        holder.rankingIV.setVisibility(View.GONE);
        switch (position) {
            case 0:
                holder.rankingIV.setVisibility(View.VISIBLE);
                holder.rankingIV.setImageResource(R.drawable.mt_gameover_one);
                break;
            case 1:
                holder.rankingIV.setVisibility(View.VISIBLE);
                holder.rankingIV.setImageResource(R.drawable.mt_gameover_two);
                break;
            case 2:
                holder.rankingIV.setVisibility(View.VISIBLE);
                holder.rankingIV.setImageResource(R.drawable.mt_gameover_three);
                break;
            case 3:
                holder.rankingTV.setVisibility(View.VISIBLE);
                holder.rankingTV.setText("4.");
                break;
            case 4:
                holder.rankingTV.setVisibility(View.VISIBLE);
                holder.rankingTV.setText("5.");
                break;
        }
        holder.diamondIV.setVisibility(View.INVISIBLE);

        if (null == mList ) return;
        if (position < 0 || position >= mList.size()) return;
        GameEndScore score = mList.get(position);

        if (null == score) return;

        holder.rootView.setVisibility(View.VISIBLE);
        holder.diamondIV.setVisibility(View.VISIBLE);
        // 头像
        if (!TextUtils.isEmpty(score.getAvatar())) {
            ImageLoaderHelper.loadCircleImg(holder.avatarIV, score.getAvatar());
        } else {
            holder.avatarIV.setVisibility(View.INVISIBLE);
        }
        // 昵称
        holder.nameTV.setText(score.getNickname());
        // 钻石数
        holder.diamondTV.setText("+" + score.getGetDiamond());
    }

    @Override
    public int getItemCount() {
//        return null == mList ? 0 : Math.min(5, mList.size());
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView rankingIV;
        TextView rankingTV;
        ImageView avatarIV;
        TextView nameTV;
        TextView diamondTV;
        ImageView diamondIV;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            rankingIV = itemView.findViewById(R.id.ranking_image_view);
            rankingTV = itemView.findViewById(R.id.ranking_text_view);
            avatarIV = itemView.findViewById(R.id.avatar_image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            diamondTV = itemView.findViewById(R.id.diamond_text_view);
            diamondIV =itemView.findViewById(R.id.diamond_image_view);
        }
    }
}
