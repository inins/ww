package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.utils.TimeUtils;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.FunpointAndTopic;
import com.wang.social.home.mvp.entities.Topic;

import java.util.Date;

import butterknife.BindView;

public class RecycleAdapterHome extends BaseAdapter<FunpointAndTopic> {

    private final static int TYPE_SRC_FUNPOINT = R.layout.home_item_funpoint;
    private final static int TYPE_SRC_TOPIC = R.layout.home_item_topic;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        if (viewType == TYPE_SRC_FUNPOINT) {
            return new HolderFunpoint(context, parent, viewType);
        } else if (viewType == TYPE_SRC_TOPIC) {
            return new HolderTopic(context, parent, viewType);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderFunpoint) {
            ((HolderFunpoint) holder).setData(valueList.get(position).getFunpoint(), position, onItemClickListener);
        } else if (holder instanceof HolderTopic) {
            ((HolderTopic) holder).setData(valueList.get(position).getTopic(), position, onItemClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (valueList.get(position).isFunpoint()) {
            return TYPE_SRC_FUNPOINT;
        } else {
            return TYPE_SRC_TOPIC;
        }
    }

    public class HolderFunpoint extends BaseViewHolder<Funpoint> {
        @BindView(R2.id.text_title)
        TextView textTitle;
        @BindView(R2.id.text_note)
        TextView textNote;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.card_pic)
        CardView cardPic;

        public HolderFunpoint(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Funpoint bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadImg(imgPic, bean.getImgUrl());
            FontUtils.boldText(textTitle);
            textTitle.setText(bean.getNewsTitle());
            cardPic.setVisibility(!TextUtils.isEmpty(bean.getImgUrl()) ? View.VISIBLE : View.GONE);
            textNote.setText(bean.getNoteStr());

            itemView.setOnClickListener(v -> {
                if (onFunpointClickListener != null) onFunpointClickListener.onFunpointClick(position, bean);
            });
        }

        @Override
        public void onRelease() {
        }
    }

    public class HolderTopic extends BaseViewHolder<Topic> {
        @BindView(R.id.img_flag)
        ImageView imgFlag;
        @BindView(R.id.conertext_tag)
        TextView conertextTag;
        @BindView(R.id.text_time)
        TextView textTime;
        @BindView(R.id.text_title)
        TextView textTitle;
        @BindView(R.id.text_content)
        TextView textContent;
        @BindView(R.id.img_header)
        ImageView imgHeader;
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.img_pic)
        ImageView imgPic;
        @BindView(R.id.text_zan)
        TextView textZan;
        @BindView(R.id.text_eva)
        TextView textEva;
        @BindView(R.id.text_watch)
        TextView textWatch;

        public HolderTopic(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Topic bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getUserCover());
            ImageLoaderHelper.loadImgTest(imgPic);
            ImageLoaderHelper.loadImg(imgPic, bean.getTopicImage());
            textTitle.setText(bean.getTitle());
            textContent.setText(bean.getFirstStrff());
            textName.setText(bean.getUserName());
            textZan.setText(String.valueOf(bean.getTopicSupportNum()));
            textEva.setText(String.valueOf(bean.getTopicCommentNum()));
            textWatch.setText(String.valueOf(bean.getTopicReadNum()));
            textTime.setText(TimeUtils.date2String(bean.getCreateTime(), "MM-dd"));
            conertextTag.setText(bean.getTagStr());
            imgFlag.setVisibility(bean.isFree() ? View.GONE : View.VISIBLE);

            itemView.setOnClickListener(v -> {
                if (onTopicClickListener != null) onTopicClickListener.onTopicClick(position, bean);
            });
        }

        @Override
        public void onRelease() {
        }
    }

    //////////////////////

    private OnFunpointClickListener onFunpointClickListener;
    private OnTopicClickListener onTopicClickListener;

    public void setOnFunpointClickListener(OnFunpointClickListener onFunpointClickListener) {
        this.onFunpointClickListener = onFunpointClickListener;
    }

    public void setOnTopicClickListener(OnTopicClickListener onTopicClickListener) {
        this.onTopicClickListener = onTopicClickListener;
    }

    public interface OnFunpointClickListener {
        void onFunpointClick(int position, Funpoint funpoint);
    }

    public interface OnTopicClickListener {
        void onTopicClick(int position, Topic topic);
    }

    /////////////////////

//    public void reFreshReadCountById(int newsId) {
//        if (StrUtil.isEmpty(getData())) return;
//        for (int i = 0; i < getData().size(); i++) {
//            Funpoint funpoint = getData().get(i);
//            if (funpoint.getNewsId() == newsId) {
//                funpoint.setReadTotal(funpoint.getReadTotal() + 1);
//                notifyItemChanged(i);
//            }
//        }
//    }
}
