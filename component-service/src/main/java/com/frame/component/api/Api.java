package com.frame.component.api;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:20
 * =========================================
 */

public interface Api {

//            String DOMAIN = "http://frogking.top:8080";  //开发
//    String DOMAIN = "http://192.168.1.147:8088";  //测试
    String DOMAIN = "http://www.wangsocial.com:8088";  //正式

    /**
     * 接口请求成功状态码
     */
    int SUCCESS_CODE = 199999;

    /**
     * 订单已经支付
     */
    int ERROR_CODE_ORDER_PAYED = 102002;

    /**
     * 七牛文件存储路径
     */
    String QINIU_PREFIX = "http://resouce.dongdongwedding.com/";

    /**
     * 获取用户二维码
     */
    String USER_QRCODE = "/user/getUserQrcodeByUserId";

    //获取趣聊二维码
    String GROUP_QRCODE = "/group/getGroupQrcodeBygroupId";

    /**
     * 分享/说明H5页面根路径
     */
    String WEB_BASE_URL = "http://wangsocial.com/share/v_2.0/test/";
}
