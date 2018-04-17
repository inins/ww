package com.wang.social.personal.mvp.ui.view.bundleimgview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.frame.utils.SizeUtils;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.common.DragItemTouchCallback;
import com.frame.component.common.GridSpacingItemDecoration;
import com.wang.social.personal.common.OnRecyclerItemClickListener;
import com.wang.social.personal.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liaoinstan on 2016/6/12 0012.
 */
public class BundleImgView extends FrameLayout {

    private RecyclerView recyclerView;
    private RecycleAdapterBundleImg adapter;
    private List<BundleImgEntity> results = new ArrayList<>();

    private ViewGroup root;

    private Context context;

    //一行多少个item数量
    private int colcount = 4;
    private boolean dragble;
    private boolean editble;

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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.personal_BundleImgView);
        editble = a.getBoolean(R.styleable.personal_BundleImgView_personal_editble, true);
        dragble = a.getBoolean(R.styleable.personal_BundleImgView_personal_dragble, false);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.personal_lay_bundleview, this, true);
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
        //测试数据
//        results.add(new BundleImgEntity("http://v1.qzone.cc/avatar/201503/30/13/53/5518e4e8d705e435.jpg%21200x200.jpg"));
//        results.add(new BundleImgEntity("http://img1.touxiang.cn/uploads/20131103/03-030932_368.jpg"));
//        results.add(new BundleImgEntity("http://pic.qqtn.com/up/2016-7/2016072614451372403.jpg"));
//        results.add(new BundleImgEntity("http://b.hiphotos.baidu.com/zhidao/pic/item/d1a20cf431adcbefef0f982fabaf2edda3cc9fe4.jpg"));
//        results.add(new BundleImgEntity("http://d.hiphotos.baidu.com/zhidao/pic/item/7a899e510fb30f24b12d36e7cd95d143ac4b039b.jpg"));

        adapter = new RecycleAdapterBundleImg(results);
        adapter.setEditble(editble);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new GridLayoutManager(context, colcount));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(4, SizeUtils.dp2px(10), GridLayoutManager.VERTICAL, false));
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
        void onPhotoDelClick(View v);

        void onPhotoShowClick(String path);

        void onPhotoAddClick(View v);
    }

    public abstract static class OnBundleSimpleClickListener implements OnBundleClickListener {

        @Override
        public void onPhotoDelClick(View v) {
        }

        @Override
        public void onPhotoShowClick(String path) {
        }

        @Override
        public abstract void onPhotoAddClick(View v);
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


    public int getMaxcount() {
        return adapter.getMaxcount();
    }

    public void setMaxcount(int maxcount) {
        adapter.setMaxcount(maxcount);
    }
}
