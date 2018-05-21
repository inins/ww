package com.wang.social.home.mvp.ui.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funshow.FunshowBean;
import com.frame.component.entities.user.UserBoard;
import com.frame.component.helper.CommonHelper;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetPayStoneHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.ui.base.BaseController;
import com.frame.component.ui.dialog.DialogSureFunshowPay;
import com.frame.component.view.FunshowView;
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
import com.wang.social.home.mvp.entities.funshow.FunshowHomeDetail;
import com.wang.social.home.mvp.model.api.HomeService;

import java.util.List;

import butterknife.BindView;

public class DetailFunshowController extends BaseController {

    //    @BindView(R2.id.img_pic)
//    ImageView imgPic;
//    @BindView(R2.id.text_title)
//    TextView textTitle;
//    @BindView(R2.id.img_player)
//    ImageView imgPlayer;
//    @BindView(R2.id.img_tag_pay)
//    ImageView imgTagPay;
//    @BindView(R2.id.text_pic_count)
//    TextView textPicCount;
//    @BindView(R2.id.text_position)
//    TextView textPosition;
//    @BindView(R2.id.text_zan)
//    TextView textZan;
//    @BindView(R2.id.text_comment)
//    TextView textComment;
//    @BindView(R2.id.text_share)
//    TextView textShare;
    @BindView(R2.id.funshow_view)
    FunshowView funshowView;

    private int userId;
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
        }
    }

    public DetailFunshowController(View root, int userId) {
        super(root);
        this.userId = userId;
        int layout = R.layout.home_lay_funshow_carddetail;
        addLoadingLayout();
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
    }

    @Override
    protected void onInitData() {
        netGetNewFunshowByUser(userId);
    }

    private void setFunshowData(FunshowHome bean) {
        if (bean != null) {
            currentFunshow = bean;
            FunshowBean funshowBean = bean.tans2FunshowBean();
            funshowView.setData(funshowBean);
            getRoot().setOnClickListener(v -> {
                if (!funshowBean.isFree() && !funshowBean.isPay()) {
                    DialogSureFunshowPay.showDialog(getContext(), funshowBean.getPrice(), () -> {
                        NetPayStoneHelper.newInstance().netPayFunshow(getIView(), funshowBean.getId(), funshowBean.getPrice(), () -> {
                            CommonHelper.FunshowHelper.startDetailActivity(getContext(), funshowBean.getId());
                            currentFunshow.setTalkPayed(1);
                        });
                    });
                } else {
                    CommonHelper.FunshowHelper.startDetailActivity(getContext(), funshowBean.getId());
                }
            });
        }
    }

    ///////////////////////////////////////

    public void netGetNewFunshowByUser(int userId) {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(HomeService.class).getNewFunshowByUser(userId),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowHome>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowHome>> basejson) {
                        BaseListWrap<FunshowHome> wrap = basejson.getData();
                        List<FunshowHome> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            setFunshowData(list.get(0));
                            getLoadingLayout().showOut();
                        } else {
                            getLoadingLayout().showLackView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                        getLoadingLayout().showFailView();
                    }
                }, () -> {
                    getLoadingLayout().showLoadingView();
                }, null);
    }
}
