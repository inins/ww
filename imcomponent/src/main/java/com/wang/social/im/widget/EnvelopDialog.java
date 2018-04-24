package com.wang.social.im.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.frame.di.component.AppComponent;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.integration.IRepositoryManager;
import com.frame.utils.FrameUtils;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.ui.EnvelopDetailActivity;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Bo on 2017-12-11.
 */

public class EnvelopDialog extends Dialog {

    private ImageView drpIvbClose;
    private ImageView drpCivHead;
    private TextView drpTvFrom;
    private TextView drpTvMessage;
    private TextView drpTvbOpen;
    private TextView drpTvMulti;
    private TextView drpTvDiamond;
    private TextView drpTvbLookDetail;
    private TextView drpTvTip;
    private ProgressBar drpLoading;

    private Context context;
    private CompositeDisposable disposables;

    private EnvelopInfo envelopInfo;

    private ImageLoader mImageLoader;
    private IRepositoryManager mRepositoryManager;
    private RxErrorHandler mErrorHandler;

    public EnvelopDialog(@NonNull Context context, EnvelopInfo envelopInfo) {
        super(context, R.style.common_MyDialog);
        this.context = context;
        this.envelopInfo = envelopInfo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_dialog_envelop);

        disposables = new CompositeDisposable();

        AppComponent appComponent = FrameUtils.obtainAppComponentFromContext(context);
        mImageLoader = appComponent.imageLoader();
        mRepositoryManager = appComponent.repoitoryManager();
        mErrorHandler = appComponent.rxErrorHandler();

        initView();
        initListener();
    }

    @Override
    protected void onStop() {
        if (disposables != null && !disposables.isDisposed()) {
            disposables.dispose();
        }
        super.onStop();
    }

    protected void initView() {
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);

        setCanceledOnTouchOutside(false);

        drpIvbClose = findViewById(R.id.drp_ivb_close);
        drpCivHead = findViewById(R.id.drp_civ_head);
        drpTvFrom = findViewById(R.id.drp_tv_from);
        drpTvMessage = findViewById(R.id.drp_tv_message);
        drpTvbOpen = findViewById(R.id.drp_tvb_open);
        drpLoading = findViewById(R.id.drp_loading);
        drpTvMulti = findViewById(R.id.drp_tv_multi);
        drpTvbLookDetail = findViewById(R.id.drp_tvb_detail);
        drpTvTip = findViewById(R.id.drp_tv_tip);
        drpTvDiamond = findViewById(R.id.drp_tv_diamond);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        //展示发送者信息
        mImageLoader.loadImage(context, ImageConfigImpl
                .builder()
                .placeholder(R.drawable.common_default_circle_placeholder)
                .errorPic(R.drawable.common_default_circle_placeholder)
                .url(envelopInfo.getFromPortrait())
                .imageView(drpCivHead)
                .build());
        drpTvFrom.setText(envelopInfo.getFromNickname());
        if (envelopInfo.getType() == EnvelopInfo.EnvelopType.SPELL) {
            drpTvMulti.setText(R.string.im_envelop_give_spell);
        } else {
            drpTvMulti.setText(R.string.im_envelop_give_you);
        }

        showInfo();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mImageLoader = null;
        mErrorHandler = null;
        mRepositoryManager = null;
        if (disposables != null) {
            disposables.clear();
            disposables = null;
        }
        context = null;
        envelopInfo = null;
        drpIvbClose = null;
        drpCivHead = null;
        drpTvFrom = null;
        drpTvMessage = null;
        drpTvbOpen = null;
        drpTvMulti = null;
        drpTvDiamond = null;
        drpTvbLookDetail = null;
        drpTvTip = null;
        drpLoading = null;
    }

    private void showInfo() {
        if (envelopInfo.getStatus() == EnvelopInfo.Status.LIVING) {
            drpTvMessage.setText(envelopInfo.getMessage());
        } else {
            drpTvbOpen.setVisibility(View.GONE);
            if (envelopInfo.getGotDiamond() > 0) {
                drpTvMessage.setVisibility(View.GONE);
                drpTvDiamond.setVisibility(View.VISIBLE);
                drpTvDiamond.setText(String.valueOf(envelopInfo.getGotDiamond()));
            } else {
                if (envelopInfo.getStatus() == EnvelopInfo.Status.OVERDUE) {
                    drpTvMessage.setText(envelopInfo.isSelf() ? R.string.im_envelop_overdue_self : R.string.im_envelop_overdue);
                } else if (envelopInfo.getStatus() == EnvelopInfo.Status.EMPTY) {
                    drpTvMessage.setText(R.string.im_envelop_empty);
                }
            }

            //红包类型为等额红包时，未领到红包的人不能查看领取详情
            if (envelopInfo.isSelf() || envelopInfo.getGotDiamond() > 0 || envelopInfo.getType() != EnvelopInfo.EnvelopType.EQUAL) {
                drpTvbLookDetail.setVisibility(View.VISIBLE);
            } else {
                drpTvTip.setVisibility(View.VISIBLE);
            }
        }
    }

    protected void initListener() {
        drpIvbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        drpTvbOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);

                openRedPacket();
            }
        });

        drpTvbLookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                EnvelopDetailActivity.start(context, envelopInfo.getEnvelopId());
            }
        });
    }

    private void openRedPacket() {
        drpLoading.setVisibility(View.VISIBLE);

    }
}