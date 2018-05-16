package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.utils.TimeUtils;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.notify.GroupJoinRequest;
import com.wang.social.im.mvp.model.entities.notify.RequestBean;

import butterknife.BindView;

public class RecycleAdapterGroupRequest extends BaseAdapter<GroupJoinRequest> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.im_item_groupjoin_request);
    }

    public class Holder extends BaseViewHolder<GroupJoinRequest> {
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
        protected void bindData(GroupJoinRequest bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getHeadUrl());
            textName.setText(bean.getGroupName());
            textCount.setText(bean.getMemberNum()+"人");
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

    private OnJoinClickListener onJoinClickListener;

    public void setOnJoinClickListener(OnJoinClickListener onJoinClickListener) {
        this.onJoinClickListener = onJoinClickListener;
    }

    public interface OnJoinClickListener {
        void onJoinClick(GroupJoinRequest bean, int position);
    }
}
