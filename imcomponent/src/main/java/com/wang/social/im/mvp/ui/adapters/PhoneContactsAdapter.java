package com.wang.social.im.mvp.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frame.component.utils.UIUtil;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.PhoneContact;
import com.wang.social.im.view.indexlist.IndexableAdapter;

import timber.log.Timber;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-08 16:31
 * ============================================
 */
public class PhoneContactsAdapter extends IndexableAdapter<PhoneContact> {

    private LayoutInflater mInflater;
    private OnHandleListener mHandleListener;

    public PhoneContactsAdapter(Context context, OnHandleListener handleListener) {
        this.mInflater = LayoutInflater.from(context);
        this.mHandleListener = handleListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_index_friends, parent, false);
        return new IndexViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.im_item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle, boolean isSticky) {
        ((IndexViewHolder) holder).tvIndex.setText(indexTitle);
        if (isSticky) {
            ((IndexViewHolder) holder).tvIndex.setTextColor(ContextCompat.getColor(((IndexViewHolder) holder).tvIndex.getContext(), R.color.common_colorAccent));
        } else {
            ((IndexViewHolder) holder).tvIndex.setTextColor(ContextCompat.getColor(((IndexViewHolder) holder).tvIndex.getContext(), R.color.common_text_blank));
        }
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, PhoneContact entity) {
        ContactViewHolder viewHolder = (ContactViewHolder) holder;
        if (!TextUtils.isEmpty(entity.getName())) {
            viewHolder.tvPortrait.setText(String.valueOf(entity.getName().charAt(0)));
            viewHolder.tvName.setText(entity.getName());
        }
        if (!TextUtils.isEmpty(entity.getPhoneNumber())) {
            viewHolder.tvPhoneNumber.setText(entity.getPhoneNumber());
        }
        if (entity.isFriend()) {
            viewHolder.tvAdded.setVisibility(View.VISIBLE);
            viewHolder.tvUnjoin.setVisibility(View.GONE);
            viewHolder.tvHandle.setVisibility(View.GONE);
        } else if (entity.isJoined()) {
            viewHolder.tvAdded.setVisibility(View.GONE);
            viewHolder.tvHandle.setVisibility(View.VISIBLE);
            viewHolder.tvUnjoin.setVisibility(View.GONE);
            viewHolder.tvHandle.setText("添加");
        } else {
            viewHolder.tvAdded.setVisibility(View.GONE);
            viewHolder.tvHandle.setVisibility(View.VISIBLE);
            viewHolder.tvUnjoin.setVisibility(View.VISIBLE);
            viewHolder.tvHandle.setText("邀请");
        }

        viewHolder.tvHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandleListener != null) {
                    mHandleListener.onHandle(entity);
                }
            }
        });
    }

    private class IndexViewHolder extends RecyclerView.ViewHolder {

        private TextView tvIndex;

        public IndexViewHolder(View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
        }
    }

    private class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView tvPortrait, tvName, tvPhoneNumber, tvHandle, tvUnjoin, tvAdded;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvPortrait = itemView.findViewById(R.id.ic_tv_portrait);
            tvName = itemView.findViewById(R.id.ic_tv_name);
            tvPhoneNumber = itemView.findViewById(R.id.ic_tv_phone_number);
            tvHandle = itemView.findViewById(R.id.ic_tvb_handle);
            tvUnjoin = itemView.findViewById(R.id.ic_tv_unjoin);
            tvAdded = itemView.findViewById(R.id.ic_tv_added);
        }
    }

    public interface OnHandleListener {

        void onHandle(PhoneContact contact);
    }
}
