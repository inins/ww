package com.wang.social.location.mvp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.location.R;
import com.wang.social.location.R2;
import com.wang.social.location.mvp.model.entities.LocationInfo;

import butterknife.BindView;
import lombok.Setter;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-04-26 11:31
 * ============================================
 */
public class LocationAdapter extends BaseAdapter<LocationInfo> {

    @Setter
    private int selectedPosition;

    public LocationInfo getSelectLocation() {
        if (valueList.size() > 0) {
            return valueList.get(selectedPosition);
        }
        return null;
    }

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new ViewHolder(context, parent);
    }

    class ViewHolder extends BaseViewHolder<LocationInfo> {

        @BindView(R2.id.il_iv_selected)
        ImageView ilIvSelected;
        @BindView(R2.id.il_tv_place)
        TextView ilTvPlace;
        @BindView(R2.id.il_tv_address)
        TextView ilTvAddress;

        public ViewHolder(Context context, ViewGroup root) {
            super(context, root, R.layout.loc_item_location);
        }

        @Override
        protected void bindData(LocationInfo itemValue, int position, OnItemClickListener onItemClickListener) {
            if (selectedPosition == position) {
                ilIvSelected.setVisibility(View.VISIBLE);
            } else {
                ilIvSelected.setVisibility(View.GONE);
            }
            ilTvPlace.setText(itemValue.getPlace());
            ilTvAddress.setText(itemValue.getAddress());
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }
}
