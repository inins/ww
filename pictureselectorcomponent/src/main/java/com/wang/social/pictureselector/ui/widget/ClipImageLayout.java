package com.wang.social.pictureselector.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * Created by Martin on 2017/1/17.
 */
public class ClipImageLayout extends RelativeLayout {
  private ClipZoomImageView zoomImageView;
  private ClipImageBorderView clipImageView;
  private int horizontalPadding = 50;

  public ClipImageLayout(Context context, AttributeSet attrs) {
    super(context, attrs);

    zoomImageView = new ClipZoomImageView(context);
    clipImageView = new ClipImageBorderView(context);

    android.view.ViewGroup.LayoutParams lp = new LayoutParams(
        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
        android.view.ViewGroup.LayoutParams.MATCH_PARENT);

    this.addView(zoomImageView, lp);
    this.addView(clipImageView, lp);

    horizontalPadding = (int) TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, horizontalPadding, getResources()
            .getDisplayMetrics());
    zoomImageView.setHorizontalPadding(horizontalPadding);
    clipImageView.setHorizontalPadding(horizontalPadding);
  }

  public ClipZoomImageView getZoomImageView() {
    return zoomImageView;
  }

  public String clip() {
    return zoomImageView.clip();
  }
}
