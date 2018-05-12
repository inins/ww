package com.wang.social.funshow.mvp.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.view.FunshowView;
import com.wang.social.funshow.R;
import com.wang.social.funshow.R2;
import com.wang.social.funshow.mvp.entities.funshow.Funshow;
import com.frame.component.ui.dialog.MorePopupWindow;
import com.frame.component.utils.VideoCoverUtil;

import butterknife.BindView;

public class RecycleAdapterHome extends BaseAdapter<Funshow> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.funshow_item_home);
    }

    public class Holder extends BaseViewHolder<Funshow> {
        @BindView(R2.id.funshow_view)
        FunshowView funshowView;
        MorePopupWindow popupWindow;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
            popupWindow = new MorePopupWindow(getContext());
        }

        @Override
        protected void bindData(Funshow bean, int position, OnItemClickListener onItemClickListener) {
            funshowView.setData(bean.tans2FunshowBean());
            funshowView.setZanCallback((isZan, zanCount) -> {
                bean.setIsSupport(isZan);
                bean.setTalkSupportNum(zanCount);
            });
            funshowView.getMoreBtn().setOnClickListener(view -> {
                popupWindow.setOnDislikeClickListener(v -> {
                    if (onDislikeClickListener != null)
                        onDislikeClickListener.onDislikeClick(v, bean);
                });
                popupWindow.showPopupWindow(view);
            });
        }

        @Override
        public void onRelease() {
        }

        @Override
        protected boolean useItemClickListener() {
            return true;
        }
    }

    //////////////////////////////


    public int getIndexById(int talkId) {
        if (getData() == null) return -1;
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getTalkId() == talkId) {
                return i;
            }
        }
        return -1;
    }

    public void refreshZanById(int talkId, boolean isZan, int zanCount) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setTalkSupportNum(zanCount);
            funshow.setIsSupport(isZan);
            notifyItemChanged(index);
        }
    }

    public void refreshCommentById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setTalkCommentNum(funshow.getTalkCommentNum() + 1);
            notifyItemChanged(index);
        }
    }

    public void refreshShareById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setTalkShareNum(funshow.getTalkShareNum() + 1);
            notifyItemChanged(index);
        }
    }

    public void refreshNeedPayById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            Funshow funshow = getData().get(index);
            funshow.setIsShopping(false);
            notifyItemChanged(index);
        }
    }

    /////////////////////////////

    public interface OnDislikeClickListener {
        void onDislikeClick(View v, Funshow funshow);
    }

    private OnDislikeClickListener onDislikeClickListener;

    public void setOnDislikeClickListener(OnDislikeClickListener onDislikeClickListener) {
        this.onDislikeClickListener = onDislikeClickListener;
    }
}
