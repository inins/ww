package com.frame.component.view.bannerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.service.R;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.tmall.ultraviewpager.UltraViewPager;

import java.util.List;

/**
 * Created by liaoinstan on 2017/10/9
 */
public class BannerView extends FrameLayout {

    private Context context;
    private TextView text_banner_count;
    private UltraViewPager viewPager;
    private BannerAdapter adapter;
    private List<Image> images;

    //自定义属性
    private int selectedColor;
    private int unSelectedColor;
    private boolean isAutoScroll;
    private boolean numberIndicator;    //是否使用数字指示器，默认小圆点
    private Square square = Square.NONE;

    //是否显示为正方形：NONE：否 BY_WIDTH：由宽决定高 BY_HIGHT：由高决定宽
    public enum Square {
        NONE, BY_WIDTH, BY_HIGHT
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        if (attrs != null) {
            final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BannerView, 0, 0);
            selectedColor = ta.getColor(R.styleable.BannerView_selected_color, Color.rgb(255, 255, 255));
            unSelectedColor = ta.getColor(R.styleable.BannerView_unselected_color, Color.argb(33, 255, 255, 255));
            isAutoScroll = ta.getBoolean(R.styleable.BannerView_autoscroll, true);
            numberIndicator = ta.getBoolean(R.styleable.BannerView_number_indicator, false);
            square = Square.values()[ta.getInt(R.styleable.BannerView_is_square, 0)];
            ta.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        if (!isInEditMode())LayoutInflater.from(getContext()).inflate(R.layout.view_bannerview, this, true);
        super.onFinishInflate();
        if (!isInEditMode())initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        switch (square) {
            case NONE:
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                break;
            case BY_WIDTH:
                super.onMeasure(widthMeasureSpec, widthMeasureSpec);
                break;
            case BY_HIGHT:
                super.onMeasure(heightMeasureSpec, heightMeasureSpec);
                break;
        }
    }

    private void initView() {
        viewPager = findViewById(R.id.viewpager);
        text_banner_count = findViewById(R.id.text_banner_count);
        viewPager.getViewPager().setPageMargin(0);
        viewPager.setInfiniteLoop(true);
        adapter = new BannerAdapter();
        viewPager.setAdapter(adapter);
        if (!numberIndicator) {
            //小圆点模式
            viewPager.initIndicator();
            viewPager.getIndicator()
                    .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                    .setFocusColor(selectedColor)
                    .setNormalColor(unSelectedColor)
                    .setMargin(0, 0, 0, SizeUtils.dp2px(20))
                    .setGravity(Gravity.CENTER)
                    .setRadius(SizeUtils.dp2px(5));
            viewPager.getIndicator().setGravity(Gravity.RIGHT | Gravity.BOTTOM);
            viewPager.getIndicator().build();
            text_banner_count.setVisibility(GONE);
        } else {
            //数字模式
            text_banner_count.setVisibility(VISIBLE);
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCountIndicator();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private void initCtrl() {
        //如果允许滚动标志为true，并且数据条目>1则自动滚动，否则不滚动
        if (isAutoScroll && !StrUtil.isEmpty(images) && images.size() > 1) {
            viewPager.setAutoScroll(3000);
        } else {
            viewPager.disableAutoScroll();
        }
        adapter = new BannerAdapter();
        viewPager.setAdapter(adapter);
    }

    private void setCountIndicator() {
        if (numberIndicator) {
            int currentItem = viewPager.getCurrentItem();
            int count = viewPager.getAdapter().getCount();
            text_banner_count.setText(currentItem + 1 + "/" + count);
        }
    }

    private class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images != null ? images.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View root = LayoutInflater.from(container.getContext()).inflate(R.layout.lay_banner_item, null);
            ImageView imageView = (ImageView) root.findViewById(R.id.banner_img);

            //绑定网络图片
            if (onLoadImgListener != null) {
                onLoadImgListener.onloadImg(imageView, images.get(position).getImg(), R.drawable.default_rect);
            } else {
                ImageLoaderHelper.loadImg(imageView, images.get(position).getImg());
            }

            //点击事件
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerClickListener != null) {
                        onBannerClickListener.onBannerClick(position, images.get(position));
                    }
                }
            });

            //添加到容器
            container.addView(root);
            return root;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


    //#######################接口
    private OnBannerClickListener onBannerClickListener;

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public interface OnBannerClickListener {
        void onBannerClick(int position, Image image);
    }

    private OnLoadImgListener onLoadImgListener;

    public void setOnLoadImgListener(OnLoadImgListener onLoadImgListener) {
        this.onLoadImgListener = onLoadImgListener;
    }

    public interface OnLoadImgListener {
        void onloadImg(ImageView imageView, String imgurl, int defaultSrc);
    }

    //#######################对外方法
    public void setDatas(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
        setCountIndicator();
    }

    public void notifyDataSetChanged() {
        initCtrl();
    }

    public void showTitle(boolean needShow) {
    }
}
