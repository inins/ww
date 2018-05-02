package com.wang.social.im.mvp.ui.adapters.holders;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.utils.FrameUtils;
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
    @BindView(R2.id.edh_view_line)
    View edhLine;

    private ImageLoader mImageLoader;

    public EnvelopAdoptHeaderViewHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.im_header_envelop_adopt);
        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();
    }

    @Override
    protected void bindData(EnvelopInfo itemValue, int position, BaseAdapter.OnItemClickListener onItemClickListener) {
        mImageLoader.loadImage(getContext(), ImageConfigImpl
                .builder()
                .isCircle(true)
                .imageView(edhCivHead)
                .url(itemValue.getFromPortrait())
                .placeholder(R.drawable.common_default_circle_placeholder)
                .errorPic(R.drawable.common_default_circle_placeholder)
                .build());

        edhTvName.setText(itemValue.getFromNickname());

        edhTvMessage.setText(itemValue.getMessage());

        if (itemValue.getGotDiamond() > 0) {
            edhTvDiamond.setVisibility(View.VISIBLE);
            edhIvDiamond.setVisibility(View.VISIBLE);
            edhTvDiamondTip.setVisibility(View.VISIBLE);
            edhTvDiamond.setText(String.valueOf(itemValue.getGotDiamond()));
        }
        //个人红包或等额红包领取方不显示红包领取信息
        if ((itemValue.getType() == EnvelopInfo.EnvelopType.PRIVATE && !itemValue.isSelf()) ||
                (itemValue.getType() == EnvelopInfo.EnvelopType.EQUAL && !itemValue.isSelf())) {
            edhTvAdoptInfo.setVisibility(View.GONE);
            edhLine.setVisibility(View.GONE);
        } else {
            String adoptInfo = "";
            if (itemValue.getType() == EnvelopInfo.EnvelopType.EQUAL) {
                adoptInfo = UIUtil.getString(R.string.im_envelop_equal_info, itemValue.getCount(), itemValue.getDiamond());
            } else {
                if (itemValue.getStatus() == EnvelopInfo.Status.LIVING) {
                    adoptInfo = UIUtil.getString(R.string.im_envelop_adopt_format, itemValue.getCount() - itemValue.getLastCount(), itemValue.getCount(),
                            itemValue.getDiamond() - itemValue.getLastDiamond(), itemValue.getDiamond());
                } else if (itemValue.getStatus() == EnvelopInfo.Status.EMPTY) {
                    if (itemValue.getType() == EnvelopInfo.EnvelopType.PRIVATE){
                        adoptInfo = UIUtil.getString(R.string.im_envelop_equal_info, itemValue.getCount(), itemValue.getDiamond());
                    }else {
                        //计算被抢光时间
                        String time;
                        if (itemValue.getSpendTime() >= 3600) {
                            time = itemValue.getSpendTime() / 3600 + "小时";
                        } else if (itemValue.getSpendTime() >= 60) {
                            time = itemValue.getSpendTime() / 60 + "分钟";
                        } else {
                            time = itemValue.getSpendTime() + "秒";
                        }
                        adoptInfo = UIUtil.getString(R.string.im_envelop_over_format, itemValue.getCount(), itemValue.getDiamond(), time);
                    }
                } else if (itemValue.getStatus() == EnvelopInfo.Status.OVERDUE) {
                    adoptInfo = UIUtil.getString(R.string.im_envelop_adopt_overdue_format, itemValue.getCount() - itemValue.getLastCount(), itemValue.getCount(),
                            itemValue.getDiamond() - itemValue.getLastDiamond(), itemValue.getDiamond());
                }
            }
            edhTvAdoptInfo.setVisibility(View.VISIBLE);
            edhTvAdoptInfo.setText(adoptInfo);
        }
    }

    @Override
    public void onRelease() {
        super.onRelease();
        mImageLoader.clear(getContext(), ImageConfigImpl.builder()
                .imageView(edhIvHeader)
                .build());
    }
}