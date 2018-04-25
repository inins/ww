package com.wang.social.topic.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
import com.wang.social.topic.R;
import com.wang.social.topic.mvp.model.entities.Music;

import java.util.List;

public class BGMListAdapter extends RecyclerView.Adapter<BGMListAdapter.ViewHolder> {

    private Context mContext;
    private List<Music> mList;

    public interface StateProvider {
        boolean isPlaying(Music music);
        boolean isSelected(Music music);
    }

    /**
     * 点击监听
     */
    public interface ClickListener {
        void onControl(Music music);
        void onSelect(Music music);
    }


    private ClickListener mClickListener;
    private StateProvider mStateProvider;

    public BGMListAdapter(RecyclerView recyclerView, List<Music> list) {
        this.mContext = recyclerView.getContext().getApplicationContext();
        this.mList = list;
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public void setStateProvider(StateProvider stateProvider) {
        mStateProvider = stateProvider;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.topic_adapter_bgm_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null == mList) return;
        if (position < 0 || position >= mList.size()) {
            throw new IndexOutOfBoundsException("话题背景音乐列表错误");
        }

        Music music = mList.get(position);

        if (music.getMusicId() == -1) {
            holder.controlIV.setVisibility(View.INVISIBLE);
        } else {
            holder.controlIV.setVisibility(View.VISIBLE);

            // 播放状态
            if (null != mStateProvider && mStateProvider.isPlaying(music)) {
                holder.controlIV.setImageResource(R.drawable.common_ic_music_stop_big);
            } else {
                holder.controlIV.setImageResource(R.drawable.common_ic_music_start_big);
            }
        }

        // 名字
        holder.nameTV.setText(music.getMusicName());

        // 选中状态
        if (null != mStateProvider && mStateProvider.isSelected(music)) {
            holder.selectedIV.setVisibility(View.VISIBLE);
        } else {
            holder.selectedIV.setVisibility(View.INVISIBLE);
        }

        // 选择
        holder.rootView.setTag(music);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickListener && v.getTag() instanceof Music) {
                    mClickListener.onSelect((Music) v.getTag());
                }
            }
        });

        // 播放控制
        holder.controlIV.setTag(music);
        holder.controlIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mClickListener && v.getTag() instanceof Music) {
                    mClickListener.onControl(music);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == mList ? 0 : mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView controlIV;
        TextView nameTV;
        ImageView selectedIV;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = itemView.findViewById(R.id.root_view);
            controlIV = itemView.findViewById(R.id.control_image_view);
            nameTV = itemView.findViewById(R.id.name_text_view);
            selectedIV = itemView.findViewById(R.id.selected_image_view);
        }
    }
}
