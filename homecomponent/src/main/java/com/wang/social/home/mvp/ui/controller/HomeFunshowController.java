package com.wang.social.home.mvp.ui.controller;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.ui.dialog.MorePopupWindow;
import com.frame.component.utils.ListUtil;
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

import java.util.List;

import butterknife.BindView;

public class HomeFunshowController extends BaseController {

    @BindView(R2.id.funshow_view)
    FunshowView funshowView;

    private MorePopupWindow popupWindow;

    private List<FunshowHome> funshowList;
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
            case EventBean.EVENT_FUNSHOW_DISSLIKE: {
                //在详情页不喜欢，收到通知刷新下一条趣晒
                nextFunshow();
                break;
            }
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
        netGetNewFunshow();
    }

    private void setFunshowData(FunshowHome bean) {
        if (bean != null) {
            currentFunshow = bean;
            funshowView.setData(bean.tans2FunshowBean());
            funshowView.setZanCallback((isZan, zanCount) -> {
                bean.setIsLike(isZan);
                bean.setSupportTotal(zanCount);
            });
            funshowView.getMoreBtn().setOnClickListener(view -> {
                popupWindow.setOnDislikeClickListener(v -> {
                    nextFunshow();
                });
                popupWindow.showPopupWindow(view);
            });
            getRoot().setOnClickListener(v -> {
                CommonHelper.FunshowHelper.startDetailActivity(getContext(), bean.getTalkId());
            });
        }
    }

    private void nextFunshow() {
        //移除最顶部的一条数据，取第下一条
        if (StrUtil.isEmpty(funshowList)) return;
        ListUtil.remove(funshowList, 0);
        FunshowHome funshow = ListUtil.get(funshowList, 0);
        setFunshowData(funshow);
    }

    public void netGetNewFunshow() {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(HomeService.class).getNewFunshow(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowHome>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowHome>> basejson) {
                        BaseListWrap<FunshowHome> wrap = basejson.getData();
                        List<FunshowHome> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            funshowList = list;
                            setFunshowData(list.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                }, () -> {
                    getLoadingLayout().showLoadingView();
                }, () -> {
                    getLoadingLayout().showOut();
                });
    }
}
