package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.notify.GroupRequest;

import butterknife.BindView;

public class RecycleAdapterGroupRequest extends BaseAdapter<GroupRequest> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.im_item_groupjoin_request);
    }

    public class Holder extends BaseViewHolder<GroupRequest> {
        @BindView(R2.id.img_dot)
        ImageView imgDot;
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_count)
        TextView textCount;
        @BindView(R2.id.text_type)
        TextView textType;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_note)
        TextView textNote;
        @BindView(R2.id.text_do)
        TextView textDo;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(GroupRequest bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getHeadUrl());
            textName.setText(bean.getGroupName());
            textCount.setText(bean.getMemberNum()+"人");
            imgDot.setVisibility(bean.isRead() ? View.GONE : View.VISIBLE);
            textDo.setEnabled(!bean.isAgree());
            textDo.setText(bean.isAgree() ? R.string.im_notify_friend_request_agreed : R.string.im_notify_friend_request_agree);
            textDo.setOnClickListener(v -> {
                if (onJoinClickListener != null) onJoinClickListener.onJoinClick(bean, position);
            });
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

    /////////////////////////////

    private OnAgreeGroupClickListener onJoinClickListener;

    public void setOnJoinClickListener(OnAgreeGroupClickListener onJoinClickListener) {
        this.onJoinClickListener = onJoinClickListener;
    }

    public interface OnAgreeGroupClickListener {
        void onJoinClick(GroupRequest bean, int position);
    }
}
