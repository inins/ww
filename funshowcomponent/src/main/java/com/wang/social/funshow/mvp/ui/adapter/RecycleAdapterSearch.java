package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.funpoint.Funpoint;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;

import butterknife.BindView;

public class RecycleAdapterSearch extends BaseAdapter<Funshow> {

    private String tags;
    private String key;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_home);
    }

    public class Holder extends BaseViewHolder<Funshow> {

        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.img_pic)
        ImageView imgPic;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Funshow bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImgTest(imgHeader);
            ImageLoaderHelper.loadImgTest(imgPic);
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }


    /////////////////////

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
