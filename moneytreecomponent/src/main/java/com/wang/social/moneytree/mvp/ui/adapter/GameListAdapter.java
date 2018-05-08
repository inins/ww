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

import java.util.List;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {

    public interface ClickListener {
        void onEnter(GameBean gameBean);
    }


    private Context mContext;
    private List<GameBean> mList;
    private ClickListener mClickListener;

    public GameListAdapter(RecyclerView recyclerView, List<GameBean> list) {
        mContext = recyclerView.getContext().getApplicationContext();
        mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.mt_adapter_game_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (0 < position || position >= mList.size()) return;

        GameBean game = mList.get(position);

        ImageLoaderHelper.loadImg(holder.avatarIV, game.getRoomAvatar());
        holder.nameTV.setText(game.getRoomNickname());
        holder.infoTV.setText(String.format(
                mContext.getString(R.string.mt_format_number_of_person),
                game.getJoinNum(), game.getPeopleNum()));
        holder.priceTV.setText(game.getDiamond());

        holder.rightTV.setTag(game);
        holder.rightTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickListener && v.getTag() instanceof GameBean) {
                    mClickListener.onEnter((GameBean) v.getTag());
                }
            }
        });
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

        public ViewHolder(View itemView) {
            super(itemView);

            avatarIV = itemView.findViewById(R.id.avatar_image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            priceTV = itemView.findViewById(R.id.price_text_view);
            infoTV = itemView.findViewById(R.id.info_text_view);
            rightTV = itemView.findViewById(R.id.right_text_view);
        }
    }
}
