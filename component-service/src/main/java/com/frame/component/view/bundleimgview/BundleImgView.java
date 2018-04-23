package com.frame.component.view.bundleimgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.component.common.DragItemTouchCallback;
import com.frame.component.common.OnRecyclerItemClickListener;
import com.frame.component.service.R;
import com.frame.component.utils.VibratorUtil;
import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.frame.component.common.GridSpacingItemDecoration;
import com.frame.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaoinstan on 2016/6/12 0012.
 */
public class BundleImgView extends FrameLayout {

    private ViewGroup root;
    private Context context;

    private RecyclerView recyclerView;
    private RecycleAdapterBundleImg adapter;
    private List<BundleImgEntity> results = new ArrayList<>();

    private GridLayoutManager layoutManager;
    private GridSpacingItemDecoration itemDecoration;

    //一行多少个item数量
    private int colcount;
    private boolean dragble;
    private boolean editble;
    //图片间距
    private int spaceWidth;
    //图片圆角
    private int corner;
    //图片宽高比例
    private float wihi;

    public BundleImgView(@NonNull Context context) {
        this(context, null);
    }

    public BundleImgView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BundleImgView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        // 初始化各项组件
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BundleImgView);
        colcount = a.getInteger(R.styleable.BundleImgView_colcount, 4);
        editble = a.getBoolean(R.styleable.BundleImgView_editble, true);
        spaceWidth = a.getDimensionPixelOffset(R.styleable.BundleImgView_space_width, dp2px(getContext(), 10));
        corner = a.getDimensionPixelOffset(R.styleable.BundleImgView_corner, dp2px(getContext(), 4));
        wihi = a.getFloat(R.styleable.BundleImgView_wihi, 1f);
        dragble = a.getBoolean(R.styleable.BundleImgView_dragble, false);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.view_bundleview, this, true);
        initBase();
        initView();
        initCtrl();
    }

    private void initBase() {
    }

    private void initView() {
        recyclerView = (RecyclerView) root.findViewById(R.id.recycle_bundle);
    }

    private void initCtrl() {
        if (isInEditMode()) {
            results.add(new BundleImgEntity());
            results.add(new BundleImgEntity());
        }

        adapter = new RecycleAdapterBundleImg(results);
        adapter.setEditble(editble);
        adapter.setCorner(corner);
        adapter.setWihi(wihi);
        layoutManager = new GridLayoutManager(context, colcount);
        itemDecoration = new GridSpacingItemDecoration(colcount, spaceWidth, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new DragItemTouchCallback(adapter).setOnDragListener(null));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (dragble) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.vibrate(context, 70);   //震动70ms
                }
            }
        });
    }

    //############### 对外方法 ################

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }

    public void setOnBundleClickListener(OnBundleClickListener bundleClickListener) {
        adapter.setBundleClickListener(bundleClickListener);
    }

    public interface OnBundleClickListener {
        void onPhotoDelClick(View v, int position);

        void onPhotoShowClick(String path, int position);

        void onPhotoAddClick(View v);
    }

    public static class OnBundleSimpleClickListener implements OnBundleClickListener {

        @Override
        public void onPhotoDelClick(View v, int position) {
        }

        @Override
        public void onPhotoShowClick(String path, int position) {
        }

        @Override
        public void onPhotoAddClick(View v) {
        }
    }

    //设置是否可拓展
    public void setDragble(boolean dragble) {
        this.dragble = dragble;
    }

    //获取结果集
    public List<BundleImgEntity> getResults() {
        return results;
    }

    //获取结果集(string 集合)
    public List<String> getResultsStrArray() {
        List<String> strResults = new ArrayList<>();
        if (!StrUtil.isEmpty(results)) {
            for (BundleImgEntity entity : results) {
                strResults.add(entity.getPath());
            }
        }
        return strResults;
    }

    //获取结果集数量
    public int getResultCount() {
        if (results == null) return 0;
        return results.size();
    }

    public void setEditble(boolean editble) {
        this.editble = editble;
        adapter.setEditble(editble);
    }

    public void addPhoto(BundleImgEntity bundle) {
        adapter.getResults().add(bundle);
        adapter.notifyItemInserted(adapter.getResults().size());
        adapter.notifyItemChanged(adapter.getItemCount() - 1);
    }

    public void addPhotos(List<BundleImgEntity> bundles) {
        adapter.getResults().addAll(bundles);
        adapter.notifyItemRangeInserted(adapter.getResults().size() - bundles.size(), bundles.size());
        adapter.notifyItemChanged(adapter.getItemCount() - 1);
    }

    public void clear() {
        adapter.getResults().clear();
    }

    public void setPhotos(List<BundleImgEntity> results) {
        adapter.getResults().clear();
        if (!StrUtil.isEmpty(results)) adapter.getResults().addAll(results);
        adapter.notifyDataSetChanged();
    }

    public void setOnBundleLoadImgListener(OnBundleLoadImgListener onBundleLoadImgListener) {
        adapter.setOnBundleLoadImgListener(onBundleLoadImgListener);
    }

    public interface OnBundleLoadImgListener {
        void onloadImg(ImageView imageView, String imgurl, int defaultSrc);
    }

    ///////////////////////

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    //////////////////////

    public int getMaxcount() {
        return adapter.getMaxcount();
    }

    public void setMaxcount(int maxcount) {
        adapter.setMaxcount(maxcount);
    }

    public int getColcount() {
        return colcount;
    }

    public void setColcount(int colcount) {
        this.colcount = colcount;
        layoutManager.setSpanCount(colcount);
        itemDecoration.setSpanCount(colcount);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount() - 1);
    }

    public void setColcountWihi(int colcount, float wihi) {
        this.colcount = colcount;
        layoutManager.setSpanCount(colcount);
        itemDecoration.setSpanCount(colcount);
        this.wihi = wihi;
        adapter.setWihi(wihi);
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }
}
