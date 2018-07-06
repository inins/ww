package com.wang.social.personal.mvp.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.utils.StrUtil;
import com.wang.social.personal.R;
import com.wang.social.personal.R2;
import com.wang.social.personal.mvp.entities.lable.Lable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class RecycleAdapterLable extends BaseAdapter<Lable> {

    private boolean deleteEnable;

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.personal_item_lable);
    }

    public class Holder extends BaseViewHolder<Lable> {
        @BindView(R2.id.text_name)
        TextView text_name;
        @BindView(R2.id.img_tag)
        ImageView img_tag;
        @BindView(R2.id.img_del)
        ImageView img_del;
        AnimationSet animationSet;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);

            //初始化摇晃动画
            Animation scaleAnim = new ScaleAnimation(0.985f, 1.025f, 0.985f, 1.025f);
            scaleAnim.setDuration(200);
            scaleAnim.setRepeatMode(Animation.REVERSE);
            scaleAnim.setRepeatCount(Animation.INFINITE);
            scaleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            Animation rotateAnim = new RotateAnimation(-3, 3, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotateAnim.setDuration(65);
            rotateAnim.setRepeatMode(Animation.REVERSE);
            rotateAnim.setRepeatCount(Animation.INFINITE);
            rotateAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            animationSet = new AnimationSet(false);
            animationSet.addAnimation(scaleAnim);
//            animationSet.setStartOffset(new Random().nextInt(100));
            animationSet.addAnimation(rotateAnim);
        }

        @Override
        protected void bindData(Lable lable, int position, OnItemClickListener onItemClickListener) {
            img_del.setOnClickListener((view) -> {
                if (onLableDelClickListener != null)
                    onLableDelClickListener.OnDelClick(RecycleAdapterLable.this, lable, position);
            });

            img_del.setVisibility(deleteEnable ? View.VISIBLE : View.GONE);
            img_tag.setVisibility(lable.getShowTagBool() ? View.VISIBLE : View.GONE);
            text_name.setText(lable.getName());

            if (deleteEnable) {
                //随机延迟 0-0.1秒，制造错落感
                new Handler().postDelayed(() -> itemView.startAnimation(animationSet), new Random().nextInt(100));
            } else {
                itemView.clearAnimation();
            }
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

    /////////////////////

    public String getIds() {
        return getIds(valueList);
    }

    private String getIds(List<Lable> lableList) {
        if (StrUtil.isEmpty(lableList)) return "";
        String ids = "";
        for (Lable lable : lableList) {
            ids += lable.getId() + ",";
        }
        return StrUtil.subLastChart(ids, ",");
    }

    public String getIdsByAdd(Lable lable) {
        if (StrUtil.isEmpty(valueList)) return String.valueOf(lable.getId());
        List<Lable> tempList = new ArrayList<>();
        tempList.addAll(valueList);
        tempList.add(lable);
        return getIds(tempList);
    }

    public String getIdsByDel(Lable lable) {
        if (StrUtil.isEmpty(valueList)) return "";
        List<Lable> tempList = new ArrayList<>();
        for (Lable item : valueList) {
            if (item.getId() == lable.getId()) continue;
            tempList.add(item);
        }
        return getIds(tempList);
    }

    //是否已经存在某标签了
    public boolean isIncludeLable(int lableId) {
        if (StrUtil.isEmpty(getData())) return false;
        for (Lable lable : getData()) {
            if (lable.getId() == lableId) {
                return true;
            }
        }
        return false;
    }

    /////////////////////

    public boolean isDeleteEnable() {
        return deleteEnable;
    }

    public void setDeleteEnable(boolean deleteEnable) {
        this.deleteEnable = deleteEnable;
    }

    private OnLableDelClickListener onLableDelClickListener;

    public void setOnLableDelClickListener(OnLableDelClickListener onLableDelClickListener) {
        this.onLableDelClickListener = onLableDelClickListener;
    }

    public interface OnLableDelClickListener {
        void OnDelClick(RecycleAdapterLable adapter, Lable lable, int position);
    }
}
