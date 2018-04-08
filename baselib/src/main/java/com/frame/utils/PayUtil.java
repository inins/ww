package com.frame.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 支付工具类
 * <p>
 * Author: ChenJing
 * Date: 2017-06-30 上午 11:56
 * Version: 1.0
 */

public class PayUtil {

    private static final String JAVA_APP = "javaapp";

    /**
     * 发起微信支付
     * <p>
     * 注册 WxPayEvent时间监听是否支付完成
     *
     * @param payInfo
     */
//    public static void payForWx(PayInfo payInfo) {
//        if (!isWeixinAvilible(AppContext.getInstance().getContext())) {
//            ToastUtils.showShort(R.string.toast_wx_install_invalid);
//            return;
//        }
//        IWXAPI wxApi = WXAPIFactory.createWXAPI(AppContext.getInstance().getContext(), payInfo.getAppId(), false);
//        wxApi.registerApp(payInfo.getAppId());
//
//        PayReq payReq = new PayReq();
//        payReq.appId = payInfo.getAppId();
//        payReq.partnerId = payInfo.getPartnerId();
//        payReq.prepayId = payInfo.getPrepayId();
//        payReq.nonceStr = payInfo.getNonceStr();
//        payReq.timeStamp = payInfo.getTimeStamp();
//        payReq.packageValue = payInfo.getPackageValue();
//        payReq.sign = payInfo.getSign();
//
//        wxApi.sendReq(payReq);
//    }
//
//    /**
//     * 发起支付宝支付
//     *
//     * @param activity
//     * @param orderInfo      服务端返回的支付信息
//     * @param alipayListener
//     */
//    public static void payForApliy(final Activity activity, final String orderInfo, final AlipayListener alipayListener) {
//        Flowable.create(new FlowableOnSubscribe<Map<String, String>>() {
//            @Override
//            public void subscribe(FlowableEmitter<Map<String, String>> e) throws Exception {
//                // 构造PayTask 对象
//                PayTask alipay = new PayTask(activity);
//                // 调用支付接口，获取支付结果
//                Map<String, String> result = alipay.payV2(orderInfo, true);
//
//                e.onNext(result);
//                e.onComplete();
//            }
//        }, BackpressureStrategy.BUFFER)
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Map<String, String>>() {
//                    @Override
//                    public void accept(@NonNull Map<String, String> s) throws Exception {
//                        PayResult payResult = new PayResult(s);
//                        /**
//                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
//                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
//                         * docType=1) 建议商户依赖异步通知
//                         */
//                        String resultStatus = payResult.getResultStatus();
//                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//                        if (alipayListener == null) {
//                            return;
//                        }
//
//                        if (TextUtils.equals(resultStatus, "9000")) {
//                            alipayListener.paySuccess();
//                        } else if (TextUtils.equals(resultStatus, "8000")) {// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                            alipayListener.paying();
//                        } else { //支付失败
//                            alipayListener.payFail();
//                        }
//                    }
//                });
//    }

    /**
     * 外部调用自己系统接口加密规则
     * 1.对参数进行a-z排序(ASCII码从小到大排序（字典序))
     * 2.语言(javaapp)进行md5加密，并且拼接两端
     * 3.取出随机数，并且拼接到两端
     * 4.字符串进行md5加密
     *
     * @return
     */
    public static String signStr(Map<String, String> params, String creditCode) {
        try {
            List<Map.Entry<String, String>> sortMap = sortMapByKey(params);
            // 获取系统加密字符串
            String typeMd5 = md5(JAVA_APP);
            StringBuffer bf = new StringBuffer(typeMd5);
            bf.append("&");
            bf.append(creditCode);
            bf.append("&");
            for (Map.Entry<String, String> param : sortMap) {
                bf.append(param.getKey()).append("=").append(param.getValue())
                        .append("&");
            }
            bf.append(creditCode);
            bf.append("&");
            bf.append(typeMd5);

            Log.e("test",bf.toString());
            // 加密后字符串
            return md5(bf.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String md5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * 排序Map
     *
     * @param oriMap
     * @return
     */
    private static List<Map.Entry<String, String>> sortMapByKey(Map<String, String> oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        List<Map.Entry<String, String>> list = new ArrayList<>(oriMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            @Override
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {

                return o1.getKey().compareTo(o2.getKey());
            }
        });
        return list;
    }

    private static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();//
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);//
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
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
         * 支付失败
         */
        void payFail();
    }
}
