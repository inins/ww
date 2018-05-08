package com.wang.social.home.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.view.ConerTextView;
import com.frame.utils.StrUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;

import butterknife.BindView;
import butterknife.OnClick;

public class RecycleAdapterHome extends BaseAdapter<Funpoint> {

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
            ((BaseViewHolder) holder).setData(valueList.get(position), position, onItemClickListener);
        } else if (holder instanceof HolderTopic) {
            ((BaseViewHolder) holder).setData(valueList.get(position), position, onItemClickListener);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 3 == 0) {
            return TYPE_SRC_TOPIC;
        } else {
            return TYPE_SRC_FUNPOINT;
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
//            ImageLoaderHelper.loadImg(imgPic, bean.getImgUrl());
//            FontUtils.boldText(textTitle);
//            textTitle.setText(bean.getNewsTitle());
//            cardPic.setVisibility(!TextUtils.isEmpty(bean.getImgUrl()) ? View.VISIBLE : View.GONE);
//            textNote.setText(bean.getNoteStr());
            ImageLoaderHelper.loadImgTest(imgPic);
        }

        @Override
        public void onRelease() {
        }
    }

    public class HolderTopic extends BaseViewHolder<Funpoint> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.conertext_tag)
        ConerTextView conertextTag;

        public HolderTopic(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Funpoint bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImgTest(imgHeader);
            ImageLoaderHelper.loadImgTest(imgPic);
            conertextTag.setTagText("一起考研,大学里,纪律生活");
        }

        @Override
        public void onRelease() {
        }
    }

    /////////////////////

    public void reFreshReadCountById(int newsId) {
        if (StrUtil.isEmpty(getData())) return;
        for (int i = 0; i < getData().size(); i++) {
            Funpoint funpoint = getData().get(i);
            if (funpoint.getNewsId() == newsId) {
                funpoint.setReadTotal(funpoint.getReadTotal() + 1);
                notifyItemChanged(i);
            }
        }
    }
}
