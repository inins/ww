package com.wang.social.moneytree.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.component.utils.SpannableStringUtil;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.mvp.model.entities.GameScore;

import java.util.List;

public class GameScoreAdapter extends RecyclerView.Adapter<GameScoreAdapter.ViewHolder> {

    private Context mContext;
    private List<GameScore> mList;

    public GameScoreAdapter(RecyclerView recyclerView, List<GameScore> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.mt_adapter_game_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) return;

        GameScore score = mList.get(position);

        String nickname = score.getNickname();
        int nicknameColor = Color.parseColor("#CE4D12");

        if (score.isMyself()) {
            nickname = mContext.getString(R.string.mt_me);
            nicknameColor = Color.parseColor("#D39F1B");
        }

        String[] strings = {
                nickname,
                String.format(mContext.getString(R.string.mt_format_game_score), score.getGotDiamond())
        };
        int[] colors = {
                nicknameColor,
                Color.parseColor("#434343")};
        SpannableStringBuilder text = SpannableStringUtil.createV2(strings, colors);

        holder.tv.setText(text);
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

            tv = itemView.findViewById(R.id.text_view);
        }
    }
}
