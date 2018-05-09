package com.wang.social.home.mvp.ui.controller;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frame.component.entities.BaseListWrap;
import com.frame.component.entities.funpoint.Funpoint;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wang.social.home.R;
import com.wang.social.home.R2;
import com.wang.social.home.mvp.contract.HomeContract;
import com.wang.social.home.mvp.entities.FunpointAndTopic;
import com.wang.social.home.mvp.entities.Funshow;
import com.wang.social.home.mvp.entities.Topic;
import com.wang.social.home.mvp.model.api.HomeService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeFunshowController extends BaseController {

    @BindView(R2.id.img_header)
    ImageView img_header;
    @BindView(R2.id.img_pic)
    ImageView imgPic;
    @BindView(R2.id.img_more)
    ImageView imgMore;
    @BindView(R2.id.text_name)
    TextView textName;
    @BindView(R2.id.text_time)
    TextView textTime;
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

    public HomeFunshowController(IView iView, View root) {
        super(iView, root);
        int layout = R.layout.home_lay_funshow_item;
        registEventBus();
        onInitCtrl();
        onInitData();
    }

    @Override
    protected void onInitCtrl() {
    }

    @Override
    protected void onInitData() {
        netGetNewFunshow();
    }

    private void setFunshowData(Funshow bean) {
        if (bean != null) {
            ImageLoaderHelper.loadCircleImg(img_header, bean.getHeadImg());
            textName.setText(bean.getNickname());
            textTitle.setText(bean.getContent());
            textPosition.setText(bean.getAddress());
            textZan.setText(bean.getSupportTotal() + "");
            textComment.setText(bean.getCommentTotal() + "");
            textShare.setText(bean.getShareTotal() + "");
            textPicCount.setText("1/" + bean.getUrls());
            textTime.setText(TimeUtils.date2String(bean.getCreateTime(), "MM-dd HH:mm"));
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
        }
    }

    public void netGetNewFunshow() {
        ApiHelperEx.execute(getIView(), false,
                ApiHelperEx.getService(HomeService.class).getNewFunshow(),
                new ErrorHandleSubscriber<BaseJson<BaseListWrap<Funshow>>>() {
                    @Override
                    public void onNext(BaseJson<BaseListWrap<Funshow>> basejson) {
                        BaseListWrap<Funshow> wrap = basejson.getData();
                        List<Funshow> list = wrap.getList();
                        if (!StrUtil.isEmpty(list)) {
                            setFunshowData(list.get(0));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showToastLong(e.getMessage());
                    }
                });
    }
}
