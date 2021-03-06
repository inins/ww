package com.frame.component.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetReadHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.acticity.WebActivity;
import com.frame.component.utils.viewutils.FontUtils;
import com.frame.utils.StrUtil;
import com.frame.component.entities.funpoint.Funpoint;

import butterknife.BindView;

public class RecycleAdapterFunpoint extends BaseAdapter<Funpoint> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.lay_item_funpoint);
    }

    public class Holder extends BaseViewHolder<Funpoint> {
        @BindView(R2.id.text_title)
        TextView textTitle;
        @BindView(R2.id.text_note)
        TextView textNote;
        @BindView(R2.id.img_pic)
        ImageView imgPic;
        @BindView(R2.id.card_pic)
        CardView cardPic;

        public Holder(Context context, ViewGroup root, int layoutRes) {
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
                NetReadHelper.newInstance().netReadFunpoint(bean.getNewsId(), () -> {
                    reFreshReadCountById(bean.getNewsId());
                });
                WebActivity.start(getContext(), bean.getUrl());
            });
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
