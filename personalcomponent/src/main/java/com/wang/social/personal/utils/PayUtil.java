package com.wang.social.personal.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

//import com.alipay.sdk.app.PayTask;

import com.alipay.sdk.app.PayTask;
import com.frame.utils.ToastUtil;
import com.frame.utils.Utils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wang.social.personal.mvp.entities.recharge.PayInfo;
import com.wang.social.personal.mvp.entities.recharge.PayResult;
import com.wang.social.socialize.SocializeUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付工具类
 * <p>
 * Author: ChenJing
 * Date: 2017-06-30 上午 11:56
 * Version: 1.0
 */

public class PayUtil {

    /**
     * 发起微信支付
     * <p>
     * 注册 WxPayEvent时间监听是否支付完成
     *
     * @param payInfo
     */
    public static void payForWx(PayInfo payInfo) {
        IWXAPI wxApi = WXAPIFactory.createWXAPI(Utils.getContext(), SocializeUtil.WX_APP_ID, false);
        if (!wxApi.isWXAppInstalled() || !wxApi.isWXAppSupportAPI()) {
            ToastUtil.showToastShort("请下载安装微信最新版本");
        }
        PayReq payReq = new PayReq();
        payReq.appId = payInfo.getAppid();
        payReq.partnerId = payInfo.getPartnerid();
        payReq.prepayId = payInfo.getPrepayid();
        payReq.nonceStr = payInfo.getNoncestr();
        payReq.timeStamp = String.valueOf(payInfo.getTimestamp());
        payReq.packageValue = payInfo.getPackageValue();
        payReq.sign = payInfo.getSign();

        wxApi.sendReq(payReq);
    }


    /**
     * 发起支付宝支付
     *
     * @param activity
     * @param orderInfo      服务端返回的支付信息
     * @param alipayListener
     */
    @SuppressWarnings("all")
    public static void payForApliy(final Activity activity, final String orderInfo, final AlipayListener alipayListener) {
        Flowable.create(new FlowableOnSubscribe<Map<String, String>>() {
            @Override
            public void subscribe(FlowableEmitter<Map<String, String>> e) throws Exception {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(orderInfo, true);

                e.onNext(result);
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Map<String, String>>() {
                    @Override
                    public void accept(@NonNull Map<String, String> s) throws Exception {
                        PayResult payResult = new PayResult(s);
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (alipayListener == null) {
                            return;
                        }

                        if (TextUtils.equals(resultStatus, "9000")) {
                            alipayListener.paySuccess();
                        } else if (TextUtils.equals(resultStatus, "8000")) {// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            alipayListener.paying();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            alipayListener.cancel();
                        } else { //支付失败
                            alipayListener.payFail();
                        }
                    }
                });
    }


    public interface AlipayListener {

        /**
         * 支付成功
         */
        void paySuccess();

        /**
         * 支付中
         */
        void paying();

        /**
         * 取消支付
         */
        void cancel();

        /**
         * 支付失败
         */
        void payFail();
    }
}
