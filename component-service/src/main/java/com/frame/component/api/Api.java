package com.frame.component.api;

/**
 * =========================================
 * <p>
 * Create by ChenJing on 2018-03-20 17:20
 * =========================================
 */

public interface Api {

    String DOMAIN = "http://frogking.top:8080";  //开发
//    String DOMAIN = "http://wangsocial.com:8080";  //开发
//    String DOMAIN = "http://192.168.1.149:8088";  //测试

    /**
     * 接口请求成功状态码
     */
    int SUCCESS_CODE = 199999;

    /**
     * 七牛文件存储路径
     */
    String QINIU_PREFIX = "http://resouce.dongdongwedding.com/";

    //获取用户二维码
    String USER_QRCODE = "/user/getUserQrcodeByUserId";

}
