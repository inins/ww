package com.wang.social.home.mvp.ui.controller;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.api.CommonService;
import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.user.UserBoard;
import com.frame.component.helper.ImageLoaderHelper;
import com.frame.component.helper.NetZanHelper;
import com.frame.component.ui.base.BaseController;
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

    @BindView(R2.id.img_pic)
    ImageView imgPic;
    @BindView(R2.id.text_title)
    TextView textTitle;
    @BindView(R2.id.img_player)
    ImageView imgPlayer;
    @BindView(R2.id.img_tag_pay)
    ImageView imgTagPay;
    @BindView(R2.id.text_pic_count)
    TextView textPicCount;
    @BindView(R2.id.text_position)
    TextView textPosition;
    @BindView(R2.id.text_zan)
    TextView textZan;
    @BindView(R2.id.text_comment)
    TextView textComment;
    @BindView(R2.id.text_share)
    TextView textShare;

    private int userId;

    public DetailFunshowController(View root, int userId) {
        super(root);
        this.userId = userId;
        int layout = R.layout.home_lay_funshow_carddetail;
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
            textTitle.setText(bean.getContent());
            textPosition.setText(bean.getProvince() + bean.getCity());
            textZan.setText(bean.getSupportTotal() + "");
            textComment.setText(bean.getCommentTotal() + "");
            textShare.setText(bean.getShareTotal() + "");
            textPicCount.setText("1/" + bean.getUrls());
            imgTagPay.setVisibility(bean.isFree() ? View.VISIBLE : View.GONE);
            textZan.setSelected(bean.isLiked());

            imgPlayer.setVisibility(bean.isVideo() ? View.VISIBLE : View.GONE);
            ImageLoaderHelper.loadImg(imgPic, bean.getUrl());

            textZan.setOnClickListener(v -> {
                IView iView = (getContext() instanceof IView) ? (IView) getContext() : null;
                NetZanHelper.newInstance().funshowZan(iView, textZan, bean.getTalkId(), !textZan.isSelected(), null);
            });
            textShare.setOnClickListener(v -> {
            });
            getRoot().setOnClickListener(v -> {
                ToastUtil.showToastShort("funshowId:" + bean.getTalkId());
            });
        }
    }

    ///////////////////////////////////////

    public void netGetNewFunshowByUser(int userId) {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(HomeService.class).getNewFunshowByUser(userId),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<FunshowHomeDetail>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<FunshowHomeDetail>> basejson) {
                        BaseListWrap<FunshowHomeDetail> wrap = basejson.getData();
                        List<FunshowHomeDetail> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            setFunshowData(list.get(0).tans2FunshowHome());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
