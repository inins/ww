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
import com.wang.social.im.mvp.model.entities.notify.FriendRequest;
import com.wang.social.im.mvp.model.entities.notify.RequestBean;

import butterknife.BindView;

public class RecycleAdapterFriendRequest extends BaseAdapter<RequestBean> {

    private boolean isGroup;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.im_item_friend_request);
    }

    public class Holder extends BaseViewHolder<RequestBean> {
        @BindView(R2.id.img_header)
        ImageView imgHeader;
        @BindView(R2.id.text_lable_gender)
        TextView textLableGender;
        @BindView(R2.id.text_lable_astro)
        TextView textLableAstro;
        @BindView(R2.id.text_name)
        TextView textName;
        @BindView(R2.id.text_tag)
        TextView textTag;
        @BindView(R2.id.text_do)
        TextView textDo;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
        }

        @Override
        protected void bindData(RequestBean bean, int position, OnItemClickListener onItemClickListener) {
            ImageLoaderHelper.loadCircleImg(imgHeader, bean.getAvatar());
            textName.setText(bean.getNickname());
            textLableGender.setSelected(!bean.isMale());
            textLableGender.setText(TimeUtils.getBirthdaySpan(bean.getBirthday()));
            textLableAstro.setText(TimeUtils.getAstro(bean.getBirthday()));
            textTag.setText(getContext().getResources().getString(isGroup ? R.string.im_notify_findchat_request_reason : R.string.im_notify_friend_request_reason) + bean.getReason());
            textDo.setEnabled(!bean.isDeal());
            textDo.setText(bean.getStatusText());
            textDo.setOnClickListener(v -> {
                if (onAgreeClickListener != null) onAgreeClickListener.onAgreeClick(bean, position);
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

    public void setGroup(boolean group) {
        isGroup = group;
    }

    /////////////////////////////

    private OnAgreeClickListener onAgreeClickListener;

    public void setOnAgreeClickListener(OnAgreeClickListener onAgreeClickListener) {
        this.onAgreeClickListener = onAgreeClickListener;
    }

    public interface OnAgreeClickListener {
        void onAgreeClick(RequestBean bean, int position);
    }
}
