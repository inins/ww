package com.wang.social.moneytree.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.moneytree.R;
import com.wang.social.moneytree.mvp.model.entities.GameBean;
import com.wang.social.moneytree.mvp.model.entities.GameRecord;

import java.util.List;

public class GameRecordListAdapter extends RecyclerView.Adapter<GameRecordListAdapter.ViewHolder> {

    public interface ClickListener {
        void onEnter(GameBean gameBean);
    }


    private Context mContext;
    private List<GameRecord> mList;
    private ClickListener mClickListener;

    public GameRecordListAdapter(RecyclerView recyclerView, List<GameRecord> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.mt_adapter_game_record_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (0 < position || position >= mList.size()) return;

        GameRecord game = mList.get(position);

        ImageLoaderHelper.loadImg(holder.avatarIV, game.getRoomAvatar());
        holder.nameTV.setText(game.getRoomNickname());
        holder.infoTV.setText(game.getPeopleNum());
        holder.priceTV.setText(game.getDiamond());
        holder.rightTV.setText(game.getGotDiamond());

        holder.typeIV.setVisibility(View.VISIBLE);
        // type:(0:未结束；1:赢；2:输；3:平；4:游戏失败)
        if (game.getType() == GameRecord.TYPE_PLAYING) {

        } else if (game.getType() == GameRecord.TYPE_WIN) {
        } else if (game.getType() == GameRecord.TYPE_LOSE) {

        } else if (game.getType() == GameRecord.TYPE_TIE) {

        } else if (game.getType() == GameRecord.TYPE_FAILED) {

        }
    }

    @Override
    public int getItemCount() {
        return null == mList ?  0 :  mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarIV;
        TextView nameTV;
        TextView priceTV;
        TextView infoTV;
        TextView rightTV;
        ImageView typeIV;

        public ViewHolder(View itemView) {
            super(itemView);

            avatarIV = itemView.findViewById(R.id.avatar_image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            priceTV = itemView.findViewById(R.id.price_text_view);
            infoTV = itemView.findViewById(R.id.info_text_view);
            rightTV = itemView.findViewById(R.id.right_text_view);
            typeIV = itemView.findViewById(R.id.type_icon_image_view);
        }
    }
}
