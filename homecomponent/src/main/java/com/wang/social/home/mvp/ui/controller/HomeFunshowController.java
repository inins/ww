package com.wang.social.home.mvp.ui.controller;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.entities.user.ShatDownUser;
import com.frame.component.helper.AppDataHelper;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.ui.dialog.DialogSureFunshowPay;
import com.frame.component.ui.dialog.MorePopupWindow;
import com.frame.component.utils.ListUtil;
import com.frame.component.view.DialogPay;
import com.frame.component.view.FunshowView;
import com.frame.component.view.LoadingLayout;
import com.frame.entities.EventBean;
import com.frame.http.api.ApiHelperEx;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.mvp.IView;
import com.frame.utils.StrUtil;
import com.frame.utils.TimeUtils;
import com.frame.utils.ToastUtil;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.entities.funshow.FunshowHome;
import com.wang.social.home.mvp.model.api.HomeService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class HomeFunshowController extends BaseController {

    @BindView(R2.id.funshow_view)
    FunshowView funshowView;

    private MorePopupWindow popupWindow;

    private FunshowHome currentFunshow;

    @Override
    public void onCommonEvent(EventBean event) {
        switch (event.getEvent()) {
            case EventBean.EVENT_FUNSHOW_UPDATE_ZAN: {
                //在详情页点赞，收到通知刷新点赞状态及其点赞数量
                int talkId = (int) event.get("talkId");
                boolean isZan = (boolean) event.get("isZan");
                int zanCount = (int) event.get("zanCount");
                if (currentFunshow != null && talkId == currentFunshow.getTalkId()) {
                    currentFunshow.setIsLike(isZan);
                    currentFunshow.setSupportTotal(zanCount);
                    setFunshowData(currentFunshow);
                }
                break;
            }
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_EVA: {
                //在详情页评论，收到通知刷新评论数量
                int talkId = (int) event.get("talkId");
                if (currentFunshow != null && talkId == currentFunshow.getTalkId()) {
                    currentFunshow.setCommentTotal(currentFunshow.getCommentTotal() + 1);
                    setFunshowData(currentFunshow);
                }
                break;
            }
            case EventBean.EVENT_FUNSHOW_DETAIL_ADD_SHARE: {
                //在详情页分享，收到通知刷新分享数量
                int talkId = (int) event.get("talkId");
                if (currentFunshow != null && talkId == currentFunshow.getTalkId()) {
                    currentFunshow.setShareTotal(currentFunshow.getShareTotal() + 1);
                    setFunshowData(currentFunshow);
                }
                break;
            }
            case EventBean.EVENT_FUNSHOW_PAYED: {
                //趣晒支付了
                int talkId = (int) event.get("talkId");
                setFunshowPayed(talkId);
                break;
            }
            //详情页面删除趣晒
            case EventBean.EVENT_FUNSHOW_DEL:
                //在详情页不喜欢，重新刷新列表
            case EventBean.EVENT_FUNSHOW_DISSLIKE: {
                int talkId = (int) event.get("talkId");
                if (currentFunshow != null && talkId == currentFunshow.getTalkId()) {
                    netGetNewFunshow(false);
                }
                break;
            }
            //新增一条趣晒，收到通知刷新列表
            case EventBean.EVENT_FUNSHOW_ADD:
                refreshData();
                break;
        }
    }

    public HomeFunshowController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.lay_item_funshow;
        addLoadingLayout();
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
        popupWindow = new MorePopupWindow(getContext());
    }

    @Override
    protected void onInitData() {
        netGetNewFunshow(true);
    }

    private void setFunshowData(FunshowHome bean) {
        if (bean != null) {
            currentFunshow = bean;
            FunshowBean funshowBean = bean.tans2FunshowBean();
            funshowView.setData(funshowBean);
            funshowView.setZanCallback((isZan, zanCount) -> {
                bean.setIsLike(isZan);
                bean.setSupportTotal(zanCount);
            });
            funshowView.getMoreBtn().setOnClickListener(view -> {
                popupWindow.setOnDislikeClickListener(v -> {
                    netShatDownUser();
                });
                popupWindow.showPopupWindow(view);
            });
            getRoot().setOnClickListener(v -> {
                if (!funshowBean.hasAuth()) {
                    DialogPay.showPayFunshow(getIView(), getFragmentManager(), funshowBean.getPrice(), -1, () -> {
                        NetPayStoneHelper.newInstance().netPayFunshow(getIView(), funshowBean.getId(), funshowBean.getPrice(), () -> {
                            CommonHelper.FunshowHelper.startDetailActivity(getContext(), funshowBean.getId());
                            EventBus.getDefault().post(new EventBean(EventBean.EVENT_FUNSHOW_PAYED).put("talkId", funshowBean.getId()));
                        });
                    });
                } else {
                    CommonHelper.FunshowHelper.startDetailActivity(getContext(), funshowBean.getId());
                }
            });
        }
    }

    private void setFunshowPayed(int talkId) {
        if (talkId == currentFunshow.getTalkId()) {
            currentFunshow.setTalkPayed(1);
            setFunshowData(currentFunshow);
        }
    }

    public void refreshData() {
        netGetNewFunshow(false);
    }

    public void netShatDownUser() {
        if (funshowView == null) return;
        int userId = currentFunshow.getCreatorId();
        if (userId == AppDataHelper.getUser().getUserId()) {
            ToastUtil.showToastShort("不能屏蔽自己");
            return;
        }
        ApiHelperEx.execute(getIView(), true,
                ApiHelperEx.getService(CommonService.class).shatDownUser(userId + "", 1),
                new ErrorHandleSubscriber<BaseJson<Object>>() {
                    @Override
                    public void onNext(BaseJson<Object> basejson) {
                        netGetNewFunshow(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }

    public void netGetNewFunshow(boolean needLoading) {
        ApiHelperEx.execute(getIView(), needLoading,
                ApiHelperEx.getService(HomeService.class).getNewFunshow(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowHome>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowHome>> basejson) {
                        BaseListWrap<FunshowHome> wrap = basejson.getData();
                        List<FunshowHome> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            setFunshowData(list.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, () -> {
                    if (needLoading) getLoadingLayout().showLoadingView();
                }, () -> {
                    if (needLoading) getLoadingLayout().showOut();
                });
    }
}
