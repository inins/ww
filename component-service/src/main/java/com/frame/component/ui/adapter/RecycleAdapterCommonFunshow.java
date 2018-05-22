package com.frame.component.ui.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.api.CommonService;
import com.frame.component.entities.TestEntity;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.dialog.DialogSureFunshowPay;
import com.frame.component.ui.dialog.MorePopupWindow;
import com.frame.component.utils.FunShowUtil;
import com.frame.component.view.DialogPay;
import com.frame.component.view.FunshowView;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.ToastUtil;

import butterknife.BindView;

public class RecycleAdapterCommonFunshow extends BaseAdapter<FunshowBean> {

    @Override
    protected BaseViewHolder createViewHolder(Context context, ViewGroup parent, int viewType) {
        return new Holder(context, parent, R.layout.lay_item_funshow);
    }

    public class Holder extends BaseViewHolder<FunshowBean> {

        @BindView(R2.id.funshow_view)
        FunshowView funshowView;
        MorePopupWindow popupWindow;

        public Holder(Context context, ViewGroup root, int layoutRes) {
            super(context, root, layoutRes);
            popupWindow = new MorePopupWindow(getContext());
        }

        @Override
        protected void bindData(FunshowBean bean, int position, OnItemClickListener onItemClickListener) {
            funshowView.setData(bean);
            funshowView.setZanCallback((isZan, zanCount) -> {
                bean.setSupport(isZan);
                bean.setSupportTotal(zanCount);
            });
            funshowView.getMoreBtn().setOnClickListener(view -> {
                popupWindow.setOnDislikeClickListener(v -> {
                    if (onDislikeClickListener != null)
                        onDislikeClickListener.onDislikeClick(v, bean);
                });
                popupWindow.showPopupWindow(view);
            });
            itemView.setOnClickListener(v -> {
                if (!bean.isFree() && !bean.isPay()) {
                    DialogPay.showPayFunshow(getIView(), getFragmentManager(), bean.getPrice(), -1, () -> {
                        NetPayStoneHelper.newInstance().netPayFunshow(getIView(), bean.getId(), bean.getPrice(), () -> {
                            CommonHelper.FunshowHelper.startDetailActivity(getContext(), bean.getId());
                            refreshNeedPayById(bean.getId());
                        });
                    });
                } else {
                    CommonHelper.FunshowHelper.startDetailActivity(getContext(), bean.getId());
                }
            });
        }

        @Override
        public void onRelease() {
        }
    }

    //////////////////////////////


    public int getIndexById(int talkId) {
        if (getData() == null) return -1;
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getId() == talkId) {
                return i;
            }
        }
        return -1;
    }

    public void refreshZanById(int talkId, boolean isZan, int zanCount) {
        int index = getIndexById(talkId);
        if (index != -1) {
            FunshowBean funshow = getData().get(index);
            funshow.setSupportTotal(zanCount);
            funshow.setSupport(isZan);
            notifyItemChanged(index);
        }
    }

    public void refreshCommentById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            FunshowBean funshow = getData().get(index);
            funshow.setCommentTotal(funshow.getCommentTotal() + 1);
            notifyItemChanged(index);
        }
    }

    public void refreshShareById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            FunshowBean funshow = getData().get(index);
            funshow.setShareTotal(funshow.getShareTotal() + 1);
            notifyItemChanged(index);
        }
    }

    public void refreshNeedPayById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            FunshowBean funshow = getData().get(index);
            funshow.setPay(true);
            notifyItemChanged(index);
        }
    }

    /////////////////////////////

    public interface OnDislikeClickListener {
        void onDislikeClick(View v, FunshowBean funshow);
    }

    private OnDislikeClickListener onDislikeClickListener;

    public void setOnDislikeClickListener(OnDislikeClickListener onDislikeClickListener) {
        this.onDislikeClickListener = onDislikeClickListener;
    }
}
