package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.wang.social.im.R;
import com.wang.social.im.R2;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;

import butterknife.BindView;

/**
 * ============================================
 * {@link com.wang.social.im.mvp.ui.adapters.EnvelopAdoptInfoAdapter}红包领取详情头
 * <p>
 * Create by ChenJing on 2018-04-24 14:35
 * ============================================
 */
public class EnvelopAdoptHeaderViewHolder extends BaseViewHolder<EnvelopInfo> {

    @BindView(R2.id.edh_iv_header)
    ImageView edhIvHeader;
    @BindView(R2.id.edh_civ_head)
    ImageView edhCivHead;
    @BindView(R2.id.edh_fl_head)
    FrameLayout edhFlHead;
    @BindView(R2.id.edh_tv_name)
    TextView edhTvName;
    @BindView(R2.id.edh_tv_message)
    TextView edhTvMessage;
    @BindView(R2.id.edh_tv_diamond)
    TextView edhTvDiamond;
    @BindView(R2.id.edh_iv_diamond)
    ImageView edhIvDiamond;
    @BindView(R2.id.edh_tv_diamond_tip)
    TextView edhTvDiamondTip;
    @BindView(R2.id.edh_tv_adopt_info)
    TextView edhTvAdoptInfo;

    public EnvelopAdoptHeaderViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_header_envelop_adopt);
    }

    @Override
    protected void bindData(EnvelopInfo itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {

    }
}