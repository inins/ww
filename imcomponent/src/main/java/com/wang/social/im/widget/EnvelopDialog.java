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

import com.frame.component.utils.UIUtil;
import com.frame.di.component.AppComponent;
import com.frame.http.api.ApiException;
import com.frame.http.api.BaseJson;
import com.frame.http.api.error.ErrorHandleSubscriber;
import com.frame.http.api.error.RxErrorHandler;
import com.frame.http.imageloader.ImageLoader;
import com.frame.http.imageloader.glide.ImageConfigImpl;
import com.frame.integration.IRepositoryManager;
import com.frame.utils.FrameUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.wang.social.im.R;
import com.wang.social.im.mvp.model.api.ApiCode;
import com.wang.social.im.mvp.model.api.EnvelopService;
import com.wang.social.im.mvp.model.entities.EnvelopAdoptInfo;
import com.wang.social.im.mvp.model.entities.EnvelopInfo;
import com.wang.social.im.mvp.model.entities.EnvelopMessageCacheInfo;
import com.wang.social.im.mvp.model.entities.UIMessage;
import com.wang.social.im.mvp.model.entities.dto.EnvelopAdoptInfoDTO;
import com.wang.social.im.mvp.ui.EnvelopDetailActivity;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    private UIMessage uiMessage;
    private EnvelopInfo envelopInfo;

    private ImageLoader mImageLoader;
    private IRepositoryManager mRepositoryManager;
    private RxErrorHandler mErrorHandler;

    private Gson gson;

    public EnvelopDialog(@NonNull Context context, UIMessage uiMessage, EnvelopInfo envelopInfo) {
        super(context, R.style.common_MyDialog);
        this.context = context;
        this.envelopInfo = envelopInfo;
        this.uiMessage = uiMessage;

        gson = FrameUtils.obtainAppComponentFromContext(context).gson();
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
                .isCircle(true)
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
        gson = null;
    }

    private void showInfo() {
        if (envelopInfo.getStatus() == EnvelopInfo.Status.LIVING && envelopInfo.getGotDiamond() == 0) {
            drpTvMessage.setText(envelopInfo.getMessage());
            if (envelopInfo.isSelf() && envelopInfo.getType() == EnvelopInfo.EnvelopType.PRIVATE) {
                drpTvbOpen.setVisibility(View.GONE);
                drpTvbLookDetail.setVisibility(View.VISIBLE);
            }
        } else {
            drpTvbOpen.setVisibility(View.GONE);
            if (envelopInfo.getGotDiamond() > 0) {
                drpTvMessage.setVisibility(View.GONE);
                drpTvDiamond.setVisibility(View.VISIBLE);
                drpTvDiamond.setText(String.valueOf(envelopInfo.getGotDiamond()));
            } else {
                if (envelopInfo.getStatus() == EnvelopInfo.Status.OVERDUE) {
                    drpTvMessage.setText(envelopInfo.getType() == EnvelopInfo.EnvelopType.PRIVATE ? R.string.im_envelop_overdue : R.string.im_envelop_overdue_self);
                } else if (envelopInfo.getStatus() == EnvelopInfo.Status.EMPTY) {
                    drpTvMessage.setText(envelopInfo.isSelf() ? envelopInfo.getMessage() : UIUtil.getString(R.string.im_envelop_empty));
                }
            }

            //红包类型为等额红包时，未领到红包的人不能查看领取详情
            if (envelopInfo.getStatus() == EnvelopInfo.Status.OVERDUE) {
                if (envelopInfo.isSelf() || envelopInfo.getGotDiamond() > 0 || envelopInfo.getType() == EnvelopInfo.EnvelopType.SPELL) {
                    drpTvbLookDetail.setVisibility(View.VISIBLE);
                }
            } else {
                if (envelopInfo.isSelf() || envelopInfo.getGotDiamond() > 0 || envelopInfo.getType() != EnvelopInfo.EnvelopType.EQUAL) {
                    drpTvbLookDetail.setVisibility(View.VISIBLE);
                } else {
                    drpTvTip.setVisibility(View.VISIBLE);
                }
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

        //查看详情
        drpTvbLookDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                EnvelopDetailActivity.start(context, envelopInfo);
            }
        });
    }

    /**
     * 领取红包
     */
    private void openRedPacket() {
        mRepositoryManager
                .obtainRetrofitService(EnvelopService.class)
                .adoptEnvelop("2.0.0", envelopInfo.getEnvelopId())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        disposables.add(disposable);
                        drpLoading.setVisibility(View.VISIBLE);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (drpLoading != null)
                            drpLoading.setVisibility(View.GONE);
                    }
                })
                .map(new Function<BaseJson<EnvelopAdoptInfoDTO>, EnvelopAdoptInfo>() {
                    @Override
                    public EnvelopAdoptInfo apply(BaseJson<EnvelopAdoptInfoDTO> t) throws Exception {
                        return t.getData().transform();
                    }
                })
                .subscribe(new ErrorHandleSubscriber<EnvelopAdoptInfo>(mErrorHandler) {
                    @Override
                    public void onNext(EnvelopAdoptInfo envelopAdoptInfo) {
                        envelopInfo.setGotDiamond(envelopAdoptInfo.getGotDiamondNumber());
//                        showInfo();
                        EnvelopDetailActivity.start(getContext(), envelopInfo);
                        updateEnvelopMessageCache(envelopAdoptInfo.getGotDiamondNumber(), EnvelopMessageCacheInfo.STATUS_ADOPTED);

                        dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            switch (((ApiException) e).getErrorCode()) {
                                case ApiCode.ENVELOP_OVERDUE:
                                    envelopInfo.setStatus(EnvelopInfo.Status.OVERDUE);
                                    showInfo();
                                    updateEnvelopMessageCache(0, EnvelopMessageCacheInfo.STATUS_OVERDUE);
                                    break;
                                case ApiCode.ENVELOP_EMPTY:
                                    showInfo();
                                    envelopInfo.setStatus(EnvelopInfo.Status.EMPTY);
                                    break;
                                default:
                                    drpTvbOpen.setEnabled(true);
                                    super.onError(e);
                                    break;
                            }
                        } else {
                            drpTvbOpen.setEnabled(true);
                            super.onError(e);
                        }
                    }
                });
    }

    /**
     * 修改红包消息缓存信息
     *
     * @param godDiamond
     * @param status
     */
    private void updateEnvelopMessageCache(int godDiamond, int status) {
        TIMMessageExt messageExt = new TIMMessageExt(uiMessage.getTimMessage());
        EnvelopMessageCacheInfo cacheInfo = new EnvelopMessageCacheInfo();
        cacheInfo.setGotDiamond(godDiamond);
        cacheInfo.setStatus(status);
        messageExt.setCustomStr(gson.toJson(messageExt));
        messageExt.setCustomInt(status);

        EventBus.getDefault().post(uiMessage);
    }
}