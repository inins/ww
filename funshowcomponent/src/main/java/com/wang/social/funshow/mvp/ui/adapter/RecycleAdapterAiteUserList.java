package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.TestEntity;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.SelectHelper;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.user.Friend;

import java.util.List;

import butterknife.BindView;

public class RecycleAdapterAiteUserList extends BaseAdapter<Friend> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_aiteuser);
    }

    public class Holder extends BaseViewHolder<Friend> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.img_check)
        ImageView imgCheck;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(Friend bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImgTest(imgHeader);
            itemView.setOnClickListener(v -> {
                bean.setSelect(!bean.isSelect());
                notifyItemChanged(position);
            });
            imgCheck.setSelected(bean.isSelect());
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickName());
        }

        @Override
        public void onRelease() {
        }
    }

    public List<Friend> getSelectItems() {
        return SelectHelper.getSelectBeans(getData());
    }
}
