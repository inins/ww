package com.wang.social.im.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.frame.component.utils.UIUtil;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.http.imageloader.glide.RoundedCornersTransformation;
import com.frame.utils.FrameUtils;
import com.frame.utils.SizeUtils;
import com.wang.social.im.R;

/**
 * ============================================
 * <p>
 * Create by ChenJing on 2018-05-16 15:48
 * ============================================
 */
public class TeamFunPointPopup extends PopupWindow {

    private String mCover, mSummary;

    private OnMoreClickListener mMoreClickListener;

    private ImageLoader mImageLoader;

    public TeamFunPointPopup(Context context, String cover, String summary, OnMoreClickListener moreClickListener) {
        super(context);
        this.mCover = cover;
        this.mSummary = summary;

        this.mMoreClickListener = moreClickListener;

        mImageLoader = FrameUtils.obtainAppComponentFromContext(context).imageLoader();

        init(context);
        initView(context);
    }

    private void init(Context context) {
        this.setWidth(SizeUtils.dp2px(238));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(0x00000000));
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.im_popup_fun_point, null, false);

        ImageView ivCover = view.findViewById(R.id.pfp_iv_cover);
        TextView tvSummary = view.findViewById(R.id.pfp_tv_summary);
        TextView tvbMore = view.findViewById(R.id.pfp_tvb_more);

        setContentView(view);

        mImageLoader.loadImage(context, ImageConfigImpl
                .builder()
                .placeholder(R.drawable.im_round_image_placeholder)
                .errorPic(R.drawable.im_round_image_placeholder)
                .transformation(new RoundedCornersTransformation(UIUtil.getDimen(R.dimen.im_round_image_radius), 0, RoundedCornersTransformation.CornerType.ALL))
                .imageView(ivCover)
                .url(mCover)
                .build());

        tvSummary.setText(mSummary);

        tvbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMoreClickListener != null) {
                    mMoreClickListener.onMoreClick();
                }
                dismiss();
            }
        });
    }

    public interface OnMoreClickListener {

        void onMoreClick();
    }
}
