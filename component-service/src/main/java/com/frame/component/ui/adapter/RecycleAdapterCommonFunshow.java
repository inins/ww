package com.frame.component.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.frame.base.BaseAdapter;
import com.frame.base.BaseViewHolder;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.service.R;
import com.frame.component.service.R2;
import com.frame.component.ui.dialog.MorePopupWindow;
import com.frame.component.view.DialogPay;
import com.frame.component.view.FunshowView;
import com.frame.entities.EventBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class RecycleAdapterCommonFunshow extends BaseAdapter<FunshowBean> {

    private boolean isShowHeader = true;
    private boolean isShowMoreBtn = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_FUNSHOW_UPDATE_ZAN: {
                //在详情页点赞，收到通知刷新点赞状态及其点赞数量
                int talkId = (int) event.get("talkId");
                boolean isZan = (boolean) event.get("isZan");
                int zanCount = (int) event.get("zanCount");
                this.refreshZanById(talkId, isZan, zanCount);
                break;
            }
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_EVA: {
                //在详情页评论，收到通知刷新评论数量
                int talkId = (int) event.get("talkId");
                this.refreshCommentById(talkId);
                break;
            }
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_SHARE: {
                //在详情页分享，收到通知刷新分享数量
                int talkId = (int) event.get("talkId");
                this.refreshShareById(talkId);
                break;
            }
            case EventBean.EVENT_FUNSHOW_PAYED: {
                //趣晒支付了
                int talkId = (int) event.get("talkId");
                this.refreshPayedById(talkId);
                break;
            }
        }
    }

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
            funshowView.setShowHeader(isShowHeader);
            funshowView.getMoreBtn().setVisibility(isShowMoreBtn ? View.VISIBLE : View.GONE);
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
                            EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_PAYED).put("talkId", bean.getId()));
                            refreshPayedById(bean.getId());
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

    public void refreshPayedById(int talkId) {
        int index = getIndexById(talkId);
        if (index != -1) {
            FunshowBean funshow = getData().get(index);
            funshow.setPay(true);
            notifyItemChanged(index);
        }
    }

    /////////////////////////////

    public void setShowHeader(boolean showHeader) {
        isShowHeader = showHeader;
    }

    public void setShowMoreBtn(boolean showMoreBtn) {
        isShowMoreBtn = showMoreBtn;
    }

    /////////////////////////////

    public interface OnDislikeClickListener {
        void onDislikeClick(View v, FunshowBean funshow);
    }

    private OnDislikeClickListener onDislikeClickListener;

    public void setOnDislikeClickListener(OnDislikeClickListener onDislikeClickListener) {
        this.onDislikeClickListener = onDislikeClickListener;
    }

    /////////////////////////////  eventBus

    public void registEventBus() {
        EventBus.getDefault().register(this);
    }

    public void unRegistEventBus() {
        EventBus.getDefault().unregister(this);
    }
}
